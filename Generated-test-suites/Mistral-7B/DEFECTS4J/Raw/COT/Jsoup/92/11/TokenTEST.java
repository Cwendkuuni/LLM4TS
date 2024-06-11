import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
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

        Doctype doctypeWithName = new Doctype();
        doctypeWithName.getName("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());

        Doctype resetDoctypeWithName = doctypeWithName.reset();
        assertEquals("Doctype", resetDoctypeWithName.tokenType());
        assertEquals(null, resetDoctypeWithName.getName());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals("StartTag", startTag.tokenType().name());

        StartTag resetStartTag = startTag.reset();
        assertEquals("StartTag", resetStartTag.tokenType().name());

        StartTag startTagWithName = new StartTag();
        startTagWithName.name("div");
        assertEquals("StartTag", startTagWithName.tokenType().name());
        assertEquals("div", startTagWithName.name());

        StartTag resetStartTagWithName = startTagWithName.reset();
        assertEquals("StartTag", resetStartTagWithName.tokenType().name());
        assertEquals(null, resetStartTagWithName.name());

        StartTag startTagWithAttributes = new StartTag();
        startTagWithAttributes.name("input").attributes().put("type", "text").put("name", "username");
        assertEquals("StartTag", startTagWithAttributes.tokenType().name());
        assertEquals("input", startTagWithAttributes.name());
        assertEquals("type", startTagWithAttributes.getAttributes().getClass().getSimpleName());
        assertEquals("text", startTagWithAttributes.getAttributes().get("type"));
        assertEquals("name", startTagWithAttributes.getAttributes().get("name"));
        assertEquals("username", startTagWithAttributes.getAttributes().get("name"));

        StartTag resetStartTagWithAttributes = startTagWithAttributes.reset();
        assertEquals("StartTag", resetStartTagWithAttributes.tokenType().name());
        assertEquals(null, resetStartTagWithAttributes.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        assertEquals("EndTag", endTag.tokenType().name());

        EndTag resetEndTag = endTag.reset();
        assertEquals("EndTag", resetEndTag.tokenType().name());

        EndTag endTagWithName = new EndTag();
        endTagWithName.name("div");
        assertEquals("EndTag", endTagWithName.tokenType().name());
        assertEquals("div", endTagWithName.name());

        EndTag resetEndTagWithName = endTagWithName.reset();
        assertEquals("EndTag", resetEndTagWithName.tokenType().name());
        assertEquals(null, resetEndTagWithName.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType().name());

        Comment resetComment = comment.reset();
        assertEquals("Comment", resetComment.tokenType().name());

        Comment commentWithData = new Comment();
        commentWithData.getData("This is a comment");
        assertEquals("Comment", commentWithData.tokenType().name());
        assertEquals("This is a comment", commentWithData.getData());

        Comment resetCommentWithData = commentWithData.reset();
        assertEquals("Comment", resetCommentWithData.tokenType().name());
        assertEquals(null, resetCommentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        character.data('A');
        assertEquals("Character", character.tokenType().name());
        assertEquals("A", character.getData());

        Character resetCharacter = character.reset();
        assertEquals("Character", resetCharacter.tokenType().name());
        assertEquals(null, resetCharacter.getData());
    }

    @Test
    public void testCData() {
        CData cData = new CData("This is CData");
        assertEquals("Character", cData.tokenType().name());
        assertEquals("<![CDATA[This is CData]]>", cData.toString());

        CData resetCData = cData.reset();
        assertEquals("Character", resetCData.tokenType().name());
        assertEquals(null, resetCData.getData());
    }

    @Test(expected = AssertionError.class)
    public void testEOF() {
        EOF eof = new EOF();
        Assert.assertFalse(eof.isDoctype());
        Assert.assertFalse(eof.isStartTag());
        Assert.assertFalse(eof.isEndTag());
        Assert.assertFalse(eof.isComment());
        Assert.assertFalse(eof.isCharacter());
        Assert.assertTrue(eof.isEOF());

        EOF resetEOF = eof.reset();
        Assert.assertTrue(resetEOF.isEOF());
    }
}