import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype"); // initial state

        doctype.reset();

        Doctype resetDoctype = doctype;
        resetDoctype.getName().equals(""); // reset state
        resetDoctype.isForceQuirks().equals(false);
    }
}