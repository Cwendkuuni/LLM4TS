import org.jsoup.Jsoup;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class XmlTreeBuilderTest {
  
  @Test
  public void testDefaultSettings() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    ParseSettings settings = builder.defaultSettings();
    assertNotNull("Default settings should not be null", settings);
  }
  
  @Test
  public void testParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<root><element>test</element></root>";
    String baseUri = "http://example.com";
    Document doc = builder.parse(input, baseUri);
    assertNotNull("Parsed document should not be null", doc);
    assertEquals("Root node should have the correct tag name", "root", doc.nodeName());
    assertEquals("Number of child nodes should be correct", 1, doc.childNodes().size());
    assertTrue("Child node should be an element", doc.childNode(0) instanceof Element);
    assertEquals("Child element should have the correct tag name", "element", doc.childNode(0).nodeName());
    assertTrue("Child element should have text node as child", doc.childNode(0).childNodes().get(0) instanceof TextNode);
    assertEquals("Child element should have correct text", "test", ((TextNode)doc.childNode(0).childNodes().get(0)).getWholeText());
  }
  
  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<root><element>test</element></root>";
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    builder.initialiseParse(input, baseUri, errors, settings);
    
    assertEquals("Stack should have the document as the first element", builder.currentElement(), builder.doc);
    assertEquals("Document output settings syntax should be set to XML", Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
  }
  
  @Test
  public void testProcessStartTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    XmlTreeBuilder.Tag tag = builder.new Tag();
    tag.name = "element";
    Token.StartTag startTag = new Token.StartTag(tag, true);
    
    builder.process(startTag);
    
    assertNotNull("Current element should not be null", builder.currentElement());
    assertEquals("Current element should have the correct tag name", "element", builder.currentElement().nodeName());
  }
  
  @Test
  public void testProcessEndTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    XmlTreeBuilder.Tag tag = builder.new Tag();
    tag.name = "element";
    Token.EndTag endTag = new Token.EndTag(tag);
    
    builder.stack.add(builder.doc);
    builder.stack.add(new Element(Tag.valueOf("element"), "http://example.com"));
    
    builder.process(endTag);
    
    assertNotNull("Current element should not be null", builder.currentElement());
    assertEquals("Current element should have the correct tag name", "root", builder.currentElement().nodeName());
  }
  
  @Test
  public void testProcessComment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Comment comment = new Token.Comment("Test comment");
    
    builder.process(comment);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a comment", builder.doc.childNodes().get(0) instanceof Comment);
    assertEquals("Comment should have the correct data", "Test comment", ((Comment)builder.doc.childNodes().get(0)).getData());
  }
  
  @Test
  public void testProcessCharacter() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Character character = new Token.Character("Test character");
    
    builder.process(character);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a text node", builder.doc.childNodes().get(0) instanceof TextNode);
    assertEquals("Text node should have the correct data", "Test character", ((TextNode)builder.doc.childNodes().get(0)).getWholeText());
  }
  
  @Test
  public void testProcessDoctype() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Doctype doctype = new Token.Doctype("html", null, "http://example.com");
    
    builder.process(doctype);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a document type", builder.doc.childNodes().get(0) instanceof DocumentType);
    DocumentType docType = (DocumentType)builder.doc.childNodes().get(0);
    assertEquals("Document type should have the correct name", "html", docType.nodeName());
    assertNull("Document type should have null public identifier", docType.getPublicIdentifier());
    assertEquals("Document type should have the correct system identifier", "http://example.com", docType.getSystemIdentifier());
  }
  
  @Test
  public void testInsertNode() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Element element = new Element(Tag.valueOf("element"), "http://example.com");
    builder.stack.add(builder.doc);
    
    builder.insertNode(element);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be the inserted element", builder.doc.childNodes().get(0) == element);
  }
  
  @Test
  public void testInsertStartTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag(builder.new Tag(), true);
    
    Element el = builder.insert(startTag);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be the inserted element", builder.doc.childNodes().get(0) == el);
    assertTrue("Inserted element should have the correct tag name", el.nodeName().equals("tag"));
    assertTrue("Builder stack should contain the inserted element", builder.stack.contains(el));
  }
  
  @Test
  public void testInsertComment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Comment comment = new Token.Comment("Test comment");
    
    builder.insert(comment);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a comment", builder.doc.childNodes().get(0) instanceof Comment);
    assertEquals("Comment should have the correct data", "Test comment", ((Comment)builder.doc.childNodes().get(0)).getData());
  }
  
  @Test
  public void testInsertCharacter() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Character character = new Token.Character("Test character");
    
    builder.insert(character);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a text node", builder.doc.childNodes().get(0) instanceof TextNode);
    assertEquals("Text node should have the correct data", "Test character", ((TextNode)builder.doc.childNodes().get(0)).getWholeText());
  }
  
  @Test
  public void testInsertDoctype() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Doctype doctype = new Token.Doctype("html", "publicID", "systemID");
    
    builder.insert(doctype);
    
    assertEquals("Number of child nodes of the document should be correct", 1, builder.doc.childNodes().size());
    assertTrue("Child node should be a document type", builder.doc.childNodes().get(0) instanceof DocumentType);
    DocumentType docType = (DocumentType)builder.doc.childNodes().get(0);
    assertEquals("Document type should have the correct name", "html", docType.nodeName());
    assertEquals("Document type should have the correct public identifier", "publicID", docType.getPublicIdentifier());
    assertEquals("Document type should have the correct system identifier", "systemID", docType.getSystemIdentifier());
  }
  
  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.EndTag endTag = new Token.EndTag(builder.new Tag());
    
    Element el1 = new Element(Tag.valueOf("element"), "http://example.com");
    Element el2 = new Element(Tag.valueOf("element"), "http://example.com");
    builder.stack.add(builder.doc);
    builder.stack.add(el1);
    builder.stack.add(el2);
    
    builder.popStackToClose(endTag);
    
    assertEquals("Stack should have the document as the first element", builder.currentElement(), builder.doc);
    assertEquals("Stack should not contain the first found element", false, builder.stack.contains(el1));
    assertEquals("Stack should contain the second element", true, builder.stack.contains(el2));
  }
  
  @Test
  public void testParseFragment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String inputFragment = "<root><element>test</element></root>";
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    
    List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
    
    assertNotNull("Parsed fragment should not be null", nodes);
    assertEquals("Number of parsed nodes should be correct", 1, nodes.size());
    assertTrue("Parsed node should be an element", nodes.get(0) instanceof Element);
    assertEquals("Parsed element should have the correct tag name", "root", nodes.get(0).nodeName());
    assertEquals("Number of child nodes of the parsed element should be correct", 1, nodes.get(0).childNodes().size());
    assertTrue("Child node of the parsed element should be an element", nodes.get(0).childNodes().get(0) instanceof Element);
    assertEquals("Child element should have the correct tag name", "element", nodes.get(0).childNodes().get(0).nodeName());
    assertTrue("Child element should have text node as child", nodes.get(0).childNodes().get(0).childNodes().get(0) instanceof TextNode);
    assertEquals("Child element should have correct text", "test", ((TextNode)nodes.get(0).childNodes().get(0).childNodes().get(0)).getWholeText());
  }
}