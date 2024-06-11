import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Parser parser;
 private ParseSettings settings;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 parser = new Parser(builder);
 settings = builder.defaultSettings();
 }

 // Test for all methods in XmlTreeBuilder class

 @Test
 public void testDefaultSettings() {
 ParseSettings defaultSettings = builder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, defaultSettings.getClass());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml></xml>";
 builder.parse(new StringReader(input), "");
 Document doc = builder.doc;
 assertNotNull(doc);
 assertEquals("xml", doc.nodeName());
 assertFalse(doc.outputSettings().isSynthetic());
 assertTrue(stackContains(doc));
 }

 @Test
 public void testParse() {
 String input = "<xml></xml>";
 Document doc = builder.parse(new StringReader(input), "");
 assertNotNull(doc);
 assertEquals("xml", doc.nodeName());
 assertFalse(doc.outputSettings().isSynthetic());
 assertTrue(stackContains(doc));
 }

 @Test
 public void testParseWithBaseUri() {
 String input = "<xml xmlns:my=\"http://example.com\"><my:tag/></xml>";
 Document doc = builder.parse(new StringReader(input), "http://example.com");
 assertNotNull(doc);
 assertEquals("xml", doc.nodeName());
 assertFalse(doc.outputSettings().isSynthetic());
 assertTrue(stackContains(doc));
 Element tag = (Element) doc.select("my:tag").first();
 assertNotNull(tag);
 assertEquals("my:tag", tag.nodeName());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<tag attribute=\"value\"/>";
 Token token = builder.getInputToken();
 builder.process(token);
 Element tag = (Element) builder.stack.get(builder.stack.size() - 1);
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.attr("attribute"));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<tag><subtag/></tag>";
 Token token = builder.getInputToken();
 builder.process(token);
 Element tag = (Element) builder.stack.get(builder.stack.size() - 1);
 assertEquals("tag", tag.nodeName());
 assertEquals(1, tag.childNodes().size());
 tag = (Element) tag.childNodes().get(0);
 assertEquals("subtag", tag.nodeName());
 token = builder.getInputToken();
 builder.process(token);
 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String input = "<!--comment-->";
 Token token = builder.getInputToken();
 builder.process(token);
 Comment comment = (Comment) builder.stack.get(builder.stack.size() - 1);
 assertEquals("comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<char>x</char>";
 Token token = builder.getInputToken();
 builder.process(token);
 TextNode node = (TextNode) builder.stack.get(builder.stack.size() - 1);
 assertEquals("x", node.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
 Token token = builder.getInputToken();
 builder.process(token);
 DocumentType doctype = (DocumentType) builder.stack.get(builder.stack.size() - 1);
 assertEquals("xml", doctype.nodeName());
 assertEquals("xml.dtd", doctype.systemId());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testProcessInvalidToken() {
 String input = "<invalid>";
 Token token = builder.getInputToken();
 builder.process(token);
 Validate.fail("Unexpected exception: XmlTreeBuilderTest.testProcessInvalidToken()");
 }

 @Test
 public void testInsertNode() {
 Element parent = new Element("parent");
 builder.insertNode(parent);
 assertEquals(parent, builder.currentElement());
 }

 @Test
 public void testInsertComment() {
 String comment = "-- comment --";
 Token token = new Token(Token.Type.COMMENT, comment, 0, 0);
 builder.insert(token);
 Comment insertedComment = (Comment) builder.stack.get(builder.stack.size() - 1);
 assertEquals(comment, insertedComment.data());
 }

 @Test
 public void testInsertCharacter() {
 String character = "x";
 Token token = new Token(Token.Type.CHARACTER, character, 0, 0);
 builder.insert(token);
 TextNode insertedCharacter = (TextNode) builder.stack.get(builder.stack.size() - 1);
 assertEquals(character, insertedCharacter.text());
 }

 @Test
 public void testInsertDoctype() {
 String name = "xml";
 String publicId = "xml.dtd";
 String systemId = "xml.dtd";
 String systemLiteral = "";
 DocumentType doctype = new DocumentType(name, publicId, systemId, systemLiteral);
 Token token = new Token(Token.Type.DOCTYPE, doctype, 0, 0);
 builder.insert(token);
 DocumentType insertedDoctype = (DocumentType) builder.stack.get(builder.stack.size() - 1);
 assertEquals(name, insertedDoctype.nodeName());
 assertEquals(publicId, insertedDoctype.publicId());
 assertEquals(systemId, insertedDoctype.systemId());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<tag><subtag></subtag></tag>";
 Token startTagToken = builder.getInputToken();
 builder.process(startTagToken);
 Element tag = (Element) builder.stack.get(builder.stack.size() - 1);
 Element subtag = (Element) builder.stack.get(builder.stack.size() - 2);
 assertEquals("tag", tag.nodeName());
 assertEquals("subtag", subtag.nodeName());
 Token endTagToken = builder.getInputToken();
 builder.process(endTagToken);
 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testParseFragment() {
 String input = "<tag><subtag/></tag>";
 List<Node> nodes = builder.parseFragment(input, "", "");
 assertEquals(2, nodes.size());
 Element tag = (Element) nodes.get(0);
 Element subtag = (Element) nodes.get(1);
 assertEquals("tag", tag.nodeName());
 assertEquals(1, tag.childNodes().size());
 assertEquals("subtag", subtag.nodeName());
 }

 @Test
 public void testParseFragmentWithContext() {
 String input = "<tag><subtag/></tag>";
 Element context = new Element("context");
 List<Node> nodes = builder.parseFragment(input, context, "", "");
 assertEquals(2, nodes.size());
 Element tag = (Element) nodes.get(0);
 Element subtag = (Element) nodes.get(1);
 assertEquals("tag", tag.nodeName());
 assertEquals(1, tag.childNodes().size());
 assertEquals("subtag", subtag.nodeName());
 assertEquals(context, tag.parent());
 }

 @Test
 public void testStackContains() {
 Document doc = builder.parse("<xml/>", "");
 assertTrue(stackContains(doc));
 }

 private boolean stackContains(Node node) {
 for (int i = builder.stack.size() - 1; i >= 0; i--) {
 if (node.equals(builder.stack.get(i))) {
 return true;
 }
 }
 return false;
 }
}