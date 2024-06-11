import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_StateData_ReturnsCharacterToken() {
 given(reader.current()).willReturn('a');
 given(reader.read()).willReturn('a');

 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("a", token.toString());
 }

 @Test
 public void testRead_StateData_ReturnsPendingToken() {
 given(reader.current()).willReturn('<');
 given(reader.read()).willReturn('<');
 given(reader.current()).willReturn('>');

 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume '<'
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.read(); // consume '>'

 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 }

 @Test(expected = NullPointerException.class)
 public void testRead_NoPendingToken_ThrowsException() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.read();

 assertNull(tokeniser.read());
 }

 @Test
 public void testEmit_CharacterToken() {
 tokeniser.emit(new Character('a'));
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("a", token.toString());
 }

 @Test
 public void testEmit_StartTagToken() {
 StartTag startTag = new StartTag("div", new ArrayList<>());
 tokeniser.emit(startTag);
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("<div>", token.toString());
 }

 @Test
 public void testEmit_EndTagToken() {
 EndTag endTag = new EndTag("div");
 tokeniser.emit(endTag);
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("</div>", token.toString());
 }

 @Test
 public void testEmit_CommentToken() {
 Comment comment = new Comment("-- This is a comment");
 tokeniser.emit(comment);
 Token token = tokeniser.read();
 assertEquals(TokenType.Comment, token.type);
 assertEquals("-- This is a comment", token.toString());
 }

 @Test
 public void testEmit_DoctypeToken() {
 Doctype doctype = new Doctype("html", new ArrayList<>());
 tokeniser.emit(doctype);
 Token token = tokeniser.read();
 assertEquals(TokenType.Doctype, token.type);
 assertEquals("<!DOCTYPE html>", token.toString());
 }

 @Test
 public void testCharacterReference_NumericReference_ReturnsCharacter() {
 given(reader.current()).willReturn('#');
 given(reader.consume()).willReturn('X');
 given(reader.consumeHexSequence()).willReturn("41");
 given(reader.consume()).willReturn(';');

 when(reader.current()).thenReturn('a');

 tokeniser.consumeCharacterReference(null, false);
 assertEquals('A', tokeniser.read().toString().charAt(0));
 }

 @Test
 public void testCharacterReference_NamedReference_ReturnsCharacter() {
 given(reader.current()).willReturn('#');
 given(reader.consume()).willReturn('A');
 given(reader.consume()).willReturn(';');
 given(reader.matchConsume(';')).willReturn(true);
 given(Entities.getCharacterByName("amp")).willReturn('&');

 tokeniser.consumeCharacterReference(null, false);
 assertEquals('&', tokeniser.read().toString().charAt(0));
 }

 @Test
 public void testCharacterReference_InvalidReference_ReturnsNull() {
 given(reader.current()).willReturn('#');given(reader.consume()).willReturn('A');
 given(reader.consume()).willReturn(';');
 given(reader.matchConsume(';')).willReturn(false);

 tokeniser.consumeCharacterReference(null, false);
 assertNull(tokeniser.read());
 }

 @Test
 public void testCharacterReference_InvalidReferenceWithSemicolon_ReturnsNull() {
 given(reader.current()).willReturn('#');
 given(reader.consume()).willReturn('A');
 given(reader.consume()).willReturn(';');
 given(reader.consume()).willReturn(';');

 tokeniser.consumeCharacterReference(null, false);
 assertNull(tokeniser.read());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReference_InvalidCharacter_ThrowsException() {
 given(reader.current()).willReturn('#');
 given(reader.consume()).willReturn('X');
 given(reader.consumeHexSequence()).willReturn("FFFFFFFD");
 given(reader.consume()).willReturn(';');

 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testCreateTagPending_StartTag() {
 StartTag tag = tokeniser.createTagPending(true);
 assertEquals(TokenType.StartTag, tag.type);
 tag.tagName = "div";
 assertEquals("div", tag.toString());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 EndTag tag = tokeniser.createTagPending(false);
 assertEquals(TokenType.EndTag, tag.type);
 tag.tagName = "div";
 assertEquals("</div>", tag.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateTagPending_InvalidTagType() {
 tokeniser.createTagPending(false);
 }

 @Test
 public void testEmitTagPending_StartTag() {
 StartTag tag = new StartTag("div", new ArrayList<>());
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("<div>", token.toString());
 }

 @Test
 public void testEmitTagPending_EndTag() {
 EndTag tag = new EndTag("div");
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("</div>", token.toString());
 }

 @Test
 public void testCreateCommentPending() {
 Comment comment = tokeniser.createCommentPending();
 assertEquals(TokenType.Comment, comment.type);
 assertEquals("--", comment.toString());
 }

 @Test
 public void testEmitCommentPending() {
 Comment comment = new Comment("-- This is a comment");
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.Comment, token.type);
 assertEquals("-- This is a comment", token.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitCommentPending_PendingCommentAlreadyExists() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 }

 @Test
 public void testCreateDoctypePending() {
 Doctype doctype = tokeniser.createDoctypePending();
 assertEquals(TokenType.Doctype, doctype.type);
 assertEquals("<!DOCTYPE", doctype.toString());
 }

 @Test
 public void testEmitDoctypePending() {
 Doctype doctype = new Doctype("html", new ArrayList<>());
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertEquals(TokenType.Doctype, token.type);
 assertEquals("<!DOCTYPE html>", token.toString());
 }

 @Test
 public void testIsAppropriateEndTagToken_True() {
 StartTag startTag = new StartTag("div", new ArrayList<>());
 tokeniser.emit(startTag);
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTempBuffer();
 tokeniser.read(); // consume <
 tokeniser.read(); // consume div
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.read(); // consume >

 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() {
 StartTag startTag = new StartTag("div", new ArrayList<>());
 tokeniser.emit(startTag);
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTempBuffer();
 tokeniser.read(); // consume <
 tokeniser.read(); // consume div
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume >

 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError_NullPointerException() {
 tokeniser.error(null);
 }

 @Test
 public void testError_ParseError() {
 tokeniser.error(new ParseError(1, "Expected TagStart but was <"));
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.TagStart);
 }

 @Test
 public void testCharacterReferenceError_InvalidReference() {
 tokeniser.characterReferenceError("Invalid character reference: numeric reference with no numerals");
 }

 @Test
 public void testCharacterReferenceError_NamedReference_Invalid() {
 tokeniser.characterReferenceError("Invalid named referenece '%s'");
 }
}