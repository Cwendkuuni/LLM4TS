import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cData;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cData = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        assertNotNull(doctype.reset());
    }

    @Test
    public void testDoctypeGetName() {
        assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        assertEquals("", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        assertNotNull(startTag.reset());
    }

    @Test
    public void testStartTagName() {
        assertNull(startTag.tagName);
    }

    @Test
    public void testStartTagNormalName() {
        assertNull(startTag.normalName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagReset() {
        assertNotNull(endTag.reset());
    }

    @Test
    public void testCommentReset() {
        assertNotNull(comment.reset());
    }

    @Test
    public void testCommentGetData() {
        assertEquals("", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        assertNotNull(character.reset());
    }

    @Test
    public void testCharacterData() {
        assertNull(character.getData());
    }

    @Test
    public void testCDataToString() {
        assertEquals("<![CDATA[]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        assertNotNull(eof.reset());
    }

    @Test
    public void testIsDoctype() {
        assertFalse(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        assertNull(doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertNull(startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertNull(endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(comment.isComment());
    }

    @Test
    public void testAsComment() {
        assertNull(comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(character.isCharacter());
    }

    @Test
    public void testIsCData() {
        assertFalse(cData.isCData());
    }

    @Test
    public void testAsCharacter() {
        assertNull(character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(eof.isEOF());
    }
}