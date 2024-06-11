
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class TokenTest {

  @Test
  public void testTokenType() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };

    token.type = Token.TokenType.StartTag;
    assertEquals("StartTag", token.tokenType());

    token.type = Token.TokenType.EndTag;
    assertEquals("EndTag", token.tokenType());

    token.type = Token.TokenType.Doctype;
    assertEquals("Doctype", token.tokenType());

    token.type = Token.TokenType.Comment;
    assertEquals("Comment", token.tokenType());

    token.type = Token.TokenType.Character;
    assertEquals("Character", token.tokenType());

    token.type = Token.TokenType.EOF;
    assertEquals("EOF", token.tokenType());
  }

  @Test
  public void testResetDoctypeToken() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("HTML");
    doctype.pubSysKey = "pub";
    doctype.publicIdentifier.append("123");
    doctype.systemIdentifier.append("456");
    doctype.forceQuirks = true;

    Token.Doctype resetDoctype = (Token.Doctype) doctype.reset();

    assertEquals("", resetDoctype.getName());
    assertNull(resetDoctype.getPubSysKey());
    assertEquals("", resetDoctype.getPublicIdentifier());
    assertEquals("", resetDoctype.getSystemIdentifier());
    assertFalse(resetDoctype.isForceQuirks());
  }

  @Test
  public void testResetTagToken() {
    Token.Tag tag = new Token.StartTag();
    tag.tagName = "p";
    tag.normalName = "p";
    tag.appendAttributeName("id");
    tag.appendAttributeValue("123");
    tag.selfClosing = true;
    tag.newAttribute();

    Token.Tag resetTag = tag.reset();

    assertNull(resetTag.tagName);
    assertNull(resetTag.normalName);
    assertNull(resetTag.pendingAttributeName);
    assertEquals("", resetTag.pendingAttributeValue.toString());
    assertNull(resetTag.pendingAttributeValueS);
    assertFalse(resetTag.hasEmptyAttributeValue);
    assertFalse(resetTag.hasPendingAttributeValue);
    assertFalse(resetTag.selfClosing);
    assertNull(resetTag.attributes);
  }

  @Test
  public void testStartTagNameAttr() {
    String name = "startTag";
    Attributes attributes = new Attributes();
    attributes.put("id", "123");
    Token.StartTag tag = new Token.StartTag().nameAttr(name, attributes);

    assertEquals(name, tag.tagName);
    assertEquals(name.toLowerCase(), tag.normalName);
    assertNotNull(tag.attributes);
  }

  @Test
  public void testEndTagToString() {
    Token.EndTag tag = new Token.EndTag();
    tag.tagName = "endTag";

    assertEquals("</endTag>", tag.toString());
  }

  @Test
  public void testCommentToken() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("This is a comment");
    comment.bogus = true;

    Token.Comment resetComment = (Token.Comment) comment.reset();

    assertEquals("", resetComment.getData());
    assertFalse(resetComment.bogus);
  }

  @Test
  public void testCharacterData() {
    String data = "This is character data";
    Token.Character character = new Token.Character().data(data);

    assertEquals(data, character.getData());
    assertEquals(data, character.toString());
  }

  @Test
  public void testCDataToString() {
    String data = "This is CDATA";
    Token.CData cdata = new Token.CData(data);

    assertEquals("<![CDATA[" + data + "]]>", cdata.toString());
  }

  @Test
  public void testEOFReset() {
    Token.EOF eof = new Token.EOF();
    Token.EOF resetEOF = (Token.EOF) eof.reset();

    assertNotNull(resetEOF);
  }

  @Test
  public void testIsDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    Token token = doctype;

    assertTrue(token.isDoctype());
    assertFalse(token.isStartTag());
    assertFalse(token.isEndTag());
    assertFalse(token.isComment());
    assertFalse(token.isCharacter());
    assertFalse(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    Token token = startTag;

    assertFalse(token.isDoctype());
    assertTrue(token.isStartTag());
    assertFalse(token.isEndTag());
    assertFalse(token.isComment());
    assertFalse(token.isCharacter());
    assertFalse(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    Token token = endTag;

    assertFalse(token.isDoctype());
    assertFalse(token.isStartTag());
    assertTrue(token.isEndTag());
    assertFalse(token.isComment());
    assertFalse(token.isCharacter());
    assertFalse(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsComment() {
    Token.Comment comment = new Token.Comment();
    Token token = comment;

    assertFalse(token.isDoctype());
    assertFalse(token.isStartTag());
    assertFalse(token.isEndTag());
    assertTrue(token.isComment());
    assertFalse(token.isCharacter());
    assertFalse(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsCharacter() {
    Token.Character character = new Token.Character();
    Token token = character;

    assertFalse(token.isDoctype());
    assertFalse(token.isStartTag());
    assertFalse(token.isEndTag());
    assertFalse(token.isComment());
    assertTrue(token.isCharacter());
    assertFalse(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsCData() {
    Token.CData cdata = new Token.CData("");
    Token token = cdata;

    assertFalse(token.isDoctype());
    assertFalse(token.isStartTag());
    assertFalse(token.isEndTag());
    assertFalse(token.isComment());
    assertFalse(token.isCharacter());
    assertTrue(token.isCData());
    assertFalse(token.isEOF());
  }

  @Test
  public void testIsEOF() {
    Token.EOF eof = new Token.EOF();
    Token token = eof;

    assertFalse(token.isDoctype());
    assertFalse(token.isStartTag());
    assertFalse(token.isEndTag());
    assertFalse(token.isComment());
    assertFalse(token.isCharacter());
    assertFalse(token.isCData());
    assertTrue(token.isEOF());
  }

}
