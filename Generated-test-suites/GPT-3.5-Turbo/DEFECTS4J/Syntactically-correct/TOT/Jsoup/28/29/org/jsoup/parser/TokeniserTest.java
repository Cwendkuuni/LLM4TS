import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead_NoEmitPending_CharacterTokenEmit() {
        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testRead_EmitPending_CharacterTokenEmit() {
        tokeniser.emit("test");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_StartTagToken_EmitPending() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new ArrayList<>();
        startTag.attributes.add(new Attribute("attribute1", "value1"));
        tokeniser.emit(startTag);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("attribute1", ((Token.StartTag) token).attributes.get(0).getKey());
        assertEquals("value1", ((Token.StartTag) token).attributes.get(0).getValue());
    }

    @Test
    public void testEmit_EndTagTokenAndEmitPendingAttributes_Error() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes = new ArrayList<>();
        endTag.attributes.add(new Attribute("attribute1", "value1"));
        tokeniser.emit(endTag);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.EndTag, token.type);
        assertNull(((Token.EndTag) token).attributes);
    }

    @Test
    public void testEmit_CharacterData_EmitPending() {
        tokeniser.emit("test");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testConsumeCharacterReference_AdditionalAllowedCharacterNull_NoCharacterReferenceConsumed() {
        tokeniser = new Tokeniser(new CharacterReader(Character.toString('\u0009')), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_AdditionalAllowedCharacterNotNullButSame_NoCharacterReferenceConsumed() {
        tokeniser = new Tokeniser(new CharacterReader(Character.toString('\u0009')), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference('\u0009', false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNumberedSemicolonMissing_ParseError() {
        tokeniser = new Tokeniser(new CharacterReader("&#65"), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', (char) character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNumberedInvalidCharacterRange_ParseError() {
        tokeniser = new Tokeniser(new CharacterReader("&#7"), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', (char) character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNumberedInvalidFormatException_ParseError() {
        tokeniser = new Tokeniser(new CharacterReader("&#xG"), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', (char) character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNumbered_Success() {
        tokeniser = new Tokeniser(new CharacterReader("&#65;"), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('A', (char) character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNamedIllegalCharacter_ParseError() {
        tokeniser = new Tokeniser(new CharacterReader("&abcdef;" ), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNamedIllegalCharacterButEndsWithSemicolon_Success() {
        tokeniser = new Tokeniser(new CharacterReader("&amp;" ), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('&', (char) character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNamed_EqualsFollows_ReturnNull() {
        tokeniser = new Tokeniser(new CharacterReader("&amp="), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_EncodedReferenceNamed_AlphanumericCharactersFollow_Success() {
        tokeniser = new Tokeniser(new CharacterReader("&ampA"), new ParseErrorList());

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }
}