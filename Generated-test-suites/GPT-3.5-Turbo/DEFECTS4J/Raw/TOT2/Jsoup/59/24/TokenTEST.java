import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "TestKey";
        doctype.publicIdentifier.append("TestPub");
        doctype.systemIdentifier.append("TestSys");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("Test");

        assertEquals("Test", tag.tagName);
        assertEquals("test", tag.normalName);
    }

    // Additional test methods for other methods in Token class

}