import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        doctype.newAttribute().getName("html");
        doctype.newAttribute().setName("publicId").setValue("publicId");
        doctype.newAttribute().setName("systemId").setValue("systemId");
        doctype.isForceQuirks(true);

        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("html", doctype.asDoctype().getName());
        Assert.assertEquals("publicId", doctype.asDoctype().getPubSysKey());
        Assert.assertEquals("systemId", doctype.asDoctype().getPublicIdentifier());
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        startTag.name("html");
        startTag.newAttribute().setName("id").setValue("id");
        startTag.attributes().put("class", "class");

        Assert.assertEquals("StartTag", startTag.tokenType().toString());
        Assert.assertEquals("html", startTag.name());
        Assert.assertEquals("id", startTag.getAttributes().getValue("id"));
        Assert.assertEquals("class", startTag.getAttributes().getClass());
        Assert.assertNull(startTag.asStartTag().getAttributes().getValue("nonexistent"));
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        endTag.name("html");

        Assert.assertEquals("EndTag", endTag.tokenType().toString());
        Assert.assertEquals("html", endTag.name());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        comment.appendAttributeValue("--comment--");

        Assert.assertEquals("Comment", comment.tokenType().toString());
        Assert.assertEquals("--comment--", comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        character.data("Hello World!");

        Assert.assertEquals("Character", character.tokenType().toString());
        Assert.assertEquals("Hello World!", character.getData());
    }

    @Test(expected = AssertionError.class)
    public void testMalformedToken() {
        Token malformedToken = new Token();
        malformedToken.type = new TokenType("InvalidType");

        Assert.fail("MalformedToken should not be created");
    }
}