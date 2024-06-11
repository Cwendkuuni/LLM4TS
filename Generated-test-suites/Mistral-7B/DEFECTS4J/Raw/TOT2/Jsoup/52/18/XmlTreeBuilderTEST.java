import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 // Test case for initialiseParse method
 @Test
 public void testInitialiseParse() {
 String input = "<xml version='1.0' encoding='UTF-8'?>\n" +
 "<root>\n" +
 "<element attr1='value1' attr2='value2'>\n" +
 "<subElement attr3='value3'/>\n" +
 "</element>\n" +
 "</root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
 Document doc = nodes.get(0).asDocument();
 assertNotNull(doc);
 assertTrue(doc.hasRootElement());
 Element root = doc.rootElement();
 assertNotNull(root);
 }

 // Test case for process method with StartTag
 @Test
 public void testProcessStartTag() {
 String input = "<root>\n" +
 "<element attr1='value1' attr2='value2'>\n";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = xmlTreeBuilder.tokeniser().next();
 xmlTreeBuilder.process(token);
 Element element = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertNotNull(element);
 assertTrue(element.nodeName().equals("element"));
 }

 // Test case for process method with EndTag
 @Test
 public void testProcessEndTag() {
 String input = "<root>\n" +
 "<element attr1='value1'>\n" +
 "</element>\n";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = xmlTreeBuilder.tokeniser().next();
 xmlTreeBuilder.tokeniser().next(); // skip StartTag
 xmlTreeBuilder.tokeniser().next(); // skip textContent
 xmlTreeBuilder.tokeniser().next(); // EndTag
 xmlTreeBuilder.process(token);
 assertTrue(xmlTreeBuilder.stack.size() == 0);
 }

 // Test case for process method with Comment
 @Test
 public void testProcessComment() {
 String input = "<!-- This is a comment -->";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = xmlTreeBuilder.tokeniser().next();
 xmlTreeBuilder.process(token);
 Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertNotNull(comment);
 assertTrue(comment.getData().equals(" This is a comment "));
 }

 // Test case for process method with Character
 @Test
 public void testProcessCharacter() {
 String input = "<root>\n" +
 "<element>\n" +
 "Text content\n" +
 "</element>\n";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token;
 while ((token = xmlTreeBuilder.tokeniser().next()) != null) {
 xmlTreeBuilder.process(token);
 }
 TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertNotNull(textNode);
 assertTrue(textNode.getData().equals("Text content"));
 }

 // Test case for process method with Doctype
 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE root [ \n<!ATTLIST element attr1 CDATA #IMPLIED>\n]>\n" +
 "<root>\n" +
 "<element attr1='value1'>\n";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = xmlTreeBuilder.tokeniser().next();
 xmlTreeBuilder.process(token);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertNotNull(doctypeNode);
 assertTrue(doctypeNode.getName().equals("root"));
 }

 // Test case for popStackToClose method
 @Test
 public void testPopStackToClose() {
 String input = "<root>\n" +
 "<element>\n" +
 "<subElement/>\n" +
 "</element>\n" +
 "</root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
 Document doc = nodes.get(0).asDocument();
 Element root = doc.rootElement();
 Token endTag = new Token.EndTag("subElement");
 xmlTreeBuilder.popStackToClose(endTag);
 assertTrue(xmlTreeBuilder.stack.size() == 1);
 Element subElement = (Element) xmlTreeBuilder.stack.get(0);
 assertTrue(subElement.nodeName().equals("element"));
 }
}