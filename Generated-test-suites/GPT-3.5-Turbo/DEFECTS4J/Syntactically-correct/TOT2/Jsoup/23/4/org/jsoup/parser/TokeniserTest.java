
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"), new ParseErrorList());
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#169;"), new ParseErrorList());
        Character output = tokeniser.consumeCharacterReference(null, false);
        assertEquals((char) 169, output.charValue());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }
}
