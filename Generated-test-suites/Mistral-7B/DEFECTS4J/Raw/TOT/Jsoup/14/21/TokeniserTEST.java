import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 characterReader = new CharacterReader(new StringReader(""));
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testRead_EmptyInput() {
 assertEquals(null, tokeniser.read());
 }

 @Test
 public void testRead_WhiteSpace() {
 characterReader.setCurrent('\t');
 tokeniser = new Tokeniser(characterReader);
 assertEquals(new Token.Character(""), tokeniser.read());
 characterReader.advance();
 tokeniser.transition(TokeniserState.Data);
 assertEquals(new Token.Character(""), tokeniser.read());
 }

 @Test
 public void testRead_SingleCharacter() {
 characterReader.setCurrent('a');
 tokeniser = new Tokeniser(characterReader);
 assertEquals(new Token.Character("a"), tokeniser.read());
 characterReader.advance();
 tokeniser.transition(TokeniserState.Data);
 assertEquals(new Token.Character(""), tokeniser.read());
 }

 @Test
 public void testRead_SelfClosingTag() {
 characterReader.setCurrent('<');
 characterReader.advance();
 assertEquals("<", tokeniser.read().tagName);
 characterReader.mark();
 tokeniser.consumeCharacterReference(null, false);
 characterReader.unconsume();
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals("", tokeniser.read().tagName);
 }

 @Test
 public void testRead_StartTag() {
 characterReader.setCurrent('<');
 characterReader.advance();
 assertEquals("<", tokeniser.read().tagName);
 characterReader.mark();
 tokeniser.consumeCharacterReference(null, false);
 characterReader.unconsume();
 tokeniser.emit(new Token.StartTag("tag"));
 assertEquals("tag", tokeniser.read().tagName);
 }

 @Test
 public void testRead_EndTag() {
 characterReader.setCurrent('<');
 characterReader.advance();
 characterReader.advance();
 assertEquals("", tokeniser.read().tagName);
 characterReader.mark();
 tokeniser.consumeCharacterReference(null, false);
 characterReader.unconsume();
 tokeniser.emit(new Token.EndTag("tag"));
 assertEquals("/tag", tokeniser.read().tagName);
 }

 @Test
 public void testRead_InvalidCharacterReference() {
 characterReader.setCurrent('&');
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_InvalidNamedCharacterReference() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('x');
 characterReader.advance();
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_InvalidCharacterReferenceWithAttribute() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('x');
 characterReader.advance();
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_EOF() {
 characterReader.setCurrent('a');
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 assertEquals(new Token.Character("a"), tokeniser.read());
 characterReader.advance();
 characterReader.setCurrent('\0');
 assertEquals(null, tokeniser.read());
 tokeniser.eofError(TokeniserState.Data);
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_InvalidState() {
 characterReader.setCurrent('<');
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_MalformedCharacterReference() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('x');
 characterReader.advance();
 characterReader.consume('>');
 characterReader.advance();
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_NullCharacterReference() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('n');
 characterReader.advance();
 characterReader.consume('a');
 characterReader.advance();
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals(Tokeniser.replacementChar, tokeniser.read().charContent.charAt(0));
 }

 @Test
 public void testRead_CharacterReferenceOutsideAttribute() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('a');
 characterReader.advance();
 characterReader.consume('m');
 characterReader.advance();
 characterReader.consume('p');
 characterReader.advance();
 characterReader.consume(';');
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals("a", tokeniser.read().charContent.toString());
 }

 @Test
 public void testRead_CharacterReferenceInAttribute() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('a');
 characterReader.advance();
 characterReader.consume('m');
 characterReader.consume('p');
 characterReader.advance();
 characterReader.consume(' ');
 characterReader.consume('a');
 characterReader.advance();
 characterReader.consume('=');
 characterReader.consume(' ');
 characterReader.consume('x');
 characterReader.advance();
 characterReader.consume(' ');
 characterReader.consume('>');
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testRead_ParseErrorMessageContainsCorrectInformation() {
 characterReader.setCurrent('&');
 characterReader.advance();
 characterReader.consume('x');
 characterReader.advance();
 characterReader.consume('>');
 characterReader.advance();
 tokeniser = new Tokeniser(characterReader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals("Invalid character reference", tokeniser.errors.get(0).getMessage());
 assertEquals(7, tokeniser.errors.get(0).getLineNumber());
 assertEquals(1, tokeniser.errors.get(0).getColumnNumber());
 assertEquals(TokeniserState.Data, tokeniser.errors.get(0).getState());
 }
}