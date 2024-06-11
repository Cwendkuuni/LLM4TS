import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("Test String");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.Tag();
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&lt;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('<', character.charValue());
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("<test>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);

        Token.Tag tag = tokeniser.tagPending;
        assertEquals("test", tag.tagName);
    }

}