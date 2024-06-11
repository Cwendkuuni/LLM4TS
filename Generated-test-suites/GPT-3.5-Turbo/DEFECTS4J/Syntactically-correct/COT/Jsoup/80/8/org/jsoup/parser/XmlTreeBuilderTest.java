import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class XmlTreeBuilderTest {

  @Test
  public void testDefaultSettings() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    ParseSettings settings = treeBuilder.defaultSettings();
    Assert.assertEquals(ParseSettings.preserveCase, settings);
  }

  @Test
  public void testParseWithReader() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Document doc = treeBuilder.parse(new StringReader("<root></root>"), "http://example.com");
    Assert.assertEquals("http://example.com", doc.baseUri());
    Assert.assertEquals("<root></root>\n", doc.html());
  }
  
  @Test
  public void testParseWithString() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Document doc = treeBuilder.parse("<root></root>", "http://example.com");
    Assert.assertEquals("http://example.com", doc.baseUri());
    Assert.assertEquals("<root></root>\n", doc.html());
  }
  
  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Reader input = new StringReader("<root></root>");
    String baseUri = "http://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();
    ParseSettings settings = ParseSettings.preserveCase;
    
    treeBuilder.initialiseParse(input, baseUri, errors, settings);
    
    Assert.assertEquals(ParseErrorList.noTracking(), errors);
    Assert.assertEquals("http://example.com", treeBuilder.baseUri);
  }
  
  @Test
  public void testProcessStartTag() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("div");
    Element el = treeBuilder.insert(startTag);
    
    Assert.assertEquals("div", el.tagName());
    Assert.assertEquals(null, el.parent());
    Assert.assertEquals(1, el.childNodes().size());
  }
  
  @Test
  public void testProcessEndTag() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    treeBuilder.insert(new Token.StartTag("div"));
    Token.EndTag endTag = new Token.EndTag("div");
    treeBuilder.process(endTag);
    
    Assert.assertEquals(0, treeBuilder.stack.size());
  }
  
  @Test
  public void testProcessComment() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Comment commentToken = new Token.Comment("test comment");
    treeBuilder.insert(commentToken);
    
    Assert.assertEquals(1, treeBuilder.doc.childNodes().size());
    Assert.assertTrue(treeBuilder.doc.childNode(0) instanceof Comment);
  }
  
  @Test
  public void testProcessCharacter() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Character character = new Token.Character("test character");
    treeBuilder.insert(character);
    
    Assert.assertEquals(1, treeBuilder.doc.childNodes().size());
    Assert.assertTrue(treeBuilder.doc.childNode(0) instanceof TextNode);
  }
  
  @Test
  public void testProcessDoctype() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Doctype doctype = new Token.Doctype("html");
    treeBuilder.insert(doctype);
    
    Assert.assertEquals(1, treeBuilder.doc.childNodes().size());
    Assert.assertTrue(treeBuilder.doc.childNode(0) instanceof DocumentType);
  }
  
  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    treeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
    treeBuilder.stack.add(new Element(Tag.valueOf("p"), "http://example.com"));
    treeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
    
    Token.EndTag endTag = new Token.EndTag("div");
    treeBuilder.popStackToClose(endTag);
    
    Assert.assertEquals(1, treeBuilder.stack.size());
    Assert.assertEquals("p", treeBuilder.stack.get(0).tagName());
  }
  
  @Test
  public void testParseFragment() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    List<Node> nodes = treeBuilder.parseFragment("<div></div>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    
    Assert.assertEquals(1, nodes.size());
    Assert.assertTrue(nodes.get(0) instanceof Element);
  }
}