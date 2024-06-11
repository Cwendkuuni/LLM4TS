import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        StringReader reader = new StringReader("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        errors = new ArrayList<>();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadCharacter() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character characterToken = (Character) token;
        Assert.assertEquals("H", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("t", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("/", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("1", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("l", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("o", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("w", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("r", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("l", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("d", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", characterToken.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", characterToken.getData());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadStartTag() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag startTagToken = (StartTag) token;
        Assert.assertEquals("html", startTagToken.tagName);

        token = tokeniser.read();
        Assert.assertEquals("/", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("1", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("t", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("i", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("t", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("l", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("e", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", token.getData());

        Assert.assertEquals(0, errors.size());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("1", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("b", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("o", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("d", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("y", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", token.getData());

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character '1' in state [Data]", error.getMessage());
    }

    @Test
    public void testReadEndTag() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag startTagToken = (StartTag) token;
        Assert.assertEquals("html", startTagToken.tagName);

        token = tokeniser.read();
        Assert.assertEquals("/", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("1", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("t", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("i", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("t", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("l", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("e", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("/", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("h", token.getData());

        token = tokeniser.read();
        Assert.assertEquals("1", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(" ", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", token.getData());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testReadComment() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Comment commentToken = (Comment) token;
        Assert.assertEquals("<!--", commentToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("-->", token.getData());

        Assert.assertEquals(0, errors.size());

        // Test error case: missing -- in comment
        StringReader reader = new StringReader("<html><!-- Test");
        Tokeniser tokeniserError = new Tokeniser(reader, errors);
        token = tokeniserError.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Comment commentTokenError = (Comment) token;
        Assert.assertEquals("<!--", commentTokenError.getData());

        token = tokeniserError.read();
        Assert.assertEquals("Test", token.getData());

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character 'T' in state [Comment]", error.getMessage());
    }

    @Test
    public void testReadDoctype() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Doctype doctypeToken = (Doctype) token;
        Assert.assertEquals("<!DOCTYPE", doctypeToken.getData());

        token = tokeniser.read();
        Assert.assertEquals("html", token.getData());

        token = tokeniser.read();
        Assert.assertEquals(">", token.getData());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTransitionWithInvalidState() {
        tokeniser = new Tokeniser(new StringReader(""), errors);

        TokeniserState dataState = TokeniserState.Data;
        tokeniser.transition(TokeniserState.Script);

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character '[', expected '<' in state [Data]", error.getMessage());
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser = new Tokeniser(new StringReader("&#10;"), errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertNull(character);

        character = tokeniser.consumeCharacterReference(null, true);
        Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assert.assertEquals("\n", ((Character) tokeniser.read().getData()).getData());

        character = tokeniser.consumeCharacterReference('\n', false);
        Assert.assertNull(character);

        character = tokeniser.consumeCharacterReference('\n', true);
        Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assert.assertEquals("\n", ((Character) tokeniser.read().getData()).getData());

        character = tokeniser.consumeCharacterReference('x', false);
        Assert.assertNull(character);

        character = tokeniser.consumeCharacterReference('x', true);
        Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assert.assertEquals("x", ((Character) tokeniser.read().getData()).getData());

        character = tokeniser.consumeCharacterReference("foo", false);
        Assert.assertNull(character);

        character = tokeniser.consumeCharacterReference("foo", true);
        Assert.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assert.assertEquals("f", ((Character) tokeniser.read().getData()).getData());
        Assert.assertEquals("o", ((Character) tokeniser.read().getData()).getData());
        Assert.assertEquals("o", ((Character) tokeniser.read().getData()).getData());

        tokeniser.read(); // consume '>'

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Invalid character reference: invalid named referenece 'foo'", error.getMessage());
    }

    @Test
    public void testErrorHandling() {
        tokeniser = new Tokeniser(new StringReader("</script>"), errors);

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character '/', expected 'S' in state [AfterTagName]", error.getMessage());
    }
}