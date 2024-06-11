import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("testInput.html");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_DataState() {
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_DataState_SingleCharacter() {
 reader.current('a');
 tokeniser.read();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals('a', token.data());
 }

 @Test
 public void testRead_DataState_MultipleCharacters() {
 reader.current('a');
 reader.current('b');
 reader.current('c');
 tokeniser.read(); // consume 'a'
 tokeniser.read(); // consume 'b'
 tokeniser.read(); // consume 'c'
 tokeniser.read();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("bc", token.data());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_DataState_EmptyReader() {
 reader.reset();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testRead_DataState_InvalidCharacterReference() {
 reader.current('&');
 tokeniser.read(); // consume '&'
 tokeniser.read();
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNull(codePoints);
 }

 @Test
 public void testRead_DataState_NamedCharacterReference() {
 reader.current('&');
 reader.current('a');
 reader.current('m');
 reader.current('p');
 reader.current(';');
 tokeniser.read(); // consume '&'
 tokeniser.read(); // consume 'a'
 tokeniser.read(); // consume 'm'
 tokeniser.read(); // consume 'p'
 tokeniser.read(); // consume ';'
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 Assert.assertArrayEquals(new int[]{160}, codePoints);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_DataState_SelfClosingFlagAcknowledged() {
 tokeniser.selfClosingFlagAcknowledged = false;
 tokeniser.read();
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.emit(new Token.StartTag("p"));
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.InTag, state);
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.emit(new Token.EndTag("p"));
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.OutOfTag, state);
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit("test");
 tokeniser.read();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("test", token.data());
 }

 @Test
 public void testEmit_String() {
 tokeniser.emit("test");
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit("string");
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("string", token.data());
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 Assert.assertTrue(errors.size() > 0);
 }

 @Test
 public void testEOFError() {
 reader.reset();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertTrue(errors.size() > 0);
 }

 @Test
 public void testCharacterReferenceError() {
 reader.current('&');
 reader.current('x');
 reader.current(';');
 tokeniser.read(); // consume '&'
 tokeniser.read(); // consume 'x'
 tokeniser.read(); // consume ';'
 tokeniser.characterReferenceError("Invalid character reference");
 Assert.assertTrue(errors.size() > 0);
 }

 @Test
 public void testError_InvalidCharacterInState() {
 reader.current('&');
 tokeniser.error(TokeniserState.InTag);
 }

 @Test
 public void testError_EOFInState() {
 reader.reset();
 tokeniser.error(TokeniserState.Data);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 Token token = tokeniser.tagPending();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.TagStart, state);
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 }

 @Test
 public void testCreateEndTagPending() {
 tokeniser.createTagPending(false);
 Token token = tokeniser.tagPending();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.TagEnd, state);
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Comment, token.type);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit("test");
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTempBuffer();
 tokeniser.emit("temp");
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("temp", token.data());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.createTagPending(true);
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp;";
 String expected = "& amp; < > ' \" é \u00A0";
 String output = tokeniser.unescapeEntities(false);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp;";
 String expected = "&amp;amp; &lt;lt; &gt;gt; &apos;apos; &quot;quot; &eacute;eacute; &#xA0;";
 String output = tokeniser.unescapeEntities(true);
 Assert.assertEquals(expected, output);
 }
}