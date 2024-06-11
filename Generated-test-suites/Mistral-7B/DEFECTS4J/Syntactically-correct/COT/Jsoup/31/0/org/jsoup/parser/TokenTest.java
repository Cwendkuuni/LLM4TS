import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype_basic() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctypeToken.tokenType());
    }

    @Test
    public void testDoctype_getName_basic() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().name("html");
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctype_getName_setTwice() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().name("html");
        doctypeToken.asDoctype().name("body"); // should throw an exception
    }

    @Test
    public void testDoctype_getPublicIdentifier_basic() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().setPublicIdentifier("_public");
        Assert.assertEquals("_public", doctypeToken.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks_basic() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag_basic() {
        Token tagToken = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, tagToken.tokenType());
    }

    @Test
    public void testTag_setName_basic() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().name("div");
        Assert.assertEquals("div", tagToken.asStartTag().name());
    }

    @Test(expected = IllegalStateException.class)
    public void testTag_setName_setTwice() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().name("div");
        tagToken.asStartTag().name("p"); // should throw an exception
    }

    @Test
    public void testTag_setSelfClosing_basic() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().isSelfClosing(true);
        Assert.assertTrue(tagToken.asStartTag().isSelfClosing());
    }

    @Test
    public void testTag_newAttribute_basic() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().newAttribute();
    }

    @Test
    public void testTag_newAttribute_withValue_basic() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().newAttribute().appendAttributeName("id").appendAttributeValue("test");
        Assert.assertEquals("id", tagToken.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("id").getValue());
    }

    @Test
    public void testTag_finaliseTag_basic() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().newAttribute().appendAttributeName("id").appendAttributeValue("test");
        tagToken.asStartTag().finaliseTag();
    }

    @Test(expected = IllegalStateException.class)
    public void testTag_finaliseTag_afterNewAttribute_withValue_twice() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().newAttribute().appendAttributeName("id").appendAttributeValue("test");
        tagToken.asStartTag().finaliseTag(); // finalised
        tagToken.asStartTag().newAttribute().appendAttributeName("class").appendAttributeValue("test2"); // should throw an exception
    }

    @Test
    public void testComment_basic() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, commentToken.tokenType());
    }

    @Test
    public void testComment_setData_basic() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().appendData("This is a comment.");
        Assert.assertEquals("This is a comment.", commentToken.asComment().getData());
    }

    @Test
    public void testCharacter_basic() {
        Token characterToken = new Token.Character('A');
        Assert.assertEquals(TokenType.Character, characterToken.tokenType());
        Assert.assertEquals('A', characterToken.asCharacter().getData().charAt(0));
    }

    @Test
    public void testEOF_basic() {
        Token eofToken = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eofToken.tokenType());
    }
}