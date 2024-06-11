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
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype");
        doctype.reset();
        Assert.assertEquals("doctype", doctype.tokenType());
        Assert.assertNull(doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertNull(doctype.getPublicIdentifier());
        Assert.assertNull(doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype");
        doctype.setName("newName");
        doctype.setPubSysKey("publicSysKey");
        doctype.setPublicIdentifier("publicIdentifier");
        doctype.setSystemIdentifier("systemIdentifier");
        doctype.setForceQuirks(true);

        Assert.assertEquals("doctype", doctype.tokenType());
        Assert.assertEquals("newName", doctype.getName());
        Assert.assertEquals("publicSysKey", doctype.getPubSysKey());
        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.reset();
        Assert.assertEquals("StartTag", tag.tokenType().name());
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertNull(tag.attributes);
        Assert.assertFalse(tag.isSelfClosing);
    }

    @Test
    public void testTagSettersAndGetters() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("html", null);
        startTag.nameAttr("html", new Attributes());
        startTag.name("body");
        startTag.newAttribute();
        startTag.appendAttributeName("class").appendAttributeValue("classValue");
        startTag.newAttribute();
        startTag.appendAttributeName("id").appendAttributeValue("idValue");
        startTag.finaliseTag();

        Assert.assertEquals("StartTag", startTag.tokenType().name());
        Assert.assertEquals("html", startTag.name());
        Assert.assertEquals("html", startTag.normalName());
        Assert.assertEquals("class", startTag.pendingAttributeName);
        Assert.assertEquals("classValue", startTag.pendingAttributeValue.toString());
        Assert.assertSame(startTag.getAttributes(), startTag.attributes);
        Assert.assertEquals(2, startTag.attributes.size());
        Assert.assertEquals("classValue", startTag.attributes.get("class").getValue());
        Assert.assertEquals("idValue", startTag.attributes.get("id").getValue());
        Assert.assertFalse(startTag.isSelfClosing);
    }

    @Test(expected = IllegalStateException.class)
    public void testTagNewAttributeWithNullAttributeName() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName(null);
        startTag.newAttribute();
    }

    @Test
    public void testTagAppendAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("href");
        startTag.appendAttributeValue("http://example.com");
        startTag.appendAttributeValue("?query=");
        startTag.appendAttributeValue("test");
        startTag.finaliseTag();

        Assert.assertEquals("StartTag", startTag.tokenType().name());
        Assert.assertEquals("tagName", startTag.name());
        Assert.assertEquals("tagName", startTag.normalName());
        Assert.assertEquals("href", startTag.pendingAttributeName);
        Assert.assertEquals("http://example.com?query=test", startTag.pendingAttributeValue.toString());
        Assert.assertSame(startTag.getAttributes(), startTag.attributes);
        Assert.assertEquals(1, startTag.attributes.size());
        Assert.assertNotNull(startTag.attributes.get("href"));
    }

    @Test(expected = IllegalStateException.class)
    public void testTagAppendAttributeValueWithNullValue() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("href");
        startTag.appendAttributeValue(null);
        startTag.newAttribute();
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertTrue(token instanceof Doctype);
        Assert.assertEquals(TokenType.Doctype, token.type);

        token = new StartTag();
        assertTrue(token instanceof StartTag);
        Assert.assertEquals(TokenType.StartTag, token.type);

        token = new EndTag();
        assertTrue(token instanceof EndTag);
        Assert.assertEquals(TokenType.EndTag, token.type);

        token = new Comment();
        assertTrue(token instanceof Comment);
        Assert.assertEquals(TokenType.Comment, token.type);

        token = new Character();
        assertTrue(token instanceof Character);
        Assert.assertEquals(TokenType.Character, token.type);

        token = new EOF();
        assertTrue(token instanceof EOF);
        Assert.assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testTokens() {
        Token doctype = new Doctype();
        Token startTag = new StartTag();
        Token endTag = new EndTag();
        Token comment = new Comment();
        Token character = new Character();
        Token eof = new EOF();

        Assert.assertNotNull(doctype);
        Assert.assertNotNull(startTag);
        Assert.assertNotNull(endTag);
        Assert.assertNotNull(comment);
        Assert.assertNotNull(character);
        Assert.assertNotNull(eof);
    }

    @Test
    public void testTokensGetters() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        Assert.assertEquals("doctype", doctype.tokenType().name());
        Assert.assertEquals("doctype", doctype.tokenType().toString());

        Assert.assertEquals("StartTag", startTag.tokenType().name());
        Assert.assertEquals("StartTag", startTag.tokenType().toString());

        Assert.assertEquals("EndTag", endTag.tokenType().name());
        Assert.assertEquals("EndTag", endTag.tokenType().toString());

        Assert.assertEquals("Comment", comment.tokenType().name());
        Assert.assertEquals("Comment", comment.tokenType().toString());

        Assert.assertEquals("Character", character.tokenType().name());
        Assert.assertEquals("Character", character.tokenType().toString());

        Assert.assertEquals("EOF", eof.tokenType().name());
        Assert.assertEquals("EOF", eof.tokenType().toString());

        Assert.assertEquals("doctype", doctype.tokenType());
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());

        Assert.assertEquals("StartTag", startTag.tokenType());
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());

        Assert.assertEquals("EndTag", endTag.tokenType());
        Assert.assertEquals(TokenType.EndTag, endTag.tokenType());

        Assert.assertEquals("Comment", comment.tokenType());
        Assert.assertEquals(TokenType.Comment, comment.tokenType());

        Assert.assertEquals("Character", character.tokenType());
        Assert.assertEquals(TokenType.Character, character.tokenType());

        Assert.assertEquals("EOF", eof.tokenType());
        Assert.assertEquals(TokenType.EOF, eof.tokenType());
    }

    @Test
    public void testTokensToString() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        Assert.assertEquals("<doctype>", doctype.toString());
        Assert.assertEquals("<doctype/>", doctype.toString(true));

        Assert.assertEquals("<tagname>", startTag.toString());
        Assert.assertEquals("<tagname attr=\"value\" />", startTag.toString(true));

        Assert.assertEquals("</tagname>", endTag.toString());
        Assert.assertEquals("<!-- -->", comment.toString());

        Assert.assertEquals("char", character.toString());
        Assert.assertEquals("char", character.toString(true));

        Assert.assertEquals("", eof.toString());
        Assert.assertEquals("", eof.toString(true));
    }
}