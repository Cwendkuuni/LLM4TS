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
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setup() {
        errors = new ArrayList<>();
        StringReader reader = new StringReader("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_DataState_NormalCharacter() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("H", token.text());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.text());

        // continue testing until end of document
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_DataState_SelfClosingTag() {
        tokeniser.reader.consume('<');
        tokeniser.reader.consume('i');
        tokeniser.reader.consume('m');
        tokeniser.reader.consume('g');
        tokeniser.reader.consume(' ');
        tokeniser.reader.consume('/');
        tokeniser.reader.consume('p');
        tokeniser.reader.consume('>');

        tokeniser.read(); // should throw an exception as selfClosingFlagAcknowledged is not acknowledged
    }

    @Test
    public void testRead_DataState_CharacterReference() {
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('D');

        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals((char) 0xF0DD, character); // UTF-16 codepoint of "FFFD"

        tokeniser.reader.consume(';');

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("D", token.text());
    }

    @Test
    public void testRead_DataState_CharacterReferenceInAttribute() {
        tokeniser.reader.consume('<');
        tokeniser.reader.consume('i');
        tokeniser.reader.consume('m');
        tokeniser.reader.consume('g');
        tokeniser.reader.consume(' ');
        tokeniser.reader.consume('c');
        tokeniser.reader.consume('l');
        tokeniser.reader.consume('a');
        tokeniser.reader.consume('s');
        tokeniser.reader.consume('s');
        tokeniser.reader.consume('=');
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('D');
        tokeniser.reader.consume(' ');
        tokeniser.reader.consume('>');

        character = tokeniser.consumeCharacterReference(null, true);
        Assert.assertEquals((char) 0xF0DD, character); // UTF-16 codepoint of "FFFD"

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("s", token.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_DataState_InvalidCharacterReference() {
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('F');
        tokeniser.reader.consume('D');
        tokeniser.reader.consume('x'); // invalid character

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.reader.consume(';');

        tokeniser.read(); // should throw an exception
    }

    @Test
    public void testRead_ScriptState_ScriptToken() {
        // Consume script opening tag
        tokeniser.reader.consume('<');
        tokeniser.reader.consume('s');
        tokeniser.reader.consume('c');
        tokeniser.reader.consume('r');
        tokeniser.reader.consume('i');
        tokeniser.reader.consume('p');
        tokeniser.reader.consume('t');
        tokeniser.reader.consume('>');

        // Consume script data
        tokeniser.reader.consume('S');
        tokeniser.reader.consume('C');
        tokeniser.reader.consume('R');
        tokeniser.reader.consume('I');
        tokeniser.reader.consume('P');
        tokeniser.reader.consume('T');

        token = tokeniser.read();
        tokeniser.createTempBuffer();
        tokeniser.emit(token);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Script, token.type);
        Assert.assertNull(token.text());

        String scriptContent = tokeniser.dataBuffer.toString();
        Assert.assertEquals("Script", scriptContent.substring(0, 6));
        Assert.assertEquals("data", scriptContent.substring(7, 11));
        Assert.assertEquals("type", scriptContent.substring(11, 16));
        Assert.assertEquals("text/javascript", scriptContent.substring(16, 25));
        Assert.assertEquals("{\"src\":\"test.js\"}", scriptContent.substring(25));

        // Consume script closing tag
        tokeniser.reader.consume('<');
        tokeniser.reader.consume('/');
        tokeniser.reader.consume('s');
        tokeniser.reader.consume('c');
        tokeniser.reader.consume('r');
        tokeniser.reader.consume('i');
        tokeniser.reader.consume('p');
        tokeniser.reader.consume('t');
        tokeniser.reader.consume('>');

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("script", token.tagName);
    }

    @Test
    public void testErrorHandling_UnexpectedCharacter() {
        tokeniser.reader.consume('&'); // invalid character

        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals(TokeniserState.Data, error.inputState);
        Assert.assertEquals("Unexpected character '&' in input state [Data]", error.message);
    }

    @Test
    public void testErrorHandling_UnexpectedEndOfFile() {
        tokeniser.reader.consume('>'); // consume end tag

        tokeniser.reader.consume(10000); // consume unexpected number of characters

        token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        Assert.assertEquals(TokeniserState.Data, error.inputState);
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.message);
    }
}