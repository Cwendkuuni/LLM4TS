import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
      Token.Doctype doctype = new Token.Doctype();
      assertEquals(Token.TokenType.Doctype, doctype.type);

      Token.StartTag startTag = new Token.StartTag();
      assertEquals(Token.TokenType.StartTag, startTag.type);

      Token.EndTag endTag = new Token.EndTag();
      assertEquals(Token.TokenType.EndTag, endTag.type);

      Token.Comment comment = new Token.Comment();
      assertEquals(Token.TokenType.Comment, comment.type);

      Token.Character character = new Token.Character();
      assertEquals(Token.TokenType.Character, character.type);

      Token.EOF eof = new Token.EOF();
      assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.name.append("html");
      doctype.reset();
      assertEquals("", doctype.getName());
      assertEquals(null, doctype.getPubSysKey());
      assertEquals("", doctype.getPublicIdentifier());
      assertEquals("", doctype.getSystemIdentifier());
      assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.name.append("html");
      assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.pubSysKey = "-//W3C//DTD HTML 4.0 Transitional//EN";
      assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
      assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
      assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
      Token.Doctype doctype = new Token.Doctype();
      doctype.forceQuirks = true;
      assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
      Token.StartTag startTag = new Token.StartTag();
      startTag.tagName = "div";
      startTag.normalName = "div";
      startTag.pendingAttributeName = "id";
      startTag.pendingAttributeValue.append("main");
      startTag.pendingAttributeValueS = null;
      startTag.hasEmptyAttributeValue = true;
      startTag.hasPendingAttributeValue = true;
      startTag.selfClosing = true;
      startTag.attributes = new Attributes();
      startTag.attributes.put(new Attribute("class", "container"));
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
    public void testEndTagReset() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.tagName = "div";
      endTag.normalName = "div";
      endTag.pendingAttributeName = "id";
      endTag.pendingAttributeValue.append("main");
      endTag.pendingAttributeValueS = null;
      endTag.hasEmptyAttributeValue = true;
      endTag.hasPendingAttributeValue = true;
      endTag.selfClosing = true;
      endTag.attributes = new Attributes();
      endTag.attributes.put(new Attribute("class", "container"));
      endTag.reset();
      assertNull(endTag.tagName);
      assertNull(endTag.normalName);
      assertNull(endTag.pendingAttributeName);
      assertEquals("", endTag.pendingAttributeValue.toString());
      assertNull(endTag.pendingAttributeValueS);
      assertFalse(endTag.hasEmptyAttributeValue);
      assertFalse(endTag.hasPendingAttributeValue);
      assertFalse(endTag.selfClosing);
      assertNull(endTag.attributes);
    }

    @Test
    public void testStartTagName() {
      Token.StartTag startTag = new Token.StartTag();
      startTag.tagName = "div";
      startTag.normalName = "div";
      assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
      Token.StartTag startTag = new Token.StartTag();
      startTag.normalName = "div";
      assertEquals("div", startTag.normalName());
    }

    @Test
    public void testEndTagName() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.tagName = "div";
      endTag.normalName = "div";
      assertEquals("div", endTag.name());
    }

    @Test
    public void testEndTagNormalName() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.normalName = "div";
      assertEquals("div", endTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
      Token.StartTag startTag = new Token.StartTag();
      startTag.selfClosing = true;
      assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagToString() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.tagName = "div";
      assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
      Token.Comment comment = new Token.Comment();
      comment.data.append("This is a comment");
      comment.bogus = true;
      comment.reset();
      assertEquals("", comment.getData());
      assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
      Token.Comment comment = new Token.Comment();
      comment.data.append("This is a comment");
      assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
      Token.Character character = new Token.Character();
      character.data = "This is a character";
      character.reset();
      assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
      Token.Character character = new Token.Character();
      character.data("This is a character");
      assertEquals("This is a character", character.getData());
    }

    @Test
    public void testEOFReset() {
      Token.EOF eof = new Token.EOF();
      assertSame(eof.reset(), eof);
    }

}