import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private String xmlInput;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlInput = "<root><child1><subchild1>Hello World</subchild1></child1><child2></child2></root>";
 }

 @Test
 public void testParse() {
 Document doc = xmlTreeBuilder.parse(xmlInput, "");
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertEquals(2, doc.childNodes().size());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("root");
 Element el = xmlTreeBuilder.insert(startTag);
 assertEquals("root", el.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().child(0)).getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character(' ');
 xmlTreeBuilder.insert(characterToken);
 assertEquals(" ", ((TextNode) xmlTreeBuilder.currentElement().child(xmlTreeBuilder.currentElement().childNodes().size() - 1)).getText());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html");
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType docType = (DocumentType) xmlTreeBuilder.currentElement().child(0);
 assertEquals("html", docType.getNodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child><subchild>Hello World</subchild></child></root>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals("root", nodes.get(0).nodeName());
 }
}