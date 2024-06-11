package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    startTagToken = new Token.StartTag("div");
    endTagToken = new Token.EndTag("div");
    commentToken = new Token.Comment();
    characterToken = new Token.Character("test");
    eofToken = new Token.EOF();
  }
  
  @Test
  public void testDoctypeTokenType() {
    assertEquals(Token.TokenType.Doctype, doctypeToken.type);
  }
  
  @Test
  public void testDoctypeName() {
    doctypeToken.name.append("html");
    assertEquals("html", doctypeToken.getName());
  }
  
  @Test
  public void testDoctypePublicIdentifier() {
    doctypeToken.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
    assertEquals("-//W3C//DTD HTML 4.01//EN", doctypeToken.getPublicIdentifier());
  }
  
  @Test
  public void testDoctypeSystemIdentifier() {
    doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeToken.getSystemIdentifier());
  }
  
  @Test
  public void testDoctypeForceQuirks() {
    assertFalse(doctypeToken.isForceQuirks());
  }
  
  @Test
  public void testStartTagTokenType() {
    assertEquals(Token.TokenType.StartTag, startTagToken.type);
  }
  
  @Test
  public void testStartTagName() {
    assertEquals("div", startTagToken.name());
  }
  
  @Test
  public void testStartTagSelfClosing() {
    assertFalse(startTagToken.isSelfClosing());
  }
  
  @Test
  public void testStartTagAttributes() {
    assertNull(startTagToken.getAttributes());
  }
  
  @Test
  public void testEndTagTokenType() {
    assertEquals(Token.TokenType.EndTag, endTagToken.type);
  }
  
  @Test
  public void testEndTagName() {
    assertEquals("div", endTagToken.name());
  }
  
  @Test
  public void testCommentTokenType() {
    assertEquals(Token.TokenType.Comment, commentToken.type);
  }
  
  @Test
  public void testCommentData() {
    commentToken.data.append("This is a comment");
    assertEquals("This is a comment", commentToken.getData());
  }
  
  @Test
  public void testCharacterTokenType() {
    assertEquals(Token.TokenType.Character, characterToken.type);
  }
  
  @Test
  public void testCharacterData() {
    assertEquals("test", characterToken.getData());
  }
  
  @Test
  public void testEOFTokenType() {
    assertEquals(Token.TokenType.EOF, eofToken.type);
  }
  
  @Test
  public void testEOFToken() {
    assertTrue(eofToken.isEOF());
  }
  
}