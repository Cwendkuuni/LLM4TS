
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("doctype");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("doctype");

        assertEquals("doctype", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        doctype.pubSysKey = "key";

        assertEquals("key", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("public");

        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("system");

        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "special";
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
    public void testStartTagNewAttribute() {
        startTag.newAttribute();

        assertNotNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testStartTagFinaliseTag() {
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");

        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);
        assertEquals(1, startTag.attributes.size());
        assertEquals("attribute", startTag.attributes.get(0).getKey());
        assertEquals("value", startTag.attributes.get(0).getValue());
    }

    @Test
    public void testStartTagName() {
        startTag.name("tag");

        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        startTag.attributes = new Attributes();

        assertEquals(startTag.attributes, startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        startTag.appendTagName("e");
        startTag.appendTagName("x");
        startTag.appendTagName("a");
        startTag.appendTagName("m");
        startTag.appendTagName("p");

        assertEquals("examp", startTag.tagName);
        assertEquals("examp", startTag.normalName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        startTag.appendAttributeName("attr");
        startTag.appendAttributeName("ibute");

        assertEquals("attribute", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        startTag.appendAttributeValue("value");
        startTag.appendAttributeValue("1");

        assertEquals("value1", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testEndTagToString() {
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        comment.data.append("comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("comment");

        assertEquals("comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        character.data("data");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        character.data("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        doctype.type = Token.TokenType.Doctype;

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertSame(doctype, doctype);
    }

    @Test
    public void testIsStartTag() {
        startTag.type = Token.TokenType.StartTag;

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertSame(startTag, startTag);
    }

    @Test
    public void testIsEndTag() {
        endTag.type = Token.TokenType.EndTag;

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertSame(endTag, endTag);
    }

    @Test
    public void testIsComment() {
        comment.type = Token.TokenType.Comment;

        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        assertSame(comment, comment);
    }

    @Test
    public void testIsCharacter() {
        character.type = Token.TokenType.Character;

        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        assertSame(character, character);
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }
}
