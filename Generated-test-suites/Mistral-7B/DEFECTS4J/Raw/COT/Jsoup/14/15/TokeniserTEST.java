import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("Test input".getBytes()));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testRead_Character() {
 tokeniser.emit("A");
 tokeniser.emit("B");
 tokeniser.emit("C");
 tokeniser.reader.reset();

 Token token = tokeniser.read();
 Assert.assertEquals("A", token.getData());
 }

 @Test(expected = AssertionError.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.error(TokeniserState.Error);
 tokeniser.read();
 }

 @Test
 public void testEmit_Character() {
 tokeniser.read();
 tokeniser.emit('A');
 Token token = tokeniser.read();
 Assert.assertEquals("A", token.getData());
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("p"));
 Token tag = tokeniser.read();
 Assert.assertEquals("p", tag.tagName);
 }

 @Test(expected = AssertionError.class)
 public void testEmit_EndTag_AttributesPresent() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit(new Token.EndTag("p"));
 tokeniser.emit(new Token.Attribute("class", "test"));
 }

 @Test
 public void testEmit_String() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit("Test String");
 Token token = tokeniser.read();
 Assert.assertEquals("Test String", token.getData());
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.consumeCharacterReference(null, false);
 Token token = tokeniser.read();
 Assert.assertEquals("\u0041", token.getData()); // ASCII value of 'A'
 }

 @Test
 public void testConsumeCharacterReference_Invalid_Numbered() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.consumeCharacterReference('\u03B4', null); // Greek letter pi
 }

 @Test
 public void testConsumeCharacterReference_Invalid_Named() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.consumeCharacterReference("ampersand", true); // Test for attribute names
 }

 @Test
 public void testCreateTagPending_StartTag() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.emitTagPending();
 }

 @Test(expected = AssertionError.class)
 public void testCreateTagPending_EndTag() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTagPending(false);
 tokeniser.read();
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.emitTagPending();
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.createTagPending(false);
 tokeniser.read();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError_UnexpectedCharacter() {
 tokeniser.reader.reset();
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testError_UnexpectedEndOfFile() {
 tokeniser.reader.consume('\n');
 tokeniser.error(TokeniserState.Error);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.reader.reset();
 tokeniser.consumeCharacterReference('&', false);
 Assert.assertEquals(1, errors.size());
 }
}