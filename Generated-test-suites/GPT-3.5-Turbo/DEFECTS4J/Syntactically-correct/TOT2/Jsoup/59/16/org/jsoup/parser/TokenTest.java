import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TestName");
        doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "TestTag";
        tag.pendingAttributeName = "TestAttribute";
        tag.appendAttributeValue("TestValue");
        tag.setEmptyAttributeValue();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "a";
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("href", "http://www.example.com"));
        assertEquals("<a href=\"http://www.example.com\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("Test Character");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        // No assertions needed as it is minimal functionality
    }

    @Test
    public void testIsMethods() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
        Token.Character character = new Token.Character();
        assertTrue(character.isCharacter());
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}