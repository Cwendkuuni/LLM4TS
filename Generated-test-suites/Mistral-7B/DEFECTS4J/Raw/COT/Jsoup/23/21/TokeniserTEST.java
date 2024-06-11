import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader(""));
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_NoTokens() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testRead_Character() {
        reader.setCurrent('a');
        tokeniser.emit(' ');
        tokeniser.emit('a');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.data);

        reader.setCurrent(' ');
        tokeniser.read();
        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.data);
    }

    @Test
    public void testRead_StartTag() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('<');
        tokeniser.emit('p');
        tokeniser.emit('>');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("p", token.tagName);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testRead_EndTag() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('/');
        tokeniser.emit('p');
        tokeniser.emit('>');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("p", token.tagName);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnexpectedEndTag() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('/');
        tokeniser.emit('p');
        tokeniser.emit('>');

        tokeniser.read(); // read StartTag
        tokeniser.acknowledgeSelfClosingFlag();

        tokeniser.read(); // read Character

        tokeniser.read(); // read EndTag, but lastStartTag is not p
        tokeniser.isAppropriateEndTagToken();
        Assert.fail();
    }

    @Test
    public void testRead_String() {
        reader.setCurrent('"');
        tokeniser.emit('"');
        tokeniser.emit('a');
        tokeniser.emit('b');
        tokeniser.emit('"');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.String, token.type);
        Assert.assertEquals("ab", token.data);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnmatchedString() {
        reader.setCurrent('"');
        tokeniser.emit('"');
        tokeniser.emit('a');
        tokeniser.emit('b');
        tokeniser.emit('/');
        tokeniser.emit('>');

        tokeniser.read(); // read StartTag
        tokeniser.acknowledgeSelfClosingFlag();

        tokeniser.read(); // read Character

        tokeniser.read(); // read EndTag
        tokeniser.isAppropriateEndTagToken();
        Assert.fail();
    }

    @Test
    public void testRead_Comment() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('/');
        tokeniser.emit('*');
        tokeniser.emit('\n');
        tokeniser.emit('\n');
        tokeniser.emit('*');
        tokeniser.emit('/');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertNotNull(token.data);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testRead_Doctype() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('<!');
        tokeniser.emit('d');
        tokeniser.emit('o');
        tokeniser.emit('c');
        tokeniser.emit('t');
        tokeniser.emit('y');
        tokeniser.emit('p');
        tokeniser.emit('>');

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertNotNull(token.data);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnexpectedDoctype() {
        reader.setCurrent('<');
        tokeniser.emit(' ');
        tokeniser.emit('<!');
        tokeniser.emit('d');
        tokeniser.emit('o');
        tokeniser.emit('c');
        tokeniser.emit('t');
        tokeniser.emit('y');
        tokeniser.emit('p');
        tokeniser.emit('>');

        tokeniser.read(); // read Doctype

        tokeniser.read(); // read Character

        tokeniser.read(); // read EndTag
        tokeniser.isAppropriateEndTagToken();
        Assert.fail();
    }

    @Test
    public void testError_UnexpectedCharacter() {
        reader.setCurrent('a');
        tokeniser.error(TokeniserState.Data);

        Token token = tokeniser.read();
        Assert.assertNull(token);

        Assert.assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        Assert.assertEquals("Unexpected character 'a' in input state [Data]", parseError.getMessage());
        Assert.assertEquals(reader.pos(), parseError.getPosition());
    }

    @Test
    public void testError_EndOfFile() {
        reader.setCurrent('a');
        reader.advance();
        reader.advance();

        tokeniser.eofError(TokeniserState.Data);

        Token token = tokeniser.read();
        Assert.assertNull(token);

        Assert.assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseError.getMessage());
        Assert.assertEquals(reader.pos(), parseError.getPosition());
    }

    @Test
    public void testCharacterReference_Numbered() {
        reader.setCurrent('#');
        tokeniser.emit(' ');
        tokeniser.emit('X');
        reader.mark();
        reader.consumeHexSequence();
        reader.unconsume();
        reader.consume(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('\u0000', character.toInt());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testCharacterReference_Named() {
        reader.setCurrent('&');
        tokeniser.emit(' ');
        tokeniser.consumeLetterSequence();
        reader.mark();
        reader.consume(';');
        reader.unconsume();

        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('\u0000', character.toInt());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testCharacterReference_InvalidNumbered() {
        reader.setCurrent('#');
        tokeniser.emit(' ');
        tokeniser.emit('X');
        reader.mark();
        reader.consumeDigitSequence();
        reader.unconsume();
        reader.consume(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals(Tokeniser.replacementChar, character.toChar());

        Assert.assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        Assert.assertEquals("Character outside of valid range", parseError.getMessage());
        Assert.assertEquals(reader.pos(), parseError.getPosition());
    }

    @Test
    public void testCharacterReference_InvalidNamed() {
        reader.setCurrent('&');
        tokeniser.emit(' ');
        tokeniser.consumeLetterSequence();
        reader.mark();
        reader.consume(';');
        reader.unconsume();

        reader.setCurrent('>');
        tokeniser.consumeCharacterReference(null, false);

        Assert.assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        Assert.assertEquals("Invalid named reference '<&quo>', invalid semi-colon", parseError.getMessage());
        Assert.assertEquals(reader.pos(), parseError.getPosition());
    }

    @Test
    public void testCharacterReference_InAttribute() {
        reader.setCurrent('&');
        tokeniser.emit(' ');
        tokeniser.consumeLetterSequence();
        reader.mark();
        reader.consume(';');
        reader.unconsume();

        reader.setCurrent(' ');
        tokeniser.consumeCharacterReference(null, true);

        Assert.assertEquals(0, errors.size());
    }
}