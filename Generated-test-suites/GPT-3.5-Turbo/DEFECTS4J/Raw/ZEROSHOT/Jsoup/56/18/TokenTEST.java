import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testTokenType_Doctype() {
        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    public void testTokenType_StartTag() {
        assertEquals("StartTag", startTag.tokenType());
    }

    @Test
    public void testTokenType_EndTag() {
        assertEquals("EndTag", endTag.tokenType());
    }

    @Test
    public void testTokenType_Comment() {
        assertEquals("Comment", comment.tokenType());
    }

    @Test
    public void testTokenType_Character() {
        assertEquals("Character", character.tokenType());
    }

    @Test
    public void testTokenType_EOF() {
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctype_reset() {
        doctype.setName("name");
        doctype.setPublicIdentifier("publicIdentifier");
        doctype.setSystemIdentifier("systemIdentifier");
        doctype.setForceQuirks(true);

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag_reset() {
        startTag.name("tagName");
        startTag.setSelfClosing(true);

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTag_reset() {
        endTag.name("tagName");

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.attributes);
    }

    @Test
    public void testComment_reset() {
        comment.data.append("data");
        comment.setBogus(true);

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacter_reset() {
        character.data("data");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOF_reset() {
        eof.reset();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testDoctype_getName() {
        doctype.name.append("name");

        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        doctype.publicIdentifier.append("publicIdentifier");

        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        doctype.systemIdentifier.append("systemIdentifier");

        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag_name() {
        String name = "tagName";

        StartTag result = startTag.name(name);

        assertEquals(name, result.tagName);
        assertEquals(name.toLowerCase(), result.normalName);
    }

    @Test
    public void testStartTag_isSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag_name() {
        String name = "tagName";

        EndTag result = endTag.name(name);

        assertEquals(name, result.tagName);
        assertEquals(name.toLowerCase(), result.normalName);
    }

    @Test
    public void testAppendTagName_appendString() {
        String append = "append";

        startTag.appendTagName(append);

        assertEquals(append, startTag.tagName);
        assertEquals(append.toLowerCase(), startTag.normalName);
    }

    @Test
    public void testAppendTagName_appendChar() {
        char append = 'c';

        startTag.appendTagName(append);

        assertEquals(String.valueOf(append), startTag.tagName);
        assertEquals(String.valueOf(append).toLowerCase(), startTag.normalName);
    }

    @Test
    public void testAppendAttributeName_appendString() {
        String append = "append";

        startTag.appendAttributeName(append);

        assertEquals(append, startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeName_appendChar() {
        char append = 'c';

        startTag.appendAttributeName(append);

        assertEquals(String.valueOf(append), startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue_appendString() {
        String append = "append";

        startTag.appendAttributeValue(append);

        assertEquals(append, startTag.pendingAttributeValue.toString());
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValue_appendChar() {
        char append = 'c';

        startTag.appendAttributeValue(append);

        assertEquals(String.valueOf(append), startTag.pendingAttributeValue.toString());
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValue_appendCharArray() {
        char[] append = {'a', 'p', 'p', 'e', 'n', 'd'};

        startTag.appendAttributeValue(append);

        assertEquals(String.valueOf(append), startTag.pendingAttributeValue.toString());
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValue_appendCodepoints() {
        int[] appendCodepoints = {97, 112, 112, 101, 110, 100};

        startTag.appendAttributeValue(appendCodepoints);

        assertEquals("append", startTag.pendingAttributeValue.toString());
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }
}