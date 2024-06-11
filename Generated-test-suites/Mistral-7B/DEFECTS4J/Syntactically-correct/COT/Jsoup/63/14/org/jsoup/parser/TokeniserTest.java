import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader("");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_DataState() {
 // test reading data in Data state
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.Data, tokeniser.getState().readType);
 Assert.assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 }

 @Test
 public void testRead_StartTagState() {
 // test reading start tag in StartTag state
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_EndTagState() {
 // test reading end tag in EndTag state
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_SelfClosingTagState() {
 // test reading self-closing tag in SelfClosingTag state
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_ScriptState() {
 // test reading script in Script state
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.Script, tokeniser.read().getType());
 }

 @Test
 public void testRead_DoctypeState() {
 // test reading doctype in Doctype state
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 }

 @Test
 public void testRead_CommentState() {
 // test reading comment in Comment state
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 }

 @Test
 public void testTransition_DataStateToStartTagState() {
 // test transition from Data state to StartTag state
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 Assert.assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testTransition_StartTagStateToEndTagState() {
 // test transition from StartTag state to EndTag state
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 Assert.assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testTransition_EndTagStateToStartTagState() {
 // test transition from EndTag state to StartTag state
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 Assert.assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testTransition_SelfClosingTagStateToEndTagState() {
 // test transition from SelfClosingTag state to EndTag state
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 Assert.assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testTransition_EndTagStateToSelfClosingTagState() {
 // test transition from EndTag state to SelfClosingTag state
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.SelfClosingTag);
 Assert.assertEquals(TokeniserState.SelfClosingTag, tokeniser.getState());
 }

 @Test
 public void testTransition_DataStateToScriptState() {
 // test transition from Data state to Script state
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Script);
 Assert.assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testTransition_ScriptStateToDataState() {
 // test transition from Script state to Data state
 tokeniser.transition(TokeniserState.Script);
 tokeniser.advanceTransition(TokeniserState.Data);
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_DoctypeStateToDataState() {
 // test transition from Doctype state to Data state
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Data);
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_CommentStateToDataState() {
 // test transition from Comment state to Data state
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Data);
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testEmit_CharacterToken() {
 // test emitting a character token
 tokeniser.emit('a');
 Assert.assertEquals(Character.toString('a'), tokeniser.read().getData());
 }

 @Test
 public void testEmit_StartTagToken() {
 // test emitting a start tag token
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Assert.assertEquals("tagName", tokeniser.read().getData().tagName);
 }

 @Test
 public void testEmit_EndTagToken() {
 // test emitting an end tag token
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertEquals("tagName", tokeniser.read().getData().tagName);
 }

 @Test
 public void testEmit_ScriptToken() {
 // test emitting a script token
 tokeniser.createTempBuffer();
 tokeniser.emit("script");
 Assert.assertEquals("script", tokeniser.read().getData().getData());
 }

 @Test
 public void testEmit_DoctypeToken() {
 // test emitting a doctype token
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Assert.assertEquals("public", tokeniser.read().getData().getData());
 }

 @Test
 public void testEmit_CommentToken() {
 // test emitting a comment token
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Assert.assertEquals("--comment", tokeniser.read().getData().getData());
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacterReference() {
 // test consuming a valid character reference
 tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(Character.toString((char) 0x26), tokeniser.read().getData().getData());
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference() {
 // test consuming an invalid character reference
 tokeniser.consumeCharacterReference('x', false);
 Assert.assertEquals(Character.toString(Tokeniser.replacementChar), tokeniser.read().getData().getData());
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReferenceInAttribute() {
 // test consuming an invalid character reference in an attribute
 tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals(Character.toString(Tokeniser.replacementChar), tokeniser.read().getData().getData());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReference_UnexpectedCharacter() {
 // test consuming an unexpected character
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference('x', false);
 }

 @Test
 public void testUnescapeEntities_EmptyString() {
 // test unescaping an empty string
 Assert.assertEquals("", tokeniser.unescapeEntities(false));
 }

 @Test
 public void testUnescapeEntities_SimpleEntities() {
 // test unescaping simple entities
 Assert.assertEquals("&amp;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&lt;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&gt;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&nbsp;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&quot;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&apos;", tokeniser.unescapeEntities(false));
 }

 @Test
 public void testUnescapeEntities_NamedEntities() {
 // test unescaping named entities
 Assert.assertEquals("&copy;", tokeniser.unescapeEntities(false));
 Assert.assertEquals("&trade;", tokeniser.unescapeEntities(false));
 }

 @Test
 public void testUnescapeEntities_EntitiesWithAttributes() {
 // test unescaping entities with attributes
 StringBuilder html = new StringBuilder("<a href=\"http://example.com/\">Example</a>");
 Assert.assertEquals(html.toString(), tokeniser.unescapeEntities(true));
 }

}