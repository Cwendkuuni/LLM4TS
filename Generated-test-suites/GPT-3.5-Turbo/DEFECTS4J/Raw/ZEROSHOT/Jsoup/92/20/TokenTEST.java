import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token.Character();
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        assertNull(token.getData());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "testKey";
        doctype.publicIdentifier.append("testPublic");
        doctype.systemIdentifier.append("testSystem");
        doctype.forceQuirks = true;

        Token resetToken = doctype.reset();
        assertSame(doctype, resetToken);
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testTag";
        tag.normalName = "testTag";
        tag.pendingAttributeName = "testAttribute";
        tag.pendingAttributeValue.append("testValue");
        tag.pendingAttributeValueS = "testValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();

        Token resetToken = tag.reset();
        assertSame(tag, resetToken);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag tag = new Token.StartTag();
        tag.nameAttr("testName", new org.jsoup.nodes.Attributes());
        assertEquals("testName", tag.tagName);
        assertEquals("testname", tag.normalName);
        assertNotNull(tag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag tag = new Token.StartTag();
        String toString = tag.toString();
        assertEquals("<>", toString);

        tag.name = "testName";
        toString = tag.toString();
        assertEquals("<testName>", toString);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag tag = new Token.EndTag();
        tag.tagName = "testName";
        String toString = tag.toString();
        assertEquals("</testName>", toString);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        comment.bogus = true;

        Token resetToken = comment.reset();
        assertSame(comment, resetToken);
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("testData");

        Token resetToken = character.reset();
        assertSame(character, resetToken);
        assertNull(character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("testData");
        String toString = cdata.toString();
        assertEquals("<![CDATA[testData]]>", toString);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token resetToken = eof.reset();
        assertSame(eof, resetToken);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        Token nonDoctype = new Token.StartTag();
        assertFalse(nonDoctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        assertNotNull(doctype);

        Token nonDoctype = new Token.StartTag();
        assertNull(nonDoctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        Token nonStartTag = new Token.Doctype();
        assertFalse(nonStartTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        assertNotNull(startTag);

        Token nonStartTag = new Token.Doctype();
        assertNull(nonStartTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        Token nonEndTag = new Token.Doctype();
        assertFalse(nonEndTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        assertNotNull(endTag);

        Token nonEndTag = new Token.Doctype();
        assertNull(nonEndTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        Token nonComment = new Token.Doctype();
        assertFalse(nonComment.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        assertNotNull(comment);

        Token nonComment = new Token.Doctype();
        assertNull(nonComment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());

        Token nonCharacter = new Token.Doctype();
        assertFalse(nonCharacter.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token token = new Token.CData("testData");
        assertTrue(token.isCData());

        Token nonCData = new Token.Doctype();
        assertFalse(nonCData.isCData());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        Token.Character character = token.asCharacter();
        assertNotNull(character);

        Token nonCharacter = new Token.Doctype();
        assertNull(nonCharacter.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        Token nonEOF = new Token.Doctype();
        assertFalse(nonEOF.isEOF());
    }
}