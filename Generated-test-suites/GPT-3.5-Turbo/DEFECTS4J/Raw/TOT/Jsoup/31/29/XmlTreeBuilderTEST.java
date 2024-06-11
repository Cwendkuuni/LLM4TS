import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest{
  
  private XmlTreeBuilder xmlTreeBuilder;

  @Before
  public void setUp(){
    xmlTreeBuilder = new XmlTreeBuilder();
  }

  @Test
  public void testInitialiseParse(){
    String input = "<tag></tag>";
    String baseUri = "http://www.example.com";
    ParseErrorList errors = new ParseErrorList();
    
    xmlTreeBuilder.initialiseParse(input, baseUri, errors);
    
    // Check if the document is added to the stack
    assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
  }

  @Test
  public void testProcess_StartTag(){
    Token token = new Token(Token.TokenType.StartTag);
    token.value = "<tag>";
    
    boolean result = xmlTreeBuilder.process(token);
    
    // Check if the correct tag is inserted
    assertEquals(token.asStartTag().name(), xmlTreeBuilder.currentElement().tagName());
    // Check if the process method returns true
    assertTrue(result);
  }

  @Test
  public void testProcess_EndTag(){
    Token token = new Token(Token.TokenType.EndTag);
    token.value = "</tag>";

    boolean result = xmlTreeBuilder.process(token);

    // Check if the stack is properly modified after popping the element
    assertTrue(xmlTreeBuilder.stack.isEmpty());
    // Check if the process method returns true
    assertTrue(result);
  }

  @Test
  public void testProcess_Comment(){
    Token token = new Token(Token.TokenType.Comment);
    token.value = "<!-- This is a comment -->";

    boolean result = xmlTreeBuilder.process(token);

    // Check if the comment is inserted
    assertEquals(token.asComment().getData(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    // Check if the process method returns true
    assertTrue(result);
  }

  @Test
  public void testProcess_Character(){
    Token token = new Token(Token.TokenType.Character);
    token.value = "Some text";

    boolean result = xmlTreeBuilder.process(token);

    // Check if the character is inserted
    assertEquals(token.asCharacter().getData(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    // Check if the process method returns true
    assertTrue(result);
  }

  @Test
  public void testProcess_Doctype(){
    Token token = new Token(Token.TokenType.Doctype);
    token.value = "<!DOCTYPE html>";

    boolean result = xmlTreeBuilder.process(token);

    // Check if the doctype is inserted
    assertEquals(token.asDoctype().getName(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    // Check if the process method returns true
    assertTrue(result);
  }
  
  @Test
  public void testInsertNode(){
    Node node = new Node("test");
    
    xmlTreeBuilder.insertNode(node);
    
    // Check if the node is correctly added as a child of the current element
    assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
  }
  
  @Test
  public void testInsert_StartTag(){
    Token.StartTag startTag = new Token.StartTag();
    startTag.value = "<tag>";
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    // Check if the correct element is inserted
    assertEquals(startTag.name(), element.tagName());
    // Check if the element is added to the stack if it is not self-closing
    assertTrue(xmlTreeBuilder.stack.contains(element));
    // Check if the process method correctly acknowledges the self-closing flag
    assertEquals(Token.TokenType.EndTag, xmlTreeBuilder.tokeniser.getTokenType());
  }
  
  @Test
  public void testInsert_StartTag_SelfClosing(){
    Token.StartTag startTag = new Token.StartTag();
    startTag.value = "<tag/>";
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    // Check if the correct element is inserted
    assertEquals(startTag.name(), element.tagName());
    // Check if the element is not added to the stack if it is self-closing
    assertFalse(xmlTreeBuilder.stack.contains(element));
    // Check if the process method correctly acknowledges the self-closing flag
    assertEquals(Token.TokenType.EOF, xmlTreeBuilder.tokeniser.getTokenType());
  }
  
  @Test
  public void testInsert_StartTag_UnknownTag(){
    Token.StartTag startTag = new Token.StartTag();
    startTag.value = "<unknown>";
    
    Element element = xmlTreeBuilder.insert(startTag);
    
    // Check if the correct element is inserted
    assertEquals(startTag.name(), element.tagName());
    // Check if the element is not added to the stack if it is self-closing
    assertFalse(xmlTreeBuilder.stack.contains(element));
    // Check if the tag is flagged as self-closing
    assertTrue(element.tag().isSelfClosing());
    // Check if the process method correctly acknowledges the self-closing flag
    assertEquals(Token.TokenType.EOF, xmlTreeBuilder.tokeniser.getTokenType());
  }
  
  @Test
  public void testInsert_Comment(){
    Token.Comment commentToken = new Token.Comment();
    commentToken.value = "<!-- This is a comment -->";
    
    xmlTreeBuilder.insert(commentToken);
    
    // Check if the comment is inserted
    assertEquals(commentToken.getData(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
  }
  
  @Test
  public void testInsert_Character(){
    Token.Character characterToken = new Token.Character();
    characterToken.value = "Some text";
    
    xmlTreeBuilder.insert(characterToken);
    
    // Check if the character is inserted
    assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
  }
  
  @Test
  public void testInsert_Doctype(){
    Token.Doctype doctype = new Token.Doctype();
    doctype.value = "<!DOCTYPE html>";
    
    xmlTreeBuilder.insert(doctype);
    
    // Check if the doctype is inserted
    assertEquals(doctype.getName(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
  }
  
  @Test
  public void testPopStackToClose(){
    Element elem1 = new Element("tag1");
    Element elem2 = new Element("tag2");
    Element elem3 = new Element("tag3");
    
    xmlTreeBuilder.stack.add(elem1);
    xmlTreeBuilder.stack.add(elem2);
    xmlTreeBuilder.stack.add(elem3);
    
    Token.EndTag endTag = new Token.EndTag();
    endTag.value = "</tag2>";
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    // Check if the element is correctly removed from the stack
    assertFalse(xmlTreeBuilder.stack.contains(elem2));
  }
  
  @After
  public void tearDown(){
    xmlTreeBuilder = null;
  }
}