import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test for tokenType() method
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    // Test for reset() method in Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Name");
        doctype.pubSysKey = "Test PubSysKey";
        doctype.publicIdentifier.append("Test Public Identifier");
        doctype.systemIdentifier.append("Test System Identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    // Test for newAttribute() method in Tag class
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("Test Attribute Name");
        tag.appendAttributeValue("Test Attribute Value");

        tag.newAttribute();

        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());
    }

    // Add more test cases for other methods as needed

}