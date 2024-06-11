import org.junit.Test;
import org.scribe.model.Verifier;

import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier v = new Verifier("abcd");
        assertEquals("abcd", v.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        Verifier v = new Verifier(null);
    }

    @Test
    public void testVerifierEmptyConstructor() {
        Verifier v = new Verifier("");
        assertEquals("", v.getValue());
    }

    @Test
    public void testVerifierOnlyWhitespaceConstructor() {
        Verifier v = new Verifier(" ");
        assertEquals(" ", v.getValue());
    }

    @Test
    public void testVerifierLongValueConstructor() {
        Verifier v = new Verifier("abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghi");
        assertEquals("abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghi", v.getValue());
    }

    @Test
    public void testVerifierSpecialCharactersConstructor() {
        Verifier v = new Verifier("!@#$%^&*()-=_+{}[]|\\\"':;<>,.?/");
        assertEquals("!@#$%^&*()-=_+{}[]|\\\"':;<>,.?/", v.getValue());
    }
}