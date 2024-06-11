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
        character = new Token.Character("");
        eof = new Token.EOF();
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
    public void testDoctypeGetName() {
        doctype.name.append("doctypeName");
        assertEquals("doctypeName", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        startTag.tagName = "startTagName";
        assertEquals("startTagName", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        assertNull(startTag.getAttributes());
        startTag.attributes = new org.jsoup.nodes.Attributes();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        endTag.tagName = "endTagName";
        assertEquals("endTagName", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("commentData");
        assertEquals("commentData", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        character.data = "characterData";
        assertEquals("characterData", character.getData());
    }

    @Test
    public void testEOF() {
        assertTrue(eof.isEOF());
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
    public void testAsDoctype() {
        assertEquals(doctype, doctype.asDoctype());
        assertNull(startTag.asDoctype());
        assertNull(endTag.asDoctype());
        assertNull(comment.asDoctype());
        assertNull(character.asDoctype());
        assertNull(eof.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertTrue(startTag.isStartTag());
        assertFalse(doctype.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertEquals(startTag, startTag.asStartTag());
        assertNull(doctype.asStartTag());
        assertNull(endTag.asStartTag());
        assertNull(comment.asStartTag());
        assertNull(character.asStartTag());
        assertNull(eof.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertTrue(endTag.isEndTag());
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertEquals(endTag, endTag.asEndTag());
        assertNull(doctype.asEndTag());
        assertNull(startTag.asEndTag());
        assertNull(comment.asEndTag());
        assertNull(character.asEndTag());
        assertNull(eof.asEndTag());
    }

    @Test
    public void testIsComment() {
        assertTrue(comment.isComment());
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        assertEquals(comment, comment.asComment());
        assertNull(doctype.asComment());
        assertNull(startTag.asComment());
        assertNull(endTag.asComment());
        assertNull(character.asComment());
        assertNull(eof.asComment());
    }

    @Test
    public void testIsCharacter() {
        assertTrue(character.isCharacter());
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        assertEquals(character, character.asCharacter());
        assertNull(doctype.asCharacter());
        assertNull(startTag.asCharacter());
        assertNull(endTag.asCharacter());
        assertNull(comment.asCharacter());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertTrue(eof.isEOF());
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
    }

}