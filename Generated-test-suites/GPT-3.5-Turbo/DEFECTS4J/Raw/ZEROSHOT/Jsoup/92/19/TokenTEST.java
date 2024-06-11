import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cdata;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.reset();
        assertEquals(null, doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals(null, doctype.getPublicIdentifier());
        assertEquals(null, doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.reset();
        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals(0, startTag.pendingAttributeValue.length());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertEquals(null, startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
        assertEquals(null, endTag.pendingAttributeName);
        assertEquals(0, endTag.pendingAttributeValue.length());
        assertEquals(null, endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertEquals(null, endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        assertEquals(0, comment.data.length());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        assertEquals(null, character.data);
    }

    @Test
    public void testCDataToString() {
        cdata.data("test");
        assertEquals("<![CDATA[test]]>", cdata.toString());
    }

    @Test
    public void testEOFReset() {
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(cdata.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        assertSame(doctype, doctype.asDoctype());
        assertNotSame(doctype, startTag.asDoctype());
        assertNotSame(doctype, endTag.asDoctype());
        assertNotSame(doctype, comment.asDoctype());
        assertNotSame(doctype, character.asDoctype());
        assertNotSame(doctype, cdata.asDoctype());
        assertNotSame(doctype, eof.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(cdata.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertNotSame(startTag, doctype.asStartTag());
        assertSame(startTag, startTag.asStartTag());
        assertNotSame(startTag, endTag.asStartTag());
        assertNotSame(startTag, comment.asStartTag());
        assertNotSame(startTag, character.asStartTag());
        assertNotSame(startTag, cdata.asStartTag());
        assertNotSame(startTag, eof.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(cdata.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertNotSame(endTag, doctype.asEndTag());
        assertNotSame(endTag, startTag.asEndTag());
        assertSame(endTag, endTag.asEndTag());
        assertNotSame(endTag, comment.asEndTag());
        assertNotSame(endTag, character.asEndTag());
        assertNotSame(endTag, cdata.asEndTag());
        assertNotSame(endTag, eof.asEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(cdata.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        assertNotSame(comment, doctype.asComment());
        assertNotSame(comment, startTag.asComment());
        assertNotSame(comment, endTag.asComment());
        assertSame(comment, comment.asComment());
        assertNotSame(comment, character.asComment());
        assertNotSame(comment, cdata.asComment());
        assertNotSame(comment, eof.asComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(cdata.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsCData() {
        assertFalse(doctype.isCData());
        assertFalse(startTag.isCData());
        assertFalse(endTag.isCData());
        assertFalse(comment.isCData());
        assertFalse(character.isCData());
        assertFalse(eof.isCData());
    }

    @Test
    public void testAsCharacter() {
        assertNotSame(character, doctype.asCharacter());
        assertNotSame(character, startTag.asCharacter());
        assertNotSame(character, endTag.asCharacter());
        assertNotSame(character, comment.asCharacter());
        assertSame(character, character.asCharacter());
        assertNotSame(character, cdata.asCharacter());
        assertNotSame(character, eof.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertFalse(cdata.isEOF());
        assertTrue(eof.isEOF());
    }
}