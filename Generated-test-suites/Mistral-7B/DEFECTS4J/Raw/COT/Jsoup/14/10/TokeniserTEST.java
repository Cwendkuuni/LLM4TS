import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader("test.html");
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testRead_getDataState_ReturnsCorrectToken() {
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_TagState_ReturnsCorrectTagToken() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('a');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume('>');
 Token token = tokeniser.read();
 StartTag startTag = (StartTag) token;
 Assert.assertEquals("tag", startTag.tagName);
 }

 @Test(expected = AssertionError.class)
 public void testRead_WhenInvalidCharacterIsFound_ThrowsError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.mark();
 tokeniser.reader.consume('&');
 tokeniser.reader.unconsume();
 tokeniser.read();
 }

 @Test
 public void testEmit_CharacterToken() {
 tokeniser.emit(new Character("a"));
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("a", token.asCharacter().getText());
 }

 @Test
 public void testEmit_StartTagToken() {
 StartTag startTag = new StartTag("tag");
 tokeniser.emit(startTag);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("tag", token.asStartTag().getTagName());
 }

 @Test
 public void testEmit_EndTagToken() {
 EndTag endTag = new EndTag("tag");
 tokeniser.emit(endTag);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("tag", token.asEndTag().getTagName());
 }

 @Test
 public void testEmit_CommentToken() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, token.type);
 Assert.assertTrue(token.asComment().getText().startsWith("--"));
 }

 @Test
 public void testEmit_DoctypeToken() {
 Doctype doctype = new Doctype("html", null, null, null);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 tokeniser.reader.consume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, token.type);
 Assert.assertEquals("html", token.asDoctype().getName());
 }

 @Test(expected = AssertionError.class)
 public void testEmit_WhenInvalidTokenIsEmitted_ThrowsError() {
 tokeniser.emit(new Token("invalid"));
 }

 @Test
 public void testTransition_DataState_ToTagState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('a');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume('>');
 Assert.assertEquals(TokeniserState.Tag, tokeniser.getState().ordinal());
 }

 @Test
 public void testAdvanceTransition_DataState_ToTagState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Tag);
 Assert.assertEquals(TokeniserState.Tag, tokeniser.getState().ordinal());
 }

 @Test(expected = AssertionError.class)
 public void testAdvanceTransition_ToInvalidState_ThrowsError() {
 tokeniser.transition(TokeniserState.Data);tokeniser.advanceTransition(TokeniserState.DOCTYPE);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacterReference_ReturnsCorrectCharacter() {
 tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('a', tokeniser.read().asCharacter().getText().charAt(0));
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference_ReturnsReplacementChar() {
 tokeniser.consumeCharacterReference('\uFFFD', false);
 Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
 Assert.assertEquals(Tokeniser.replacementChar, tokeniser.read().asCharacter().getText().charAt(0));
 }

 @Test
 public void testConsumeCharacterReference_InAttribute_ReturnsReplacementChar() {
 tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
 Assert.assertEquals(Tokeniser.replacementChar, tokeniser.read().asCharacter().getText().charAt(0));
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReference_InvalidCharacterReferenceFormat_ThrowsError() {
 tokeniser.consumeCharacterReference('&', false);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReference_InvalidCharacterReferenceInAttributeFormat_ThrowsError() {
 tokeniser.consumeCharacterReference('&', true);
 }
}