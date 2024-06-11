import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 charArrayReader = new CharArrayReader(new char[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 't', 'y', 'p', 'e', '>', '\n'});
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testRead_NoSelfClosingFlagAcknowledged() {
 // Basic functionality test for read() method
 Assert.assertNotNull(tokeniser.read());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 // Test for exception handling scenario: Self closing flag not acknowledged
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 tokeniser.read(); // This should throw an exception
 }

 @Test
 public void testRead_CharacterBuffer() {
 // Basic functionality test for read() method when a character is read
 char expected = 'h';
 char actual = tokeniser.read().getData();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testRead_EmitPendingToken() {
 // Basic functionality test for read() method when an emitPending token is returned
 char expected = 't';
 charBuffer.setLength(0);
 tokeniser.emit(new Character("t"));
 tokeniser.isEmitPending = true;
 char actual = tokeniser.read().getData();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmit_Character() {
 // Basic functionality test for emit() method with Character token
 tokeniser.emit(new Character("a"));
 Character actual = tokeniser.read();
 Assert.assertEquals("a", actual.getData());
 }

 @Test
 public void testEmit_StartTag() {
 // Basic functionality test for emit() method with StartTag token
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 StartTag expected = tokeniser.read();
 StartTag actual = tokeniser.read();
 Assert.assertEquals(expected.tagName, actual.tagName);
 }

 @Test
 public void testEmit_EndTag() {
 // Basic functionality test for emit() method with EndTag token
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 EndTag expected = tokeniser.read();
 EndTag actual = tokeniser.read();
 Assert.assertEquals(expected.tagName, actual.tagName);
 }

 @Test
 public void testEmit_Comment() {
 // Basic functionality test for emit() method with Comment token
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Comment actual = tokeniser.read();
 Assert.assertNotNull(actual);
 Assert.assertEquals("", actual.getData());
 }

 @Test
 public void testEmit_Doctype() {
 // Basic functionality test for emit() method with Doctype token
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Doctype actual = tokeniser.read();
 Assert.assertNotNull(actual);
 }

 @Test
 public void testCreateTempBuffer() {
 // Test for createTempBuffer() method
 tokeniser.createTempBuffer();
 }

 @Test
 public void testIsAppropriateEndTagToken_MatchingTags() {
 // Test for isAppropriateEndTagToken() method with matching start and end tags
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_MismatchingTags() {
 // Test for isAppropriateEndTagToken() method with mismatching start and end tags
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.tagPending.tagName = "body"; // Change the tag name to mismatch
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_NoPendingToken() {
 // Test for exception handling scenario: No pending token
 tokeniser.read();
 tokeniser.read(); // This should throw an exception
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacterReference() {
 // Test for consumeCharacterReference() method with valid character reference
 Character expected = new Character("é");
 Character actual = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_InvalidCharacterReference() {
 // Test for exception handling scenario: Invalid character reference
 tokeniser.consumeCharacterReference('&', false);
 }

 @Test
 public void testConsumeCharacterReference_MissingSemicolon() {
 // Test for consumeCharacterReference() method with missing semicolon
 tokeniser.consumeCharacterReference("#r", false);
 }

 @Test
 public void testConsumeCharacterReference_NamedEntity() {
 // Test for consumeCharacterReference() method with named entity
 Character expected = new Character("&copy;");
 Character actual = tokeniser.consumeCharacterReference("copy", false);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCharacterReference_NamedEntityWithAttribute() {
 // Test for consumeCharacterReference() method with named entity and attribute
 tokeniser.consume("class=\"foo\" "); // Consume whitespace and attribute
 Character expected = new Character("&amp;");
 Character actual = tokeniser.consumeCharacterReference("amp", false);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_InvalidNamedEntity() {
 // Test for exception handling scenario: Invalid named entity
 tokeniser.consumeCharacterReference("xyz", false);
 }

 @Test
 public void testError_ParsingError() {
 // Test for error() method with parsing error
 tokeniser.error(TokeniserState.Data);
 }

 @Test(expected = ParseError.class)
 public void testEofError() {
 // Test for eofError() method with end of file error
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError() {
 // Test for characterReferenceError() method with character reference error
 tokeniser.characterReferenceError();
 }

 @Test
 public void testCurrentNodeInHtmlNS() {
 // Test for currentNodeInHtmlNS() method
 Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
 }

}