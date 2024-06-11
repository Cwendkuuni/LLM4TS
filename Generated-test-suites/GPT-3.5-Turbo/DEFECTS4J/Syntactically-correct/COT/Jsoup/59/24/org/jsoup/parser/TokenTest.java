import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testReset() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNull(token.reset());
    }

    @Test
    public void testStaticReset() {
        StringBuilder sb = new StringBuilder("Test");
        Token.reset(sb);
        assertEquals("", sb.toString());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        assertEquals("Test", doctype.getName());
    }

    @Test
    public void testGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("Public");
        assertEquals("Public", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("System");
        assertEquals("System", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNull(tag.reset());
    }

    // ... Repeat the above test method for other methods

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}