import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);

        doctype.name("html");
        Assert.assertEquals("html", doctype.getName());

        doctype.publicIdentifier("_public");
        Assert.assertEquals("_public", doctype.getPublicIdentifier());

        doctype.systemIdentifier("system.html");
        Assert.assertEquals("system.html", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        Assert.assertSame(doctype, resetDoctype);
    }

    @Test
    public void testTag() {
        Tag tag = new StartTag();
        Assert.assertEquals(TokenType.StartTag, tag.type);

        tag.name("div");
        Assert.assertEquals("div", tag.name());

        tag.attributes(new Attributes().put("class", "test"));
        Assert.assertNotNull(tag.getAttributes());

        tag.selfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());

        tag.reset();
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        Assert.assertEquals(TokenType.Comment, comment.type);

        comment.data("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());

        Comment resetComment = comment.reset();
        Assert.assertSame(comment, resetComment);
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        Assert.assertEquals(TokenType.Character, character.type);

        character.data("A");
        Assert.assertEquals("A", character.getData());

        Character resetCharacter = character.reset();
        Assert.assertSame(character, resetCharacter);
    }

    @Test(expected = AssertionError.class)
    public void testEOF() {
        EOF eof = new EOF();
        Assert.assertEquals(TokenType.EOF, eof.type);

        eof.reset();
    }

    @Test
    public void testResetTokens() {
        Doctype doctype = new Doctype();
        Tag tag = new StartTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        doctype.name("html");
        tag.name("div");
        comment.data("This is a comment");
        character.data("A");

        Doctype resetDoctype = doctype.reset();
        Tag resetTag = tag.reset();
        Comment resetComment = comment.reset();
        Character resetCharacter = character.reset();
        EOF resetEof = eof.reset();

        Assert.assertSame(doctype, resetDoctype);
        Assert.assertSame(tag, resetTag);
        Assert.assertSame(comment, resetComment);
        Assert.assertSame(character, resetCharacter);
        Assert.assertSame(eof, resetEof);
    }

    @Test
    public void testDoctypeWithEmptyIdentifier() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.publicIdentifier("");
        doctype.systemIdentifier("system.html");

        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("system.html", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        Assert.assertSame(doctype, resetDoctype);
    }

    @Test
    public void testTagWithEmptyAttributeValue() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.attributes(new Attributes().put("class", ""));

        Assert.assertEquals("div", tag.name());
        Assert.assertNotNull(tag.getAttributes());
        Assert.assertEquals(0, tag.getAttributes().size());

        tag.reset();
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagWithEmptyAttributeName() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.appendAttributeName(" ");
        tag.appendAttributeValue("class");
        tag.appendAttributeValue("test");

        Assert.assertEquals("div", tag.name());
        Assert.assertNotNull(tag.getAttributes());
        Assert.assertEquals("class", tag.pendingAttributeName);
        Assert.assertEquals("test", tag.pendingAttributeValue);
        Assert.assertEquals(1, tag.getAttributes().size());
        Assert.assertEquals("class", tag.getAttributes().keySet().iterator().next());
        Assert.assertEquals("test", tag.getAttributes().getValue(tag.getAttributes().keySet().iterator().next()));

        tag.newAttribute();
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertEquals(1, tag.getAttributes().size());

        tag.reset();
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }
}