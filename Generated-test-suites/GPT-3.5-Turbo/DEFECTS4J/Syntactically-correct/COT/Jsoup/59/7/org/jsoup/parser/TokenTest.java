
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        Assert.assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        Assert.assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        Assert.assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        Assert.assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        Assert.assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Name");
        doctype.pubSysKey = "PubSysKey";
        doctype.publicIdentifier.append("Public Identifier");
        doctype.systemIdentifier.append("System Identifier");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        Assert.assertEquals("", resetDoctype.name.toString());
        Assert.assertNull(resetDoctype.pubSysKey);
        Assert.assertEquals("", resetDoctype.publicIdentifier.toString());
        Assert.assertEquals("", resetDoctype.systemIdentifier.toString());
        Assert.assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Name");
        doctype.pubSysKey = "PubSysKey";
        doctype.publicIdentifier.append("Public Identifier");
        doctype.systemIdentifier.append("System Identifier");
        doctype.forceQuirks = true;

        Assert.assertEquals("Name", doctype.getName());
        Assert.assertEquals("PubSysKey", doctype.getPubSysKey());
        Assert.assertEquals("Public Identifier", doctype.getPublicIdentifier());
        Assert.assertEquals("System Identifier", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    // Test other methods and classes in the Token class
    // ...

}

