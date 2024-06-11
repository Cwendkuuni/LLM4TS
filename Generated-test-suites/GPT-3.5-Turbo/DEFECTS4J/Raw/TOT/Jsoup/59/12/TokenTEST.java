import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setup() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testReset_Doctype() {
        doctypeToken.name.append("test name");
        doctypeToken.pubSysKey = "pubSysKey";
        doctypeToken.publicIdentifier.append("public identifier");
        doctypeToken.systemIdentifier.append("system identifier");
        doctypeToken.forceQuirks = true;
        
        doctypeToken.reset();
        
        assertEquals("", doctypeToken.name.toString());
        assertNull(doctypeToken.pubSysKey);
        assertEquals("", doctypeToken.publicIdentifier.toString());
        assertEquals("", doctypeToken.systemIdentifier.toString());
        assertFalse(doctypeToken.forceQuirks);
    }

    @Test
    public void testReset_StartTag() {
        startTagToken.tagName = "div";
        startTagToken.normalName = "div";
        startTagToken.pendingAttributeName = "class";
        startTagToken.pendingAttributeValue.append("test value");
        startTagToken.pendingAttributeValueS = null;
        startTagToken.hasEmptyAttributeValue = true;
        startTagToken.hasPendingAttributeValue = true;
        startTagToken.selfClosing = true;
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("class", "test value"));
        startTagToken.attributes = attributes;

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
    public void testReset_EndTag() {
        endTagToken.tagName = "div";
        endTagToken.normalName = "div";
        endTagToken.pendingAttributeName = "class";
        endTagToken.pendingAttributeValue.append("test value");
        endTagToken.pendingAttributeValueS = null;
        endTagToken.hasEmptyAttributeValue = true;
        endTagToken.hasPendingAttributeValue = true;
        endTagToken.selfClosing = true;
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("class", "test value"));
        endTagToken.attributes = attributes;

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
    public void testReset_Comment() {
        commentToken.data.append("test data");
        commentToken.bogus = true;

        commentToken.reset();
        
        assertEquals("", commentToken.data.toString());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testReset_Character() {
        characterToken.data = "test data";

        characterToken.reset();
        
        assertNull(characterToken.data);
    }

    @Test
    public void testReset_EOF() {
        Token.EOF eof = eofToken.reset();
        
        assertSame(eofToken, eof);
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctypeToken.isDoctype());
        assertFalse(startTagToken.isDoctype());
        assertFalse(endTagToken.isDoctype());
        assertFalse(commentToken.isDoctype());
        assertFalse(characterToken.isDoctype());
        assertFalse(eofToken.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctypeToken.isStartTag());
        assertTrue(startTagToken.isStartTag());
        assertFalse(endTagToken.isStartTag());
        assertFalse(commentToken.isStartTag());
        assertFalse(characterToken.isStartTag());
        assertFalse(eofToken.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctypeToken.isEndTag());
        assertFalse(startTagToken.isEndTag());
        assertTrue(endTagToken.isEndTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(eofToken.isEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctypeToken.isComment());
        assertFalse(startTagToken.isComment());
        assertFalse(endTagToken.isComment());
        assertTrue(commentToken.isComment());
        assertFalse(characterToken.isComment());
        assertFalse(eofToken.isComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctypeToken.isCharacter());
        assertFalse(startTagToken.isCharacter());
        assertFalse(endTagToken.isCharacter());
        assertFalse(commentToken.isCharacter());
        assertTrue(characterToken.isCharacter());
        assertFalse(eofToken.isCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctypeToken.isEOF());
        assertFalse(startTagToken.isEOF());
        assertFalse(endTagToken.isEOF());
        assertFalse(commentToken.isEOF());
        assertFalse(characterToken.isEOF());
        assertTrue(eofToken.isEOF());
    }
}