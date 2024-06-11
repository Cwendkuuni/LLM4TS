import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadMethod() {
        CharacterReader reader = new CharacterReader("Hello World");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token;

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('H', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('e', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('l', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('l', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('o', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(' ', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('W', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('o', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('r', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('l', token.getData());

        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndOfInput, token.type);
    }
}