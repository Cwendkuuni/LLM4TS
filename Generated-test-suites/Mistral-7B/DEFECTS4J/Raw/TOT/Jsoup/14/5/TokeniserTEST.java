import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader characterReader;
 private Tokeniser tokeniser;

 @Test
 public void testRead_SimpleCharacter() throws Exception {
 characterReader = new CharacterReader("abc");
 tokeniser = new Tokeniser(characterReader);

 Token token = tokeniser.read();
 assertNull(token.tagName);
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.text);
 }

 @Test
 public void testRead_SelfClosingTag() throws Exception {
 characterReader = new CharacterReader("<img src=\"image.jpg\" />");
 tokeniser = new Tokeniser(characterReader);

 Token token = tokeniser.read();
 assertEquals("img", token.tagName);
 assertTrue(token.selfClosing);
 }

 @Test
 public void testRead_ErrorHandling() throws Exception {
 characterReader = new CharacterReader("<html><script>");
 tokeniser = new Tokeniser(characterReader);
 tokeniser.setTrackErrors(true);

 tokeniser.read(); // should read <
 tokeniser.read(); // should read html
 tokeniser.read(); // should read <
 tokeniser.read(); // should read !
 tokeniser.read(); // should read DCD
 tokeniser.read(); // should read <
 tokeniser.read(); // should read script
 tokeniser.read(); // should read >
 tokeniser.read(); // should throw an error and set isTrackErrors to false

 assertFalse(tokeniser.isTrackErrors());
 assertNull(tokeniser.read()); // should return null after an error
 }

 @Test
 public void testEmit_Character() throws Exception {
 characterReader = new CharacterReader("abc");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('c'));

 Token token = tokeniser.read();
 assertNull(token.tagName);
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("c", token.text);
 }

 @Test
 public void testEmit_Tag() throws Exception {
 characterReader = new CharacterReader("<p>Hello World</p>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.Character('H'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.Character('W'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('d'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.EndTag("p"));

 Token token = tokeniser.read();
 assertEquals("p", token.tagName);
 assertTrue(token.selfClosing);
 }

 @Test
 public void testEmit_Comment() throws Exception {
 characterReader = new CharacterReader("<!-- This is a comment -->");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createCommentPending();
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('!'));
 tokeniser.emit(new Token.Character('--'));
 tokeniser.emitCommentPending();

 Token token = tokeniser.read();
 assertEquals("comment", token.tagName);
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("This is a comment", token.text);
 }

 @Test
 public void testEmit_Doctype() throws Exception {
 characterReader = new CharacterReader("<!DOCTYPE html>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createDoctypePending();
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('!'));
 tokeniser.emit(new Token.Character('D'));
 tokeniser.emit(new Token.Character('O'));
 tokeniser.emit(new Token.Character('C'));
 tokeniser.emit(new Token.Character('T'));ype >
 tokeniser.emit(new Token.Character('Y'));
 tokeniser.emit(new Token.Character('P'));
 tokeniser.emit(new Token.Character('E'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.Character('h'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('m'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emitDoctypePending();

 Token token = tokeniser.read();
 assertEquals("doctype", token.tagName);
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testGetState_Data() throws Exception {
 characterReader = new CharacterReader("abc");
 tokeniser = new Tokeniser(characterReader);

 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testGetState_AfterStartTag() throws Exception {
 characterReader = new CharacterReader("<p>Hello World</p>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emitStartTag("p");
 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testGetState_AfterEndTag() throws Exception {
 characterReader = new CharacterReader("<p>Hello World</p>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emitStartTag("p");
 tokeniser.emitEndTag("p");
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testGetState_AfterComment() throws Exception {
 characterReader = new CharacterReader("<!-- This is a comment -->");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertEquals(TokeniserState.Comment, tokeniser.getState());
 }

 @Test
 public void testGetState_AfterDoctype() throws Exception {
 characterReader = new CharacterReader("<!DOCTYPE html>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 assertEquals(TokeniserState.Doctype, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() throws Exception {
 characterReader = new CharacterReader("<img src=\"image.jpg\" />");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals(false, tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_SimpleCharacter() throws Exception {
 characterReader = new CharacterReader("&#133;");
 tokeniser = new Tokeniser(characterReader);

 assertEquals((char) 133, tokeniser.consumeCharacterReference(null, false));
 }

 @Test
 public void testConsumeCharacterReference_NamedCharacter() throws Exception {
 characterReader = new CharacterReader("&copy;");
 tokeniser = new Tokeniser(characterReader);

 assertEquals('\u00A9', tokeniser.consumeCharacterReference(null, false));
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference() throws Exception {
 characterReader = new CharacterReader("&#10000;");
 tokeniser = new Tokeniser(characterReader);

 assertEquals(replacementChar, tokeniser.consumeCharacterReference(null, false));
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReferenceInAttribute() throws Exception {
 characterReader = new CharacterReader("<div style=\"color:#FooFFF\"></div>");
 tokeniser = new Tokeniser(characterReader);

 assertEquals(null, tokeniser.consumeCharacterReference(null, true));
 }

 @Test
 public void testCreateTagPending_StartTag() throws Exception {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createTagPending(true);

 assertEquals("StartTag", tokeniser.tagPending.tagName);
 }

 @Test
 public void testCreateTagPending_EndTag() throws Exception {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createTagPending(false);

 assertEquals("EndTag", tokeniser.tagPending.tagName);
 }

 @Test
 public void testEmitTagPending_StartTag() throws Exception {
 characterReader = new CharacterReader("pHello World");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 assertEquals("p", tokeniser.lastStartTag.tagName);
 }

 @Test
 public void testEmitTagPending_EndTag() throws Exception {
 characterReader = new CharacterReader("pHello World</p>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();

 assertEquals("p", tokeniser.lastStartTag.tagName);
 }

 @Test
 public void testCreateTempBuffer() throws Exception {
 characterReader = new CharacterReader("<script>var foo = \"<b>bar</b>\";</script>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.createTempBuffer();
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('c'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('i'));
 tokeniser.emit(new Token.Character('p'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('/'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emitTempBuffer();

 Token token = tokeniser.read();
 assertEquals("script", token.tagName);
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testIsAppropriateEndTagToken() throws Exception {
 characterReader = new CharacterReader("</p></p>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emitStartTag("p");
 tokeniser.emitTagPending();
 tokeniser.emitStartTag("p");
 tokeniser.emitTagPending();

 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() throws Exception {
 characterReader = new CharacterReader("</p></div>");
 tokeniser = new Tokeniser(characterReader);

 tokeniser.emitStartTag("p");
 tokeniser.emitTagPending();
 tokeniser.emitStartTag("div");
 tokeniser.emitTagPending();

 assertFalse(tokeniser.isAppropriateEndTagToken());
 }
}