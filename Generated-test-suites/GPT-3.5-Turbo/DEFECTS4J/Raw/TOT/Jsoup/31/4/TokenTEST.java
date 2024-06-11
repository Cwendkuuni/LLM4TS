import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "attrName";
        tag.pendingAttributeValue = new StringBuilder("attrValue");

        tag.newAttribute();

        assertEquals("attrName", tag.getAttributes().get("attrName"));
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tag";
        tag.pendingAttributeName = "attrName";

        tag.finaliseTag();

        assertTrue(tag.getAttributes().isEmpty());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tag");
        assertEquals("tag", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("tag");
        assertEquals("tag", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("attrName");
        assertEquals("attrName", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("attrValue");
        assertEquals("attrValue", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes.put("attrName", "attrValue");

        assertEquals("<tag attrName=\"attrValue\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";

        assertEquals("<tag>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}