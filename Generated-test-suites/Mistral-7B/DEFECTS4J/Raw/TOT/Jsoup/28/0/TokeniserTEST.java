import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.Entities;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testSimpleCharacters() {
        InputStream input = new ByteArrayInputStream("<html><body>Hello World</body></html>".getBytes());
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token;
        do {
            token = tokeniser.read();
            Assert.assertEquals("Unexpected token type: " + token.type, Token.TokenType.Character, token.type);
            Assert.assertEquals("Unexpected character: " + token.value, Character.toString(token.value[0]), token.value);
        } while (token.value.length > 0);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testSelfClosingTag() {
        InputStream input = new ByteArrayInputStream("<img src=\"image.jpg\" />");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();

        Assert.assertEquals("Unexpected token type: " + startTag.type, Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("Unexpected tag name: " + startTag.value, "img", startTag.tagName);
        Assert.assertFalse("Tag should not have attributes", startTag.attributes != null);
        Assert.assertEquals("Unexpected tag: " + endTag.value, "</img>", endTag.value);
        Assert.assertEquals("Unexpected token type: " + endTag.type, Token.TokenType.EndTag, endTag.type);
        Assert.assertNull("End tag should have no attributes", endTag.attributes);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testNonSelfClosingTag() {
        InputStream input = new ByteArrayInputStream("<p>This is a paragraph</p>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();

        Assert.assertEquals("Unexpected token type: " + startTag.type, Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("Unexpected tag name: " + startTag.value, "p", startTag.tagName);
        Assert.assertNull("Tag should have no attributes", startTag.attributes);
        Assert.assertEquals("Unexpected token type: " + endTag.type, Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("Unexpected tag name: " + endTag.value, "</p>", endTag.value);
        Assert.assertNull("End tag should have no attributes", endTag.attributes);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testErrorHandling() {
        InputStream input = new ByteArrayInputStream("<script>Hello World</script><script>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token;
        while ((token = tokeniser.read()) != null) {
            if (token.type == Token.TokenType.Character) {
                Assert.assertEquals("Unexpected character: " + token.value, Character.toString(token.value[0]), "</script>");
                tokeniser.error("Unexpected character in script tag");
            }
        }

        Assert.assertEquals(1, tokeniser.errors.size());
        Assert.assertEquals("Unexpected character in script tag", tokeniser.errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReference() {
        InputStream input = new ByteArrayInputStream("<html><body>&#123;</body></html>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token = tokeniser.read();
        Assert.assertEquals("Unexpected token type: " + token.type, Token.TokenType.Character, token.type);
        Assert.assertEquals("Unexpected character: " + token.value, Character.toString((char) 123), token.value);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testNamedCharacterReference() {
        InputStream input = new ByteArrayInputStream("<html><body>&auml;foo</body></html>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token = tokeniser.read();
        Assert.assertEquals("Unexpected token type: " + token.type, Token.TokenType.Character, token.type);
        Assert.assertEquals("Unexpected character: " + token.value, String.valueOf(Character.toChars(0xA4)[0]), token.value);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testConsumeCharacterReferenceError() {
        InputStream input = new ByteArrayInputStream("<html><body>&foo;</body></html>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testComment() {
        InputStream input = new ByteArrayInputStream("<html><!--This is a comment--></html>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token = tokeniser.read();
        Assert.assertEquals("Unexpected token type: " + token.type, Token.TokenType.Comment, token.type);
        Assert.assertEquals("Unexpected comment: " + token.value, "--This is a comment--", token.value);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testDoctype() {
        InputStream input = new ByteArrayInputStream("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        Token token = tokeniser.read();
        Assert.assertEquals("Unexpected token type: " + token.type, Token.TokenType.Doctype, token.type);
        Assert.assertEquals("Unexpected doctype: " + token.value, "html", token.value);

        Assert.assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testErrorOnEmptyInput() {
        InputStream input = new ByteArrayInputStream("");
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), new ArrayList<ParseError>());

        tokeniser.eofError(TokeniserState.Data);

        Assert.assertEquals(1, tokeniser.errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getMessage());
    }
}