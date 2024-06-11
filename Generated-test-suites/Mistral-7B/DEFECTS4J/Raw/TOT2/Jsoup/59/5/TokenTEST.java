import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        Doctype expectedDoctype = new Doctype();
        expectedDoctype.name.append("html");

        Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
        Assert.assertEquals(expectedDoctype.getClass().getSimpleName(), doctype.tokenType());
        Assert.assertEquals(expectedDoctype.getName(), doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertNull(doctype.getPublicIdentifier());
        Assert.assertNull(doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        Assert.assertSame(doctype, resetDoctype);
    }

    @Test
    public void testTagStart() {
        Tag tagStart = new StartTag();
        tagStart.name("html");

        StartTag expectedTagStart = new StartTag();
        expectedTagStart.name("html");

        Assert.assertEquals(expectedTagStart.tokenType(), tagStart.tokenType());
        Assert.assertEquals(expectedTagStart.getClass().getSimpleName(), tagStart.tokenType());
        Assert.assertEquals(expectedTagStart.name(), tagStart.name());
        Assert.assertEquals(expectedTagStart.toString(), tagStart.toString());
        Assert.assertNull(tagStart.getAttributes());

        tagStart.newAttribute();
        tagStart.appendAttributeName("class").appendAttributeValue("test");
        Attributes attributes = tagStart.getAttributes();
        Assert.assertNotNull(attributes);
        Assert.assertEquals("class", attributes.keySet().iterator().next());
        Assert.assertEquals("test", attributes.get("class"));

        tagStart.finaliseTag();
        Assert.assertNull(tagStart.getAttributes());
    }

    @Test
    public void testTagEnd() {
        Tag tagEnd = new EndTag();
        tagEnd.name("html");

        EndTag expectedTagEnd = new EndTag();
        expectedTagEnd.name("html");

        Assert.assertEquals(expectedTagEnd.tokenType(), tagEnd.tokenType());
        Assert.assertEquals(expectedTagEnd.getClass().getSimpleName(), tagEnd.tokenType());
        Assert.assertEquals(expectedTagEnd.name(), tagEnd.name());
        Assert.assertEquals(expectedTagEnd.toString(), tagEnd.toString());
    }

    @Test
    public void testComment() {
        Token comment = new Comment();
        comment.data.append("This is a comment.");

        Comment expectedComment = new Comment();
        expectedComment.data.append("This is a comment.");

        Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
        Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
        Assert.assertEquals(expectedComment.getData(), comment.getData());
        Assert.assertEquals(expectedComment.toString(), comment.toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        character.data("A character.");

        Character expectedCharacter = new Character();
        expectedCharacter.data("A character.");

        Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
        Assert.assertEquals(expectedCharacter.getClass().getSimpleName(), character.tokenType());
        Assert.assertEquals(expectedCharacter.getData(), character.getData());
        Assert.assertEquals(expectedCharacter.toString(), character.toString());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();

        EOF expectedEOF = new EOF();

        Assert.assertEquals(expectedEOF.tokenType(), eof.tokenType());
        Assert.assertEquals(expectedEOF.getClass().getSimpleName(), eof.tokenType());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Object invalidToken = new Object();

        // This test will fail with an AssertionError as the given object is not a valid Token instance
        Token token = (Token) invalidToken;
    }
}