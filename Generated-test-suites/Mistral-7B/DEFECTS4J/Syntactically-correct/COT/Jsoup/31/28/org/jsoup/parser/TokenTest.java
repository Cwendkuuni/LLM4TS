import org.junit.Test;
import org.junit.Assert;
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
    public void testDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctype_getName() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("publicid");
        Assert.assertEquals("publicid", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("systemid");
        Assert.assertEquals("systemid", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Doctype doctype = new Doctype();
        Assert.assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertFalse(doctype.isStartTag());
        doctype.name.append("div"); // This should throw an AssertionError
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.type);
    }

    @Test
    public void testStartTag_name() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        Assert.assertEquals("p", startTag.name());
    }

    @Test
    public void testStartTag_isSelfClosing() {
        StartTag startTag = new StartTag();
        Assert.assertFalse(startTag.isSelfClosing());

        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTag_getAttributes() {
        StartTag startTag = new StartTag();
        Attribute attribute = new Attribute("class", "test");
        startTag.attributes.put(attribute);

        Assert.assertNotNull(startTag.getAttributes());
        Assert.assertEquals(1, startTag.getAttributes().size());
    }

    @Test
    public void testStartTag_toString() {
        StartTag startTag = new StartTag("p");
        Assert.assertEquals("<p>", startTag.toString());

        startTag.attributes.put(new Attribute("class", "test"));
        Assert.assertEquals("<p class=\"test\">", startTag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidStartTag() {
        StartTag startTag = new StartTag();
        Assert.assertFalse(startTag.isEndTag());
        startTag.name.append("div"); // This should throw an AssertionError
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        Assert.assertEquals(TokenType.EndTag, endTag.type);
    }

    @Test
    public void testEndTag_name() {
        EndTag endTag = new EndTag();
        endTag.name("p");
        Assert.assertEquals("p", endTag.name());
    }

    @Test
    public void testEndTag_toString() {
        EndTag endTag = new EndTag("p");
        Assert.assertEquals("</p>", endTag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidEndTag() {
        EndTag endTag = new EndTag();
        Assert.assertFalse(endTag.isStartTag());
        endTag.name.append("div"); // This should throw an AssertionError
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        Assert.assertEquals(TokenType.Comment, comment.type);
    }

    @Test
    public void testComment_getData() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testComment_toString() {
        Comment comment = new Comment();
        comment.data.append("<!-- This is a comment -->");
        Assert.assertEquals("<!--This is a comment -->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        Assert.assertEquals(TokenType.Character, character.type);
    }

    @Test
    public void testCharacter_getData() {
        Character character = new Character("A");
        Assert.assertEquals("A", character.getData());
    }

    @Test
    public void testCharacter_toString() {
        Character character = new Character('A');
        Assert.assertEquals("A", character.toString());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        Assert.assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Doctype doctype = new Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        StartTag startTag = new StartTag();
        Assert.assertTrue(startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        EndTag endTag = new EndTag();
        Assert.assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Comment comment = new Comment();
        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Character character = new Character("A");
        Assert.assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        EOF eof = new EOF();
        Assert.assertTrue(eof.isEOF());
    }
}