import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
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
    public void testRead_EmptyInput() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[0]));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_Whitespace() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(" \t\r\n\f".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tokens.add(tokeniser.read());
        }

        Assert.assertEquals(5, tokens.size());
        for (Token token : tokens) {
            Assert.assertEquals(Token.TokenType.Whitespace, token.type);
        }
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_SimpleTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        Assert.assertNotNull(startTag);
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("p", startTag.tagName);

        Token endTag = tokeniser.read();
        Assert.assertNotNull(endTag);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("p", endTag.tagName);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_SelfClosingTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<img src='image.png' />".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        Assert.assertNotNull(startTag);
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("img", startTag.tagName);
        Assert.assertTrue(startTag.selfClosing);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_ErrorHandling() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<script>function test() {}}".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character '{' in input state [Data]", errors.get(0).getMessage());

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character '(' in input state [Script]", errors.get(0).getMessage());
    }

    @Test
    public void testRead_CharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#x1F600;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\uD83C\uDC00", token.toString());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_CharacterReferenceInvalid() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#x1F60Z;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference: invalid character 'Z'", errors.get(0).getMessage());

        reader.unconsume(); // unconsume the 'Z' to restore the input stream

        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_CharacterReferenceWithAdditionalAllowedCharacter() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#x1F600;&#x1F601;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\uD83C\uDC00", token.toString());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\uD83C\uDC01", token.toString());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&#&#;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Character reference with no numerals", errors.get(0).getMessage());

        reader.unconsume(); // unconsume the '#' to restore the input stream

        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_NamedCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&amp;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("&amp;", token.toString());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_InvalidNamedCharacterReference() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&abcd;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid named reference 'abcd'", errors.get(0).getMessage());

        reader.unconsume(); // unconsume the '&' to restore the input stream

        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_EndTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p><p></p>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag1 = tokeniser.read();
        Assert.assertNotNull(startTag1);
        Assert.assertEquals(Token.TokenType.StartTag, startTag1.type);
        Assert.assertEquals("p", startTag1.tagName);

        Token startTag2 = tokeniser.read();
        Assert.assertNotNull(startTag2);
        Assert.assertEquals(Token.TokenType.StartTag, startTag2.type);
        Assert.assertEquals("p", startTag2.tagName);

        Token endTag = tokeniser.read();
        Assert.assertNotNull(endTag);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("p", endTag.tagName);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_InvalidEndTag() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<p><p></p></div>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag1 = tokeniser.read();
        Assert.assertNotNull(startTag1);
        Assert.assertEquals(Token.TokenType.StartTag, startTag1.type);
        Assert.assertEquals("p", startTag1.tagName);

        Token startTag2 = tokeniser.read();
        Assert.assertNotNull(startTag2);
        Assert.assertEquals(Token.TokenType.StartTag, startTag2.type);
        Assert.assertEquals("p", startTag2.tagName);

        Token endTag = tokeniser.read();
        Assert.assertNull(endTag);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());

        reader.unconsume(); // unconsume the '>' to restore the input stream

        endTag = tokeniser.read();
        Assert.assertNull(endTag);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());

        tokeniser.acknowledgeSelfClosingFlag();
        endTag = tokeniser.read();
        Assert.assertNull(endTag);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testRead_ComplexInput() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><p>Test1</p><script>function test() {}</script><p>Test2</p></body></html>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag1 = tokeniser.read();
        Assert.assertNotNull(startTag1);
        Assert.assertEquals(Token.TokenType.StartTag, startTag1.type);
        Assert.assertEquals("html", startTag1.tagName);

        Token startTag2 = tokeniser.read();
        Assert.assertNotNull(startTag2);
        Assert.assertEquals(Token.TokenType.StartTag, startTag2.type);
        Assert.assertEquals("head", startTag2.tagName);

        Token startTag3 = tokeniser.read();
        Assert.assertNotNull(startTag3);
        Assert.assertEquals(Token.TokenType.StartTag, startTag3.type);
        Assert.assertEquals("title", startTag3.tagName);

        Token text1 = tokeniser.read();
        Assert.assertNotNull(text1);
        Assert.assertEquals(Token.TokenType.Character, text1.type);
        Assert.assertEquals("Test", text1.toString());

        Token endTag1 = tokeniser.read();
        Assert.assertNotNull(endTag1);
        Assert.assertEquals(Token.TokenType.EndTag, endTag1.type);
        Assert.assertEquals("title", endTag1.tagName);

        Token startTag4 = tokeniser.read();
        Assert.assertNotNull(startTag4);
        Assert.assertEquals(Token.TokenType.StartTag, startTag4.type);
        Assert.assertEquals("body", startTag4.tagName);

        Token text2 = tokeniser.read();
        Assert.assertNotNull(text2);
        Assert.assertEquals(Token.TokenType.Character, text2.type);
        Assert.assertEquals("Test1", text2.toString());

        Token startTag5 = tokeniser.read();
        Assert.assertNotNull(startTag5);
        Assert.assertEquals(Token.TokenType.StartTag, startTag5.type);
        Assert.assertEquals("script", startTag5.tagName);
        Assert.assertTrue(startTag5.selfClosing);

        Token text3 = tokeniser.read();
        Assert.assertNotNull(text3);
        Assert.assertEquals(Token.TokenType.Character, text3.type);
        Assert.assertEquals("function test() {}", text3.toString());

        Token endTag2 = tokeniser.read();
        Assert.assertNotNull(endTag2);
        Assert.assertEquals(Token.TokenType.EndTag, endTag2.type);
        Assert.assertEquals("script", endTag2.tagName);

        Token text4 = tokeniser.read();
        Assert.assertNotNull(text4);
        Assert.assertEquals(Token.TokenType.Character, text4.type);
        Assert.assertEquals("Test2", text4.toString());

        Token endTag3 = tokeniser.read();
        Assert.assertNotNull(endTag3);
        Assert.assertEquals(Token.TokenType.EndTag, endTag3.type);
        Assert.assertEquals("body", endTag3.tagName);

        Token endTag4 = tokeniser.read();
        Assert.assertNotNull(endTag4);
        Assert.assertEquals(Token.TokenType.EndTag, endTag4.type);
        Assert.assertEquals("html", endTag4.tagName);

        Assert.assertEquals(0, errors.size());
    }
}