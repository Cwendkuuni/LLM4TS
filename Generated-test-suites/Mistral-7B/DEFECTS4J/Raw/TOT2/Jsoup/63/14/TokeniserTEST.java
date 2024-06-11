import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = new CharacterReader("test.html");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_Character() {
        reader.current('a');
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("a", token.data());
    }

    @Test
    public void testRead_EndTag() {
        reader.current('<');
        reader.current('/');
        reader.current('d');
        reader.current('o');
        reader.current('c');
        reader.current('t');
        reader.current('y');
        reader.current('p');
        reader.current('>');

        Token token = tokeniser.read();
        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("DOCTYPE", token.tagName());
    }

    @Test
    public void testRead_StartTag() {
        reader.current('<');
        reader.current('p');
        reader.current('>');

        Token token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("p", token.tagName());
    }

    @Test
    public void testRead_SelfClosingTag() {
        reader.current('<');
        reader.current('img');
        reader.current('>');

        Token token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("img", token.tagName());
        assertEquals(true, token.selfClosing());
    }

    @Test
    public void testErrorHandling_SelfClosingFlagNotAcknowledged() {
        reader.current('<');
        reader.current('/');
        reader.current('p');
        reader.current('>');
        reader.current('<');
        reader.current('/');
        reader.current('p');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.error(TokeniserState.AfterStartTag);
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
    }

    @Test
    public void testErrorHandling_UnexpectedCharacter() {
        reader.current('&');
        reader.current('a');

        tokeniser.read();
        tokeniser.error(TokeniserState.Data);
        assertEquals("Invalid character reference: invalid named reference '<a>'", errors.get(0).getMessage());
    }
}