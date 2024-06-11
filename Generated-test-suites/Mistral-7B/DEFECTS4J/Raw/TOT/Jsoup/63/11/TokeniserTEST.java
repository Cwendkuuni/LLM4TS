import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    @Test
    public void testReadSimpleText() {
        CharacterReader reader = new CharacterReader("abcdefg");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        for (int i = 0; i < 7; i++) {
            token = tokeniser.read();
            assertEquals(Token.TokenType.Character, token.type);
            assertEquals(Character.toString((char) reader.current()), token.data());
            reader.next();
        }

        token = tokeniser.read(); // consume last token
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src='image.png' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.tagName());
        assertTrue(token.selfClosing());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("img", token.tagName());
        assertNull(token.attributes());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());
        reader.next(); // consume whitespace

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(".", token.data());
        reader.next(); // consume '.'

        token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("<p>&x;</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(codeRef);

        token = tokeniser.read(); // consume '&'
        assertEquals(Token.TokenType.Error, token.type);
        assertNotNull(token.error());
        assertEquals("invalid character reference: invalid character reference 'x'", token.error().getMessage());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
        assertNull(token.attributes());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        assertNull(tokeniser.read());
        assertEquals(1, errors.size());
    }

    @Test
    public void testReadAttributes() {
        CharacterReader reader = new CharacterReader("<p id='par1' class='class1 class2'></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        assertNull(token.selfClosing());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Attribute, token.type);
        assertEquals("id", token.attributeName());
        assertEquals("par1", token.attributeValue());
        reader.next(); // consume '='
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Attribute, token.type);
        assertEquals("class", token.attributeName());
        Token.AttributeList attributeList = token.attributes();
        assertNotNull(attributeList);
        assertEquals(2, attributeList.size());
        assertEquals("class1", attributeList.get(0).value());
        assertEquals("class2", attributeList.get(1).value());
        reader.next(); // consume ' '

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
        assertNull(token.attributes());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        assertNull(tokeniser.read());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidEndTag() {
        CharacterReader reader = new CharacterReader("<p><p");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        reader.next(); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        reader.next(); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertNotNull(token.error());
        assertEquals("Unexpected character '<' in state [Data]", token.error().getMessage());

        tokeniser.error(TokeniserState.Data); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
        assertNull(token.attributes());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertNotNull(token.error());
        assertEquals("Self closing flag not acknowledged", token.error().getMessage());

        tokeniser.acknowledgeSelfClosingFlag();

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        assertNull(tokeniser.read());
        assertEquals(2, errors.size());
    }

    @Test
    public void testReadInvalidCharacter() {
        CharacterReader reader = new CharacterReader("<p>&#x123456;</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
        reader.next(); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{0x1234, 0x56}, codeRef);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
        assertNull(token.attributes());
        reader.next(); // consume '>'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        assertNull(tokeniser.read());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidXML() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.doctypePending().publicId());
        assertEquals("", token.doctypePending().systemId());
        reader.next(); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertNotNull(token.error());
        assertEquals("Unexpected character '<' in state [Doctype]", token.error().getMessage());

        tokeniser.error(TokeniserState.Doctype); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName());
        reader.next(); // consume '<'

        token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
        assertNotNull(token.error());
        assertEquals("Error while parsing DOCTYPE declaration", token.error().getMessage());

        tokeniser.error(TokeniserState.Data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", token.data());

        assertNull(tokeniser.read());
        assertEquals(2, errors.size());
    }
}