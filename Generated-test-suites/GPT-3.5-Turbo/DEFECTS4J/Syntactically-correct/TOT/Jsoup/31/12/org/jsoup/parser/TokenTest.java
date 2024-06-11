import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class TokenTest {
  
  private Token.Doctype doctypeToken;
  private Token.StartTag startTagToken;
  private Token.EndTag endTagToken;
  private Token.Comment commentToken;
  private Token.Character characterToken;
  private Token.EOF eofToken;
  
  @Before
  public void setUp() {
    doctypeToken = new Token.Doctype();
    startTagToken = new Token.StartTag();
    endTagToken = new Token.EndTag();
    commentToken = new Token.Comment();
    characterToken = new Token.Character("");
    eofToken = new Token.EOF();
  }
  
  @Test
  public void testDoctypeGetName() {
    doctypeToken.name.append("html");
    assertEquals("html", doctypeToken.getName());
  }
  
  @Test
  public void testDoctypeGetPublicIdentifier() {
    doctypeToken.publicIdentifier.append("Public Identifier");
    assertEquals("Public Identifier", doctypeToken.getPublicIdentifier());
  }
  
  @Test
  public void testDoctypeGetSystemIdentifier() {
    doctypeToken.systemIdentifier.append("System Identifier");
    assertEquals("System Identifier", doctypeToken.getSystemIdentifier());
  }
  
  @Test
  public void testDoctypeIsForceQuirks() {
    assertFalse(doctypeToken.isForceQuirks());
    
    doctypeToken.forceQuirks = true;
    assertTrue(doctypeToken.isForceQuirks());
  }
  
  @Test
  public void testStartTagNewAttribute() {
    startTagToken.newAttribute();
    assertEquals(1, startTagToken.attributes.size());
    assertTrue(startTagToken.attributes.hasKey("attribute"));
    assertEquals("", startTagToken.attributes.get("attribute"));
  }
  
  @Test
  public void testStartTagFinaliseTag() {
    startTagToken.appendAttributeName("attribute");
    startTagToken.appendAttributeValue("value");
    startTagToken.finaliseTag();
    
    assertEquals(1, startTagToken.attributes.size());
    assertTrue(startTagToken.attributes.hasKey("attribute"));
    assertEquals("value", startTagToken.attributes.get("attribute"));
  }
  
  @Test
  public void testStartTagName() {
    startTagToken.tagName = "tag";
    assertEquals("tag", startTagToken.name());
  }
  
  @Test
  public void testStartTagIsSelfClosing() {
    assertFalse(startTagToken.isSelfClosing());
    
    startTagToken.selfClosing = true;
    assertTrue(startTagToken.isSelfClosing());
  }
  
  @Test
  public void testStartTagGetAttributes() {
    startTagToken.attributes = new Attributes();
    assertEquals(startTagToken.attributes, startTagToken.getAttributes());
  }
  
  @Test
  public void testEndTagName() {
    endTagToken.tagName = "tag";
    assertEquals("tag", endTagToken.name());
  }
  
  @Test
  public void testCommentGetData() {
    commentToken.data.append("This is a comment");
    assertEquals("This is a comment", commentToken.getData());
  }
  
  @Test
  public void testCharacterGetData() {
    characterToken = new Token.Character("Data");
    assertEquals("Data", characterToken.getData());
  }
  
  @Test
  public void testEOF() {
    assertTrue(eofToken.isEOF());
  }
}