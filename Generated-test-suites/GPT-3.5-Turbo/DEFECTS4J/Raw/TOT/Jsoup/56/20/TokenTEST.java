import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);

        token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);

        token = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, token.type);

        token = new Token.Comment();
        assertEquals(Token.TokenType.Comment, token.type);

        token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);

        token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
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
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.name("tag");

        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new org.jsoup.nodes.Attributes();
        startTag.attributes.put("attr", "value");

        assertEquals("<tag attr=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.toString());
    }

    @Test
    public void testEOFRest() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertSame(eof, eof.reset());
    }
}