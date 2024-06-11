import org.jsoup.parser.Token;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TestName");
        doctype.pubSysKey = "TestPubSysKey";
        doctype.publicIdentifier.append("TestPublicId");
        doctype.systemIdentifier.append("TestSystemId");
        doctype.forceQuirks = true;

        Token resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("TestString");
        Token.reset(sb);
        assertEquals("", sb.toString());
    }
}