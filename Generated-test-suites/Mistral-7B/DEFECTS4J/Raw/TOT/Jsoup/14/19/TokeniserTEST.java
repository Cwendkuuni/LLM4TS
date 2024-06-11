import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testReadSimpleText() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("Hello World".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        List<ParseError> errors = new ArrayList<>();

        tokeniser.setTrackErrors(true);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("H", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("o", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("W", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("o", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("r", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("d", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndOfInput, token.type);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadHtmlWithTags() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        List<ParseError> errors = new ArrayList<>();

        tokeniser.setTrackErrors(true);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("title", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("T", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("s", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("t", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("title", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("h1", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("H", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("o", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("h1", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadHtmlWithErrors() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<html<title".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        List<ParseError> errors = new ArrayList<>();

        tokeniser.setTrackErrors(true);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals("Unexpected character in input", token.error.getMessage());
        Assert.assertEquals("title".charAt(0), token.error.getCharacter());
        Assert.assertEquals(TokeniserState.StartTagBody, token.state);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(token, errors.get(0).getToken());

        tokeniser.read(); // consume error

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals("Unexpected character in input", token.error.getMessage());
        Assert.assertEquals("title".charAt(1), token.error.getCharacter());
        Assert.assertEquals(TokeniserState.StartTagBody, token.state);
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals(token, errors.get(1).getToken());

        tokeniser.read(); // consume error

        tokeniser.read(); // consume '<'

        tokeniser.read(); // consume 't'

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals("Unexpected character in input", token.error.getMessage());
        Assert.assertEquals("i", token.error.getCharacter());
        Assert.assertEquals(TokeniserState.StartTagTitle, token.state);
        Assert.assertEquals(3, errors.size());
        Assert.assertEquals(token, errors.get(2).getToken());

        tokeniser.read(); // consume 'l'

        tokeniser.read(); // consume 'e'

        tokeniser.read(); // consume '>'

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("title", token.tagName);

        tokeniser.read(); // consume '</title>'

        tokeniser.read(); // consume '<'

        tokeniser.read(); // consume 'b'

        tokeniser.read(); // consume 'o'

        tokeniser.read(); // consume 'd'

        tokeniser.read(); // consume 'y'

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);

        Assert.assertEquals(3, errors.size());
        Assert.assertEquals("There is an unread token pending!", errors.get(3).getMessage());
    }

    @Test
    public void testReadHtmlWithSelfClosingTags() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<img src=\"image.jpg\" alt=\"Image\"/><p>Text</p>".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        Token token;
        List<ParseError> errors = new ArrayList<>();

        tokeniser.setTrackErrors(true);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.tagName);
        Assert.assertTrue(token.selfClosing);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Attribute, token.type);
        Assert.assertEquals("src", token.attributeName);
        Assert.assertEquals("image.jpg\"", token.attributeValue);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("p", token.tagName);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("T", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("x", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("t", token.data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName);

        Assert.assertEquals(0, errors.size());
    }
}