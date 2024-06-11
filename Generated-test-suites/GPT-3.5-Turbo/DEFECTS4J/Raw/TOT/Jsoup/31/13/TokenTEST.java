import org.junit.After;
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
    
    @After
    public void tearDown() {
        doctype = null;
        startTag = null;
        endTag = null;
        comment = null;
        character = null;
        eof = null;
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("publicId");
        assertEquals("publicId", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("systemId");
        assertEquals("systemId", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStartTagSetNameToEmptyString() {
        startTag.name("");
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        assertNull(startTag.getAttributes());
        startTag.attributes = new Attributes();
        assertNotNull(startTag.getAttributes());
    }
    
    @Test
    public void testEndTagName() {
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }
    
    @Test
    public void testCommentGetData() {
        comment.data.append("test comment");
        assertEquals("test comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        character = new Token.Character("test character");
        assertEquals("test character", character.getData());
    }
    
    @Test
    public void testEOF() {
        assertNotNull(eof);
        assertEquals(Token.TokenType.EOF, eof.type);
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
    public void testAsStartTag() {
        assertEquals(startTag, startTag.asStartTag());
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
    public void testAsEndTag() {
        assertEquals(endTag, endTag.asEndTag());
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
    public void testAsComment() {
        assertEquals(comment, comment.asComment());
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
    public void testAsCharacter() {
        assertEquals(character, character.asCharacter());
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