import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
        characterToken = new Token.Character("");
        eofToken = new Token.EOF();
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("Character", characterToken.tokenType());
        assertEquals("EOF", eofToken.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        doctypeToken.name.append("test");
        assertEquals("test", doctypeToken.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctypeToken.publicIdentifier.append("public");
        assertEquals("public", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctypeToken.systemIdentifier.append("system");
        assertEquals("system", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctypeToken.isForceQuirks());
        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagNewAttribute() {
        assertNull(startTagToken.attributes);
        startTagToken.pendingAttributeName = "attr";
        startTagToken.pendingAttributeValue = new StringBuilder("value");
        startTagToken.newAttribute();
        assertNotNull(startTagToken.attributes);
        assertEquals("attr", startTagToken.attributes.get("attr").getKey());
        assertEquals("value", startTagToken.attributes.get("attr").getValue());
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
    }

    @Test
    public void testStartTagFinaliseTag() {
        assertNull(startTagToken.attributes);
        startTagToken.pendingAttributeName = "attr";
        startTagToken.pendingAttributeValue = new StringBuilder("value");
        startTagToken.finaliseTag();
        assertNotNull(startTagToken.attributes);
        assertEquals("attr", startTagToken.attributes.get("attr").getKey());
        assertEquals("value", startTagToken.attributes.get("attr").getValue());
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
    }

    @Test
    public void testStartTagName() {
        startTagToken.tagName = "tag";
        assertEquals("tag", startTagToken.name());
    }

    @Test
    public void testStartTagNameSet() {
        startTagToken.name("tag");
        assertEquals("tag", startTagToken.tagName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTagToken.isSelfClosing());
        startTagToken.selfClosing = true;
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        startTagToken.attributes = attributes;
        assertSame(attributes, startTagToken.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        startTagToken.tagName = null;
        startTagToken.appendTagName("tag");
        assertEquals("tag", startTagToken.tagName);
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        startTagToken.tagName = null;
        startTagToken.appendTagName('t');
        assertEquals("t", startTagToken.tagName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        startTagToken.pendingAttributeName = null;
        startTagToken.appendAttributeName("attr");
        assertEquals("attr", startTagToken.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        startTagToken.pendingAttributeName = null;
        startTagToken.appendAttributeName('a');
        assertEquals("a", startTagToken.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        startTagToken.pendingAttributeValue = null;
        startTagToken.appendAttributeValue("value");
        assertEquals("value", startTagToken.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        startTagToken.pendingAttributeValue = null;
        startTagToken.appendAttributeValue('v');
        assertEquals("v", startTagToken.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagToString() {
        endTagToken.tagName = "tag";
        assertEquals("</tag>", endTagToken.toString());
    }

    @Test
    public void testCommentGetData() {
        commentToken.data.append("data");
        assertEquals("data", commentToken.getData());
    }

    @Test
    public void testCommentToString() {
        commentToken.data.append("data");
        assertEquals("<!--data-->", commentToken.toString());
    }

    @Test
    public void testCharacterGetData() {
        characterToken.getData();
        assertEquals("", characterToken.getData());
    }

    @Test
    public void testCharacterToString() {
        characterToken = new Token.Character("data");
        assertEquals("data", characterToken.toString());
    }

    @Test
    public void testIsDoctype() {
        assertFalse(doctypeToken.isDoctype());
        assertTrue(doctypeToken.asDoctype() instanceof Token.Doctype);
    }

    @Test
    public void testIsStartTag() {
        assertFalse(startTagToken.isStartTag());
        assertTrue(startTagToken.asStartTag() instanceof Token.StartTag);
    }

    @Test
    public void testIsEndTag() {
        assertFalse(endTagToken.isEndTag());
        assertTrue(endTagToken.asEndTag() instanceof Token.EndTag);
    }

    @Test
    public void testIsComment() {
        assertFalse(commentToken.isComment());
        assertTrue(commentToken.asComment() instanceof Token.Comment);
    }

    @Test
    public void testIsCharacter() {
        assertFalse(characterToken.isCharacter());
        assertTrue(characterToken.asCharacter() instanceof Token.Character);
    }

    @Test
    public void testIsEOF() {
        assertFalse(eofToken.isEOF());
    }

}