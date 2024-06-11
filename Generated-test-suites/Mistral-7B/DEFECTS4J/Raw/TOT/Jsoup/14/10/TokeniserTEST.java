import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader characterReader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return 0;
 }

 @Override
 public char current() {
 return '\0';
 }

 @Override
 public void mark(int readAheadLimit) {
 // no-op
 }

 @Override
 public void mark() {
 // no-op
 }

 @Override
 public void reset() {
 // no-op
 }

 @Override
 public boolean isEmpty() {
 return false;
 }
 };
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testReadCharacter() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return 'a';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals('a', token.data);
 }

 @Test
 public void testReadSelfClosingTag() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return 'i'; // for <img>;
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 tokeniser.read(); // <img
 tokeniser.emit(tokeniser.createTagPending(true));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 tokeniser.read(); // </img>
 tokeniser.emit(tokeniser.createTagPending(false));
 tokeniser.emitTagPending();
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("img", token.data.tagName);
 }

 @Test
 public void testReadEndTagWithNoCorrespondingStartTag() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return 'i'; // for <img>;
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 tokeniser.read(); // <img
 tokeniser.emit(tokeniser.createTagPending(true));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 tokeniser.read(); // </img>
 tokeniser.emit(tokeniser.createTagPending(false));
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data); // read <html>
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 tokeniser.read(); // </html>
 tokeniser.emit(tokeniser.createTagPending(false));
 tokeniser.emitTagPending();
 tokeniser.emit(tokeniser.createTagPending(false)); // </img> without matching start tag
 Assert.assertEquals(Token.TokenType.Error, tokeniser.read().type);
 }

 @Test
 public void testReadCharacterReference() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '&';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(Character.MIN_VALUE, character.intValue());
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testReadCharacterReferenceWithAllowedCharacter() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '&';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 Character character = tokeniser.consumeCharacterReference('w', false);
 Assert.assertEquals('w', character);
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testReadCharacterReferenceWithUnexpectedCharacter() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '<';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertEquals(Token.TokenType.Error, tokeniser.read().type);
 }

 @Test
 public void testReadNamedCharacterReference() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '&';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.consume('a');
 tokeniser.consume('m');
 tokeniser.consume('p');
 tokeniser.consume(';');
 Assert.assertEquals(Character.forDigit(97, 10), tokeniser.read().data.charAt(0)); // small a
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testReadNamedCharacterReferenceWithUnmatchedSemiColon() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '&';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.consume('a');
 tokeniser.consume('m');
 tokeniser.consume('p');
 Assert.assertEquals(Token.TokenType.Error, tokeniser.read().type);
 }

 @Test
 public void testReadNamedCharacterReferenceWithInvalidCharacter() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '&';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('&');
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.consume('x');
 tokeniser.consume('y');
 tokeniser.consume('z');
 tokeniser.consume(';');
 Assert.assertEquals(Token.TokenType.Error, tokeniser.read().type);
 }

 @Test
 public void testReadComment() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '*';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('*');
 tokeniser.emit('!');
 tokeniser.emit(Tokeniser.replacementChar);
 tokeniser.emit('c');
 tokeniser.emit('o');
 tokeniser.emit('m');
 tokeniser.emit('m');
 tokeniser.emit('e');
 tokeniser.emit('n');
 tokeniser.emit('t');
 tokeniser.emit(' '); // white space
 tokeniser.emit('*');
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, token.type);
 Assert.assertEquals("!--comment-->", token.data.data);
 }

 @Test
 public void testReadDoctypeDeclaration() {
 characterReader = new CharacterReader() {
 @Override
 public int read() throws IOException {
 return '<';
 }
 };
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('d');
 tokeniser.emit('o');
 tokeniser.emit('c');
 tokeniser.emit('t');
 tokeniser.emit('y');
 tokeniser.emit('p');
 tokeniser.emit(' ');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 tokeniser.emit(tokeniser.createDoctypePending());
 tokeniser.emitDoctypePending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data); // read <html>
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 tokeniser.read(); // </html>
 tokeniser.emit(tokeniser.createTagPending(false));
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, token.type);
 Assert.assertEquals("html", token.data.publicId);
 Assert.assertEquals("", token.data.systemId);
 }
}