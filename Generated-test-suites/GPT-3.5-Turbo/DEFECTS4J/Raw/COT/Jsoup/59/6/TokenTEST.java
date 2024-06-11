import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new StartTag();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testTokenTypeAsString() {
        Token token = new EndTag();
        Assert.assertEquals("EndTag", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        StartTag startTag = new StartTag();
        startTag.name = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        Assert.assertNull(startTag.name);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }

    @Test
    public void testResetComment() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Character character = new Character();
        character.data = "A";

        character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testResetEOF() {
        EOF eof = new EOF();

        eof.reset();

        Assert.assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testIsDoctypeFalse() {
        Token token = new StartTag();
        Assert.assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Doctype();
        Doctype doctype = token.asDoctype();
        Assert.assertNotNull(doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testIsStartTagFalse() {
        Token token = new EndTag();
        Assert.assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new StartTag();
        StartTag startTag = token.asStartTag();
        Assert.assertNotNull(startTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testIsEndTagFalse() {
        Token token = new StartTag();
        Assert.assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new EndTag();
        EndTag endTag = token.asEndTag();
        Assert.assertNotNull(endTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testIsCommentFalse() {
        Token token = new StartTag();
        Assert.assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Comment();
        Comment comment = token.asComment();
        Assert.assertNotNull(comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Character();
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testIsCharacterFalse() {
        Token token = new StartTag();
        Assert.assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Character();
        Character character = token.asCharacter();
        Assert.assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new EOF();
        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testIsEOFFalse() {
        Token token = new StartTag();
        Assert.assertFalse(token.isEOF());
    }
}