// ... (previous test cases)


import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testReadAllTokens() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head><title>Test</title></head>\n<body>\n<h1>Hello World!</h1>\n</body>\n</html>".getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());

        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token.StartTag(TokeniserState.StartTag, "html", null));
        expectedTokens.add(new Token.Doctype(TokeniserState.Doctype, null));
        expectedTokens.add(new Token.StartTag(TokeniserState.StartTag, "head", null));
        expectedTokens.add(new Token.StartTag(TokeniserState.StartTag, "title", null));
        expectedTokens.add(new Token.Character('T'));
        expectedTokens.add(new Token.Character('e'));
        expectedTokens.add(new Token.Character('s'));
        expectedTokens.add(new Token.Character('t'));
        expectedTokens.add(new Token.Character(' '));
        expectedTokens.add(new Token.EndTag(TokeniserState.EndTag, "title", null));
        expectedTokens.add(new Token.StartTag(TokeniserState.StartTag, "body", null));
        expectedTokens.add(new Token.StartTag(TokeniserState.StartTag, "h1", null));
        expectedTokens.add(new Token.Character('H'));
        expectedTokens.add(new Token.Character('e'));
        expectedTokens.add(new Token.Character('l'));
        expectedTokens.add(new Token.Character('l'));
        expectedTokens.add(new Token.Character('o'));
        expectedTokens.add(new Token.Character(' '));
        expectedTokens.add(new Token.Character('W'));
        expectedTokens.add(new Token.Character('o'));
        expectedTokens.add(new Token.Character('r'));
        expectedTokens.add(new Token.Character('l'));
        expectedTokens.add(new Token.Character('d'));
        expectedTokens.add(new Token.EndTag(TokeniserState.EndTag, "h1", null));
        expectedTokens.add(new Token.EndTag(TokeniserState.EndTag, "body", null));
        expectedTokens.add(new Token.EndTag(TokeniserState.EndTag, "html", null));

        List<Token> actualTokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            actualTokens.add(token);
        }

        assertEquals(expectedTokens.size(), actualTokens.size());
        for (int i = 0; i < expectedTokens.size(); i++) {
            Token expectedToken = expectedTokens.get(i);
            Token actualToken = actualTokens.get(i);
            assertEquals(expectedToken.type, actualToken.type);
            assertEquals(expectedToken.tagName, actualToken.tagName);
            assertEquals(expectedToken.data, actualToken.data);
        }
    }

    @Test
    public void testReadEmptyInputStream() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream(new byte[0])), new ParseErrorList());
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInvalidCharacterReference() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#123;".getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        tokeniser.consumeCharacterReference(null, false);
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals("Invalid character reference: invalid character reference: character outside of valid range", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacters() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#%&/;".getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        tokeniser.consumeCharacterReference(null, false);
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals("Invalid character reference: invalid character: character '%' is an invalid reference character", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidSyntax() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#x;".getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        tokeniser.consumeCharacterReference(null, false);
        assertTrue(tokeniser.errors.size() > 0);
        assertEquals("Invalid character reference: invalid syntax", tokeniser.errors.get(0).getMessage());
    }

    // ... (other test cases)
}