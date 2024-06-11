import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testTokenType() {
    Token token = new Token.Doctype();
    assertEquals("Doctype", token.tokenType());
    
    token = new Token.StartTag();
    assertEquals("StartTag", token.tokenType());
    
    token = new Token.EndTag();
    assertEquals("EndTag", token.tokenType());
    
    token = new Token.Comment();
    assertEquals("Comment", token.tokenType());
    
    token = new Token.Character("");
    assertEquals("Character", token.tokenType());
    
    token = new Token.EOF();
    assertEquals("EOF", token.tokenType());
  }
  
  @Test
  public void testDoctypeGetName() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    assertEquals("html", doctype.getName());
  }
  
  @Test
  public void testDoctypeGetPublicIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.publicIdentifier.append("//DTD HTML 4.01 Transitional//EN");
    assertEquals("//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
  }
  
  @Test
  public void testDoctypeGetSystemIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
    assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
  }
  
  @Test
  public void testDoctypeIsForceQuirks() {
    Token.Doctype doctype = new Token.Doctype();
    assertFalse(doctype.isForceQuirks());
    
    doctype.forceQuirks = true;
    assertTrue(doctype.isForceQuirks());
  }
  
  @Test
  public void testStartTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "div";
    assertEquals("div", startTag.name());
    
    startTag.name("span");
    assertEquals("span", startTag.name());
  }
  
  @Test
  public void testStartTagIsSelfClosing() {
    Token.StartTag startTag = new Token.StartTag();
    assertFalse(startTag.isSelfClosing());
    
    startTag.selfClosing = true;
    assertTrue(startTag.isSelfClosing());
  }
  
  @Test
  public void testStartTagGetAttributes() {
    Token.StartTag startTag = new Token.StartTag();
    assertNotNull(startTag.getAttributes());
    
    Attributes attributes = new Attributes();
    attributes.put("class", "container");
    
    startTag.attributes = attributes;
    assertEquals(attributes, startTag.getAttributes());
  }
  
  @Test
  public void testStartTagAppendTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.appendTagName("d");
    assertEquals("d", startTag.tagName);
    
    startTag.appendTagName("i");
    assertEquals("di", startTag.tagName);
    
    startTag.appendTagName('v');
    assertEquals("div", startTag.tagName);
  }
  
  @Test
  public void testStartTagAppendAttributeName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.appendAttributeName("c");
    assertEquals("c", startTag.pendingAttributeName);
    
    startTag.appendAttributeName("l");
    assertEquals("cl", startTag.pendingAttributeName);
    
    startTag.appendAttributeName('a');
    assertEquals("cla", startTag.pendingAttributeName);
  }
  
  @Test
  public void testStartTagAppendAttributeValue() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.appendAttributeValue("h");
    assertEquals("h", startTag.pendingAttributeValue.toString());
    
    startTag.appendAttributeValue("t");
    assertEquals("ht", startTag.pendingAttributeValue.toString());
    
    startTag.appendAttributeValue('m');
    assertEquals("htm", startTag.pendingAttributeValue.toString());
  }
  
  @Test
  public void testEndTagName() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "div";
    assertEquals("div", endTag.name());
    
    endTag.name("span");
    assertEquals("span", endTag.name());
  }
  
  @Test
  public void testCommentGetData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("This is a comment.");
    assertEquals("This is a comment.", comment.getData());
  }
  
  @Test
  public void testCharacterGetData() {
    Token.Character character = new Token.Character("Hello, World!");
    assertEquals("Hello, World!", character.getData());
  }
  
  @Test
  public void testIsDoctype() {
    Token token = new Token.Doctype();
    assertTrue(token.isDoctype());
    
    token = new Token.StartTag();
    assertFalse(token.isDoctype());
  }
  
  @Test
  public void testAsDoctype() {
    Token token = new Token.Doctype();
    Token.Doctype doctype = token.asDoctype();
    assertNotNull(doctype);
    
    token = new Token.StartTag();
    assertNull(token.asDoctype());
  }
  
  @Test
  public void testIsStartTag() {
    Token token = new Token.StartTag();
    assertTrue(token.isStartTag());
    
    token = new Token.EndTag();
    assertFalse(token.isStartTag());
  }
  
  @Test
  public void testAsStartTag() {
    Token token = new Token.StartTag();
    Token.StartTag startTag = token.asStartTag();
    assertNotNull(startTag);
    
    token = new Token.EndTag();
    assertNull(token.asStartTag());
  }
  
  @Test
  public void testIsEndTag() {
    Token token = new Token.EndTag();
    assertTrue(token.isEndTag());
    
    token = new Token.Comment();
    assertFalse(token.isEndTag());
  }
  
  @Test
  public void testAsEndTag() {
    Token token = new Token.EndTag();
    Token.EndTag endTag = token.asEndTag();
    assertNotNull(endTag);
    
    token = new Token.Comment();
    assertNull(token.asEndTag());
  }
  
  @Test
  public void testIsComment() {
    Token token = new Token.Comment();
    assertTrue(token.isComment());
    
    token = new Token.Character("");
    assertFalse(token.isComment());
  }
  
  @Test
  public void testAsComment() {
    Token token = new Token.Comment();
    Token.Comment comment = token.asComment();
    assertNotNull(comment);
    
    token = new Token.Character("");
    assertNull(token.asComment());
  }
  
  @Test
  public void testIsCharacter() {
    Token token = new Token.Character("");
    assertTrue(token.isCharacter());
    
    token = new Token.StartTag();
    assertFalse(token.isCharacter());
  }
  
  @Test
  public void testAsCharacter() {
    Token token = new Token.Character("");
    Token.Character character = token.asCharacter();
    assertNotNull(character);
    
    token = new Token.StartTag();
    assertNull(token.asCharacter());
  }
  
  @Test
  public void testIsEOF() {
    Token token = new Token.EOF();
    assertTrue(token.isEOF());
    
    token = new Token.Doctype();
    assertFalse(token.isEOF());
  }
  
  
}