
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//PUBLIC");
        assertEquals("//PUBLIC", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("//SYSTEM");
        assertEquals("//SYSTEM", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue = new StringBuilder("container");
        startTag.newAttribute();
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        assertEquals("class", startTag.attributes.get("class").getKey());
        assertEquals("container", startTag.attributes.get("class").getValue());
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
    }

    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue = new StringBuilder("header");
        startTag.finaliseTag();
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        assertEquals("id", startTag.attributes.get("id").getKey());
        assertEquals("header", startTag.attributes.get("id").getValue());
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNameSet() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.tagName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.getAttributes());

        Attributes attributes = new Attributes();
        startTag.attributes = attributes;
        assertSame(attributes, startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagNameString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("p");
        assertEquals("p", startTag.tagName);
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName('@');
        assertEquals("@", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeNameString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName('@');
        assertEquals("@", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValueString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("container");
        assertEquals("container", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue('$');
        assertEquals("$", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testEndTagNameSet() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("div", endTag.tagName);
    }

    @Test
    public void testDoctypeIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testDoctypeAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testStartTagIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testStartTagAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testEndTagIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testEndTagAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}
