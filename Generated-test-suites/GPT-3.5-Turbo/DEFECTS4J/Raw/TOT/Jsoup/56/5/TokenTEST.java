
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void doctypeResetShouldResetAllFields() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void tagResetShouldResetAllFields() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "a";
        tag.normalName = "a-normal";
        tag.pendingAttributeName = "href";
        tag.pendingAttributeValue.append("http://example.com");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }

    @Test
    public void startTagToStringWithAttributesShouldReturnExpectedString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "a";
        startTag.attributes = new Attributes();
        startTag.attributes.put("href", "http://example.com");
        startTag.attributes.put("target", "_blank");

        String expected = "<a href=\"http://example.com\" target=\"_blank\">";
        assertEquals(expected, startTag.toString());
    }

    @Test
    public void startTagToStringWithoutAttributesShouldReturnExpectedString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";

        String expected = "<p>";
        assertEquals(expected, startTag.toString());
    }

    @Test
    public void endTagToStringShouldReturnExpectedString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "a";

        String expected = "</a>";
        assertEquals(expected, endTag.toString());
    }

    @Test
    public void commentResetShouldResetAllFields() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void characterResetShouldResetDataField() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void eofResetShouldNotModifyObject() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertSame(eof, eof.reset());
    }

    @Test
    public void isDoctypeShouldReturnTrueForDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void isStartTagShouldReturnTrueForStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void isEndTagShouldReturnTrueForEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void isCommentShouldReturnTrueForCommentToken() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void isCharacterShouldReturnTrueForCharacterToken() {
        Token.Character character = new Token.Character();

        assertTrue(character.isCharacter());
    }

    @Test
    public void isEOFShouldReturnTrueForEOFToken() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }
}

