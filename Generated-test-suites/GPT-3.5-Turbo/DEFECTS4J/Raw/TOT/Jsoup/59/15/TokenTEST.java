import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test name");
        doctype.pubSysKey = "Test pubSysKey";
        doctype.publicIdentifier.append("Test publicIdentifier");
        doctype.systemIdentifier.append("Test systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test name");
        doctype.pubSysKey = "Test pubSysKey";
        doctype.publicIdentifier.append("Test publicIdentifier");
        doctype.systemIdentifier.append("Test systemIdentifier");
        doctype.forceQuirks = true;

        assertEquals("Test name", doctype.getName());
        assertEquals("Test pubSysKey", doctype.getPubSysKey());
        assertEquals("Test publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("Test systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "Test tag";
        startTag.normalName = "test tag";
        startTag.pendingAttributeName = "test attribute";
        startTag.pendingAttributeValue.append("Test value");
        startTag.pendingAttributeValueS = "Test value S";
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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("Test name", new Attributes());

        assertEquals("Test name", startTag.tagName);
        assertEquals("test name", startTag.normalName);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "Test tag";
        endTag.normalName = "test tag";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Test data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Token token = doctype;

        assertSame(token, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Token token = startTag;

        assertSame(token, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Token token = endTag;

        assertSame(token, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        Token token = comment;

        assertSame(token, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();

        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        Token token = character;

        assertSame(token, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "Test tag";
        startTag.attributes = new Attributes();
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "Test tag";
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test data");
        Token.Character character = new Token.Character();
        character.data = "Test data";
        Token.EOF eof = new Token.EOF();

        assertEquals("<Test tag>", startTag.toString());
        assertEquals("</Test tag>", endTag.toString());
        assertEquals("<!--Test data-->", comment.toString());
        assertEquals("Test data", character.toString());
        assertNotNull(eof.toString());
    }
}