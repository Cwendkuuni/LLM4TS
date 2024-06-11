import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctypeToken.reset();
        assertEquals("", doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertEquals("", doctypeToken.getPublicIdentifier());
        assertEquals("", doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTagToken.reset();
        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertEquals("", startTagToken.pendingAttributeValue.toString());
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTagToken.reset();
        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertNull(endTagToken.pendingAttributeName);
        assertEquals("", endTagToken.pendingAttributeValue.toString());
        assertNull(endTagToken.pendingAttributeValueS);
        assertFalse(endTagToken.hasEmptyAttributeValue);
        assertFalse(endTagToken.hasPendingAttributeValue);
        assertFalse(endTagToken.selfClosing);
        assertNull(endTagToken.attributes);
    }

    @Test
    public void testCommentReset() {
        commentToken.reset();
        assertEquals("", commentToken.getData());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testCharacterReset() {
        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFReset() {
        eofToken.reset();
        assertNotNull(eofToken);
    }

    @Test
    public void testTokenTypeIsDoctype() {
        assertTrue(doctypeToken.isDoctype());
        assertFalse(startTagToken.isDoctype());
        assertFalse(endTagToken.isDoctype());
        assertFalse(commentToken.isDoctype());
        assertFalse(characterToken.isDoctype());
        assertFalse(eofToken.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        assertEquals(doctypeToken, doctypeToken.asDoctype());
        assertNotEquals(doctypeToken, startTagToken.asDoctype());
        assertNotEquals(doctypeToken, endTagToken.asDoctype());
        assertNotEquals(doctypeToken, commentToken.asDoctype());
        assertNotEquals(doctypeToken, characterToken.asDoctype());
        assertNotEquals(doctypeToken, eofToken.asDoctype());
    }

    @Test
    public void testTokenTypeIsStartTag() {
        assertFalse(doctypeToken.isStartTag());
        assertTrue(startTagToken.isStartTag());
        assertFalse(endTagToken.isStartTag());
        assertFalse(commentToken.isStartTag());
        assertFalse(characterToken.isStartTag());
        assertFalse(eofToken.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertEquals(startTagToken, startTagToken.asStartTag());
        assertNotEquals(startTagToken, doctypeToken.asStartTag());
        assertNotEquals(startTagToken, endTagToken.asStartTag());
        assertNotEquals(startTagToken, commentToken.asStartTag());
        assertNotEquals(startTagToken, characterToken.asStartTag());
        assertNotEquals(startTagToken, eofToken.asStartTag());
    }

    @Test
    public void testTokenTypeIsEndTag() {
        assertFalse(doctypeToken.isEndTag());
        assertFalse(startTagToken.isEndTag());
        assertTrue(endTagToken.isEndTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(eofToken.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertEquals(endTagToken, endTagToken.asEndTag());
        assertNotEquals(endTagToken, doctypeToken.asEndTag());
        assertNotEquals(endTagToken, startTagToken.asEndTag());
        assertNotEquals(endTagToken, commentToken.asEndTag());
        assertNotEquals(endTagToken, characterToken.asEndTag());
        assertNotEquals(endTagToken, eofToken.asEndTag());
    }

    @Test
    public void testTokenTypeIsComment() {
        assertFalse(doctypeToken.isComment());
        assertFalse(startTagToken.isComment());
        assertFalse(endTagToken.isComment());
        assertTrue(commentToken.isComment());
        assertFalse(characterToken.isComment());
        assertFalse(eofToken.isComment());
    }

    @Test
    public void testAsComment() {
        assertEquals(commentToken, commentToken.asComment());
        assertNotEquals(commentToken, doctypeToken.asComment());
        assertNotEquals(commentToken, startTagToken.asComment());
        assertNotEquals(commentToken, endTagToken.asComment());
        assertNotEquals(commentToken, characterToken.asComment());
        assertNotEquals(commentToken, eofToken.asComment());
    }

    @Test
    public void testTokenTypeIsCharacter() {
        assertFalse(doctypeToken.isCharacter());
        assertFalse(startTagToken.isCharacter());
        assertFalse(endTagToken.isCharacter());
        assertFalse(commentToken.isCharacter());
        assertTrue(characterToken.isCharacter());
        assertFalse(eofToken.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        assertEquals(characterToken, characterToken.asCharacter());
        assertNotEquals(characterToken, doctypeToken.asCharacter());
        assertNotEquals(characterToken, startTagToken.asCharacter());
        assertNotEquals(characterToken, endTagToken.asCharacter());
        assertNotEquals(characterToken, commentToken.asCharacter());
        assertNotEquals(characterToken, eofToken.asCharacter());
    }

    @Test
    public void testTokenTypeIsEOF() {
        assertFalse(doctypeToken.isEOF());
        assertFalse(startTagToken.isEOF());
        assertFalse(endTagToken.isEOF());
        assertFalse(commentToken.isEOF());
        assertFalse(characterToken.isEOF());
        assertTrue(eofToken.isEOF());
    }

    @Test
    public void testEOFResetReturnThis() {
        assertSame(eofToken, eofToken.reset());
    }

}