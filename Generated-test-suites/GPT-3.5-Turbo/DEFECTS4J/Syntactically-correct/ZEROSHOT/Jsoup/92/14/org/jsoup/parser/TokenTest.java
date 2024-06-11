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
    public void setUp(){
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cData = new Token.CData("testCDATA");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.reset();
        
        // Verify reset values
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.reset();
        
        // Verify reset values
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertTrue(startTag.pendingAttributeValue.toString().isEmpty());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        
        // Verify reset values
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertTrue(endTag.pendingAttributeValue.toString().isEmpty());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        
        // Verify reset values
        assertTrue(comment.data.toString().isEmpty());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        
        // Verify reset value
        assertNull(character.getData());
    }

    @Test
    public void testCDataReset() {
        cData.reset();

        // Verify reset value
        assertEquals("", cData.getData());
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        // Verify reset value
        assertEquals(Token.TokenType.EOF, eof.type);
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
        assertFalse(character.isCData());
    }

    @Test
    public void testAsCharacter() {
        assertNull(character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertTrue(eof.isEOF());
    }
}