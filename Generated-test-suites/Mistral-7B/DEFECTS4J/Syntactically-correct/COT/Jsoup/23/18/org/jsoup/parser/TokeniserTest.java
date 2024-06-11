import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ByteArrayInputStream inputStream;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("test data".getBytes());
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(new CharReaderWrapper(inputStream), errors);
 }

 @Test
 public void testRead() throws IOException {
 Token token = tokeniser.read();
 assertEquals("Character", token.getType().toString(), "Expected character token");
 assertEquals("test", token.data, "Expected data 'test'");
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithEmitPending() {
 tokeniser.emit(new Character('a'));
 tokeniser.read(); // should read the pending character, not throw an exception
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Character('a'));
 Token token = tokeniser.read();
 assertEquals("Character", token.getType().toString(), "Expected character token");
 assertEquals("a", token.data, "Expected data 'a'");
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("test");
 Token token = tokeniser.read();
 assertEquals("Character", token.getType().toString(), "Expected character token");
 assertEquals("test", token.data, "Expected data 'test'");
 }

 @Test
 public void testEmitChar() {
 tokeniser.emit('a');
 Token token = tokeniser.read();
 assertEquals("Character", token.getType().toString(), "Expected character token");
 assertEquals("a", token.data, "Expected data 'a'");
 }

 @Test
 public void testGetState() {
 TokeniserState state = tokeniser.getState();
 assertEquals(TokeniserState.Data, state, "Expected initial state");
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState(), "Expected state transition");
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged, "Expected selfClosingFlagAcknowledged to be false");
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged, "Expected selfClosingFlagAcknowledged to be true");
 }

 @Test
 public void testConsumeCharacterReference() throws IOException {
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertNull(charRef, "Expected null for invalid character reference");

 charRef = tokeniser.consumeCharacterReference('&', false);
 assertNull(charRef, "Expected null for invalid reference with '&'");

 charRef = tokeniser.consumeCharacterReference('&', true);
 assertEquals(Character.replacementChar, charRef, "Expected replacement character for invalid reference with '&' in attribute");

 charRef = tokeniser.consumeCharacterReference("#x", false);
 assertNull(charRef, "Expected null for invalid hex reference");

 charRef = tokeniser.consumeCharacterReference("#x22", false);
 assertEquals((char) 34, charRef.charValue(), "'Expected '\"' from character reference #x22");

 charRef = tokeniser.consumeCharacterReference("#x41", false);
 assertEquals((char) 65, charRef.charValue(), "'Expected 'A' from character reference #x41");

 charRef = tokeniser.consumeCharacterReference("#x7F", false);
 assertEquals(Character.replacementChar, charRef.charValue(), "'Expected replacement character for invalid character outside of valid range");

 charRef = tokeniser.consumeCharacterReference("#x{10000}", false);
 assertEquals(Character.replacementChar, charRef.charValue(), "'Expected replacement character for invalid Unicode point");
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tagPending = tokeniser.createTagPending(true);
 assertTrue(tagPending instanceof StartTag, "Expected StartTag instance");
 tagPending = tokeniser.createTagPending(false);
 assertTrue(tagPending instanceof EndTag, "Expected EndTag instance");
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token tagToken = tokeniser.read();
 assertTrue(tagToken.getType() instanceof StartTag, "Expected start tag");
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token commentToken = tokeniser.read();
 assertTrue(commentToken.getType() instanceof Comment, "Expected comment");
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token commentToken = tokeniser.read();
 assertTrue(commentToken.getType() instanceof Comment, "Expected comment");
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token doctypeToken = tokeniser.read();
 assertTrue(doctypeToken.getType() instanceof Doctype, "Expected doctype");
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token doctypeToken = tokeniser.read();
 assertTrue(doctypeToken.getType() instanceof Doctype, "Expected doctype");
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 assertTrue(tokeniser.dataBuffer != null, "Expected data buffer to be created");
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken(), "Expected true for appropriate end tag");
 }

 @Test
 public void testIsAppropriateEndTagToken_mismatching() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertFalse(tokeniser.isAppropriateEndTagToken(), "Expected false for inappropriate end tag");
 }

 @Test
 public void testAppropriateEndTagName() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertEquals("tagPending.tagName", tokeniser.appropriateEndTagName(), "Expected 'tagPending.tagName' as appropriate end tag name");
 }

 @Test
 public void testError_TokeniserState() {
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errorsList = tokeniser.getErrors();
 assertEquals(1, errorsList.size(), "Expected one error");
 assertEquals("Unexpected character '<' in input state [Data]", errorsList.get(0).getMessage(), "Expected error message with state Data");
 }

 @Test
 public void testError_TokeniserState_EOF() {
 tokeniser.eofError(TokeniserState.Data);
 List<ParseError> errorsList = tokeniser.getErrors();
 assertEquals(1, errorsList.size(), "Expected one error");
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errorsList.get(0).getMessage(), "Expected error message with state Data");
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError("numeric reference with no numerals");
 List<ParseError> errorsList = tokeniser.getErrors();
 assertEquals(1, errorsList.size(), "Expected one error");
 assertEquals("Invalid character reference: invalid character reference with no numerals", errorsList.get(0).getMessage(), "Expected error message with message 'invalid character reference with no numerals'");
 }

 @Test
 public void testError() {
 tokeniser.error("Unexpected character 'x'");
 List<ParseError> errorsList = tokeniser.getErrors();
 assertEquals(1, errorsList.size(), "Expected one error");
 assertEquals("Unexpected character 'x'", errorsList.get(0).getMessage(), "Expected error message with message 'Unexpected character 'x''");
 }

 @Test
 public void testCurrentNodeInHtmlNS() {
 // TODO: Implement namespaces correctly and test this method
 fail("CurrentNodeInHtmlNS method not implemented");
 }

 @After
 public void tearDown() {
 tokeniser = null;
 inputStream.close();
 errors = null;
 }
}