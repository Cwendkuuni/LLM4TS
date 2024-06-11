import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
  private XmlTreeBuilder xmlTreeBuilder;
  private Document doc;

  @Before
  public void setUp() {
    xmlTreeBuilder = new XmlTreeBuilder();
    doc = new Document(baseUri);
  }

  @Test
  public void testDefaultSettings() {
    ParseSettings settings = xmlTreeBuilder.defaultSettings();
    assertNotNull(settings);
  }

  @Test
  public void testInitialiseParse() {
    Reader input = new StringReader(inputString);
    String baseUri = "http://example.com";
    xmlTreeBuilder.initialiseParse(input, baseUri, parser);
    List<Node> stack = xmlTreeBuilder.getStack();
    assertEquals(doc, stack.get(stack.size()-1));
    assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
  }

  @Test
  public void testParseReaderString() {
    Reader input = new StringReader(inputString);
    String baseUri = "http://example.com";
    Document result = xmlTreeBuilder.parse(input, baseUri);
    assertNotNull(result);
  }

  @Test
  public void testParseStringString() {
    String input = "<xml>";
    String baseUri = "http://example.com";
    Document result = xmlTreeBuilder.parse(input, baseUri);
    assertNotNull(result);
  }

  @Test
  public void testProcessStartTag() {
    Token.StartTag startTag = new Token.StartTag("div");
    Element el = xmlTreeBuilder.insert(startTag);
    assertNotNull(el);
  }

  @Test
  public void testProcessEndTag() {
    Token.EndTag endTag = new Token.EndTag("div");
    xmlTreeBuilder.insert(endTag);
  }

  @Test
  public void testProcessComment() {
    Token.Comment comment = new Token.Comment("This is a comment");
    xmlTreeBuilder.insert(comment);
  }

  @Test
  public void testProcessCharacter() {
    Token.Character character = new Token.Character("This is some text");
    xmlTreeBuilder.insert(character);
  }

  @Test
  public void testProcessDoctype() {
    Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
    xmlTreeBuilder.insert(doctype);
  }

  @Test
  public void testInsertNode() {
    Node node = new Element("div");
    xmlTreeBuilder.insertNode(node);
  }

  @Test
  public void testInsertStartTag() {
    Token.StartTag startTag = new Token.StartTag("div");
    Element el = xmlTreeBuilder.insert(startTag);
    assertNotNull(el);
  }

  @Test
  public void testInsertComment() {
    Token.Comment comment = new Token.Comment("This is a comment");
    xmlTreeBuilder.insert(comment);
  }

  @Test
  public void testInsertCharacter() {
    Token.Character character = new Token.Character("This is some text");
    xmlTreeBuilder.insert(character);
  }

  @Test
  public void testInsertDoctype() {
    Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
    xmlTreeBuilder.insert(doctype);
  }

  @Test
  public void testPopStackToClose() {
    Token.EndTag endTag = new Token.EndTag("div");
    xmlTreeBuilder.popStackToClose(endTag);
  }

  @Test
  public void testParseFragmentStringStringParser() {
    String inputFragment = "<xml>";
    String baseUri = "http://example.com";
    Parser parser = new Parser(xmlTreeBuilder);
    List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
    assertNotNull(result);
  }

  @Test
  public void testParseFragmentStringElementStringParser() {
    String inputFragment = "<xml>";
    Element context = new Element("div");
    String baseUri = "http://example.com";
    Parser parser = new Parser(xmlTreeBuilder);
    List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
    assertNotNull(result);
  }

}