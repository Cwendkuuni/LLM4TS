import org.junit.Before;
import org.junit.Test;

import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tag;
import org.jsoup.Jsoup;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  private XmlTreeBuilder xmlTreeBuilder;
  
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
  public void testParseWithReader() {
    String input = "<root><child>Text</child></root>";
    String baseUri = "http://example.com";
    Reader reader = new StringReader(input);
    Document doc = xmlTreeBuilder.parse(reader, baseUri);
    assertNotNull(doc);
    assertEquals("root", doc.nodeName());
    Element child = doc.selectFirst("child");
    assertNotNull(child);
    TextNode text = child.text();
    assertNotNull(text);
    assertEquals("Text", text.text());
  }
  
  @Test
  public void testParseWithString() {
    String input = "<root><child>Text</child></root>";
    String baseUri = "http://example.com";
    Document doc = xmlTreeBuilder.parse(input, baseUri);
    assertNotNull(doc);
    assertEquals("root", doc.nodeName());
    Element child = doc.selectFirst("child");
    assertNotNull(child);
    TextNode text = child.text();
    assertNotNull(text);
    assertEquals("Text", text.text());
  }
  
  @Test
  public void testInitialiseParse() {
    String input = "<root><child>Text</child></root>";
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);
    Element root = xmlTreeBuilder.currentElement();
    assertNotNull(root);
    assertEquals("root", root.nodeName());
    assertEquals(baseUri, root.baseUri());
    assertEquals(settings, root.parser().settings());
  }
  
  @Test
  public void testProcessStartTag() {
    Token.StartTag token = new Token.StartTag(Tag.valueOf("child"), null);
    Element el = xmlTreeBuilder.insert(token);
    assertNotNull(el);
    assertEquals("child", el.nodeName());
  }
  
  @Test
  public void testProcessEndTag() {
    Element el = new Element(Tag.valueOf("child"));
    xmlTreeBuilder.stack.add(el);
    Token.EndTag token = new Token.EndTag("child");
    xmlTreeBuilder.process(token);
    assertTrue(xmlTreeBuilder.stack.isEmpty());
  }
  
  @Test
  public void testProcessComment() {
    Token.Comment token = new Token.Comment("comment");
    xmlTreeBuilder.insert(token);
    List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
    assertNotNull(nodes);
    assertFalse(nodes.isEmpty());
    Comment comment = (Comment) nodes.get(0);
    assertNotNull(comment);
    assertEquals("comment", comment.getData());
  }
  
  @Test
  public void testProcessCharacter() {
    Token.Character token = new Token.Character("text");
    xmlTreeBuilder.insert(token);
    List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
    assertNotNull(nodes);
    assertFalse(nodes.isEmpty());
    TextNode text = (TextNode) nodes.get(0);
    assertNotNull(text);
    assertEquals("text", text.text());
  }
  
  @Test
  public void testProcessDoctype() {
    Token.Doctype token = new Token.Doctype("html", "", "");
    xmlTreeBuilder.insert(token);
    DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
    assertNotNull(doctype);
    assertEquals("html", doctype.tagName());
  }
  
  @Test
  public void testInsertNode() {
    Element el = new Element(Tag.valueOf("child"));
    xmlTreeBuilder.insertNode(el);
    List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
    assertNotNull(nodes);
    assertFalse(nodes.isEmpty());
    assertEquals(el, nodes.get(0));
  }
  
  @Test
  public void testParseFragment() {
    String inputFragment = "<child>Text</child>";
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
    assertNotNull(nodes);
    assertFalse(nodes.isEmpty());
    Element child = (Element) nodes.get(0);
    assertNotNull(child);
    TextNode text = child.text();
    assertNotNull(text);
    assertEquals("Text", text.text());
  }

}