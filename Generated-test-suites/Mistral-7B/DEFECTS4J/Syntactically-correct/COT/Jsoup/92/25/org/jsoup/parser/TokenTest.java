import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctype_basic() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        doctype.reset();

        Token resetDoctype = doctype;
        assertTrue(resetDoctype == doctype);
    }

    @org.junit.Test
    public void testDoctype_getName_basic() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
    }

    @org.junit.Test
    public void testDoctype_getPubSysKey_basic() {
        Token doctype = new Token.Doctype();
        doctype.pubSysKey = "DTD HTML5";
        assertEquals("DTD HTML5", doctype.getPubSysKey());

        doctype.reset();

        assertEquals(null, doctype.getPubSysKey());
    }

    @org.junit.Test
    public void testDoctype_getPublicIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.publicIdentifier.append("DTD HTML5");
        assertEquals("DTD HTML5", doctype.getPublicIdentifier());

        doctype.reset();

        assertEquals(null, doctype.getPublicIdentifier());
    }

    @org.junit.Test
    public void testDoctype_getSystemIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.systemIdentifier.append("/path/to/dtd/html5.dtd");
        assertEquals("/path/to/dtd/html5.dtd", doctype.getSystemIdentifier());

        doctype.reset();

        assertEquals(null, doctype.getSystemIdentifier());
    }

    @org.junit.Test
    public void testDoctype_isForceQuirks_basic() {
        Token doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        doctype.reset();

        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTag_basic() {
        Token tag = new Token.Tag();
        assertEquals("Tag", tag.tokenType());

        tag.reset();

        Token resetTag = tag;
        assertTrue(resetTag == tag);
    }

    @org.junit.Test
    public void testTag_name_basic() {
        Token tag = new Token.Tag();
        tag.name("div");
        assertEquals("div", tag.name());

        tag.reset();

        assertNull(tag.name());
    }

    @org.junit.Test
    public void testTag_newAttribute_basic() {
        Token tag = new Token.Tag();
        tag.newAttribute();

        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        assertEquals(1, tag.getAttributes().size());
        assertEquals("test", tag.getAttributes().get("class"));

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("testId");

        assertEquals(2, tag.getAttributes().size());
    }

    @org.junit.Test
    public void testTag_finaliseTag_basic() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        tag.finaliseTag();

        assertEquals(1, tag.getAttributes().size());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTag_newAttribute_duplicate_throws_AssertionError() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test2");
    }

    @org.junit.Test
    public void testTag_isSelfClosing_basic() {
        Token tag = new Token.Tag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.reset();

        assertFalse(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testStartTag_basic() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        startTag.reset();

        Token resetStartTag = startTag;
        assertTrue(resetStartTag == startTag);
    }

    @org.junit.Test
    public void testStartTag_toString_basic() {
        Token startTag = new Token.StartTag();
        startTag.name("div");

        assertEquals("<div>", startTag.toString());
    }

    @org.junit.Test
    public void testStartTag_nameAttr_basic() {
        Token startTag = new Token.StartTag();
        startTag.nameAttr("div", new Token.Attributes());

        assertEquals("<div>", startTag.toString());
    }

    @org.junit.Test
    public void testEndTag_basic() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        endTag.reset();

        Token resetEndTag = endTag;
        assertTrue(resetEndTag == endTag);
    }

    @org.junit.Test
    public void testEndTag_toString_basic() {
        Token endTag = new Token.EndTag();
        endTag.name("div");

        assertEquals("</div>", endTag.toString());
    }

    @org.junit.Test
    public void testComment_basic() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        comment.reset();

        Token resetComment = comment;
        assertTrue(resetComment == comment);
    }

    @org.junit.Test
    public void testComment_getData_basic() {
        Token comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());

        comment.reset();

        assertEquals(null, comment.getData());
    }

    @org.junit.Test
    public void testCharacter_basic() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType());

        character.reset();

        Token resetCharacter = character;
        assertTrue(resetCharacter == character);
    }

    @org.junit.Test
    public void testCharacter_data_basic() {
        Token character = new Token.Character();
        character.data = "!";

        assertEquals("!", character.getData());

        character.reset();

        assertNull(character.getData());
    }

    @org.junit.Test
    public void testCData_basic() {
        Token cdata = new Token.CData("<![CDATA[This is some CDATA]]>");
        assertEquals("CData", cdata.tokenType());

        cdata.reset();

        Token resetCData = cdata;
        assertTrue(resetCData == cdata);
    }

    @org.junit.Test
    public void testEOF_basic() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());

        eof.reset();

        Token resetEof = eof;
        assertTrue(resetEof == eof);
    }
}