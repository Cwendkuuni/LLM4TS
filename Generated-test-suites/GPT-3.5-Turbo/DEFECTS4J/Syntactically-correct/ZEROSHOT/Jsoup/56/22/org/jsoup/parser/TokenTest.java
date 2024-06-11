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
        Token.Doctype resetDoctype = (Token.Doctype) doctype.reset();
        assertEquals("", resetDoctype.getName());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.name("testName")
                .attributes(new Attributes())
                .selfClosing(false);
        Token.StartTag resetStartTag = (Token.StartTag) startTag.reset();
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.name("testName");
        Token.EndTag resetEndTag = (Token.EndTag) endTag.reset();
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertEquals("", resetEndTag.pendingAttributeValue.toString());
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.data.append("testData");
        Token.Comment resetComment = (Token.Comment) comment.reset();
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.data("testData");
        Token.Character resetCharacter = (Token.Character) character.reset();
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF resetEOF = (Token.EOF) eof.reset();
        assertNotNull(resetEOF);
    }

    @Test
    public void testIsDoctype() {
        assertFalse(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        Token.Doctype asDoctype = token.asDoctype();
        assertNotNull(asDoctype);
    }

    @Test
    public void testIsStartTag() {
        assertFalse(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        Token.StartTag asStartTag = token.asStartTag();
        assertNotNull(asStartTag);
    }

    @Test
    public void testIsEndTag() {
        assertFalse(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        Token.EndTag asEndTag = token.asEndTag();
        assertNotNull(asEndTag);
    }

    @Test
    public void testIsComment() {
        assertFalse(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        Token.Comment asComment = token.asComment();
        assertNotNull(asComment);
    }

    @Test
    public void testIsCharacter() {
        assertFalse(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        Token.Character asCharacter = token.asCharacter();
        assertNotNull(asCharacter);
    }

    @Test
    public void testIsEOF() {
        assertTrue(eof.isEOF());
    }
}