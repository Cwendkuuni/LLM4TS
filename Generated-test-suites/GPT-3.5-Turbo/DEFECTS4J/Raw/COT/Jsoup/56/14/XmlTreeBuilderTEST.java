
import org.junit.Test;
import org.junit.Assert;

public class XmlTreeBuilderTest {

  @Test
  public void testDefaultSettings() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    ParseSettings settings = builder.defaultSettings();
    
    // Assert that the default settings returned by defaultSettings() is an instance of ParseSettings.preserveCase
    Assert.assertEquals(ParseSettings.preserveCase, settings);
  }

  @Test
  public void testParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<xml><tag>content</tag></xml>";
    String baseUri = "http://example.com";
    Document doc = builder.parse(input, baseUri);
    
    // Assert that the parsed document is not null
    Assert.assertNotNull(doc);
    
    // Assert that the parsed document contains the correct root element
    Assert.assertEquals("xml", doc.child(0).tagName());
    
    // Assert that the parsed document contains the correct child element and its content
    Assert.assertEquals("tag", doc.select("xml > tag").first().tagName());
    Assert.assertEquals("content", doc.select("xml > tag").first().text());
  }

  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<xml><tag>content</tag></xml>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    ParseSettings settings = ParseSettings.preserveCase;
    builder.initialiseParse(input, baseUri, errors, settings);
    
    // Assert that the stack contains the document node
    Assert.assertEquals(1, builder.stack.size());
    Assert.assertEquals("xml", builder.stack.get(0).tagName());
    
    // Assert that the output settings syntax is XML
    Assert.assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
  }

  @Test
  public void testProcessStartTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    Token.StartTag startTag = new Token.StartTag("tag", "http://example.com", false);
    builder.process(startTag);
    
    // Assert that the element is inserted correctly
    Assert.assertEquals(2, builder.stack.size());
    Assert.assertEquals("tag", builder.stack.get(1).tagName());
    Assert.assertEquals("http://example.com", builder.stack.get(1).baseUri());
  }

  @Test
  public void testProcessEndTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    builder.stack.add(new Element(Tag.valueOf("tag"), "http://example.com"));
    Token.EndTag endTag = new Token.EndTag("tag");
    builder.process(endTag);
    
    // Assert that the element is removed from the stack
    Assert.assertEquals(1, builder.stack.size());
    Assert.assertEquals("xml", builder.stack.get(0).tagName());
  }

  @Test
  public void testProcessComment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    Token.Comment commentToken = new Token.Comment("This is a comment");
    builder.process(commentToken);
    
    // Assert that the comment is inserted correctly
    Assert.assertEquals(1, builder.stack.get(0).childNodes().size());
    Assert.assertTrue(builder.stack.get(0).childNodes().get(0) instanceof Comment);
    Assert.assertEquals("This is a comment", ((Comment)builder.stack.get(0).childNodes().get(0)).getData());
  }

  @Test
  public void testProcessCharacter() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    Token.Character characterToken = new Token.Character("This is character data");
    builder.process(characterToken);
    
    // Assert that the character data is inserted correctly
    Assert.assertEquals(1, builder.stack.get(0).childNodes().size());
    Assert.assertTrue(builder.stack.get(0).childNodes().get(0) instanceof TextNode);
    Assert.assertEquals("This is character data", ((TextNode)builder.stack.get(0).childNodes().get(0)).text());
  }

  @Test
  public void testProcessDoctype() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
    builder.process(doctype);
    
    // Assert that the doctype is inserted correctly
    Assert.assertEquals(1, builder.stack.get(0).childNodes().size());
    Assert.assertTrue(builder.stack.get(0).childNodes().get(0) instanceof DocumentType);
    DocumentType documentType = (DocumentType)builder.stack.get(0).childNodes().get(0);
    Assert.assertEquals("html", documentType.name());
    Assert.assertEquals("publicId", documentType.publicId());
    Assert.assertEquals("systemId", documentType.systemId());
    Assert.assertEquals("http://example.com", documentType.baseUri());
  }

  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
    builder.stack.add(new Element(Tag.valueOf("tag1"), "http://example.com"));
    builder.stack.add(new Element(Tag.valueOf("tag2"), "http://example.com"));
    builder.stack.add(new Element(Tag.valueOf("tag1"), "http://example.com"));
    Token.EndTag endTag = new Token.EndTag("tag1");
    builder.popStackToClose(endTag);
    
    // Assert that the first occurrence of the element with tag name "tag1" is removed from the stack
    Assert.assertEquals(2, builder.stack.size());
    Assert.assertEquals("tag2", builder.stack.get(1).tagName());
  }

  @Test
  public void testParseFragment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String inputFragment = "<tag>content</tag>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    ParseSettings settings = ParseSettings.preserveCase;
    List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
    
    // Assert that the parsed fragment contains the correct elements and their content
    Assert.assertEquals(1, nodes.size());
    Assert.assertEquals("tag", nodes.get(0).nodeName());
    Assert.assertEquals("content", ((Element)nodes.get(0)).text());
  }

}
