import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // By default, it sets the name to html
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeSetName() {
        Doctype doctype = new Doctype();
        doctype.name("test");
        assertEquals("test", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setPublicIdentifier("public-id");
        assertEquals("public-id", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setSystemIdentifier("system-id");
        assertEquals("system-id", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }
}