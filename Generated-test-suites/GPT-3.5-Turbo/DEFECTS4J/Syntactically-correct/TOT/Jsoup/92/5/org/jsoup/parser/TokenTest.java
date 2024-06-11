package org.jsoup.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

  private static Token.Doctype doctypeToken;
  private static Token.StartTag startTagToken;
  private static Token.EndTag endTagToken;
  private static Token.Comment commentToken;
  private static Token.Character characterToken;
  private static Token.CData cdataToken;
  private static Token.EOF eofToken;

  @BeforeClass
  public static void setUp() {
    doctypeToken = new Token.Doctype();
    startTagToken = new Token.StartTag();
    endTagToken = new Token.EndTag();
    commentToken = new Token.Comment();
    characterToken = new Token.Character();
    cdataToken = new Token.CData();
    eofToken = new Token.EOF();
  }

  // Test cases for Doctype token
  @Test
  public void testDoctypeReset() {
    doctypeToken.name.append("html");
    doctypeToken.pubSysKey = "pub";
    doctypeToken.publicIdentifier.append("pubId");
    doctypeToken.systemIdentifier.append("sysId");
    doctypeToken.forceQuirks = true;

    doctypeToken.reset();

    assertEquals("", doctypeToken.name.toString());
    assertEquals(null, doctypeToken.pubSysKey);
    assertEquals("", doctypeToken.publicIdentifier.toString());
    assertEquals("", doctypeToken.systemIdentifier.toString());
    assertEquals(false, doctypeToken.forceQuirks);
  }

  @Test
  public void testDoctypeGetName() {
    doctypeToken.name.append("html");

    assertEquals("html", doctypeToken.getName());
  }

  @Test
  public void testDoctypeGetPubSysKey() {
    doctypeToken.pubSysKey = "pub";

    assertEquals("pub", doctypeToken.getPubSysKey());
  }

  @Test
  public void testDoctypeGetPublicIdentifier() {
    doctypeToken.publicIdentifier.append("pubId");

    assertEquals("pubId", doctypeToken.getPublicIdentifier());
  }

  @Test
  public void testDoctypeGetSystemIdentifier() {
    doctypeToken.systemIdentifier.append("sysId");

    assertEquals("sysId", doctypeToken.getSystemIdentifier());
  }

  @Test
  public void testDoctypeIsForceQuirks() {
    doctypeToken.forceQuirks = true;

    assertEquals(true, doctypeToken.isForceQuirks());
  }

  // Test cases for StartTag token
  @Test
  public void testStartTagReset() {
    startTagToken.tagName = "div";
    startTagToken.normalName = "div";
    startTagToken.pendingAttributeName = "class";
    startTagToken.pendingAttributeValue.append("container");
    startTagToken.hasPendingAttributeValue = true;
    startTagToken.selfClosing = true;
    startTagToken.attributes = new Attributes();

    startTagToken.reset();

    assertEquals(null, startTagToken.tagName);
    assertEquals(null, startTagToken.normalName);
    assertEquals(null, startTagToken.pendingAttributeName);
    assertEquals("", startTagToken.pendingAttributeValue.toString());
    assertEquals(false, startTagToken.hasPendingAttributeValue);
    assertEquals(false, startTagToken.selfClosing);
    assertEquals(null, startTagToken.attributes);
  }

  @Test
  public void testStartTagName() {
    startTagToken.tagName = "div";
    startTagToken.normalName = "div";

    assertEquals("div", startTagToken.name());
  }

  @Test
  public void testStartTagNormalName() {
    startTagToken.normalName = "div";

    assertEquals("div", startTagToken.normalName());
  }

  @Test
  public void testStartTagIsSelfClosing() {
    startTagToken.selfClosing = true;

    assertEquals(true, startTagToken.isSelfClosing());
  }

  @Test
  public void testStartTagGetAttributes() {
    startTagToken.attributes = new Attributes();

    assertEquals(true, startTagToken.getAttributes() instanceof Attributes);
  }

  // Test cases for EndTag token
  @Test
  public void testEndTagReset() {
    endTagToken.tagName = "div";
    endTagToken.normalName = "div";

    endTagToken.reset();

    assertEquals(null, endTagToken.tagName);
    assertEquals(null, endTagToken.normalName);
  }

  // Test cases for Comment token
  @Test
  public void testCommentReset() {
    commentToken.data.append("This is a comment");
    commentToken.bogus = true;

    commentToken.reset();

    assertEquals("", commentToken.data.toString());
    assertEquals(false, commentToken.bogus);
  }

  @Test
  public void testCommentGetData() {
    commentToken.data.append("This is a comment");

    assertEquals("This is a comment", commentToken.getData());
  }

  // Test cases for Character token
  @Test
  public void testCharacterReset() {
    characterToken.data = "This is a character";

    characterToken.reset();

    assertEquals(null, characterToken.data);
  }

  @Test
  public void testCharacterGetData() {
    characterToken.data = "This is a character";

    assertEquals("This is a character", characterToken.getData());
  }

  // Test cases for CData token
  @Test
  public void testCDataToString() {
    cdataToken.data("This is a CDATA");

    assertEquals("<![CDATA[This is a CDATA]]>", cdataToken.toString());
  }

  // Test cases for EOF token
  @Test
  public void testEOFReset() {
    assertEquals(eofToken, eofToken.reset());
  }

  // Token type checking methods test cases
  @Test
  public void testIsDoctype() {
    assertTrue(doctypeToken.isDoctype());
    assertFalse(startTagToken.isDoctype());
    assertFalse(endTagToken.isDoctype());
    assertFalse(commentToken.isDoctype());
    assertFalse(characterToken.isDoctype());
    assertFalse(cdataToken.isDoctype());
    assertFalse(eofToken.isDoctype());
  }

  @Test
  public void testIsStartTag() {
    assertFalse(doctypeToken.isStartTag());
    assertTrue(startTagToken.isStartTag());
    assertFalse(endTagToken.isStartTag());
    assertFalse(commentToken.isStartTag());
    assertFalse(characterToken.isStartTag());
    assertFalse(cdataToken.isStartTag());
    assertFalse(eofToken.isStartTag());
  }

  @Test
  public void testIsEndTag() {
    assertFalse(doctypeToken.isEndTag());
    assertFalse(startTagToken.isEndTag());
    assertTrue(endTagToken.isEndTag());
    assertFalse(commentToken.isEndTag());
    assertFalse(characterToken.isEndTag());
    assertFalse(cdataToken.isEndTag());
    assertFalse(eofToken.isEndTag());
  }

  @Test
  public void testIsComment() {
    assertFalse(doctypeToken.isComment());
    assertFalse(startTagToken.isComment());
    assertFalse(endTagToken.isComment());
    assertTrue(commentToken.isComment());
    assertFalse(characterToken.isComment());
    assertFalse(cdataToken.isComment());
    assertFalse(eofToken.isComment());
  }

  @Test
  public void testIsCharacter() {
    assertFalse(doctypeToken.isCharacter());
    assertFalse(startTagToken.isCharacter());
    assertFalse(endTagToken.isCharacter());
    assertFalse(commentToken.isCharacter());
    assertTrue(characterToken.isCharacter());
    assertFalse(cdataToken.isCharacter());
    assertFalse(eofToken.isCharacter());
  }

  @Test
  public void testIsCData() {
    assertFalse(doctypeToken.isCData());
    assertFalse(startTagToken.isCData());
    assertFalse(endTagToken.isCData());
    assertFalse(commentToken.isCData());
    assertFalse(characterToken.isCData());
    assertTrue(cdataToken.isCData());
    assertFalse(eofToken.isCData());
  }

  @Test
  public void testIsEOF() {
    assertFalse(doctypeToken.isEOF());
    assertFalse(startTagToken.isEOF());
    assertFalse(endTagToken.isEOF());
    assertFalse(commentToken.isEOF());
    assertFalse(characterToken.isEOF());
    assertFalse(cdataToken.isEOF());
    assertTrue(eofToken.isEOF());
  }

}