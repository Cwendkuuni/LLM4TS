import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("testInput.html");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 Token token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals('testInput.html', token.source.name());
 assertEquals("", token.data);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 reader.mark(1);
 reader.consume('i');
 tokeniser.read();
 Token token = tokeniser.read();
 assertTrue(token instanceof EndTag);
 assertEquals("input", token.tagName);
 reader.rewindToMark();
 }

 @Test(expected = IllegalStateException.class)
 public void testReadSelfClosingTagWithoutAcknowledgement() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.read();
 tokeniser.read(); // read character first
 reader.consume('i');
 tokeniser.read();
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 reader.mark(1);
 reader.consume('<');
 reader.consume('a');
 reader.consume(' ');
 reader.consume('t');
 reader.consume('t');
 reader.consume('r');
 reader.consume(' ');
 reader.consume('i');
 reader.consume(' ');
 reader.consume('s');
 reader.consume(' ');
 reader.consume('e');
 reader.consume(' ');
 reader.consume('t');
 reader.consume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 assertTrue(token instanceof StartTag);
 assertEquals("iset", token.tagName);
 reader.rewindToMark();
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 reader.mark(1);
 reader.consume('/');
 reader.consume('i');
 reader.consume(' ');
 reader.consume('s');
 reader.consume('e');
 reader.consume('t');
 reader.consume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 assertTrue(token instanceof EndTag);
 assertEquals("iset", token.tagName);
 reader.rewindToMark();
 }

 @Test
 public void testReadInvalidEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 reader.mark(1);
 reader.consume('/');
 reader.consume('i');
 reader.consume('s');
 reader.consume('e');
 reader.consume('x'); // invalid character
 reader.consume('>');
 tokeniser.read();
 tokeniser.read(); // read character first
 tokeniser.read();
 reader.rewindToMark();
 }

 @Test
 public void testReadComment() {
 tokeniser.transition(TokeniserState.Comment);
 reader.mark(1);
 reader.consume('<!--');
 reader.consumeAny('<', '>');
 reader.consume('-->');
 tokeniser.read();
 Token token = tokeniser.read();
 assertTrue(token instanceof Comment);
 reader.rewindToMark();
 }

 @Test
 public void testReadDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 reader.mark(1);
 reader.consume('<');
 reader.consume('!');
 reader.consume('d');
 reader.consume('o');
 reader.consume('c');
 reader.consume('t');
 reader.consume('y');
 reader.consume('p');
 reader.consume(' ');
 reader.consume('h');
 reader.consume('t');
 reader.consume('m');
 reader.consume('l');
 reader.consume(' ');
 reader.consume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 assertTrue(token instanceof Doctype);
 reader.rewindToMark();
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 assertTrue(tag instanceof StartTag);
 tag.finaliseTag();
 tokeniser.emitTagPending();
 Token emittedTag = tokeniser.read();
 assertEquals(tag.tagName, emittedTag.tagName);
 }

 @Test
 public void testCreateCommentPending() {
 Token.Comment comment = tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token emittedComment = tokeniser.read();
 assertEquals(comment.data, emittedComment.data);
 }

 @Test
 public void testTransitionStates() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.transition(TokeniserState.Doctype);
 }

 @Test
 public void testErrorHandling() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.error("Unexpected character '%s' in input state [%s]", 'x', TokeniserState.StartTag);
 }

 @Test
 public void testCharacterReference() {
 tokeniser.emit("&amp;");
 Token token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&amp;", token.data);
 tokeniser.emit("&lt;");
 token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&lt;", token.data);
 tokeniser.emit("&gt;");
 token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&gt;", token.data);
 tokeniser.emit("&apos;");
 token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&apos;", token.data);
 tokeniser.emit("&quot;");
 token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&quot;", token.data);
 tokeniser.emit("&#123;"); // invalid character reference
 tokeniser.error("invalid character reference '%#123;'");
 }

 @Test
 public void testCharacterReferenceWithAttributes() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("&amp;class=test&amp;id=test&quot;");
 Token startTag = tokeniser.read();
 assertTrue(startTag instanceof StartTag);
 List<Attribute> attributes = startTag.attributes;
 assertEquals(2, attributes.size());
 Attribute classAttribute = attributes.get(0);
 assertEquals("class", classAttribute.name);
 assertEquals("test", classAttribute.value);
 Attribute idAttribute = attributes.get(1);
 assertEquals("id", idAttribute.name);
 assertEquals("test", idAttribute.value);
 }

 @Test
 public void testCharacterReferenceNamed() {
 tokeniser.transition(TokeniserState.Text);
 tokeniser.emit("&copy;");
 Token token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("&copy;", token.data);
 }

 @Test
 public void testCharacterReferenceNamedWithAttributes() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("&copy;class=test&amp;id=test&quot;");
 Token startTag = tokeniser.read();
 assertTrue(startTag instanceof StartTag);
 List<Attribute> attributes = startTag.attributes;
 assertEquals(2, attributes.size());
 Attribute classAttribute = attributes.get(0);
 assertEquals("class", classAttribute.name);
 assertEquals("test", classAttribute.value);
 Attribute idAttribute = attributes.get(1);
 assertEquals("id", idAttribute.name);
 assertEquals("test", idAttribute.value);
 }

 @Test
 public void testCreateDoctypePending() {
 Token.Doctype doctype = tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token emittedDoctype = tokeniser.read();
 assertEquals(doctype.publicId, emittedDoctype.publicId);
 assertEquals(doctype.systemId, emittedDoctype.systemId);
 }
}