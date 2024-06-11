import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>".getBytes()));
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testReadAndEmitSimpleText() {
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("H", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("o", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("W", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("r", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("d", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);

        tokeniser.emit(token);

        Assert.assertEquals(null, tokeniser.read());
    }

    @Test
    public void testReadAndEmitSelfClosingTag() {
        tokeniser.transition(TokeniserState.SelfClosingTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("/", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("i", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("m", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("g", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("/", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.tagName);

        tokeniser.emit(token);

        Assert.assertEquals(null, tokeniser.read());
    }

    @Test
    public void testReadAndEmitError() {
        tokeniser.transition(TokeniserState.Error);

        tokeniser.error("Unexpected character in input: <");

        Assert.assertEquals(null, tokeniser.read());

        List<ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character in input: <", errors.get(0).message);
        Assert.assertEquals(reader.pos(), errors.get(0).position);
        Assert.assertEquals(TokeniserState.Error, errors.get(0).state);
    }

    @Test
    public void testReadAndEmitDoctype() {
        tokeniser.createDoctypePending();

        tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, tokeniser.getState().getStateType());

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("<", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("d", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("o", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("t", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("YPE", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(">", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("<", token.text);

        tokeniser.emit(token);

        tokeniser.transition(TokeniserState.Data);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("h", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("t", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("m", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("W", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("r", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("l", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("d", token.text);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);

        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName);

        tokeniser.emit(token);

        Assert.assertEquals(null, tokeniser.read());
    }
}