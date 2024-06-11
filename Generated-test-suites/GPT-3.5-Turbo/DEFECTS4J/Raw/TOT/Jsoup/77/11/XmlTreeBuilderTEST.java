import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.io.Reader;
import java.util.List;

import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {
  
  @Mock
  private Reader readerMock;
  
  private String baseUri;
  private ParseErrorList errors;
  private ParseSettings settings;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    baseUri = "http://example.com";
    errors = new ParseErrorList();
    settings = ParseSettings.preserveCase;
  }
  
  @Test
  public void testParse_withReaderAndBaseUri_shouldReturnDocument() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    
    Document document = xmlTreeBuilder.parse(readerMock, baseUri);
    
    // A valid XML document should be returned
    assertNotNull(document);
    assertEquals("Document", document.nodeName());
  }
  
  @Test
  public void testParse_withStringInputAndBaseUri_shouldReturnDocument() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String input = "<root><child>Hello World!</child></root>";
    
    Document document = xmlTreeBuilder.parse(input, baseUri);
    
    // A valid XML document should be returned
    assertNotNull(document);
    assertEquals("Document", document.nodeName());
  }
  
  @Test
  public void testInitialiseParse_shouldSetDocumentOutputSyntaxToXML() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    
    xmlTreeBuilder.initialiseParse(readerMock, baseUri, errors, settings);
    
    // The output syntax of the document settings should be set to XML
    Document document = xmlTreeBuilder.getDocument();
    assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
  }
  
  @Test
  public void testProcess_withStartTag_shouldInsertElement() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("div", null, false);
    
    xmlTreeBuilder.process(startTag);
    
    // The currentElement should have the inserted element
    assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
  }
  
  @Test
  public void testProcess_withEndTag_shouldPopStackToCloseElement() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("div", null, false);
    Token.EndTag endTag = new Token.EndTag("div");
    
    xmlTreeBuilder.process(startTag);
    xmlTreeBuilder.process(endTag);
    
    // The stack should not contain the closed element anymore
    assertFalse(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
  }
  
  @Test
  public void testProcess_withCommentToken_shouldInsertComment() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String commentData = "This is a comment.";
    Token.Comment commentToken = new Token.Comment(commentData);
    
    xmlTreeBuilder.process(commentToken);
    
    // The inserted node should be a Comment node with the same data
    assertEquals(commentData, xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
  }
  
  @Test
  public void testProcess_withCharacterToken_shouldInsertCharacterNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String characterData = "Hello World!";
    Token.Character characterToken = new Token.Character(characterData, false);
    
    xmlTreeBuilder.process(characterToken);
    
    // The inserted node should be a TextNode or CDataNode with the same data
    assertEquals(characterData, xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
  }
  
  @Test
  public void testProcess_withDoctypeToken_shouldInsertDoctypeNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String doctypeName = "html";
    Token.Doctype doctypeToken = new Token.Doctype(doctypeName);
    
    xmlTreeBuilder.process(doctypeToken);
    
    // The inserted node should be a Doctype node with the same name
    DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
    assertEquals(doctypeName, doctype.nodeName());
  }
  
  @Test
  public void testInsertNode_shouldAppendNodeToCurrentElement() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Node node = new Element("div");
    
    xmlTreeBuilder.insertNode(node);
    
    // The inserted node should be appended to the currentElement
    assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
  }
  
  @Test
  public void testInsert_withStartTag_shouldInsertElementWithAttributes() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("div", "id=\"myDiv\"", true);
    
    xmlTreeBuilder.insert(startTag);
    
    // The inserted element should have the specified tag and attributes
    Element insertedElement = xmlTreeBuilder.currentElement().children().get(0);
    assertEquals("div", insertedElement.nodeName());
    assertTrue(insertedElement.attributes().hasKey("id"));
    assertEquals("myDiv", insertedElement.attributes().get("id"));
  }
  
  @Test
  public void testInsert_withCommentToken_shouldInsertCommentNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String commentData = "This is a comment.";
    Comment comment = new Comment(commentData);
    Token.Comment commentToken = new Token.Comment(commentData);
    
    xmlTreeBuilder.insertNode(comment);
    xmlTreeBuilder.insert(commentToken);
    
    // The inserted node should be an XmlDeclaration if the comment is a valid xml declaration
    Node insertedNode = xmlTreeBuilder.currentElement().childNodes().get(0);
    assertEquals(commentData, insertedNode.outerHtml());
  }
  
  @Test
  public void testInsert_withCharacterToken_shouldInsertTextNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String characterData = "Hello World!";
    TextNode textNode = new TextNode(characterData);
    Token.Character characterToken = new Token.Character(characterData, false);
    
    xmlTreeBuilder.insertNode(textNode);
    xmlTreeBuilder.insert(characterToken);
    
    // The inserted node should be a TextNode if the characterToken is not a CData
    Node insertedNode = xmlTreeBuilder.currentElement().childNodes().get(0);
    assertEquals(characterData, insertedNode.outerHtml());
  }
  
  @Test
  public void testInsert_withCharacterTokenAndCDataTrue_shouldInsertCDataNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String cdataData = "<![CDATA[This is CDATA.]]>";
    CDataNode cdataNode = new CDataNode(cdataData);
    Token.Character characterToken = new Token.Character(cdataData, true);
    
    xmlTreeBuilder.insertNode(cdataNode);
    xmlTreeBuilder.insert(characterToken);
    
    // The inserted node should be a CDataNode if the characterToken is a CData
    Node insertedNode = xmlTreeBuilder.currentElement().childNodes().get(0);
    assertEquals(cdataData, insertedNode.outerHtml());
  }
  
  @Test
  public void testInsert_withDoctypeToken_shouldInsertDoctypeNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String doctypeName = "html";
    DocumentType doctype = new DocumentType(doctypeName);
    Token.Doctype doctypeToken = new Token.Doctype(doctypeName);
    
    xmlTreeBuilder.insertNode(doctype);
    xmlTreeBuilder.insert(doctypeToken);
    
    // The inserted node should be a Doctype node with the same name
    DocumentType insertedDoctype = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
    assertEquals(doctypeName, insertedDoctype.nodeName());
  }
  
  @Test
  public void testPopStackToClose_withExistingElement_shouldRemoveElementFromStack() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String elementName = "div";
    
    Element element = new Element(elementName);
    xmlTreeBuilder.stack.add(element);
    
    Token.EndTag endTag = new Token.EndTag(elementName);
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    // The stack should not contain the closed element anymore
    assertFalse(xmlTreeBuilder.stack.contains(element));
  }
  
  @Test
  public void testPopStackToClose_withNonExistingElement_shouldNoChangeToStack() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String elementName = "div";
    
    Element element = new Element(elementName);
    xmlTreeBuilder.stack.add(element);
    
    Token.EndTag endTag = new Token.EndTag("span");
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    // The stack should still contain the original element
    assertTrue(xmlTreeBuilder.stack.contains(element));
  }
  
  @Test
  public void testParseFragment_withValidFragment_shouldReturnListOfNodes() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String inputFragment = "<root><child>Hello World!</child></root>";
    
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
    
    // A list of nodes should be returned with the same structure as the inputFragment
    assertNotNull(nodes);
    assertEquals("root", nodes.get(0).nodeName());
    assertEquals("child", nodes.get(0).childNodes().get(0).nodeName());
    assertEquals("Hello World!", nodes.get(0).childNodes().get(0).outerHtml());
  }
  
  @Test
  public void testParseFragment_withInvalidFragment_shouldReturnEmptyListOfNodes() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String inputFragment = "<invalid>";
    
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
    
    // An empty list should be returned, indicating an invalid fragment
    assertNotNull(nodes);
    assertTrue(nodes.isEmpty());
  }
}