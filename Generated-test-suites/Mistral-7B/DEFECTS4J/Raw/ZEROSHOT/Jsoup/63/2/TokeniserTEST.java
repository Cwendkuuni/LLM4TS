package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTest;
import org.jsoup.parser.Tokeniser._Token;
import org.jsoup.parser.Tokeniser._Token.Attribute;
import org.jsoup.parser.Tokeniser._Token.Character;
import org.jsoup.parser.Tokeniser._Token.Comment;
import org.jsoup.parser.Tokeniser._Token.Doctype;
import org.jsoup.parser.Tokeniser._Token.EndTag;
import org.jsoup.parser.Tokeniser._Token.StartTag;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList errorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 characterReader = new CharacterReader("test.html");
 errorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errorList);
 }

 @Test
 public void testRead_NoCharacterPending() {
 Token token = tokeniser.read();
 assertNull(token);
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterPending() {
 tokeniser.emit("Hello World");
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("Hello World", token.data());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_MultipleCharactersPending() {
 tokeniser.emit("Hello ");
 tokeniser.emit("World");
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("Hello World", token.data());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferencePending() {
 tokeniser.emit("#x26;"); // "&"
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("&", token.data());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferencePending_Invalid() {
 tokeniser.emit("#abcd;");
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertNull(token);
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferencePending_Hex() {
 tokeniser.emit("#x1F44D;"); // "𠓏"
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("\uD840\uDC4D", token.data());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferencePending_Named() {
 tokeniser.emit("#&auml;"); // "ä"
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("ä", token.data());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferencePending_InvalidNamed() {
 tokeniser.emit("#&abcd;");
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertNull(token);
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_SelfClosingFlagPending() {
 tokeniser.emit("<br />");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 assertTrue(token.isSelfClosing());
 }

 @Test
 public void testRead_SelfClosingFlagPending_Invalid() {
 tokeniser.emit("<br>");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_StartTagPending() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("<html>");
 tokeniser.emit("<head>");
 token = tokeniser.read();
 assertTrue(token.isStartTag());
 assertEquals("html", token.tagName());
 assertNull(token.tagBody());
 assertNull(token.attributes());
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_StartTagPending_SelfClosing() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("<img src='image.jpg' />");
 token = tokeniser.read();
 assertTrue(token.isStartTag());
 assertEquals("img", token.tagName());
 assertFalse(token.tagBody() == null);
 assertNull(token.attributes());
 assertTrue(token.isSelfClosing());
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_StartTagPending_Invalid() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("<html>");
 tokeniser.emit("<head>");
 tokeniser.emit(">"); // missing tag name
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_StartTagPending_InvalidAttributes() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("<img src='image.jpg' onclickevent='click'>");
 token = tokeniser.read();
 assertTrue(token.isStartTag());
 assertEquals("img", token.tagName());
 Attribute attributes = token.attributes();
 assertEquals("onclickevent", attributes.keySet().iterator().next());
 assertEquals("click", attributes.get("onclickevent"));
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_StartTagPending_InvalidAttributesValue() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("<img src='image.jpg' alt=123>");
 token = tokeniser.read();
 assertTrue(token.isStartTag());
 assertEquals("img", token.tagName());
 Attribute attributes = token.attributes();
 assertEquals("alt", attributes.keySet().iterator().next());
 assertEquals("123", attributes.get("alt"));
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_EndTagPending() {
 tokeniser.emit("<html>");
 tokeniser.emit("<head>");
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit("</html>");
 token = tokeniser.read();
 assertTrue(token.isEndTag());
 assertEquals("html", token.tagName());
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CommentPending() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit("<!-- This is a comment -->");
 token = tokeniser.read();
 assertTrue(token.isComment());
 assertEquals("This is a comment", token.data());
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_DoctypePending() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emit("<!DOCTYPE html>");
 token = tokeniser.read();
 assertTrue(token.isDoctype());
 assertEquals("html", token.name());
 assertEquals("HTML", token.publicId());
 assertEquals("", token.systemId());
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_Error() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_EOF() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("numeric reference with no numerals");
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_InvalidNamed() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_Named() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 tokeniser.consumeCharacterReference(null, false);
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_Named_InAttribute() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 tokeniser.consumeCharacterReference(null, true);
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_InvalidNamed_InAttribute() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_Named_InAttribute_Invalid() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume('=');
 tokeniser.consumeAny("=+-_"); // attributes separator
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CharacterReferenceError_Named_InAttribute_InvalidChar() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("invalid named referenece '%s'");
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume('=');
 tokeniser.consumeAnySorted(notCharRefCharsSorted); // attributes separator
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isEmitPending());
 }
}