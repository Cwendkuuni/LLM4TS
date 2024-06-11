import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 charReader = new StringCharacterReader("test");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testRead_DataState() {
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_DataState_MultipleChars() {
 tokeniser.emit("ab");
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("b", token.value);
 }

 @Test
 public void testRead_DataState_CharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 Character refChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNotNull(refChar);
 Assert.assertEquals('a', refChar);
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testRead_DataState_CharacterReference_Invalid() {
 tokeniser.transition(TokeniserState.CharacterReference);
 Character refChar = tokeniser.consumeCharacterReference('a', false);
 Assert.assertNull(refChar);
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testRead_DataState_NamedCharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer(';');
 Character refChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNotNull(refChar);
 Assert.assertEquals('\u00A9', refChar); // copyright sign
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test(expected = AssertionError.class)
 public void testRead_DataState_NamedCharacterReference_Invalid() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer('x'); // not a semicolon
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testRead_ScriptState() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('/');
 tokeniser.matchConsumer('s');
 tokeniser.matchConsumer('/');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer('>');
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 }

 @Test
 public void testRead_SelfClosingTagState() {
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.emit('<');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer('/');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer('>');
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 }

 @Test
 public void testRead_TagState() {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.matchConsumer('>');
 Token startTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
 Token endTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
 Assert.assertEquals(startTag.tagName, endTag.tagName);
 }

 @Test
 public void testRead_CommentState() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit('*');
 tokeniser.emit('/');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.emit('*');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.emit('*');
 Token comment = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, comment.type);
 }

 @Test
 public void testRead_ErrorState() {
 tokeniser.transition(TokeniserState.Error);
 Assert.assertThrows(ParseError.class, tokeniser::read);
 }

 @Test
 public void testRead_EOFState() {
 tokeniser.transition(TokeniserState.EOF);
 Assert.assertThrows(ParseError.class, tokeniser::read);
 }
}