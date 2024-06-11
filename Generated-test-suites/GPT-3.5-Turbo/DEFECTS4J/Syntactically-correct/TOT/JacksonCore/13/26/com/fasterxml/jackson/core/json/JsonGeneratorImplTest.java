import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        jsonGenerator = Mockito.mock(JsonGeneratorImpl.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testEnableFeature_WhenQuoteFieldNamesDisabled_ThenCfgUnqNamesSetToTrue() {
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar_WhenCharCodeIsNegative_ThenMaximumNonEscapedCharSetToZero() {
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testSetCharacterEscapes_WhenCharacterEscapesIsNull_ThenOutputEscapesSetToDefault() {
        jsonGenerator.setCharacterEscapes(null);
        assertEquals(jsonGenerator.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes_WhenCalled_ThenReturnCharacterEscapes() {
        CharacterEscapes characterEscapes = Mockito.mock(CharacterEscapes.class);
        jsonGenerator._characterEscapes = characterEscapes;
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator_WhenSeparatorIsSet_ThenRootValueSeparatorSet() {
        jsonGenerator.setRootValueSeparator(null);
        assertEquals(null, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testWriteStringField_WhenFieldNameAndValueAreProvided_ThenFieldNameAndValueWritten() throws IOException {
        String fieldName = "test";
        String value = "value";
        jsonGenerator.writeFieldName(fieldName);
        jsonGenerator.writeString(value);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeFieldName(fieldName);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeString(value);
    }
}