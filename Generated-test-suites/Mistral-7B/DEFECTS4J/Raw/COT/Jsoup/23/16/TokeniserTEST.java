import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("Invalid input stream");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_Character() {
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals("A", token.getData());
 }

 @Test
 public void testRead_StartTag() {
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 token = tokeniser.read();
 assertTrue(token instanceof Token.StartTag);
 assertEquals("div", ((Token.StartTag) token).tagName);
 }

 @Test
 public void testRead_EndTag() {
 tokeniser.emit(new Token.EndTag("div"));
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 assertTrue(token instanceof Token.EndTag);
 assertEquals("div", ((Token.EndTag) token).tagName);
 }

 @Test
 public void testRead_Error() {
 reader.mark();
 reader.matchConsume('#');
 tokeniser = new Tokeniser(reader, errors);
 token = tokeniser.read();
 assertEquals(null, token);
 Validate.isTrue(errors.size() > 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_PendingEmit() {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.isEmitPending = true;
 token = tokeniser.read();
 fail("A token was expected to be emitted before reading.");
 }

 @Test
 public void testEmit_Character() {
 List<Token> tokens = new ArrayList<>();
 reader.init("ABC");
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('A');
 token = tokeniser.read();
 tokens.add(token);
 tokeniser.emit('B');
 token = tokeniser.read();
 tokens.add(token);
 tokeniser.emit('C');
 token = tokeniser.read();
 tokens.add(token);
 assertEquals(3, tokens.size());
 }

 @Test
 public void testEmit_StartTag() {
 List<Token> tokens = new ArrayList<>();
 reader.init("<div>ABC</div>");
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit('A');
 tokens.add(tokeniser.read());
 tokeniser.emit('B');
 tokens.add(tokeniser.read());
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 tokens.add(token);
 assertEquals(4, tokens.size());
 }

 @Test
 public void testEmit_EndTag() {
 List<Token> tokens = new ArrayList<>();
 reader.init("<div><p>ABC</p></div>");
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 tokens.add(token);
 assertEquals(4, tokens.size());
 }

 @Test
 public void testEmit_Comment() {
 List<Token> tokens = new ArrayList<>();
 reader.init("<!-- Test -->");
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createCommentPending();
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 tokens.add(token);
 assertEquals(1, tokens.size());
 }

 @Test
 public void testRead_SelfClosingTag() {
 tokeniser.emit(new Token.StartTag("img", "src=\"image.jpg\""));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 assertTrue(token.selfClosing);
 }

 @Test
 public void testRead_ErrorHandling() {
 reader.init("<div><p>Invalid<script>alert('XSS')</script></p></div>");
 tokeniser = new Tokeniser(reader, errors);
 List<Token> tokens = new ArrayList<>();
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read(); // Read opening div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read <p> tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read <script> tag
 Validate.isTrue(errors.size() > 0);
 tokeniser.error(TokeniserState.Data);

 token = tokeniser.read(); // Read 'A' character before <script> tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.Data);

 token = tokeniser.read(); // Read </script> tag
 Validate.isTrue(errors.size() > 1);
 tokeniser.error(TokeniserState.Data);

 token = tokeniser.read(); // Read closing p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 token = tokeniser.read(); // Read closing div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 assertEquals(7, tokens.size());
 }

 @Test
 public void testRead_UnicodeCharacterReference() {
 reader.init("&#x1F600;");
 tokeniser = new Tokeniser(reader, errors);
 token = tokeniser.read();
 assertEquals((char) 0x1F600, token.getData().charAt(0));
 }

 @Test
 public void testRead_NamedCharacterReference() {
 reader.init("&copy;");
 tokeniser = new Tokeniser(reader, errors);
 token = tokeniser.read();
 assertEquals('\u00A9', token.getData().charAt(0));
 }

 @Test
 public void testRead_NamedCharacterReference_Invalid() {
 reader.init("&Invalid;");
 tokeniser = new Tokeniser(reader, errors);
 token = tokeniser.read();
 assertNull(token);
 Validate.isTrue(errors.size() > 0);
 }

 @Test
 public void testRead_NamedCharacterReference_InAttribute() {
 reader.init("<p class=\"&copy;\" />");
 tokeniser = new Tokeniser(reader, errors);
 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read 'class' attribute
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.AttributeValue);
 token = tokeniser.read(); // Read '&copy;' character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.emitCharacterReference(null, true);
 tokeniser.transition(TokeniserState.AttributeValue);
 token = tokeniser.read(); // Read closing '>' tag
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read(); // Read closing p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 assertEquals(3, tokens.size());
 }

 @Test
 public void testRead_EndTag_Appropriate() {
 reader.init("<div><p><p></p></p></div>");
 tokeniser = new Tokeniser(reader, errors);
 List<Token> tokens = new ArrayList<>();
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read(); // Read opening div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read closing p tag
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read(); // Read closing p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 token = tokeniser.read(); // Read closing div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 assertEquals(8, tokens.size());
 }

 @Test(expected = AssertionError.class)
 public void testRead_EndTag_Inappropriate() {
 reader.init("<div><p><p><p></p></p></div>");
 tokeniser = new Tokeniser(reader, errors);
 List<Token> tokens = new ArrayList<>();
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read(); // Read opening div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read closing p tag
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag); // Should be tagName of next opening tag, not end tag
 token = tokeniser.read(); // Read opening p tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.StartTag);

 token = tokeniser.read(); // Read closing p tag
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.EndTag); // Should be tagName of next opening tag, not end tag
 token = tokeniser.read(); // Read closing div tag
 tokens.add(token);
 tokeniser.emit(token);
 tokeniser.transition(TokeniserState.EndTag);

 assertEquals(8, tokens.size());
 fail("An inappropriate end tag was accepted.");
 }

}