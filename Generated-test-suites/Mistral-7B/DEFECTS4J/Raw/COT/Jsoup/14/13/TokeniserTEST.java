import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private StringBuilder input;

 @Before
 public void setUp() {
 input = new StringBuilder("<html><head><title>Test</title></head><body><script>function test(){}</script></body></html>");
 tokeniser = new Tokeniser(new StringReader(input.toString()));
 }

 @Test
 public void testRead_NormalOperation() {
 Token token = null;
 while ((token = tokeniser.read()) != null) {
 System.out.println(token.toString());
 }
 Assert.assertFalse(tokeniser.hasMoreTokens());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_PendingToken() {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit(new Token.Character('A'));
 Token token = tokeniser.read();
 Assert.assertEquals("A", token.toString());
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.emit(new Token.StartTag("html"));
 Token token = tokeniser.read();
 Assert.assertEquals("<html", token.toString());
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.emit(new Token.EndTag("html"));
 tokeniser.read(); // <html>
 tokeniser.read(); // </html>
 Token token = tokeniser.read();
 Assert.assertEquals("", token.toString());
 }

 @Test
 public void testEmit_String() {
 tokeniser.emit("Test");
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("Test", token.toString());
 }

 @Test
 public void testEmit_SelfClosingTag() {
 tokeniser.emit(new Token.StartTag("img", new Attribute("src", "test.jpg"), true));
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("<img src=\"test.jpg\" />", token.toString());
 }

 @Test
 public void testTransition_Data() {
 tokeniser.transition(TokeniserState.Data);
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_AfterTag() {
 tokeniser.transition(TokeniserState.AfterTagName);
 tokeniser.read(); // <html>
 tokeniser.read(); // <html>
 Assert.assertEquals(TokeniserState.AfterTagName, tokeniser.getState());
 }

 @Test
 public void testTransition_AfterTagEnd() {
 tokeniser.transition(TokeniserState.AfterTagEnd);
 tokeniser.emit(new Token.EndTag("html"));
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("", token.toString());
 Assert.assertEquals(TokeniserState.AfterTagEnd, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 Character c = tokeniser.consumeCharacterReference('&', false);
 Assert.assertEquals('A', c);
 tokeniser.read(); // <
 tokeniser.read(); // html
 tokeniser.read(); // >
 tokeniser.read(); // &
 tokeniser.read(); // A
 tokeniser.read(); // ;
 tokeniser.read(); // <
 tokeniser.read(); // /script
 tokeniser.read(); // >
 tokeniser.read(); // function
 tokeniser.read(); // test
 tokeniser.read(); // ()
 tokeniser.read(); // {
 tokeniser.read(); // }
 tokeniser.read(); // ;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_InvalidFirstChar() {
 tokeniser.consumeCharacterReference(0, false);
 }

 @Test
 public void testConsumeCharacterReference_InvalidLastChar() {
 tokeniser.consumeCharacterReference(';', false);
 }

 @Test
 public void testConsumeCharacterReference_InvalidSequence() {
 tokeniser.consumeCharacterReference('A', false);
 tokeniser.consumeCharacterReference('B', false);
 tokeniser.consumeCharacterReference('C', false);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError() {
 tokeniser.consumeCharacterReference('#', false);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_MalformedSequence() {
 tokeniser.consumeCharacterReference("##", false);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_UnrecognizedEntity() {
 tokeniser.consumeCharacterReference("ZZZ", false);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_InAttribute() {
 tokeniser.consumeCharacterReference("&amp;", true);
 }

 @Test
 public void testCreateTagPending_StartTag() {
 tokeniser.createTagPending(true);
 Assert.assertEquals(Token.TagType.StartTag, tokeniser.tagPending.type);
 }

 @Test
 public void testCreateTagPending_EndTag() {
 tokeniser.createTagPending(false);
 Assert.assertEquals(Token.TagType.EndTag, tokeniser.tagPending.type);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("<html", token.toString());
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("<!--", token.toString());
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("", token.toString());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit("temp");
 tokeniser.emit("buffer");
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.emitCommentPending();
 tokeniser.emitDoctypePending();
 tokeniser.emit('<');
 tokeniser.emit("script");
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.emitCommentPending();
 tokeniser.emitDoctypePending();
 tokeniser.emit('<');
 tokeniser.emit("/script");
 tokeniser.emit('>');
 tokeniser.emitCommentPending();
 tokeniser.emitDoctypePending();
 tokeniser.emitTagPending();
 tokeniser.emit('<');
 tokeniser.emit("/html");
 tokeniser.emit('>');
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals("<html><!-- --><!DOCTYPE html>", token.toString());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit(new Token.EndTag("html"));
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.emit(new Token.StartTag("head"));
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError_UnexpectedCharacter() {
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testEofError() {
 tokeniser.reader.mark(10);
 tokeniser.reader.consume(9);
 tokeniser.eofError(TokeniserState.AfterTagEnd);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.reader.mark(10);
 tokeniser.reader.consume(1);
 tokeniser.characterReferenceError();
 }
}