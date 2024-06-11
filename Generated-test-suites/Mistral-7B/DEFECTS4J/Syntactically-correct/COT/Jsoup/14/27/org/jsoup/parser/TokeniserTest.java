import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setup() {
 charArrayInput characterInput = new charArrayInput(new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', ' ', '>'});
 characterReader = new CharacterReader(characterInput);
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testRead_Character_StateData() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume '<'
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.emit(' ');
 tokeniser.transition(TokeniserState.TagStart);
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tag.type);
 Assert.assertEquals("html", tag.tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRead_NullInput() {
 tokeniser = new Tokeniser(null);
 tokeniser.read();
 }

 @Test
 public void testRead_Character_StateTagStart() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.read(); // consume 'h'
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeCharacterReference(null, false); // consume 't'
 tokeniser.emit('a');
 tokeniser.emit('g');
 tokeniser.transition(TokeniserState.TagEnd);
 Token endTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
 Assert.assertEquals("html", endTag.tagName);
 }

 @Test
 public void testRead_Character_StateTagName() {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeCharacterReference(null, false); // consume 't'
 tokeniser.consumeCharacterReference("ag", true); // consume "ag"
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tag.type);
 Assert.assertEquals("tag", tag.tagName);
 }

 @Test
 public void testRead_Character_StateTagEnd() {
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.read(); // consume '/'
 tokeniser.consumeCharacterReference("tag", false); // consume "tag"
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tag.type);
 Assert.assertEquals("tag", tag.tagName);
 }

 @Test
 public void testRead_Character_StateComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit('*');
 tokeniser.consumeCharacterReference(null, false); // consume '-'
 tokeniser.consumeCharacterReference(null, false); // consume 'c'
 tokeniser.consumeCharacterReference(null, false); // consume 'o'
 tokeniser.consumeCharacterReference(null, false); // consume 'm'
 tokeniser.consumeCharacterReference(null, false); // consume 'm'
 tokeniser.consumeCharacterReference(null, false); // consume 'e'
 tokeniser.emit('*');
 Token comment = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, comment.type);
 Assert.assertNull(comment.tagName);
 Assert.assertEquals("comment", comment.data);
 }

 @Test
 public void testRead_Character_StateScriptData() {
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.emit('<');
 tokeniser.consumeCharacterReference(null, false); // consume 's'
 tokeniser.consumeCharacterReference("cript", false); // consume "script"
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference(null, false); // consume '<'
 tokeniser.consumeCharacterReference("script", false); // consume "script"
 tokeniser.consumeCharacterReference(null, false); // consume ">"
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.emit('<');
 tokeniser.consumeCharacterReference("/", false); // consume "/"
 tokeniser.consumeCharacterReference("s", false); // consume "s"
 tokeniser.consumeCharacterReference("c", false); // consume "c"
 tokeniser.consumeCharacterReference("r", false); // consume "r"
 tokeniser.consumeCharacterReference("i", false); // consume "i"
 tokeniser.consumeCharacterReference("p", false); // consume "p"
 tokeniser.consumeCharacterReference(">", false); // consume ">"
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("script", token.tagName);
 }

 @Test
 public void testRead_Character_StateDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.consumeCharacterReference("<!", false);
 tokeniser.consumeCharacterReference("d", false);
 tokeniser.consumeCharacterReference("o", false);
 tokeniser.consumeCharacterReference("c", false);
 tokeniser.consumeCharacterReference("t", false);
 tokeniser.consumeCharacterReference("y", false);
 tokeniser.consumeCharacterReference("p", false);
 tokeniser.consumeCharacterReference("e", false);
 tokeniser.consumeCharacterReference(" ", false);
 tokeniser.consumeCharacterReference("h", false);
 tokeniser.consumeCharacterReference("t", false);
 tokeniser.consumeCharacterReference("m", false);
 tokeniser.consumeCharacterReference("l", false);
 tokeniser.consumeCharacterReference(" ", false);
 tokeniser.consumeCharacterReference("p", false);
 tokeniser.consumeCharacterReference(">", false);
 Token doctype = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, doctype.type);
 Assert.assertNull(doctype.tagName);
 Assert.assertEquals("doctype", doctype.data);
 }

 @Test
 public void testRead_Character_StateError() {
 tokeniser.transition(TokeniserState.Error);
 tokeniser.error("Invalid character in input");
 Token error = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Error, error.type);
 Assert.assertEquals("Invalid character in input", error.data);
 }

 @Test
 public void testEmit_Character() {
 char c = 'h';
 tokeniser.emit(c);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals(new String(new char[]{c}), token.data);
 }

 @Test
 public void testEmit_Tag() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tag.type);
 Assert.assertEquals("html", tag.tagName);
 }

 @Test
 public void testEmit_Comment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token comment = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, comment.type);
 Assert.assertNull(comment.tagName);
 Assert.assertEquals("comment", comment.data);
 }

 @Test
 public void testEmit_Doctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token doctype = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, doctype.type);
 Assert.assertNull(doctype.tagName);
 Assert.assertEquals("doctype", doctype.data);
 }

 @Test
 public void testEmit_Error() {
 tokeniser.transition(TokeniserState.Error);
 tokeniser.error("Error message");
 Token error = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Error, error.type);
 Assert.assertEquals("Error message", error.data);
 }

 @Test
 public void testConsumeCharacterReference_DigitSequence() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference("123", null);
 Assert.assertEquals((char) 1, tokeniser.reader.current());
 tokeniser.consumeCharacterReference(null, false); // consume ';'
 }

 @Test
 public void testConsumeCharacterReference_NamedEntity() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference("amp", null);
 Assert.assertEquals('&', tokeniser.reader.current());
 tokeniser.consumeCharacterReference(";", false); // consume ';'
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_UnexpectedCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference("&", null); // consume '&'
 tokeniser.consumeCharacterReference(";", false); // consume ';'
 tokeniser.consumeCharacterReference("x", false); // consume 'x'
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 Assert.assertNotNull(tag);
 Assert.assertEquals(Token.TagType.START_TAG, tag.tagType);
 }

 @Test
 public void testCreateEndTagPending() {
 Token.Tag tag = tokeniser.createTagPending(false);
 Assert.assertNotNull(tag);
 Assert.assertEquals(Token.TagType.END_TAG, tag.tagType);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() {
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testTransitionStates() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.transition(TokeniserState.Error);
 }

 @Test
 public void testErrorHandling_UnexpectedCharacter() {
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error("Unexpected character in input");
 Assert.assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testErrorHandling_UnexpectedEndOfFile() {
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testErrorHandling_InvalidCharacterReference() {
 tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError();
 Assert.assertEquals(1, tokeniser.errors.size());
 }
}