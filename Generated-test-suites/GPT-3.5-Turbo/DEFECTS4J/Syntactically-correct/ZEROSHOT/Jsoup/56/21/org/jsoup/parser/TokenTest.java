package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testDoctypeReset() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
    doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
    doctype.forceQuirks = true;
    
    doctype.reset();
    
    assertEquals("", doctype.name.toString());
    assertEquals("", doctype.publicIdentifier.toString());
    assertEquals("", doctype.systemIdentifier.toString());
    assertFalse(doctype.forceQuirks);
  }
  
  @Test
  public void testDoctypeGetName() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    
    String name = doctype.getName();
    
    assertEquals("html", name);
  }
  
  @Test
  public void testDoctypeGetPublicIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
    
    String publicIdentifier = doctype.getPublicIdentifier();
    
    assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", publicIdentifier);
  }
  
  @Test
  public void testDoctypeGetSystemIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
    
    String systemIdentifier = doctype.getSystemIdentifier();
    
    assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", systemIdentifier);
  }
  
  @Test
  public void testDoctypeIsForceQuirks() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.forceQuirks = true;
    
    boolean isForceQuirks = doctype.isForceQuirks();
    
    assertTrue(isForceQuirks);
  }
  
  @Test
  public void testStartTagReset() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "div";
    startTag.normalName = "div";
    startTag.pendingAttributeName = "id";
    startTag.pendingAttributeValue.append("test");
    startTag.pendingAttributeValueS = "test";
    startTag.hasEmptyAttributeValue = true;
    startTag.hasPendingAttributeValue = true;
    startTag.selfClosing = true;
    startTag.attributes = new Attributes();
    
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
  public void testStartTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "div";
    
    String tagName = startTag.name();
    
    assertEquals("div", tagName);
  }
  
  @Test
  public void testStartTagNormalName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.normalName = "div";
    
    String normalName = startTag.normalName();
    
    assertEquals("div", normalName);
  }
  
  @Test
  public void testStartTagIsSelfClosing() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.selfClosing = true;
    
    boolean isSelfClosing = startTag.isSelfClosing();
    
    assertTrue(isSelfClosing);
  }
  
  @Test
  public void testStartTagGetAttributes() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.attributes = new Attributes();
    
    Attributes attributes = startTag.getAttributes();
    
    assertEquals(new Attributes(), attributes);
  }
  
  @Test
  public void testStartTagAppendTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "d";
    
    startTag.appendTagName("iv");
    
    assertEquals("div", startTag.tagName);
  }
  
  @Test
  public void testStartTagAppendTagName_char() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "d";
    
    startTag.appendTagName('i');
    
    assertEquals("di", startTag.tagName);
  }
  
  @Test
  public void testStartTagAppendAttributeName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.pendingAttributeName = "i";
    
    startTag.appendAttributeName("d");
    
    assertEquals("id", startTag.pendingAttributeName);
  }
  
  @Test
  public void testStartTagAppendAttributeName_char() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.pendingAttributeName = "i";
    
    startTag.appendAttributeName('d');
    
    assertEquals("id", startTag.pendingAttributeName);
  }
  
  @Test
  public void testStartTagAppendAttributeValue() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.ensureAttributeValue();
    StringBuilder pendingAttributeValue = startTag.pendingAttributeValue;
    pendingAttributeValue.append("h");
    
    startTag.appendAttributeValue("ref");
    
    assertEquals("href", pendingAttributeValue.toString());
  }
  
  @Test
  public void testStartTagAppendAttributeValue_char() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.ensureAttributeValue();
    StringBuilder pendingAttributeValue = startTag.pendingAttributeValue;
    pendingAttributeValue.append("h");
    
    startTag.appendAttributeValue('r');
    
    assertEquals("hr", pendingAttributeValue.toString());
  }
  
  @Test
  public void testStartTagAppendAttributeValue_charArray() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.ensureAttributeValue();
    StringBuilder pendingAttributeValue = startTag.pendingAttributeValue;
    pendingAttributeValue.append("h");
    
    startTag.appendAttributeValue(new char[]{'r','e','f'});
    
    assertEquals("href", pendingAttributeValue.toString());
  }
  
  @Test
  public void testStartTagAppendAttributeValue_intArray() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.ensureAttributeValue();
    StringBuilder pendingAttributeValue = startTag.pendingAttributeValue;
    pendingAttributeValue.append("h");
    
    startTag.appendAttributeValue(new int[]{114,101,102});
    
    assertEquals("href", pendingAttributeValue.toString());
  }
  
  @Test
  public void testStartTagSetEmptyAttributeValue() {
    Token.StartTag startTag = new Token.StartTag();
    
    startTag.setEmptyAttributeValue();
    
    assertTrue(startTag.hasEmptyAttributeValue);
  }
  
  @Test
  public void testEndTagReset() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "div";
    endTag.normalName = "div";
    
    endTag.reset();
    
    assertNull(endTag.tagName);
    assertNull(endTag.normalName);
  }
  
  @Test
  public void testCommentReset() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("This is a comment");
    comment.bogus = true;
    
    comment.reset();
    
    assertEquals("", comment.data.toString());
    assertFalse(comment.bogus);
  }
  
  @Test
  public void testCommentGetData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("This is a comment");
    
    String data = comment.getData();
    
    assertEquals("This is a comment", data);
  }
  
  @Test
  public void testCharacterReset() {
    Token.Character character = new Token.Character();
    character.data = "This is a character";
    
    character.reset();
    
    assertNull(character.data);
  }
  
  @Test
  public void testCharacterData() {
    Token.Character character = new Token.Character();
    
    character.data("This is a character");
    
    assertEquals("This is a character", character.getData());
  }
}