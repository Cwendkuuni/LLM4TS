import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Doctype resetDoctype = doctype.reset();
        assertEquals("Doctype", resetDoctype.tokenType());

        Doctype doctypeWithName = new Doctype().name("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());

        Doctype resetDoctypeWithName = doctypeWithName.reset();
        assertEquals("Doctype", resetDoctypeWithName.tokenType());
        assertNull(resetDoctypeWithName.getName());
    }

    @Test
    public void testReset() {
        Tag tag = new StartTag();
        tag.appendTagName("div");
        tag.newAttribute().name("class").value("myclass");
        tag.finaliseTag();

        Tag resetTag = tag.reset();
        assertEquals(Tag.class, resetTag.getClass());
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.getAttributes());
        assertFalse(resetTag.isSelfClosing());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag().name("div");
        Tag resetStartTag = startTag.reset();

        assertEquals("StartTag", startTag.tokenType().name());
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        startTag.newAttribute().name("class").value("myclass");
        startTag.finaliseTag();

        assertEquals("<div class=\"myclass\">", startTag.toString());

        Tag resetWithAttributeStartTag = startTag.reset();
        assertEquals("StartTag", resetWithAttributeStartTag.tokenType().name());
        assertNull(resetWithAttributeStartTag.tagName);
        assertNull(resetWithAttributeStartTag.normalName);
        assertNull(resetWithAttributeStartTag.getAttributes());
        assertFalse(resetWithAttributeStartTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag().name("div");
        assertEquals("EndTag", endTag.tokenType().name());
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment().data("This is a comment");
        assertEquals("Comment", comment.tokenType().name());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());

        Comment resetComment = comment.reset();
        assertEquals("Comment", resetComment.tokenType().name());
        assertNull(resetComment.getData());
    }

    @Test(expected = AssertionError.class)
    public void testNegativeCaseForCharacter() {
        Character character = new Character().data("-");
        assertEquals("Character", character.tokenType().name());
        assertEquals("-", character.getData());

        Character resetCharacter = character.reset();
        assertEquals("Character", resetCharacter.tokenType().name());
        assertNull(resetCharacter.getData());

        // Negative test case for an invalid character.
        Character invalidCharacter = new Character().data("\u0000");
        assertEquals("Character", invalidCharacter.tokenType().name());
        assertEquals("\u0000", invalidCharacter.getData());
    }

    @Test
    public void testCData() {
        CData cData = new CData().data("This is a CData section");
        assertEquals("Character", cData.tokenType().name());
        assertEquals("<![CDATA[This is a CData section]]>", cData.toString());

        CData resetCData = cData.reset();
        assertEquals("Character", resetCData.tokenType().name());
        assertNull(resetCData.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType[] tokenTypes = TokenType.values();
        for (TokenType tokenType : tokenTypes) {
            assertEquals(tokenType.name(), tokenType.toString());
        }
    }
}