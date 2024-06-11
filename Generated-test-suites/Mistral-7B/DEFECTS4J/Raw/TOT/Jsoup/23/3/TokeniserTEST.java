import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;
import org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 characterReader = new StringCharacterReader("testData.html");
 parseErrors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrors);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 Assert.assertFalse(parseErrors.size() > 0);
 }

 @Test
 public void testReadWhitespaceAndTags() {
 // Test reading whitespace and tags in order
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(' ');
 tokeniser.emit('T');
 tokeniser.emit('h');
 tokeniser.emit('i');
 tokeniser.emit('s');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('{');
 Token tagStartTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tagStartTag.type);
 tokeniser.emit('}');
 Token tagEndTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEndTag.type);
 Assert.assertEquals("tag", tagStartTag.tagName);
 Assert.assertEquals("tag", tagEndTag.tagName);
 // Test reading whitespace between tags
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('h');
 tokeniser.emit('i');
 tokeniser.emit('s');
 tokeniser.emit(' ');
 tokeniser.emit('{');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit('}');
 tagStartTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tagStartTag.type);
 tokeniser.emit('}');
 tagEndTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEndTag.type);
 Assert.assertEquals("tag", tagStartTag.tagName);
 Assert.assertEquals("tag", tagEndTag.tagName);
 }

 @Test
 public void testSelfClosingTags() {
 // Test reading self-closing tag
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 Token selfClosingTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, selfClosingTag.type);
 Assert.assertEquals("img", selfClosingTag.tagName);
 Assert.assertEquals("img", selfClosingTag.tagName); // self-closing tags don't have a start tag
 // Test reading whitespace after self-closing tag
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 selfClosingTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, selfClosingTag.type);
 Assert.assertEquals("img", selfClosingTag.tagName);
 }

 @Test
 public void testReadScriptData() {
 // Test reading script data
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('d');
 tokeniser.emit('a');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit(' ');
 tokeniser.emit('=');
 tokeniser.emit('"');
 tokeniser.emit('j');
 tokeniser.emit('a');
 tokeniser.emit('v');
 tokeniser.emit('a');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 Token scriptData = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Data, scriptData.type);
 Assert.assertEquals("scriptData", scriptiser.charBuffer.toString());
 }

 @Test
 public void testReadErrorHandling() {
 // Test error handling
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('&'); // invalid character reference
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, parseErrors.size());
 Assert.assertEquals("Unexpected character '&' in input state [DATA]", parseErrors.get(0).getMessage());
 // Test malformed character reference
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('#'); // missing letter after #
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, parseErrors.size());
 Assert.assertEquals("Invalid character reference: missing letter after '#'", parseErrors.get(0).getMessage());
 // Test unrecognized entity
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('#');
 tokeniser.emit('x'); // unrecognized entity
 tokeniser.emit(';');
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, parseErrors.size());
 Assert.assertEquals("Invalid named reference 'x'", parseErrors.get(0).getMessage());
 }

 @Test
 public void testReadEOF() {
 // Test reading EOF
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals(1, parseErrors.size());
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [DATA]", parseErrors.get(0).getMessage());
 }

 @After
 public void tearDown() {
 characterReader.close();
 parseErrors.clear();
 }

}