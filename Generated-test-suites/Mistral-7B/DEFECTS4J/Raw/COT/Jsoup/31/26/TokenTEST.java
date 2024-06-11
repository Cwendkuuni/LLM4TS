import org.junit.Test;
import org.junit.Assert.*;
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
    public void testDoctypeConstructor() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeSetters() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        assertEquals("html", doctype.getName());
        doctype.getPublicIdentifier().append("publicid");
        assertEquals("publicid", doctype.getPublicIdentifier());
        doctype.getSystemIdentifier().append("systemid");
        assertEquals("systemid", doctype.getSystemIdentifier());
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagConstructor() {
        Tag tag = new Tag();
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNameSetter() {
        Tag tag = new Tag();
        tag.name("html");
        assertEquals("html", tag.name());
    }

    @Test
    public void testTagSelfClosingSetter() {
        Tag tag = new Tag();
        tag.setSelfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testStartTagConstructor() {
        StartTag startTag = new StartTag();
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagNameSetter() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        assertEquals("html", startTag.name());
    }

    @Test
    public void testStartTagAttributesSetter() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test");
        Attributes attributes = startTag.getAttributes();
        Assert.assertEquals("class=test", attributes.toString());
    }

    @Test
    public void testEndTagConstructor() {
        EndTag endTag = new EndTag();
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());
    }

    @Test
    public void testEndTagNameSetter() {
        EndTag endTag = new EndTag();
        endTag.name("html");
        assertEquals("html", endTag.name());
    }

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testCommentDataSetter() {
        Comment comment = new Comment();
        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterConstructorNullData() {
        new Character(null);
    }

    @Test
    public void testCharacterConstructor() {
        Character character = new Character("A");
        assertTrue(character.isCharacter());
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOFConstructor() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
    }

}