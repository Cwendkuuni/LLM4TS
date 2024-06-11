import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

  @Test
  public void testDefaultSettings() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
    assertNotNull(defaultSettings);
    assertEquals(ParseSettings.preserveCase, defaultSettings);
  }

  @Test
  public void testParseWithReaderAndBaseUri() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Reader reader = new StringReader("<html><body></body></html>");
    String baseUri = "http://example.com";
    Document document = xmlTreeBuilder.parse(reader, baseUri);
    assertNotNull(document);
    assertEquals("<html>\n <head></head>\n <body></body>\n</html>", document.toString());
  }

  @Test
  public void testParseWithStringAndBaseUri() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String input = "<html><body></body></html>";
    String baseUri = "http://example.com";
    Document document = xmlTreeBuilder.parse(input, baseUri);
    assertNotNull(document);
    assertEquals("<html>\n <head></head>\n <body></body>\n</html>", document.toString());
  }

  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Reader reader = new StringReader("<html><body></body></html>");
    String baseUri = "http://example.com";
    ParseErrorList parseErrorList = ParseErrorList.noTracking();
    ParseSettings parseSettings = ParseSettings.preserveCase;
    xmlTreeBuilder.initialiseParse(reader, baseUri, parseErrorList, parseSettings);
    assertNotNull(xmlTreeBuilder.stack);
    assertNotNull(xmlTreeBuilder.doc);
    assertNotNull(xmlTreeBuilder.doc.outputSettings());
    assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
  }

  @Test
  public void testProcessWithStartTag() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token token = new Token.StartTag("tag", "name");
    xmlTreeBuilder.process(token);
    assertEquals(1, xmlTreeBuilder.stack.size());
    assertEquals("tag", xmlTreeBuilder.stack.get(0).nodeName());
  }

  @Test
  public void testProcessWithEndTag() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Element element = new Element(Tag.valueOf("tag"), "base", ParseSettings.preserveCase);
    xmlTreeBuilder.stack.add(element);
    Token token = new Token.EndTag("tag");
    xmlTreeBuilder.process(token);
    assertTrue(xmlTreeBuilder.stack.isEmpty());
  }

  @Test
  public void testProcessWithComment() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token token = new Token.Comment("comment");
    xmlTreeBuilder.process(token);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
    Comment comment = (Comment) xmlTreeBuilder.doc.childNode(0);
    assertEquals("comment", comment.getData());
  }

  @Test
  public void testProcessWithCharacter() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.Character token = new Token.Character("data");
    xmlTreeBuilder.process(token);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
    TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
    assertEquals("data", textNode.getWholeText());
  }

  @Test
  public void testProcessWithDoctype() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.Doctype token = new Token.Doctype("doctype");
    xmlTreeBuilder.process(token);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
    DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNode(0);
    assertEquals("doctype", documentType.getName());
  }

  @Test
  public void testInsertNode() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Node node = new Element(Tag.valueOf("tag"), "base", ParseSettings.preserveCase);
    xmlTreeBuilder.insertNode(node);
    assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    assertEquals("tag", xmlTreeBuilder.currentElement().childNode(0).nodeName());
  }

  @Test
  public void testInsertWithStartTag() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.StartTag token = new Token.StartTag("tag", "name");
    xmlTreeBuilder.insert(token);
    assertEquals(1, xmlTreeBuilder.stack.size());
    assertEquals("tag", xmlTreeBuilder.stack.get(0).nodeName());
  }

  @Test
  public void testInsertWithCommentToken() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.Comment commentToken = new Token.Comment("comment");
    xmlTreeBuilder.insert(commentToken);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
    Comment comment = (Comment) xmlTreeBuilder.doc.childNode(0);
    assertEquals("comment", comment.getData());
  }

  @Test
  public void testInsertWithCharacterToken() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.Character characterToken = new Token.Character("data");
    xmlTreeBuilder.insert(characterToken);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
    TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
    assertEquals("data", textNode.getWholeText());
  }

  @Test
  public void testInsertWithDoctypeToken() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Token.Doctype doctypeToken = new Token.Doctype("doctype");
    xmlTreeBuilder.insert(doctypeToken);
    assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
    DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNode(0);
    assertEquals("doctype", documentType.getName());
  }
  
  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    Element element1 = new Element(Tag.valueOf("tag"), "base", ParseSettings.preserveCase);
    Element element2 = new Element(Tag.valueOf("tag"), "base", ParseSettings.preserveCase);
    xmlTreeBuilder.stack.add(element1);
    xmlTreeBuilder.stack.add(element2);
    Token.EndTag endTag = new Token.EndTag("tag");
    xmlTreeBuilder.popStackToClose(endTag);
    assertEquals(0, xmlTreeBuilder.stack.size());
  }

  @Test
  public void testParseFragment() {
    XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    String inputFragment = "<tag>data</tag>";
    String baseUri = "http://example.com";
    ParseErrorList parseErrorList = ParseErrorList.noTracking();
    ParseSettings parseSettings = ParseSettings.preserveCase;
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parseErrorList, parseSettings);
    assertEquals(1, nodes.size());
    assertTrue(nodes.get(0) instanceof Element);
    Element element = (Element) nodes.get(0);
    assertEquals("tag", element.nodeName());
    assertEquals("data", element.text());
  }
}