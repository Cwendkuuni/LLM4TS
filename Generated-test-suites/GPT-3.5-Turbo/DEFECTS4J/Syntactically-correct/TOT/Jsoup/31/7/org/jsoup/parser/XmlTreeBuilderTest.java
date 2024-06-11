import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {
  
  private XmlTreeBuilder xmlTreeBuilder;
  
  @Before
  public void setup() {
    xmlTreeBuilder = new XmlTreeBuilder();
  }
  
  @Test
  public void testInitialiseParse() {
    String input = "<xml>Test</xml>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    
    xmlTreeBuilder.initialiseParse(input, baseUri, errors);
    
    Assert.assertEquals("http://example.com", xmlTreeBuilder.getBaseUri());
    Assert.assertEquals(errors, xmlTreeBuilder.getErrors());
    Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
    Assert.assertEquals("xml", xmlTreeBuilder.getStack().peek().nodeName());
  }
  
  @Test
  public void testProcessStartTag() {
    Token token = new Token.StartTag("tag", "attr1=value1");
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
    Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
    Assert.assertEquals("tag", xmlTreeBuilder.getStack().peek().nodeName());
    Assert.assertEquals("value1", xmlTreeBuilder.getStack().peek().attr("attr1"));
  }
  
  @Test
  public void testProcessEndTag() {
    Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1");
    xmlTreeBuilder.process(startTag);
    
    Token token = new Token.EndTag("tag");
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
    Assert.assertEquals(0, xmlTreeBuilder.getStack().size());
  }
  
  @Test
  public void testProcessComment() {
    String data = "Sample comment";
    Token.Comment token = new Token.Comment(data);
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals(data, xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testProcessCharacter() {
    String data = "Sample text";
    Token.Character token = new Token.Character(data);
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals(data, xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testProcessDoctype() {
    String name = "html";
    String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
    String systemId = "http://www.w3.org/TR/html4/loose.dtd";
    Token.Doctype token = new Token.Doctype(name, publicId, systemId);
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
                         xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testProcessEOF() {
    Token token = new Token.EOF();
    
    boolean result = xmlTreeBuilder.process(token);
    
    Assert.assertTrue(result);
  }
  
  @Test(expected = AssertionError.class)
  public void testProcessInvalidToken() {
    Token token = new Token("Invalid");
    
    xmlTreeBuilder.process(token);
  }
  
  @Test
  public void testInsertNode() {
    Element element = new Element(Tag.valueOf("tag"), "http://example.com");
    
    xmlTreeBuilder.insertNode(element);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals(element, xmlTreeBuilder.getDocument().childNode(0));
  }
  
  @Test
  public void testInsertStartTag() {
    Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1");
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("tag", xmlTreeBuilder.getDocument().childNode(0).nodeName());
    Assert.assertEquals("value1", xmlTreeBuilder.getDocument().childNode(0).attr("attr1"));
    Assert.assertEquals(element, xmlTreeBuilder.getStack().peek());
  }
  
  @Test
  public void testInsertStartTagSelfClosing() {
    Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1", true);
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("tag", xmlTreeBuilder.getDocument().childNode(0).nodeName());
    Assert.assertEquals("value1", xmlTreeBuilder.getDocument().childNode(0).attr("attr1"));
    Assert.assertEquals(element, xmlTreeBuilder.getStack().peek());
    Assert.assertTrue(element.tag().isKnownTag());
  }
  
  @Test
  public void testInsertStartTagUnknown() {
    Token.StartTag startTag = new Token.StartTag("unknown", "attr1=value1");
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("unknown", xmlTreeBuilder.getDocument().childNode(0).nodeName());
    Assert.assertEquals("value1", xmlTreeBuilder.getDocument().childNode(0).attr("attr1"));
    Assert.assertEquals(element, xmlTreeBuilder.getStack().peek());
    Assert.assertTrue(element.tag().isKnownTag());
    Assert.assertTrue(element.tag().isSelfClosing());
  }
  
  @Test
  public void testInsertComment() {
    Token.Comment commentToken = new Token.Comment("Sample comment");
    
    xmlTreeBuilder.insert(commentToken);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("<!--Sample comment-->", xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testInsertCharacter() {
    Token.Character characterToken = new Token.Character("Sample text");
    
    xmlTreeBuilder.insert(characterToken);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("Sample text", xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testInsertDoctype() {
    Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
    
    xmlTreeBuilder.insert(doctype);
    
    Assert.assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
    Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
                         xmlTreeBuilder.getDocument().childNode(0).outerHtml());
  }
  
  @Test
  public void testPopStackToClose() {
    Token.StartTag startTag1 = new Token.StartTag("tag", "");
    xmlTreeBuilder.process(startTag1);
    Token.StartTag startTag2 = new Token.StartTag("tag", "");
    xmlTreeBuilder.process(startTag2);
    Token.StartTag startTag3 = new Token.StartTag("tag", "");
    xmlTreeBuilder.process(startTag3);
    Token.EndTag endTag = new Token.EndTag("tag");
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    Assert.assertEquals(2, xmlTreeBuilder.getStack().size());
    Assert.assertEquals("tag", xmlTreeBuilder.getStack().peek().nodeName());
  }
  
  @Test
  public void testPopStackToCloseNotFound() {
    Token.StartTag startTag = new Token.StartTag("tag", "");
    xmlTreeBuilder.process(startTag);
    Token.EndTag endTag = new Token.EndTag("unknown");
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
  }
}