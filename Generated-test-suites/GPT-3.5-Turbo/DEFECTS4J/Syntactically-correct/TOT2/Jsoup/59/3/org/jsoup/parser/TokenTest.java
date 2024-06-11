import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("test");
        doctype.pubSysKey = "pubKey";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;

        assertTrue(doctype.getName().equals("test") && doctype.getPubSysKey().equals("pubKey")
                && doctype.getPublicIdentifier().equals("pubId") && doctype.getSystemIdentifier().equals("sysId")
                && doctype.isForceQuirks());

        doctype.reset();

        assertTrue(doctype.getName().equals("") && doctype.getPubSysKey() == null
                && doctype.getPublicIdentifier().equals("") && doctype.getSystemIdentifier().equals("")
                && !doctype.isForceQuirks());
    }

}