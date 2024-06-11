
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token.*;

import static org.junit.Assert.*;

public class TokenTest {

    private Doctype doctype;
    private StartTag startTag;
    private EndTag endTag;
    private Comment comment;
    private Character character;
    private EOF eof;

    @Before
    public void setup() {
        doctype = new Doctype();
        startTag = new StartTag();
        endTag = new EndTag();
        comment = new Comment();
        character = new Character();
        eof = new EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("name");

        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("public");

        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("system");

        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Attribute attribute = new Attribute("attr", "value");
        startTag.tagName = "tag";
        startTag.attributes = new Attributes(attribute);

        assertEquals("<tag attr=\"value\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        startTag.tagName = "tag";

        assertEquals("<tag>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        character.data("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        assertSame(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertSame(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertSame(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        assertSame(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        assertSame(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }
}

