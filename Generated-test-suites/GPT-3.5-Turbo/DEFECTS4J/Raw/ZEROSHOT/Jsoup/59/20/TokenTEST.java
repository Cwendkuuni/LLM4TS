import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testReset() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    doctype.pubSysKey = "PUBLIC";
    doctype.publicIdentifier.append("public-identifier");
    doctype.systemIdentifier.append("system-identifier");
    doctype.forceQuirks = true;
    
    doctype.reset();
    
    assertEquals("", doctype.name.toString());
    assertNull(doctype.pubSysKey);
    assertEquals("", doctype.publicIdentifier.toString());
    assertEquals("", doctype.systemIdentifier.toString());
    assertFalse(doctype.forceQuirks);
  }
  
  @Test
  public void testGetName() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    
    String name = doctype.getName();
    
    assertEquals("html", name);
  }
  
  @Test
  public void testGetPubSysKey() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.pubSysKey = "PUBLIC";
    
    String pubSysKey = doctype.getPubSysKey();
    
    assertEquals("PUBLIC", pubSysKey);
  }
  
  @Test
  public void testGetPublicIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.publicIdentifier.append("public-identifier");
    
    String publicIdentifier = doctype.getPublicIdentifier();
    
    assertEquals("public-identifier", publicIdentifier);
  }
  
  @Test
  public void testGetSystemIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.systemIdentifier.append("system-identifier");
    
    String systemIdentifier = doctype.getSystemIdentifier();
    
    assertEquals("system-identifier", systemIdentifier);
  }
  
  @Test
  public void testIsForceQuirks() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.forceQuirks = true;
    
    boolean isForceQuirks = doctype.isForceQuirks();
    
    assertTrue(isForceQuirks);
  }
  
  @Test
  public void testTagReset() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "div";
    startTag.normalName = "div";
    startTag.pendingAttributeName = "class";
    startTag.pendingAttributeValue.append("container");
    startTag.pendingAttributeValueS = "container";
    startTag.hasEmptyAttributeValue = true;
    startTag.hasPendingAttributeValue = true;
    startTag.selfClosing = true;
    startTag.attributes = new org.jsoup.nodes.Attributes();
    
    startTag.reset();
    
    assertNull(startTag.tagName);
    assertNull(startTag.normalName);
    assertNull(startTag.pendingAttributeName);
    assertEquals("", startTag.pendingAttributeValue.toString());
    assertNull(startTag.pendingAttributeValueS);
    assertFalse(startTag.hasEmptyAttributeValue);
    assertFalse(startTag.hasPendingAttributeValue);
    assertFalse(startTag.selfClosing);
    assertNull(startTag.attributes);
  }
  
  @Test
  public void testStartTagNameAttr() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.nameAttr("div", new org.jsoup.nodes.Attributes());
    
    assertEquals("div", startTag.tagName);
    assertEquals("div", startTag.normalName);
  }
  
  @Test
  public void testStartTagToString() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "div";
    startTag.attributes = new org.jsoup.nodes.Attributes();
    
    String result = startTag.toString();
    
    assertEquals("<div>", result);
  }
  
  @Test
  public void testEndTagToString() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "div";
    
    String result = endTag.toString();
    
    assertEquals("</div>", result);
  }
  
  @Test
  public void testCommentReset() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("comment data");
    comment.bogus = true;
    
    comment.reset();
    
    assertEquals("", comment.data.toString());
    assertFalse(comment.bogus);
  }
  
  @Test
  public void testGetCommentData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("comment data");
    
    String commentData = comment.getData();
    
    assertEquals("comment data", commentData);
  }
  
  @Test
  public void testCharacterReset() {
    Token.Character character = new Token.Character();
    character.data = "character data";
    
    character.reset();
    
    assertNull(character.data);
  }
  
  @Test
  public void testCharacterData() {
    Token.Character character = new Token.Character();
    
    character.data("character data");
    
    assertEquals("character data", character.data);
  }
  
  @Test
  public void testEOFReset() {
    Token.EOF eof = new Token.EOF();
    
    eof.reset();
    
    assertEquals(Token.EOF.class, eof.getClass());
  }
  
  @Test
  public void testIsDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    
    boolean isDoctype = doctype.isDoctype();
    
    assertTrue(isDoctype);
  }
  
  @Test
  public void testAsDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    
    Token.Doctype asDoctype = doctype.asDoctype();
    
    assertNotNull(asDoctype);
  }
  
  @Test
  public void testIsStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    
    boolean isStartTag = startTag.isStartTag();
    
    assertTrue(isStartTag);
  }
  
  @Test
  public void testAsStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    
    Token.StartTag asStartTag = startTag.asStartTag();
    
    assertNotNull(asStartTag);
  }
  
  @Test
  public void testIsEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    
    boolean isEndTag = endTag.isEndTag();
    
    assertTrue(isEndTag);
  }
  
  @Test
  public void testAsEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    
    Token.EndTag asEndTag = endTag.asEndTag();
    
    assertNotNull(asEndTag);
  }
  
  @Test
  public void testIsComment() {
    Token.Comment comment = new Token.Comment();
    
    boolean isComment = comment.isComment();
    
    assertTrue(isComment);
  }
  
  @Test
  public void testAsComment() {
    Token.Comment comment = new Token.Comment();
    
    Token.Comment asComment = comment.asComment();
    
    assertNotNull(asComment);
  }
  
  @Test
  public void testIsCharacter() {
    Token.Character character = new Token.Character();
    
    boolean isCharacter = character.isCharacter();
    
    assertTrue(isCharacter);
  }
  
  @Test
  public void testAsCharacter() {
    Token.Character character = new Token.Character();
    
    Token.Character asCharacter = character.asCharacter();
    
    assertNotNull(asCharacter);
  }
  
  @Test
  public void testIsEOF() {
    Token.EOF eof = new Token.EOF();
    
    boolean isEOF = eof.isEOF();
    
    assertTrue(isEOF);
  }
}