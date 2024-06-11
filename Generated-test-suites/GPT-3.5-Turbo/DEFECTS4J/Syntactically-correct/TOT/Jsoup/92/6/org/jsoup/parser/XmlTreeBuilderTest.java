import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  private XmlTreeBuilder xmlTreeBuilder;
  
  @Before
  public void setup() {
    xmlTreeBuilder = new XmlTreeBuilder();
  }
  
  @Test
  public void testDefaultSettings() {
    ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
    
    assertNotNull(parseSettings);
    assertEquals(ParseSettings.preserveCase, parseSettings);
  }
  
  @Test
  public void testInitialiseParse() {
    Reader input = new StringReader("");
    String baseUri = "https://example.com";
    Parser parser = new Parser();
    
    xmlTreeBuilder.initialiseParse(input, baseUri, parser);
    
    assertEquals(baseUri, xmlTreeBuilder.baseUri);
    assertEquals(parser, xmlTreeBuilder.parser);
    assertNotNull(xmlTreeBuilder.stack);
    assertNotNull(xmlTreeBuilder.doc);
    assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
  }
  
  @Test
  public void testParseWithReaderAndBaseUri() {
    Reader input = new StringReader("<xml></xml>");
    String baseUri = "https://example.com";
    
    Document document = xmlTreeBuilder.parse(input, baseUri);
    
    assertNotNull(document);
    assertEquals("xml", document.tagName());
  }
  
  @Test
  public void testParseWithStringAndBaseUri() {
    String input = "<xml></xml>";
    String baseUri = "https://example.com";
    
    Document document = xmlTreeBuilder.parse(input, baseUri);
    
    assertNotNull(document);
    assertEquals("xml", document.tagName());
  }
  
  @Test
  public void testProcessStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.name = "xml";
    startTag.attributes = new Attributes();
    
    xmlTreeBuilder.process(new Token(startTag));
    
    Element element = xmlTreeBuilder.currentElement();
    assertNotNull(element);
    assertEquals("xml", element.tagName());
  }
  
  @Test
  public void testProcessEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "xml";
    
    Element element = new Element("xml");
    xmlTreeBuilder.stack.add(element);
    
    xmlTreeBuilder.process(new Token(endTag));
    
    assertTrue(xmlTreeBuilder.stack.isEmpty());
  }
  
  @Test
  public void testProcessComment() {
    Token.Comment comment = new Token.Comment();
    comment.data = "This is a comment";
    
    xmlTreeBuilder.process(new Token(comment));
    
    Node node = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(node);
    assertTrue(node instanceof Comment);
    assertEquals("This is a comment", ((Comment) node).getData());
  }
  
  @Test
  public void testProcessCharacter() {
    Token.Character character = new Token.Character();
    character.data = "This is a character";
    
    xmlTreeBuilder.process(new Token(character));
    
    Node node = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(node);
    assertTrue(node instanceof TextNode);
    assertEquals("This is a character", ((TextNode) node).getWholeText());
  }
  
  @Test
  public void testProcessDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name = "html";
    
    xmlTreeBuilder.process(new Token(doctype));
    
    DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(doctypeNode);
    assertEquals("html", doctypeNode.getName());
  }
  
  @Test
  public void testInsertNode() {
    Element element = new Element("div");
    xmlTreeBuilder.stack.add(element);
    
    Node node = new TextNode("Hello, World!");
    xmlTreeBuilder.insertNode(node);
    
    assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
  }
  
  @Test
  public void testInsertStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.name = "div";
    startTag.attributes = new Attributes();
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    assertNotNull(element);
    assertEquals("div", element.tagName());
    assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
    assertEquals(startTag.attributes, element.attributes());
  }
  
  @Test
  public void testInsertComment() {
    Token.Comment comment = new Token.Comment();
    comment.data = "This is a comment";
    
    xmlTreeBuilder.insert(comment);
    
    Node node = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(node);
    assertTrue(node instanceof Comment);
    assertEquals("This is a comment", ((Comment) node).getData());
  }
  
  @Test
  public void testInsertCharacterWithText() {
    Token.Character character = new Token.Character();
    character.data = "This is a text";
    
    xmlTreeBuilder.insert(character);
    
    Node node = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(node);
    assertTrue(node instanceof TextNode);
    assertEquals("This is a text", ((TextNode) node).getWholeText());
  }
  
  @Test
  public void testInsertCharacterWithCData() {
    Token.Character character = new Token.Character();
    character.data = "This is CDATA";
    character.isCData = true;
    
    xmlTreeBuilder.insert(character);
    
    Node node = xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(node);
    assertTrue(node instanceof CDataNode);
    assertEquals("This is CDATA", ((CDataNode) node).getWholeText());
  }
  
  @Test
  public void testInsertDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name = "html";
    
    xmlTreeBuilder.insert(doctype);
    
    DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(doctypeNode);
    assertEquals("html", doctypeNode.getName());
  }
  
  @Test
  public void testPopStackToClose() {
    Element element1 = new Element("div");
    xmlTreeBuilder.stack.add(element1);
    Element element2 = new Element("span");
    xmlTreeBuilder.stack.add(element2);
    Element element3 = new Element("div");
    xmlTreeBuilder.stack.add(element3);
    
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "div";
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    assertFalse(xmlTreeBuilder.stack.contains(element3));
    assertFalse(xmlTreeBuilder.stack.contains(element1));
    assertTrue(xmlTreeBuilder.stack.contains(element2));
  }
  
  @Test
  public void testParseFragmentWithStringAndBaseUri() {
    String inputFragment = "<div>This is a fragment</div>";
    String baseUri = "https://example.com";
    
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser());
    
    assertFalse(nodes.isEmpty());
    assertEquals("div", nodes.get(0).nodeName());
  }
  
  @Test
  public void testParseFragmentWithStringAndElementAndBaseUri() {
    String inputFragment = "<div>This is a fragment</div>";
    Element context = new Element("div");
    String baseUri = "https://example.com";
    
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser());
    
    assertFalse(nodes.isEmpty());
    assertEquals("div", nodes.get(0).nodeName());
  }
  
}