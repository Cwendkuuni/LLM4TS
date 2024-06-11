import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype createDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01//EN";
        doctype.publicIdentifier.append("HTML PUBLIC");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = false;
        return doctype;
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = createDoctype();
        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }

    // Add other test cases for each method in the Token class

}