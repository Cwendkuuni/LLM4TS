import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 doc = builder.parse("<?xml version=\"1.0\"?>\n<root>\n<child1/>\n<child2>\n<grandchild/>\n</child2>\n</root>", "baseUrl");
 }

 @Test
 public void testParseValidXml() {
 assertNotNull(doc);
 assertEquals(3, doc.childNodeSize());
 assertEquals("root", doc.tagName());

 Element root = doc.elementSibling(0);
 assertEquals("child1", root.tagName());
 assertEquals("baseUrl", root.baseUri().toString());
 assertNull(root.previousSibling());

 Element child2 = doc.elementSibling(1);
 assertEquals("child2", child2.tagName());
 assertEquals("baseUrl", child2.baseUri().toString());

 Element grandchild = child2.childSibling(0);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("baseUrl", grandchild.baseUri().toString());
 }

 @Test(expected = org.jsoup.parser.ParseError.class)
 public void testParseInvalidXml() {
 builder = new XmlTreeBuilder();
 doc = builder.parse("<html><body>", "baseUrl"); // XML parser should throw an error for invalid XML input
 assertNull(doc);
 }

 @Test
 public void testParseEmptyXml() {
 doc = builder.parse("", "baseUrl");
 assertNotNull(doc);
 assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testParseComment() {
 doc = builder.parse("<!-- This is a comment -->", "baseUrl");
 Element comment = doc.child(0);
 assertTrue(comment instanceof Comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseCharacter() {
 doc = builder.parse("<root>\n<character>!</character>\n</root>", "baseUrl");
 TextNode charNode = (TextNode) doc.child(1);
 assertEquals("!", charNode.text());
 }

 @Test
 public void testParseDoctype() {
 doc = builder.parse("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]>", "baseUrl");
 DocumentType doctypeNode = (DocumentType) doc.child(0);
 assertEquals("root", doctypeNode.nodeName());
 assertNull(doctypeNode.getPublicIdentifier());
 assertNull(doctypeNode.getSystemIdentifier());
 }
}