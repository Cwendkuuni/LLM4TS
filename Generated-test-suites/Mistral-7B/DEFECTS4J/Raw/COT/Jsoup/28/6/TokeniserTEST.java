import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader reader;

 @Before
 public void setup() {
 reader = new CharacterReader("test input text");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 // Test reading a character token
 tokeniser.read();
 String str = tokeniser.charBuffer.toString();
 Assert.assertEquals("Character token test", str, String.valueOf(reader.current()));
 reader.advance();

 // Test reading a start tag token
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Token startTagToken = tokeniser.emitPending;
 Assert.assertEquals("Start tag token test", startTagToken.tagName, "div");

 // Test reading an end tag token
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 Token endTagToken = tokeniser.emitPending;
 Assert.assertEquals("End tag token test", endTagToken.tagName, "div");
 }

 @Test(expected = AssertionError.class)
 public void testReadUnexpectedCharacter() {
 tokeniser.transition(TokeniserState.Data);
 reader.mark(); // Mark the reader position
 reader.advance(); // Advance the reader
 tokeniser.read();
 reader.unconsume(); // Unconsume the character
 Assert.fail("Expected an AssertionError due to an unexpected character");
 }

 @Test
 public void testEmit() {
 // Test emitting a character token
 tokeniser.emit(new Token.Character("A"));
 String str = tokeniser.charBuffer.toString();
 Assert.assertEquals("Character token emission test", str, "A");
 tokeniser.read(); // Consume the character token

 // Test emitting a start tag token
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.emitTagPending();
 String startTagName = tokeniser.tagPending.tagName;
 Assert.assertEquals("Start tag emission test", startTagName, "div");
 tokeniser.read(); // Consume the start tag token

 // Test emitting an end tag token
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit(new Token.EndTag("div"));
 tokeniser.emitEndTagPending();
 String endTagName = tokeniser.tagPending.tagName;
 Assert.assertEquals("End tag emission test", endTagName, "div");
 tokeniser.read(); // Consume the end tag token
 }

 @Test(expected = AssertionError.class)
 public void testEmitUnexpectedToken() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.EndTag("div"));
 Assert.fail("Expected an AssertionError due to emitting an unexpected token");
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 Assert.assertEquals("Current state test after advancing", TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference() {
 char additionalAllowedCharacter = '&';
 tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
 Assert.assertEquals("Character reference test", tokeniser.charBuffer.toString(), "&amp;");
 tokeniser.read(); // Consume the character reference token

 additionalAllowedCharacter = '\u00A0';
 tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
 Assert.assertEquals("Character reference test", tokeniser.charBuffer.toString(), "\u00A0");
 tokeniser.read(); // Consume the character reference token

 tokeniser.consumeCharacterReference(null, true); // Test case for no additional allowed character and in attribute
 Assert.assertEquals("Character reference test with no additional allowed character and in attribute", "", tokeniser.charBuffer.toString());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReferenceInvalid() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference(null, false); // Test case for invalid character reference
 Assert.fail("Expected an AssertionError due to an invalid character reference");
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 Assert.assertTrue("TagPending isStartTag test", tokeniser.tagPending instanceof Token.StartTag);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 String startTagName = tokeniser.tagPending.tagName;
 Assert.assertEquals("TagPending emission test", startTagName, tokeniser.lastStartTag.tagName);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Assert.assertTrue("CommentPending isComment", tokeniser.commentPending instanceof Token.Comment);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 String commentText = tokeniser.commentPending.data;
 Assert.assertNotNull("CommentPending emission test", commentText);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Assert.assertTrue("DoctypePending isDoctype", tokeniser.doctypePending instanceof Token.Doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 String doctypeName = tokeniser.doctypePending.name;
 Assert.assertEquals("DoctypePending emission test", doctypeName, "html");
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertTrue("isAppropriateEndTagToken test", tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals("Error message test", errors.get(0).getMessage(), "Unexpected character 'X' in input state [DATA]");
 Assert.assertEquals("Error message test", errors.get(0).getErrorLocation().line(), 1);
 Assert.assertEquals("Error message test", errors.get(0).getErrorLocation().column(), 1);
 }

 @Test
 public void testEOFError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals("EOFError message test", errors.get(0).getMessage(), "Unexpectedly reached end of file (EOF) in input state [DATA]");
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("test error message");
 Assert.assertEquals("CharacterReferenceError message test", errors.get(0).getMessage(), "Invalid character reference: test error message");
 }

 @Test
 public void testErrorReaderIsEmpty() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals("Error message test when reader is empty", errors.get(0).getMessage(), "Unexpected character '\u0000' in input state [DATA]");
 }

 @Test
 public void testErrorReaderIsNotEmpty() {
 tokeniser.transition(TokeniserState.Data);
 reader.mark(); // Mark the reader position
 reader.advance(); // Advance the reader
 reader.unconsume(); // Unconsume the character
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals("Error message test when reader is not empty", errors.get(0).getMessage(), "Unexpected character '" + reader.current() + "' in input state [DATA]");
 }
}