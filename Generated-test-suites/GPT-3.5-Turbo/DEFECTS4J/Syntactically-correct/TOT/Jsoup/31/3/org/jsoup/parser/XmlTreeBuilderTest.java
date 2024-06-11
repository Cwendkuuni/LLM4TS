import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  // Test for initialiseParse method
  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    String input = "<xml></xml>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    
    treeBuilder.initialiseParse(input, baseUri, errors);
    
    // Test if the document is added to the stack
    assertNotNull(treeBuilder.stack.peek());
    assertEquals("xml", treeBuilder.stack.peek().nodeName());
  }
  
  // Test for process method when token type is StartTag
  @Test
  public void testProcessStartTag() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
    
    // Test if the start tag is correctly inserted and added to the stack
    assertNotNull(treeBuilder.insert(startTag));
    assertEquals("tag", treeBuilder.stack.peek().nodeName());
    
    // Test if the start tag is correctly inserted and not added to the stack if it is self closing
    startTag.setSelfClosing();
    assertNotNull(treeBuilder.insert(startTag));
    assertNull(treeBuilder.stack.peek());
  }
  
  // Test for process method when token type is EndTag
  @Test
  public void testProcessEndTag() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.EndTag endTag = new Token.EndTag("tag");
    
    // Test if the stack is correctly popped to close the matching start tag
    treeBuilder.stack.add(new Element("tag"));
    treeBuilder.stack.add(new Element("otherTag"));
    treeBuilder.stack.add(new Element("tag"));
    treeBuilder.popStackToClose(endTag);
    assertEquals("otherTag", treeBuilder.stack.peek().nodeName());
    
    // Test if the stack remains the same if no matching start tag is found
    treeBuilder.stack.add(new Element("otherTag"));
    treeBuilder.popStackToClose(endTag);
    assertEquals("otherTag", treeBuilder.stack.peek().nodeName());
  }
  
  // Test for process method when token type is Comment
  @Test
  public void testProcessComment() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Comment comment = new Token.Comment("This is a comment");
    
    // Test if the comment is correctly inserted as a Comment node
    treeBuilder.insert(comment);
    assertTrue(treeBuilder.stack.peek() instanceof Comment);
    assertEquals("This is a comment", treeBuilder.stack.peek().nodeValue());
  }
  
  // Test for process method when token type is Character
  @Test
  public void testProcessCharacter() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Character character = new Token.Character("Text");
    
    // Test if the character is correctly inserted as a TextNode
    treeBuilder.insert(character);
    assertTrue(treeBuilder.stack.peek() instanceof TextNode);
    assertEquals("Text", treeBuilder.stack.peek().nodeValue());
  }
  
  // Test for process method when token type is Doctype
  @Test
  public void testProcessDoctype() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token.Doctype doctype = new Token.Doctype("html", "", "", "");
    
    // Test if the doctype is correctly inserted as a DocumentType node
    treeBuilder.insert(doctype);
    assertTrue(treeBuilder.stack.peek() instanceof DocumentType);
    assertEquals("html", treeBuilder.stack.peek().nodeName());
  }
  
  // Test for process method when token type is EOF
  @Test
  public void testProcessEOF() {
    XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
    Token token = new Token(Token.TokenType.EOF);
    
    // Test if the method returns true for EOF token type
    assertTrue(treeBuilder.process(token));
  }
  
}