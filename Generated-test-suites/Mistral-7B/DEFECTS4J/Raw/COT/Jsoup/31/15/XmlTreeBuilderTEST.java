import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private Document doc;

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 Assert.assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcess_StartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr1=\"value1\" attr2=\"value2\"/>\n</root>").parse();
 Element root = doc.rootElement();
 Element element = root.elements().get(0);
 Token startTag = new Token.StartTag("element");
 xmlTreeBuilder.process(startTag);
 Assert.assertEquals("element", element.nodeName());
 Assert.assertEquals(2, element.attributes().size());
 Assert.assertEquals("value1", element.attributes().get("attr1").getValue());
 Assert.assertEquals("value2", element.attributes().get("attr2").getValue());
 }

 @Test
 public void testProcess_EndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<root><element/></root>").parse();
 Element root = doc.rootElement();
 Element element = root.elements().get(0).elementSibling(0);
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.process(endTag);
 Assert.assertEquals(0, root.elements().size());
 }

 @Test
 public void testProcess_Comment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<!-- Test Comment -->\n<root>\n<element>\n</root>").parse();
 Comment comment = (Comment) doc.child(0);
 Token commentToken = new Token.Comment(" Test Comment ");
 xmlTreeBuilder.process(commentToken);
 Assert.assertEquals(" Test Comment ", comment.text());
 }

 @Test
 public void testProcess_Character() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<root>\n<element>\n<text>Test Character</text>\n</element>\n</root>").parse();
 TextNode textNode = (TextNode) doc.select("root > element > text").get(0);
 Token characterToken = new Token.Character('T');
 xmlTreeBuilder.process(characterToken);
 Assert.assertEquals("Test Character", textNode.text());
 }

 @Test
 public void testProcess_Doctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>\n</root>").parse();
 DocumentType doctypeNode = doc.doctype();
 Token doctype = new Token.Doctype("xml", null, null, null);
 xmlTreeBuilder.process(doctype);
 Assert.assertEquals("xml", doctypeNode.name());
 Assert.assertNull(doctypeNode.publicIdentifier());
 Assert.assertNull(doctypeNode.systemIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testProcess_UnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>\n<text>Test Character</text>\n<foo>\n</root>").parse();
 Token foo = new Token(-1, "foo");
 xmlTreeBuilder.process(foo);
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<root><element><element/></element></root>").parse();
 Element root = doc.rootElement();
 Element element = root.elements().get(0);
 Element innerElement = element.elements().get(0);
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(endTag);
 Assert.assertEquals(1, root.elements().size());
 Assert.assertEquals(0, element.elements().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopStackToClose_NoMatchingTagFound() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.setUp("<root><element><element/><foo/></element></root>").parse();
 Element root = doc.rootElement();
 Element element = root.elements().get(0);
 Token endTag = new Token.EndTag("foo");
 xmlTreeBuilder.popStackToClose(endTag);
 }
}