import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.publicIdentifier.append("testPublicIdentifier");
        doctype.systemIdentifier.append("testSystemIdentifier");
        assertFalse(doctype.forceQuirks);

        assertEquals("testName", doctype.getName());
        assertEquals("testPublicIdentifier", doctype.getPublicIdentifier());
        assertEquals("testSystemIdentifier", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("testTag");
        assertEquals("<testTag>", startTag.toString());

        startTag.attributes.put(new Attribute("attr", "value"));
        assertEquals("<testTag attr=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("testTag");
        assertEquals("</testTag>", endTag.toString());
    }

    // Add more test cases for other methods as needed
    
}