import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        String name = "html";
        doctype.name(name);
        Assert.assertEquals(name, doctype.getName());
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.reset();
        Assert.assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        String publicIdentifier = "public";
        doctype.setPublicIdentifier(publicIdentifier);
        Assert.assertEquals(publicIdentifier, doctype.getPublicIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        doctype.setForceQuirks(false); // this should cause an error as forceQuirks is final
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        Assert.assertEquals(TokenType.StartTag, tag.type);
        tag.name("div");
        Assert.assertEquals("div", tag.name());
    }

    @Test
    public void testTagReset() {
        Tag tag = new Tag();
        tag.name("div");
        tag.reset();
        Assert.assertNull(tag.name());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new Tag();
        tag.newAttribute();
        Assert.assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new Tag();
        tag.newAttribute();
        tag.finaliseTag();
        Assert.assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new Tag();
        tag.isSelfClosing = true;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Tag tag = new Tag();
        tag.newAttribute();
        tag.newAttribute();
        Assert.assertNotNull(tag.getAttributes());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("class", new Attributes());
        Assert.assertEquals("class", startTag.tagName);
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("class", new Attributes());
        startTag.reset();
        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        Assert.assertEquals("div", endTag.name());
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        endTag.reset();
        Assert.assertNull(endTag.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        comment.reset();
        Assert.assertNull(comment.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        character.data("A");
        Assert.assertEquals("A", character.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");
        character.reset();
        Assert.assertNull(character.getData());
    }

    @Test
    public void testCommentBogus() {
        Comment comment = new Comment();
        comment.bogus = true;
        Assert.assertTrue(comment.bogus);
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        Assert.assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        Assert.assertEquals(TokenType.EOF, eof.type);
    }
}