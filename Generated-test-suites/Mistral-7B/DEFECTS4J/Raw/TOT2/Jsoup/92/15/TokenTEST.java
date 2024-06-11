import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // Initial state

        doctype.reset();

        String name = doctype.getName();
        String pubSysKey = doctype.getPubSysKey();
        String publicIdentifier = doctype.getPublicIdentifier();
        String systemIdentifier = doctype.getSystemIdentifier();
        boolean forceQuirks = doctype.isForceQuirks();

        //assert reset values
        name.isEmpty();
        pubSysKey == null;
        publicIdentifier.isEmpty();
        systemIdentifier.isEmpty();
        !forceQuirks;
    }
}