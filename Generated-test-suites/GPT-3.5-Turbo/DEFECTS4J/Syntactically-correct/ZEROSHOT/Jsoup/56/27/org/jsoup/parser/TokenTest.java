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

    @Test
    public void testDoctypeReset() {
        doctype.name.append("test name");
        doctype.publicIdentifier.append("test public identifier");
        doctype.systemIdentifier.append("test system identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertTrue(doctype.name.toString().isEmpty());
        assertTrue(doctype.publicIdentifier.toString().isEmpty());
        assertTrue(doctype.systemIdentifier.toString().isEmpty());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "test tag";
        startTag.normalName = "test tag";
        startTag.pendingAttributeName = "test attribute";
        startTag.pendingAttributeValue.append("test value");
        startTag.pendingAttributeValueS = "test value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new org.jsoup.nodes.Attributes();

        startTag.reset();

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
        endTag.tagName = "test tag";
        endTag.normalName = "test tag";
        endTag.pendingAttributeName = "test attribute";
        endTag.pendingAttributeValue.append("test value");
        endTag.pendingAttributeValueS = "test value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new org.jsoup.nodes.Attributes();

        endTag.reset();

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
        comment.data.append("test data");
        comment.bogus = true;

        comment.reset();

        assertTrue(comment.data.toString().isEmpty());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.data = "test data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testTokenIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testTokenAsDoctype() {
        assertEquals(doctype, doctype.asDoctype());
        assertNull(startTag.asDoctype());
        assertNull(endTag.asDoctype());
        assertNull(comment.asDoctype());
        assertNull(character.asDoctype());
        assertNull(eof.asDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testTokenAsStartTag() {
        assertNull(doctype.asStartTag());
        assertEquals(startTag, startTag.asStartTag());
        assertNull(endTag.asStartTag());
        assertNull(comment.asStartTag());
        assertNull(character.asStartTag());
        assertNull(eof.asStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testTokenAsEndTag() {
        assertNull(doctype.asEndTag());
        assertNull(startTag.asEndTag());
        assertEquals(endTag, endTag.asEndTag());
        assertNull(comment.asEndTag());
        assertNull(character.asEndTag());
        assertNull(eof.asEndTag());
    }

    @Test
    public void testTokenIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testTokenAsComment() {
        assertNull(doctype.asComment());
        assertNull(startTag.asComment());
        assertNull(endTag.asComment());
        assertEquals(comment, comment.asComment());
        assertNull(character.asComment());
        assertNull(eof.asComment());
    }

    @Test
    public void testTokenIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testTokenAsCharacter() {
        assertNull(doctype.asCharacter());
        assertNull(startTag.asCharacter());
        assertNull(endTag.asCharacter());
        assertNull(comment.asCharacter());
        assertEquals(character, character.asCharacter());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenReset() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.reset());
    }
}