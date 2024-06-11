import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.ParseError;
import org.junit.Test;

public class TokeniserTest {
    

    @Test
    public void testRead_NormalCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Tag expectedTag = new Tag("Character");
        expectedTag.data("a");
        assertTokenEquals(expectedTag, token);
    }

    @Test
    public void testRead_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_SelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src='image.jpg' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Tag expectedTag = new Tag("StartTag");
        expectedTag.tagName("img");
        expectedTag.selfClosing(true);
        assertTokenEquals(expectedTag, token);

        token = tokeniser.read();
        expectedTag = new Tag("EndTag");
        expectedTag.tagName("img");
        assertTokenEquals(expectedTag, token);
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("#xabcd");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] result = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{Tokeniser.replacementChar}, result);
    }

    @Test
    public void testErrorHandling_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.error(TokeniserState.Data);
        ParseError expectedError = new ParseError(0, "Unexpectedly reached end of file (EOF) in input state [Data]");
        assertEquals(expectedError, errors.get(0));
    }

    @Test
    public void testErrorHandling_InvalidCharacter() {
        CharacterReader reader = new CharacterReader("abc&");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.error(TokeniserState.Data);
        ParseError expectedError = new ParseError(4, "Invalid character reference: missing semicolon");
        assertEquals(expectedError, errors.get(0));
    }

    @Test
    public void testErrorHandling_UnexpectedEndOfFile() {
        CharacterReader reader = new CharacterReader("ab");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.eofError(TokeniserState.Data);
        ParseError expectedError = new ParseError(2, "Unexpectedly reached end of file (EOF) in input state [Data]");
        assertEquals(expectedError, errors.get(0));
    }

    private void assertTokenEquals(Tag expected, Token actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.tagName(), actual.tagName());
        assertEquals(expected.selfClosing(), actual.selfClosing());
        assertEquals(expected.data(), actual.data());
        assertEquals(expected.attributes(), actual.attributes());
    }

    
}