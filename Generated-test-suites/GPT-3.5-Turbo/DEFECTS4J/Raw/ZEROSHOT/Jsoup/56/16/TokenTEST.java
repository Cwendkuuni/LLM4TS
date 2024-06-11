import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertNull(token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("PUBLIC");
        doctype.systemIdentifier.append("SYSTEM");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("test");
        tag.pendingAttributeValueS = "test";
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
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.attributes = new Attributes();

        startTag.toString();
        assertNotNull(startTag.name());
        assertNotNull(startTag.normalName());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.toString();
        assertNotNull(endTag.name());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("test");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;

        assertTrue(token.isDoctype());
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;

        assertTrue(token.isStartTag());
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;

        assertTrue(token.isEndTag());
        assertTrue(token instanceof Token.EndTag);
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;

        assertTrue(token.isComment());
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;

        assertTrue(token.isCharacter());
        assertTrue(token instanceof Token.Character);
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token token = eof;

        assertTrue(token.isEOF());
        assertTrue(token instanceof Token.EOF);
    }

}