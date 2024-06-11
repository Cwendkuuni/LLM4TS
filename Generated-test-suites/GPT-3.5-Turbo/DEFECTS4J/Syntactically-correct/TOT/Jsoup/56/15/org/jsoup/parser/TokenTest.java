import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        doctype.name.append("html");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertEquals(false, resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");

        String name = doctype.getName();

        assertEquals("html", name);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("publicId");

        String publicIdentifier = doctype.getPublicIdentifier();

        assertEquals("publicId", publicIdentifier);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("systemId");

        String systemIdentifier = doctype.getSystemIdentifier();

        assertEquals("systemId", systemIdentifier);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();

        assertEquals(null, resetStartTag.tagName);
        assertEquals(null, resetStartTag.normalName);
        assertEquals(null, resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertEquals(null, resetStartTag.pendingAttributeValueS);
        assertEquals(false, resetStartTag.hasEmptyAttributeValue);
        assertEquals(false, resetStartTag.hasPendingAttributeValue);
        assertEquals(false, resetStartTag.selfClosing);
        assertEquals(null, resetStartTag.attributes);
    }

    @Test
    public void testStartTagName() {
        startTag.tagName = "div";

        String tagName = startTag.name();

        assertEquals("div", tagName);
    }

    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "div";

        String normalName = startTag.normalName();

        assertEquals("div", normalName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "id";
        endTag.pendingAttributeValue.append("test");
        endTag.pendingAttributeValueS = null;
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        Token.EndTag resetEndTag = endTag.reset();

        assertEquals(null, resetEndTag.tagName);
        assertEquals(null, resetEndTag.normalName);
        assertEquals(null, resetEndTag.pendingAttributeName);
        assertEquals("", resetEndTag.pendingAttributeValue.toString());
        assertEquals(null, resetEndTag.pendingAttributeValueS);
        assertEquals(false, resetEndTag.hasEmptyAttributeValue);
        assertEquals(false, resetEndTag.hasPendingAttributeValue);
        assertEquals(false, resetEndTag.selfClosing);
        assertEquals(null, resetEndTag.attributes);
    }

    @Test
    public void testEndTagName() {
        endTag.tagName = "div";

        String tagName = endTag.name();

        assertEquals("div", tagName);
    }

    @Test
    public void testCommentReset() {
        comment.data.append("Test Comment");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertEquals(false, resetComment.bogus);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("Test Comment");

        String data = comment.getData();

        assertEquals("Test Comment", data);
    }

    @Test
    public void testCharacterReset() {
        character.data = "Test Character";

        Token.Character resetCharacter = character.reset();

        assertEquals(null, resetCharacter.data);
    }

    @Test
    public void testCharacterGetData() {
        character.data = "Test Character";

        String data = character.getData();

        assertEquals("Test Character", data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF resetEOF = eof.reset();

        assertNotNull(resetEOF);
    }
}