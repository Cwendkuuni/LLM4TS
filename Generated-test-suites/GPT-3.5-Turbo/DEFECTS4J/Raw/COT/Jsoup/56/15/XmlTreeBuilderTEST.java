import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  @Test
  public void testParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<root><element></element></root>";
    String baseUri = "http://example.com";
    Document doc = builder.parse(input, baseUri);
    
    assertNotNull(doc);
    assertEquals("root", doc.nodeName());
    assertEquals(baseUri, doc.baseUri());
  }
  
  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String input = "<root><element></element></root>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    ParseSettings settings = builder.defaultSettings();
    
    builder.initialiseParse(input, baseUri, errors, settings);
    
    assertEquals(input, builder.inputHtml());
    assertEquals(baseUri, builder.baseUri());
    assertEquals(errors, builder.errors());
    assertEquals(settings, builder.settings());
    assertEquals(2, builder.stack().size());
  }
  
  @Test
  public void testProcess() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token token = new Token(Token.TokenType.StartTag, "<element>", 1);
    
    assertTrue(builder.process(token));
    
    // additional assertions
  }
  
  @Test
  public void testInsertNode() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Node node = new Element("element");
    
    builder.insertNode(node);
    
    // additional assertions
  }
  
  @Test
  public void testInsert() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Comment commentToken = new Token.Comment("comment");
    
    builder.insert(commentToken);
    
    // additional assertions
  }
  
  @Test
  public void testInsertTokenCharacter() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Character characterToken = new Token.Character("data");
    
    builder.insert(characterToken);
    
    // additional assertions
  }
  
  @Test
  public void testInsertTokenDoctype() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Doctype doctypeToken = new Token.Doctype("doctype");
    
    builder.insert(doctypeToken);
    
    // additional assertions
  }
  
  @Test
  public void testInsertStartTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("element");
    
    Element el = builder.insert(startTag);
    
    assertNotNull(el);
    assertEquals("element", el.nodeName());
    // more assertions
  }
  
  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.EndTag endTag = new Token.EndTag("element");
    
    // add elements to stack
    builder.stack().add(new Element("element"));
    builder.stack().add(new Element("element2"));
    
    builder.popStackToClose(endTag);
    
    // additional assertions
  }
  
  @Test
  public void testParseFragment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    String inputFragment = "<element>test</element>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    ParseSettings settings = builder.defaultSettings();
    
    List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
    
    assertNotNull(nodes);
    assertEquals(1, nodes.size());
    // additional assertions
  }
}

