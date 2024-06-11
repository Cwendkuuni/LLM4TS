import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype resetDoctype = doctype.reset();
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        Token.StartTag resetStartTag = startTag.reset();
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        Token.EndTag resetEndTag = endTag.reset();
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertEquals("", resetEndTag.pendingAttributeValue.toString());
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testResetComment() {
        Token.Comment resetComment = comment.reset();
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Token.Character resetCharacter = character.reset();
        assertNull(resetCharacter.data);
    }

    @Test
    public void testResetEOF() {
        Token.EOF resetEOF = eof.reset();
        assertNotNull(resetEOF);
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testGetName() {
        doctype.name.append("TEST");
        assertEquals("TEST", doctype.getName());
    }

    @Test
    public void testGetPubSysKey() {
        doctype.pubSysKey = "TEST";
        assertEquals("TEST", doctype.getPubSysKey());
    }

    @Test
    public void testGetPublicIdentifier() {
        doctype.publicIdentifier.append("TEST");
        assertEquals("TEST", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        doctype.systemIdentifier.append("TEST");
        assertEquals("TEST", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testNormalName() {
        startTag.tagName = "test";
        startTag.normalName = null;
        assertEquals("test", startTag.normalName());
    }

    @Test
    public void testName() {
        startTag.tagName = "test";
        startTag.normalName = null;
        assertEquals("test", startTag.name());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        assertNull(startTag.getAttributes());
        startTag.attributes = new org.jsoup.nodes.Attributes();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testAppendTagName() {
        startTag.tagName = null;
        startTag.normalName = null;
        startTag.appendTagName("test");
        assertEquals("test", startTag.tagName);
        assertEquals("test", startTag.normalName);
        startTag.appendTagName('s');
        assertEquals("tests", startTag.tagName);
        assertEquals("tests", startTag.normalName);
    }

    @Test
    public void testAppendAttributeName() {
        startTag.pendingAttributeName = null;
        startTag.appendAttributeName("test");
        assertEquals("test", startTag.pendingAttributeName);
        startTag.appendAttributeName('s');
        assertEquals("tests", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue() {
        startTag.pendingAttributeValue = new StringBuilder();
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = false;
        startTag.appendAttributeValue("test");
        assertEquals("test", startTag.pendingAttributeValue.toString());
        startTag.appendAttributeValue('s');
        assertEquals("tests", startTag.pendingAttributeValue.toString());
        startTag.appendAttributeValue(new char[] {'t', 'e', 's', 't'});
        assertEquals("teststest", startTag.pendingAttributeValue.toString());
        startTag.appendAttributeValue(new int[] {116, 101, 115, 116});
        assertEquals("teststesttest", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testSetEmptyAttributeValue() {
        assertFalse(startTag.hasEmptyAttributeValue);
        startTag.setEmptyAttributeValue();
        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testEnsureAttributeValue() {
        startTag.pendingAttributeValueS = "test";
        startTag.appendAttributeValue("s");
        assertEquals("tests", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void testToStringStartTag() {
        startTag.tagName = "test";
        assertNull(startTag.attributes);
        assertEquals("<test>", startTag.toString());
        startTag.attributes = new org.jsoup.nodes.Attributes().put("attr", "value");
        assertEquals("<test attr=\"value\">", startTag.toString());
    }

    @Test
    public void testToStringEndTag() {
        endTag.tagName = "test";
        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testToStringComment() {
        comment.data.append("test");
        assertFalse(comment.bogus);
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testToStringCharacter() {
        character.data = "test";
        assertEquals("test", character.toString());
    }

}