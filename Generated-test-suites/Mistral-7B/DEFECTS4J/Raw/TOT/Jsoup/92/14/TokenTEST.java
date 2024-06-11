import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();

        doctype.getName = "html";
        doctype.pubSysKey = "<?xml";
        doctype.publicIdentifier = "public -//W3C//DTD XHTML 1.0 Strict//EN";
        doctype.systemIdentifier = "system 'xhtml1-strict.dtd'";
        doctype.forceQuirks = true;

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertFalse(doctype.isForceQuirks());
        assertTrue(doctype.isDoctype());

        Token resetDoctype = doctype.reset();
        assertEquals(doctype, resetDoctype);

        doctype.isForceQuirks = true;
        resetDoctype = doctype.reset();
        assertFalse(resetDoctype.isForceQuirks());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();

        startTag.name("html");
        startTag.normalName("html");
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();
        startTag.attributes.put("version", "5");

        assertEquals("StartTag", startTag.tokenType().name());
        assertEquals("html", startTag.name());
        assertEquals("html", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertTrue(startTag.isStartTag());
        assertEquals("html", startTag.toString());

        startTag.newAttribute();
        startTag.appendAttributeName("version");
        startTag.appendAttributeValue("5");
        startTag.appendAttributeValue(" ");
        startTag.newAttribute();
        startTag.appendAttributeName("lang");
        startTag.appendAttributeValue("en");

        assertEquals(2, startTag.attributes.size());
        assertEquals("5", startTag.attributes.getValue("version"));
        assertEquals("en", startTag.attributes.getValue("lang"));

        Token resetStartTag = startTag.reset();
        assertEquals(startTag, resetStartTag);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();

        endTag.name("html");
        endTag.normalName("html");

        assertEquals("EndTag", endTag.tokenType().name());
        assertEquals("html", endTag.name());
        assertEquals("html", endTag.normalName());
        assertFalse(endTag.isSelfClosing());
        assertTrue(endTag.isEndTag());
        assertEquals("</html>", endTag.toString());

        Token resetEndTag = endTag.reset();
        assertEquals(endTag, resetEndTag);
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();

        comment.data("-- This is a comment");

        assertEquals("Comment", comment.tokenType().name());
        assertEquals("-- This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());

        Token resetComment = comment.reset();
        assertEquals(comment, resetComment);
        assertNull(resetComment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();

        character.data("A");

        assertEquals("Character", character.tokenType().name());
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());

        Token resetCharacter = character.reset();
        assertEquals(character, resetCharacter);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCDataToken() {
        Token cData = new Token.CData("<![CDATA[This is CData]]>");

        assertEquals("CData", cData.tokenType().name());
        assertEquals("<![CDATA[This is CData]]>", cData.toString());

        Token resetCData = cData.reset();
        assertEquals(cData, resetCData);
        assertNull(resetCData.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEOFTokenInvalidReset() {
        Token eof = new Token.EOF();

        eof.reset();
    }
}