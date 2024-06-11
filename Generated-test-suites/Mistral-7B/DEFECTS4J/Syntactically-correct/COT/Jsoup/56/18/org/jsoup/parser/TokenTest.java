import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName(); // initial call to getName does not reset the token

        doctype.reset();
        Assert.assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("html");
        doctype.getPublicIdentifier("_publicId_");
        doctype.setForceQuirks(true);

        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("_publicId_", doctype.getPublicIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirksGettersWithInvalidValue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setForceQuirks(-1);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");

        tag.reset();
        Assert.assertNull(tag.name());
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testTagGetters() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");

        Assert.assertEquals("div", tag.name());
        Assert.assertEquals("test", tag.getAttributes().getValue("class"));
    }

    @Test(expected = AssertionError.class)
    public void testTagGettersWithInvalidName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name(null);
    }

    @Test
    public void testTagSetters() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("div").newAttribute().name("class").value("test");

        tag.name("span");
        Assert.assertEquals("span", tag.name());

        tag.newAttribute().name("id").value("idTest");
        Assert.assertEquals("test", tag.getAttributes().getValue("class"));
        Assert.assertEquals("idTest", tag.getAttributes().getValue("id"));
    }

    @Test(expected = AssertionError.class)
    public void testTagSettersWithNullName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name(null).newAttribute().name("class").value("test");
    }

    @Test(expected = AssertionError.class)
    public void testTagSettersWithEmptyAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("div").newAttribute().name("class").value("");
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("div").newAttribute().name("class").value("test");
        tag.finaliseTag();

        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
    }
}