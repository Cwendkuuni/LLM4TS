import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @BeforeEach
 void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 void testNormalOperation() throws IOException {
 Token token;
 Token expectedToken;

 // Test reading normal characters
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("e");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("s");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character(" ");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("d");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("a");
 Assertions.assertEquals(expectedToken, token);
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);

 // Test reading tags
 tokeniser.transition(TokeniserState.TagStart);
 token = tokeniser.read();
 expectedToken = new Token.StartTag("<");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagName);
 token = tokeniser.read();
 expectedToken = new Token.Character("e");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagAttribute);
 token = tokeniser.read();
 expectedToken = new Token.Character(" ");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagAttributeName);
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagAttributeValue);
 token = tokeniser.read();
 expectedToken = new Token.Character("a");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagEnd);
 token = tokeniser.read();
 expectedToken = new Token.EndTag("/>");
 Assertions.assertEquals(expectedToken, token);

 // Test reading script tags
 tokeniser.transition(TokeniserState.Script);
 token = tokeniser.read();
 expectedToken = new Token.StartTag("<script");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 expectedToken = new Token.Character(">");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference('&', false);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference(';', false);
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.Script);
 token = tokeniser.read();
 expectedToken = new Token.EndTag("</script>");
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testErrorHandling() throws IOException {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.error(TokeniserState.TagName);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testTrackErrors() throws IOException {
 tokeniser.setTrackErrors(false);
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.error(TokeniserState.TagName);
 token = tokeniser.read();
 // There should be no error token
 Assertions.assertNull(token);
 }

 @Test
 void testConsumeCharacterReference() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference('&', false);
 tokeniser.consumeCharacterReference('a', false);
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Character("a");
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeNamedCharacterReference() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("amp", false);
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Character('&');
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeCharacterReferenceInAttribute() throws IOException {
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Character('&');
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeInvalidCharacterReference() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("invalid", false);
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeCharacterReferenceWithHexValue() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("#x", false);
 tokeniser.consumeHexadecimalDigitSequence("FF");
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Character('\u00FF');
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeCharacterReferenceWithDecimalValue() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("#", false);
 tokeniser.consumeDigitSequence("127");
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Character('\u007F');
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeCharacterReferenceWithInvalidValue() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("#", false);
 tokeniser.consumeDigitSequence("128");
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testConsumeCharacterReferenceWithOutOfRangeValue() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("#", false);
 tokeniser.consumeHexadecimalDigitSequence("110000");
 tokeniser.consumeCharacterReference(';', false);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testCreateTagPending() throws IOException {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTagPending(true);
 token = tokeniser.read();
 expectedToken = new Token.Character('<');
 Assertions.assertEquals(expectedToken, token);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 expectedToken = new Token.Character("t");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagName);
 token = tokeniser.read();
 expectedToken = new Token.Character("e");
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testCreateEndTagPending() throws IOException {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTagPending(false);
 token = tokeniser.read();
 expectedToken = new Token.Character('<');
 Assertions.assertEquals(expectedToken, token);
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeCharacterReference("slash", false);
 tokeniser.consumeCharacterReference('>', false);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 expectedToken = new Token.EndTag("/");
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testCreateDoctypePending() throws IOException {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 token = tokeniser.read();
 expectedToken = new Token.Character("<!DOCTYPE");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 expectedToken = new Token.Character("html");
 Assertions.assertEquals(expectedToken, token);
 tokeniser.consumeCharacterReference("PUBLIC", false);
 tokeniser.consumeCharacterReference(' ', false);
 tokeniser.consumeCharacterReference("\"", false);
 tokeniser.consumeCharacterReference("http://www.w3.org/TR/html4", false);
 tokeniser.consumeCharacterReference('"', false);
 tokeniser.consumeCharacterReference('>', false);
 token = tokeniser.read();
 expectedToken = new Token.Character('\n');
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testCreateTempBuffer() throws IOException {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createTempBuffer();
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.Character('d'));
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('a'));
 token = tokeniser.read();
 expectedToken = new Token.Character("test data");
 Assertions.assertEquals(expectedToken.getData(), token.getData());
 }

 @Test
 void testIsAppropriateEndTagToken() throws IOException {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 expectedToken = new Token.EndTag("</test>");
 Assertions.assertTrue(tokeniser.isAppropriateEndTagToken());
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testIsAppropriateEndTagTokenFalse() throws IOException {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 Assertions.assertFalse(tokeniser.isAppropriateEndTagToken());
 Assertions.assertEquals(new Token.EndTag("</nonsense>"), token);
 }

 @Test
 void testReadAndEmitErrorHandling() throws IOException {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.error(TokeniserState.TagName);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testReadAndEmitEndOfFileErrorHandling() throws IOException {
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 tokeniser.eofError(TokeniserState.Data);
 token = tokeniser.read();
 expectedToken = new Token.Error();
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testCreateCommentPending() throws IOException {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 expectedToken = new Token.Comment("--");
 Assertions.assertEquals(expectedToken, token);
 }

 @Test
 void testIsTrackErrors() throws IOException {
 tokeniser.setTrackErrors(true);
 errors.clear();
 tokeniser.error(TokeniserState.TagName);
 Assertions.assertEquals(1, errors.size());
 Assertions.assertNotNull(errors.get(0));
 }

 @Test
 void testSetTrackErrorsFalse() throws IOException {
 tokeniser.setTrackErrors(false);
 errors.clear();
 tokeniser.error(TokeniserState.TagName);
 Assertions.assertEquals(0, errors.size());
 }
}