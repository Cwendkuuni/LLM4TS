import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniserConstructor() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testReadCharacter() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputstream), errors);
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character characterToken = (Character) token;
        Assert.assertEquals("t", characterToken.text);
    }

    @Test
    public void testReadStartTag() {
        String testInput = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag startTag = (StartTag) token;
        Assert.assertEquals("html", startTag.tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag headTag = (StartTag) token;
        Assert.assertEquals("head", headTag.tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag titleTag = (StartTag) token;
        Assert.assertEquals("title", titleTag.tagName);

        // Test reading past the end of the input stream
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadEndTag() {
        String testInput = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        // Read start tags
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag htmlTag = (StartTag) token;
        Assert.assertEquals("html", htmlTag.tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag headTag = (StartTag) token;
        Assert.assertEquals("head", headTag.tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag titleTag = (StartTag) token;
        Assert.assertEquals("title", titleTag.tagName);

        // Read end tags
        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        EndTag titleTagEnd = (EndTag) token;
        Assert.assertEquals("title", titleTagEnd.tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        EndTag headTagEnd = (EndTag) token;
        Assert.assertEquals("head", headTagEnd.tagName);

        // Test reading past the end of the input stream
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadCharacterReference() {
        String testInput = "&amp; &gt; &lt; &nbsp; &quot; \\u00A9 &eacute;";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        Character characterToken = (Character) tokeniser.read().getTypeCast();
        Assert.assertEquals("&", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals(">", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals("<", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals(" ", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals("\"", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals("\\", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals("©", characterToken.text);
        characterToken = tokeniser.read().getTypeCast();
        Assert.assertEquals("é", characterToken.text);

        // Test reading past the end of the input stream
        tokeniser.read();
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testEmitToken() {
        String testInput = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        tokeniser.emit(new Character("html"));
        tokeniser.emit(new StartTag("html"));

        tokeniser.emit(new Character("h"));
        tokeniser.emit(new Character("e"));
        tokeniser.emit(new Character("a"));
        tokeniser.emit(new Character("d"));
        tokeniser.emit(new StartTag("head"));

        tokeniser.emit(new Character("t"));
        tokeniser.emit(new Character("i"));
        tokeniser.emit(new Character("t"));
        tokeniser.emit(new Character("l"));
        tokeniser.emit(new Character("e"));
        tokeniser.emit(new StartTag("title"));

        tokeniser.emit(new Character("T"));
        tokeniser.emit(new Character("e"));
        tokeniser.emit(new Character("s"));
        tokeniser.emit(new Character("t"));
        tokeniser.emit(new EndTag("title"));

        tokeniser.emit(new Character("b"));
        tokeniser.emit(new Character("o"));
        tokeniser.emit(new Character("d"));
        tokeniser.emit(new Character("y"));
        tokeniser.emit(new StartTag("body"));

        tokeniser.emit(new Character("H"));
        tokeniser.emit(new Character("e"));
        tokeniser.emit(new Character("l"));
        tokeniser.emit(new Character("l"));
        tokeniser.emit(new Character("o"));
        tokeniser.emit(new Character(" "));
        tokeniser.emit(new Character("W"));
        tokeniser.emit(new Character("o"));
        tokeniser.emit(new Character("r"));
        tokeniser.emit(new Character("l"));
        tokeniser.emit(new Character("d"));
        tokeniser.emit(new EndTag("body"));

        tokeniser.emit(new Character("h"));
        tokeniser.emit(new Character("1"));
        tokeniser.emit(new EndTag("html"));

        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Character("html"));
        expectedTokens.add(new StartTag("html"));
        expectedTokens.add(new Character("h"));
        expectedTokens.add(new Character("e"));
        expectedTokens.add(new Character("a"));
        expectedTokens.add(new Character("d"));
        expectedTokens.add(new StartTag("head"));
        expectedTokens.add(new Character("t"));
        expectedTokens.add(new Character("i"));
        expectedTokens.add(new Character("t"));
        expectedTokens.add(new Character("l"));
        expectedTokens.add(new Character("e"));
        expectedTokens.add(new StartTag("title"));
        expectedTokens.add(new Character("T"));
        expectedTokens.add(new Character("e"));
        expectedTokens.add(new Character("s"));
        expectedTokens.add(new Character("t"));
        expectedTokens.add(new EndTag("title"));
        expectedTokens.add(new Character("b"));
        expectedTokens.add(new Character("o"));
        expectedTokens.add(new Character("d"));
        expectedTokens.add(new Character("y"));
        expectedTokens.add(new StartTag("body"));
        expectedTokens.add(new Character("H"));
        expectedTokens.add(new Character("e"));
        expectedTokens.add(new Character("l"));
        expectedTokens.add(new Character("l"));
        expectedTokens.add(new Character("o"));
        expectedTokens.add(new Character(" "));
        expectedTokens.add(new Character("W"));
        expectedTokens.add(new Character("o"));
        expectedTokens.add(new Character("r"));
        expectedTokens.add(new Character("l"));
        expectedTokens.add(new Character("d"));
        expectedTokens.add(new EndTag("body"));
        expectedTokens.add(new Character("h"));
        expectedTokens.add(new Character("1"));
        expectedTokens.add(new EndTag("html"));

        int index = 0;
        while (!tokeniser.isEmitPending()) {
            Token actualToken = tokeniser.read();
            Token expectedToken = expectedTokens.get(index++);
            Assert.assertEquals(expectedToken.getClass(), actualToken.getClass());
            if (actualToken instanceof Character) {
                Assert.assertEquals(((Character) expectedToken).text, ((Character) actualToken).text);
            } else if (actualToken instanceof StartTag) {
                Assert.assertEquals(((StartTag) expectedToken).tagName, ((StartTag) actualToken).tagName);
            } else if (actualToken instanceof EndTag) {
                Assert.assertEquals(((EndTag) expectedToken).tagName, ((EndTag) actualToken).tagName);
            } else if (actualToken instanceof StartTag) {
                Assert.assertEquals(((StartTag) expectedToken).tagName, ((StartTag) actualToken).tagName);
                Assert.assertEquals(expectedToken.attributes, actualToken.attributes);
            }
        }
    }

    @Test
    public void testErrorHandling() {
        String testInput = "<html><script type=\"text/javascript\"><script type=\"text/javascript\";//<![CDATA[";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        tokeniser.read();
        tokeniser.read();

        Assert.assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        ParseError error2 = errors.get(1);

        Assert.assertEquals("Unexpected character 's' in input state [Data]", error1.getMessage());
        Assert.assertEquals("Unexpected character '<' in input state [Data]", error2.getMessage());
    }

    @Test
    public void testReadComment() {
        String testInput = "<!DOCTYPE html>\n<!-- This is a comment -->\n<html>\n";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Comment commentToken = (Comment) token;
        Assert.assertEquals("-->\n<html>\n", commentToken.text);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        StartTag htmlTag = (StartTag) token;
        Assert.assertEquals("html", htmlTag.tagName);

        // Test reading past the end of the comment
        token = tokeniser.read();
        Assert.assertNotNull(token);

        token = tokeniser.read();
        Assert.assertNull(token);
    }
}