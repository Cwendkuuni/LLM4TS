
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
    
    // Test for Doctype.reset()
    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    // Test for StartTag.reset()
    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;

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
    
    // Test for EndTag.reset()
    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }
    
    // Test for Comment.reset()
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    // Test for Character.reset()
    @Test
    public void testCharacterReset() {
        character.data = "Sample Character";

        character.reset();

        assertNull(character.getData());
    }
    
    // Test for EOF.reset()
    @Test
    public void testEofReset() {
        assertEquals(eof, eof.reset());
    }

    // Test for Token.isDoctype()
    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    // Test for Token.asDoctype()
    @Test
    public void testAsDoctype() {
        assertEquals(doctype, doctype.asDoctype());
        assertNull(startTag.asDoctype());
        assertNull(endTag.asDoctype());
        assertNull(comment.asDoctype());
        assertNull(character.asDoctype());
        assertNull(eof.asDoctype());
    }

    // Test for Token.isStartTag()
    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    // Test for Token.asStartTag()
    @Test
    public void testAsStartTag() {
        assertNull(doctype.asStartTag());
        assertEquals(startTag, startTag.asStartTag());
        assertNull(endTag.asStartTag());
        assertNull(comment.asStartTag());
        assertNull(character.asStartTag());
        assertNull(eof.asStartTag());
    }

    // Test for Token.isEndTag()
    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    // Test for Token.asEndTag()
    @Test
    public void testAsEndTag() {
        assertNull(doctype.asEndTag());
        assertNull(startTag.asEndTag());
        assertEquals(endTag, endTag.asEndTag());
        assertNull(comment.asEndTag());
        assertNull(character.asEndTag());
        assertNull(eof.asEndTag());
    }

    // Test for Token.isComment()
    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    // Test for Token.asComment()
    @Test
    public void testAsComment() {
        assertNull(doctype.asComment());
        assertNull(startTag.asComment());
        assertNull(endTag.asComment());
        assertEquals(comment, comment.asComment());
        assertNull(character.asComment());
        assertNull(eof.asComment());
    }

    // Test for Token.isCharacter()
    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    // Test for Token.asCharacter()
    @Test
    public void testAsCharacter() {
        assertNull(doctype.asCharacter());
        assertNull(startTag.asCharacter());
        assertNull(endTag.asCharacter());
        assertNull(comment.asCharacter());
        assertEquals(character, character.asCharacter());
        assertNull(eof.asCharacter());
    }

    // Test for Token.isEOF()
    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }
    
    // Additional test cases can be added for comprehensive testing

}
