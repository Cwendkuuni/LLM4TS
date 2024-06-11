import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.helper.Validate;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  XmlTreeBuilder xmlTreeBuilder;
  
  @Before
  public void setUp() {
    xmlTreeBuilder = new XmlTreeBuilder();
  }
  
  @Test
  public void testDefaultSettings() {
    ParseSettings settings = xmlTreeBuilder.defaultSettings();
    assertNotNull(settings);
  }
  
  @Test
  public void testParseWithReaderAndBaseUri() {
    String input = "<root><child1>Test</child1><child2>Testing123</child2></root>";
    Reader reader = new StringReader(input);
    String baseUri = "http://example.com";
    
    Document document = xmlTreeBuilder.parse(reader, baseUri);
    assertNotNull(document);
    assertEquals("root", document.nodeName());
    assertEquals(1, document.childNodeSize());
    
    Element root = document.child(0);
    assertEquals("child1", root.child(0).nodeName());
    assertEquals("Test", root.child(0).text());
    assertEquals("child2", root.child(1).nodeName());
    assertEquals("Testing123", root.child(1).text());
  }
  
  @Test
  public void testParseWithStringAndBaseUri() {
    String input = "<root><child1>Test</child1><child2>Testing123</child2></root>";
    String baseUri = "http://example.com";
    
    Document document = xmlTreeBuilder.parse(input, baseUri);
    assertNotNull(document);
    assertEquals("root", document.nodeName());
    assertEquals(1, document.childNodeSize());
    
    Element root = document.child(0);
    assertEquals("child1", root.child(0).nodeName());
    assertEquals("Test", root.child(0).text());
    assertEquals("child2", root.child(1).nodeName());
    assertEquals("Testing123", root.child(1).text());
  }
  
  @Test
  public void testInitialiseParse() {
    String input = "<root><child1>Test</child1><child2>Testing123</child2></root>";
    Reader reader = new StringReader(input);
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    
    xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
    
    List<Node> stack = xmlTreeBuilder.stack;
    assertNotNull(stack);
    assertEquals(1, stack.size());
    
    Document doc = xmlTreeBuilder.doc;
    assertNotNull(doc);
    
    Document.OutputSettings.Syntax syntax = doc.outputSettings().syntax();
    assertNotNull(syntax);
    assertEquals(Document.OutputSettings.Syntax.xml, syntax);
  }
  
  @Test
  public void testProcessStartTag() {
    Tag tag = Tag.valueOf("test", ParseSettings.preserveCase);
    Token.StartTag startTag = new Token.StartTag(tag, "http://example.com", true);
    
    Element element = xmlTreeBuilder.insert(startTag);
    assertNotNull(element);
    assertEquals("test", element.nodeName());
    assertTrue(element.isSelfClosing());
    
    assertTrue(tag.isKnownTag());
    assertTrue(tag.isSelfClosing());
  }
  
  @Test
  public void testProcessComment() {
    Token.Comment commentToken = new Token.Comment("Test Comment");
    
    xmlTreeBuilder.insert(commentToken);
    
    Node childNode = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(childNode);
    assertTrue(childNode instanceof Comment);
    
    Comment comment = (Comment) childNode;
    assertEquals("Test Comment", comment.getData());
  }
  
  @Test
  public void testProcessCharacter() {
    Token.Character token = new Token.Character("Test Character", false);
    
    xmlTreeBuilder.insert(token);
    
    Node childNode = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(childNode);
    assertTrue(childNode instanceof TextNode);
    
    TextNode textNode = (TextNode) childNode;
    assertEquals("Test Character", textNode.getWholeText());
  }
  
  @Test
  public void testProcessCharacterCData() {
    Token.Character token = new Token.Character("Test CData", true);
    
    xmlTreeBuilder.insert(token);
    
    Node childNode = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(childNode);
    assertTrue(childNode instanceof CDataNode);
    
    CDataNode cDataNode = (CDataNode) childNode;
    assertEquals("Test CData", cDataNode.getWholeText());
  }
  
  @Test
  public void testProcessDoctype() {
    Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
    
    xmlTreeBuilder.insert(doctype);
    
    Node childNode = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(childNode);
    assertTrue(childNode instanceof DocumentType);
    
    DocumentType documentType = (DocumentType) childNode;
    assertEquals("html", documentType.tagName());
    assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.publicId());
    assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.systemId());
  }
  
  @Test
  public void testPopStackToClose() {
    Token.StartTag startTag1 = new Token.StartTag(Tag.valueOf("child1", ParseSettings.preserveCase), "http://example.com", true);
    Token.StartTag startTag2 = new Token.StartTag(Tag.valueOf("child2", ParseSettings.preserveCase), "http://example.com", true);
    Token.EndTag endTag = new Token.EndTag(Tag.valueOf("child1", ParseSettings.preserveCase));
    
    xmlTreeBuilder.insert(startTag1);
    xmlTreeBuilder.insert(startTag2);
    xmlTreeBuilder.popStackToClose(endTag);
    
    List<Element> stack = xmlTreeBuilder.stack;
    assertNotNull(stack);
    assertEquals(1, stack.size());
    
    Element element = stack.get(0);
    assertNotNull(element);
    assertEquals("child2", element.nodeName());
  }
  
  @Test
  public void testParseFragment() {
    String inputFragment = "<child1>Test</child1><child2>Testing123</child2>";
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    
    Node rootNode = nodes.get(0);
    assertTrue(rootNode instanceof Element);
    Element rootElement = (Element) rootNode;
    assertEquals("child1", rootElement.child(0).nodeName());
    assertEquals("Test", rootElement.child(0).text());
    assertEquals("child2", rootElement.child(1).nodeName());
    assertEquals("Testing123", rootElement.child(1).text());
  }
  
}