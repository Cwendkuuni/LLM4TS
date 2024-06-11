import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TokeniserTest {

    private CharacterReader characterReader;
    private ParseErrorList parseErrorList;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', '\n'}));
        parseErrorList = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, parseErrorList);
    }

    @Test
    public void testEmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testNormalOperation() {
        characterReader.input(" <html>\n");
        tokeniser.read(); // <
        tokeniser.read(); // HTML
        tokeniser.read(); // >
        tokeniser.emit(tokeniser.read()); // <html>

        tokeniser.transition(TokeniserState.InsideHTML);
        tokeniser.read(); // <
        tokeniser.read(); // /html
        tokeniser.read(); // >
        tokeniser.emit(tokeniser.read()); // </html>

        assertEquals(0, parseErrorList.size());
    }

    @Test
    public void testSelfClosingFlag() {
        characterReader.input(" <img src='image.jpg' />\n");

        tokeniser.read(); // <
        tokeniser.read(); // IMG
        tokeniser.read(); // SRC
        tokeniser.read(); // =
        tokeniser.read(); // "image.jpg"
        tokeniser.read(); // >

        tokeniser.emit(tokeniser.read()); // <img src="image.jpg">
        tokeniser.acknowledgeSelfClosingFlag();

        assertEquals(0, parseErrorList.size());
    }

    @Test
    public void testErrorHandling() {
        characterReader.input(" <h1<a> \n");

        tokeniser.read(); // <
        tokeniser.read(); // H1
        tokeniser.error(TokeniserState.InsideHTML);

        assertEquals(1, parseErrorList.size());
        assertEquals("Unexpected character '<' in input state [InsideHTML]", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testEOF() {
        characterReader.input(" <html>\n");
        tokeniser.read(); // <
        tokeniser.read(); // HTML
        tokeniser.read(); // >
        tokeniser.emit(tokeniser.read()); // <html>

        tokeniser.transition(TokeniserState.InsideHTML);

        characterReader.isEmpty(); // consume EOF
        tokeniser.read();

        tokeniser.eofError(TokeniserState.InsideHTML);

        assertEquals(1, parseErrorList.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [InsideHTML]", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testCharacterReference() {
        characterReader.input(" &#x1F345; \n");

        Token token = tokeniser.read();
        tokeniser.emit(token);

        tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uD835\uDD34', token.getData().charAt(0));
        assertEquals(0, parseErrorList.size());
    }

    @Test
    public void testCharacterReferenceWithAdditionalAllowedCharacter() {
        characterReader.input(" &#x1F345;&#xA0; \n");

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Character c = tokeniser.consumeCharacterReference('\u00A0', true);
        assertEquals('\u00A0', c.charValue());

        assertEquals(0, parseErrorList.size());
    }

    @Test
    public void testCharacterReferenceWithInvalidCharacter() {
        characterReader.input(" &#x1F000; \n");

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("character outside of valid range", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceWithInvalidSequence() {
        characterReader.input(" &#xyz; \n");

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("Invalid character reference: invalid hexadecimal number", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceWithMissingSemicolon() {
        characterReader.input(" &#x1F345; \n<"); // don't consume last character

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("character reference with no numerals", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testNamedCharacterReference() {
        characterReader.input(" &copy; \n");

        tokeniser.consumeCharacterReference(null, false);

        assertEquals('\u00A9', tokeniser.emitPending().getData().charAt(0));
        assertEquals(0, parseErrorList.size());
    }

    @Test
    public void testNamedCharacterReferenceWithMissingSemicolon() {
        characterReader.input(" &copy; \n<"); // don't consume last character

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("missing semicolon", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testNamedCharacterReferenceWithInvalidName() {
        characterReader.input(" &xyz; \n");

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("invalid named referenece 'xyz'", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testNamedCharacterReferenceWithExtraCharacter() {
        characterReader.input(" &copy;a \n");

        tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, parseErrorList.size());
        assertEquals("don't want that to match", parseErrorList.get(0).getMessage());
    }

    @Test
    public void testStartTagWithExtraCharacter() {
        characterReader.input(" <input type='text' name='name' value='value' /><\n");

        tokeniser.read(); // <
        tokeniser.read(); // INPUT
        tokeniser.read(); // TYPE
        tokeniser.read(); // =
        tokeniser.read(); // "text"
        tokeniser.read(); // NAME
        tokeniser.read(); // =
        tokeniser.read(); // "name"
        tokeniser.read(); // VALUE
        tokeniser.read(); // =
        tokeniser.read(); // "value"
        tokeniser.read(); // /

        tokeniser.emit(tokeniser.read()); // <input type="text" name="name" value="value">
        tokeniser.acknowledgeSelfClosingFlag();

        tokeniser.read(); // <

        tokeniser.error(TokeniserState.InsideHTML);

        assertEquals(1, parseErrorList.size());
        assertEquals("Attributes incorrectly present on end tag", parseErrorList.get(0).getMessage());
    }
}