import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ByteArrayInputStream inputStream;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("Test Input".getBytes());
 tokeniser = new Tokeniser(inputStream);
 errors = new ArrayList<>();
 tokeniser.setTrackErrors(true);
 }

 @Test
 public void testRead_NormalCharacter() {
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("Test", token.toString());
 }

 @Test
 public void testRead_SelfClosingTag() {
 tokeniser.transition(TokeniserState.TagOpen);
 tokeniser.emit(createTagPending(true));
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 @Test
 public void testRead_InvalidCharacterReference() {
 tokeniser.transition(TokeniserState.Character);
 tokeniser.emit((char) 0xFFFE);
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals("\uFFFE", tokeniser.read().toString());
 tokeniser.error("Unexpected character in input");
 }

 @Test
 public void testRead_NamedCharacterReference() {
 tokeniser.transition(TokeniserState.Character);
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit('m');
 tokeniser.emit('p');
 tokeniser.emit(';');
 assertEquals(CharacterReference.AMPERSAND, tokeniser.read().type);
 assertEquals("amp", tokeniser.read().toString());
 }

 @Test
 public void testRead_SelfClosingTagWithAttributes() {
 tokeniser.transition(TokeniserState.TagOpen);
 tokeniser.emit(createTagPending(true));
 tokeniser.emit(" onfocus=\"test()\"");
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 @Test(expected = AssertionError.class)
 public void testRead_UnexpectedEndTag() {
 tokeniser.transition(TokeniserState.TagOpen);
 tokeniser.emit(createTagPending(true));
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit("div");
 tokeniser.transition(TokeniserState.TagAttributes);
 tokeniser.emit(" class=\"test\"");
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.emit("script");
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 fail("Unexpected EndTag token: " + token);
 }

 @Test
 public void testRead_EmptyElement() {
 tokeniser.transition(TokeniserState.TagOpen);
 tokeniser.emit(createTagPending(true));
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit("img");
 tokeniser.transition(TokeniserState.TagAttributes);
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("img", token.tagName);
 }

 @Test
 public void testRead_IgnoreComment() {
 tokeniser.transition(TokeniserState.TagOpen);
 tokeniser.emit(createTagPending(true));
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.emit("script");
 tokeniser.transition(TokeniserState.TagAttributes);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit("<!-- This is a comment -->");
 tokeniser.transition(TokeniserState.CommentEnd);
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 @Test(expected = AssertionError.class)
 public void testRead_UnexpectedCharacterReference() {
 tokeniser.transition(TokeniserState.Character);
 tokeniser.emit('&');
 tokeniser.emit('#');
 tokeniser.error("Unexpected character in input");
 }

 @Test
 public void testRead_EOF() {
 tokeniser.transition(TokeniserState.Character);
 inputStream.close();
 Token token = tokeniser.read();
 assertTrue(token.type == TokenType.Eof);
 assertNull(token.toString());
 }

 @Test
 public void testEmit_Character() {
 tokeniser.read();
 tokeniser.emit(new Token.Character("a"));
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("a", token.toString());
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("div", null, null, null, null, false));
 Token tag = tokeniser.read();
 assertEquals(TokenType.StartTag, tag.type);
 assertEquals("div", tag.tagName);
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.read();
 tokeniser.emit(new Token.EndTag("div"));
 Token tag = tokeniser.read();
 assertEquals(TokenType.EndTag, tag.type);
 assertEquals("div", tag.tagName);
 }

 @Test
 public void testEmit_Comment() {
 tokeniser.read();
 tokeniser.emitCommentPending();
 Token tag = tokeniser.read();
 assertEquals(TokenType.Comment, tag.type);
 assertEquals("", tag.toString());
 }

 @Test
 public void testEmit_Doctype() {
 tokeniser.read();
 tokeniser.emitDoctypePending();
 Token tag = tokeniser.read();
 assertEquals(TokenType.Doctype, tag.type);
 assertEquals("", tag.toString());
 }

 @Test
 public void testErrorHandling_UnexpectedCharacter() {
 inputStream = new ByteArrayInputStream("Test<".getBytes());
 tokeniser = new Tokeniser(inputStream);
 tokeniser.setTrackErrors(true);
 tokeniser.read();
 tokeniser.error("Unexpected character in input");
 assertEquals(1, errors.size());
 assertEquals("Unexpected character in input", errors.get(0).getMessage());
 }

 @Test
 public void testErrorHandling_EOF() {
 inputStream = new ByteArrayInputStream("Test<".getBytes());
 tokeniser = new Tokeniser(inputStream);
 tokeniser.setTrackErrors(true);
 tokeniser.read();
 tokeniser.read();
 inputStream.close();
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @After
 public void tearDown() {
 inputStream.close();
 tokeniser = null;
 errors.clear();
 }

}