import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        jsonGenerator = new JsonGeneratorImpl(Mockito.mock(IOContext.class), 0, Mockito.mock(ObjectCodec.class));
    }

    @Test
    public void testEnableMethod_withQuoteFieldNamesFeatureEnabled_shouldSetCfgUnqNamesToFalse() {
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testEnableMethod_withQuoteFieldNamesFeatureDisabled_shouldNotChangeCfgUnqNames() {
        jsonGenerator._cfgUnqNames = true;
        jsonGenerator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedCharMethod_withPositiveCharCode_shouldSetMaximumNonEscapedCharToCharCode() {
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testSetHighestNonEscapedCharMethod_withNegativeCharCode_shouldSetMaximumNonEscapedCharToZero() {
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedCharMethod_shouldReturnMaximumNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapesMethod_withNullCharacterEscapes_shouldSetOutputEscapesToDefaultEscapes() {
        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetCharacterEscapesMethod_withNonNullCharacterEscapes_shouldSetOutputEscapesToCharacterEscapes() {
        CharacterEscapes characterEscapes = Mockito.mock(CharacterEscapes.class);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertArrayEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapesMethod_shouldReturnCharacterEscapes() {
        CharacterEscapes characterEscapes = Mockito.mock(CharacterEscapes.class);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparatorMethod_shouldSetRootValueSeparator() {
        SerializableString separator = Mockito.mock(SerializableString.class);
        jsonGenerator.setRootValueSeparator(separator);
        assertEquals(separator, jsonGenerator._rootValueSeparator);
    }
}