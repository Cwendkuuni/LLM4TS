import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 }

 // Test defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Test initialiseParse method
 @Test
 public void testInitialiseParse() {
 Reader input = new StringReader("<root><child/></root>");
 xmlTreeBuilder.initialiseParse(input, "", new Parser(xmlTreeBuilder));
 assertEquals(Document.class, xmlTreeBuilder.doc.getClass());
 }

 // Test parse method with reader input
 @Test
 public void testParseWithReaderInput() {
 Reader input = new StringReader("<root><child/></root>");
 Document document = xmlTreeBuilder.parse(input, "");
 assertEquals(1, document.childNodes().size());
 Element root = (Element) document.childNodes().get(0);
 assertEquals("root", root.nodeName());
 }

 // Test parse method with string input
 @Test
 public void testParseWithStringInput() {
 String input = "<root><child/></root>";
 Document document = xmlTreeBuilder.parse(input, "");
 assertEquals(1, document.childNodes().size());
 Element root = (Element) document.childNodes().get(0);
 assertEquals("root", root.nodeName());
 }

 // Test process method with StartTag token
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("root");
 xmlTreeBuilder.process(startTag);
 Element root = xmlTreeBuilder.currentElement();
 assertEquals("root", root.nodeName());
 }

 // Test process method with EndTag token
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(endTag);
 assertNull(xmlTreeBuilder.currentElement());
 }

 // Test process method with Comment token
 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("--comment--");
 xmlTreeBuilder.insert(comment);
 Comment commentNode = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("--comment--", commentNode.getData());
 }

 // Test process method with Character token
 @Test
 public void testProcessCharacter() {
 Token character = new Token.Character('a');
 xmlTreeBuilder.insert(character);
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("a", textNode.text());
 }

 // Test process method with Doctype token
 @Test
 public void testProcessDoctype() {
 Token doctype = new Token.Doctype("html", "", "");
 xmlTreeBuilder.insert(doctype);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("html", doctypeNode.nodeName());
 }

 // Test popStackToClose method
 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.insert(new Element("root"));
 xmlTreeBuilder.insert(new Element("child"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
 }

 // Test parseFragment method with string input
 @Test
 public void testParseFragmentWithStringInput() {
 String input = "<root><child/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, document, "", new Parser(xmlTreeBuilder));
 assertEquals(2, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertEquals("child", nodes.get(1).nodeName());
 }

 // Test parseFragment method with null context
 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullContext() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child/></root>", null, "", new Parser(xmlTreeBuilder));
 }
}