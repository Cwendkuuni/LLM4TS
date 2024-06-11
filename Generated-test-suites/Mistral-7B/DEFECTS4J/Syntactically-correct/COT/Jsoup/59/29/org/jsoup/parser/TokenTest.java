import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype"); // checks initialization of name

        doctype.reset();
        assertEquals(null, doctype.getName());
        assertTrue(doctype.isReset());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setPubSysKey("pubsyskey");
        doctype.setPublicIdentifier("publicid");
        doctype.setSystemIdentifier("systemid");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("pubsyskey", doctype.getPubSysKey());
        assertEquals("publicid", doctype.getPublicIdentifier());
        assertEquals("systemid", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.name("tag");

        startTag.reset();
        assertEquals(null, startTag.name());
        assertTrue(startTag.isReset());
    }

    @Test
    public void testStartTagGetters() {
        StartTag startTag = new StartTag();
        startTag.name("tag");

        startTag.setAttributes(new Attributes());
        startTag.setAttributes().put("attr1", "value1");
        startTag.setAttributes().put("attr2", "value2");

        assertEquals("tag", startTag.name());
        assertEquals(2, startTag.getAttributes().size());
        Assert.assertEquals("value1", startTag.getAttributes().get("attr1"));
        Assert.assertEquals("value2", startTag.getAttributes().get("attr2"));
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();

        endTag.reset();
        assertEquals(null, endTag.name());
        assertTrue(endTag.isReset());
    }

    @Test
    public void testEndTagGetter() {
        EndTag endTag = new EndTag();
        endTag.name("tag");

        assertEquals("tag", endTag.name());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("-- comment");

        comment.reset();
        assertEquals(null, comment.getData());
        assertTrue(comment.isReset());
    }

    @Test
    public void testCommentGetter() {
        Comment comment = new Comment();
        comment.data("-- comment");

        assertEquals("-- comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("char");

        character.reset();
        assertEquals(null, character.getData());
        assertTrue(character.isReset());
    }

    @Test
    public void testCharacterGetter() {
        Character character = new Character();
        character.data("char");

        assertEquals("char", character.getData());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        Token token = new Token();
        token.type = new TokenType("INVALID");
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Doctype();
        Token startTag = new StartTag();
        Token endTag = new EndTag();
        Token comment = new Comment();
        Token character = new Character();
        Token eof = new EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());
    }
}