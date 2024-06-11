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
    public void testDoctypeReset() {
        doctype.name.append("TEST");
        doctype.pubSysKey = "TEST";
        doctype.publicIdentifier.append("TEST");
        doctype.systemIdentifier.append("TEST");
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
        doctype.name.append("TEST");

        assertEquals("TEST", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        doctype.pubSysKey = "TEST";

        assertEquals("TEST", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("TEST");

        assertEquals("TEST", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("TEST");

        assertEquals("TEST", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        startTag.tagName = "TEST";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "TEST";
        startTag.pendingAttributeValue.append("TEST");
        startTag.pendingAttributeValueS = "TEST";
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
    public void testStartTagName() {
        startTag.tagName = "TEST";

        assertEquals("TEST", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "test";

        assertEquals("test", startTag.normalName());
    }

    @Test
    public void testStartTagSetName() {
        startTag.name("TEST");

        assertEquals("TEST", startTag.tagName);
        assertEquals("test", startTag.normalName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        startTag.attributes = new Attributes();

        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        startTag.tagName = "T";
        startTag.normalName = "t";

        startTag.appendTagName("EST");

        assertEquals("TEST", startTag.tagName);
        assertEquals("test", startTag.normalName);
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        startTag.tagName = "T";
        startTag.normalName = "t";

        startTag.appendTagName('E');

        assertEquals("TE", startTag.tagName);
        assertEquals("te", startTag.normalName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        startTag.pendingAttributeName = "T";

        startTag.appendAttributeName("EST");

        assertEquals("TEST", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        startTag.pendingAttributeName = "T";

        startTag.appendAttributeName('E');

        assertEquals("TE", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        startTag.pendingAttributeValue.append("TEST");

        startTag.appendAttributeValue("123");

        assertEquals("TEST123", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        startTag.pendingAttributeValue.append("TEST");

        startTag.appendAttributeValue('1');

        assertEquals("TEST1", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueCharArr() {
        startTag.pendingAttributeValue.append("TEST");

        startTag.appendAttributeValue(new char[]{'1', '2', '3'});

        assertEquals("TEST123", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueCodepoints() {
        startTag.pendingAttributeValue.append("TEST");

        startTag.appendAttributeValue(new int[]{49, 50, 51});

        assertEquals("TEST123", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagNewAttribute() {
        startTag.pendingAttributeName = "TEST";
        startTag.pendingAttributeValue.append("TEST");
        startTag.hasPendingAttributeValue = true;

        startTag.newAttribute();

        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        startTag.pendingAttributeName = "TEST";
        startTag.attributes = new Attributes();

        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testEndTagToString() {
        endTag.tagName = "TEST";

        assertEquals("</TEST>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        comment.data.append("TEST");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("TEST");

        assertEquals("TEST", comment.getData());
    }

    @Test
    public void testCommentToString() {
        comment.data.append("TEST");

        assertEquals("<!--TEST-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        character.data = "TEST";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterGetData() {
        character.data = "TEST";

        assertEquals("TEST", character.getData());
    }

    @Test
    public void testCharacterToString() {
        character.data = "TEST";

        assertEquals("TEST", character.toString());
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        assertFalse(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = this.doctype;

        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = this.startTag;

        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = this.endTag;

        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = this.comment;

        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = this.character;

        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(eof.isEOF());
    }
}