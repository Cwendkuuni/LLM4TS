import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.ParseTree;
import org.jsoup.nodes.extract.Extractor;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
 }

 @Test
 public void testInitialiseParse() {
 // Test initially empty parse tree
 assertTrue(doc.childNodes().isEmpty());
 }

 @Test
 public void testProcessStartTag() {
 StartTag startTag = new StartTag("root", null, null, 0, 0, 0, null);
 Element root = xmlTreeBuilder.insert(startTag);
 List<Node> expectedChildNodes = new ArrayList<>();
 expectedChildNodes.add(root);
 List<Node> actualChildNodes = doc.childNodes();
 assertEquals(expectedChildNodes, actualChildNodes);
 }

 @Test
 public void testProcessEndTag() throws Exception {
 Element root = new Element("root", "");
 Element child = new Element("child", "");
 root.appendChild(child);
 doc.setRootElement(root);
 Token endTag = new Token.EndTag("child");
 xmlTreeBuilder.popStackToClose(endTag);
 List<Node> expectedChildNodes = new ArrayList<>();
 expectedChildNodes.add(root);
 List<Node> actualChildNodes = doc.childNodes();
 assertEquals(expectedChildNodes, actualChildNodes);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token token = new Token(1, "", "UnexpectedToken", 0, 0);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testProcessComment() {
 String comment = "-- this is a comment";
 Comment expectedComment = new Comment(comment, "");
 Comment actualComment = xmlTreeBuilder.insert(new Token.Comment(comment));
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testProcessCharacter() {
 Character character = new Character('a');
 TextNode expectedTextNode = new TextNode("a", "");
 TextNode actualTextNode = xmlTreeBuilder.insert(character);
 assertEquals(expectedTextNode, actualTextNode);
 }

 @Test
 public void testProcessDoctype() {
 String name = "doctype";
 String publicId = "publicId";
 String systemId = "systemId";
 Doctype expectedDoctype = new DocumentType(name, publicId, systemId, "");
 Doctype actualDoctype = xmlTreeBuilder.insert(new Doctype(name, publicId, systemId));
 assertEquals(expectedDoctype, actualDoctype);
 }

 @Test
 public void testPopStackToCloseWithNoMatchingElement() {
 String tagName = "tagName";
 Token endTag = new Token.EndTag(tagName);
 xmlTreeBuilder.popStackToClose(endTag);
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlFragment = "<root><child></child></root>";
 List<Node> expectedChildNodes = parseExpectedXml(xmlFragment);
 List<Node> actualChildNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList()).childNodes();
 assertEquals(expectedChildNodes, actualChildNodes);
 }

 @Test
 public void testParseFragmentWithXmlDeclaration() throws IOException {
 String xmlFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child></child></root>";List<Node> expectedChildNodes = parseExpectedXml(xmlFragment);
 List<Node> actualChildNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList()).childNodes();
 assertEquals(expectedChildNodes, actualChildNodes);
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 List<Node> expectedChildNodes = new ArrayList<>();
 List<Node> actualChildNodes = xmlTreeBuilder.parseFragment("", "", new ParseErrorList()).childNodes();
 assertEquals(expectedChildNodes, actualChildNodes);
 }

 private List<Node> parseExpectedXml(String xmlFragment) {
 List<Node> nodes = new ArrayList<>();
 Parser parser = new Parser();
 Document document = parser.parseInput(new StringReader(xmlFragment), "", Parser.xmlParser());
 nodes.addAll(document.childNodes());
 return nodes;
 }
}