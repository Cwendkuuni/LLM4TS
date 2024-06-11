import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

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
        doctype.name.append("test");
        doctype.pubSysKey = "public";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();

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
        endTag.tagName = "test";
        endTag.normalName = "test";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = null;
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        Token.EndTag resetEndTag = endTag.reset();

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
        comment.data.append("test");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.data = "test";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF resetEOF = eof.reset();

        assertSame(eof, resetEOF);
    }

    // Additional test cases for edge cases and exceptions can be added here

}