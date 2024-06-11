import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

    @Mock
    private IOContext mockIOContext;
    @Mock
    private ObjectCodec mockCodec;
    @Mock
    private CharacterEscapes mockCharacterEscapes;
    @Mock
    private SerializableString mockSerializableString;

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonGenerator = new JsonGeneratorImpl(mockIOContext, 0, mockCodec);
    }

    @Test
    public void testEnable() {
        JsonGenerator result = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(result == jsonGenerator);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        jsonGenerator._checkStdFeatureChanges(0, 0);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGenerator result = jsonGenerator.setHighestNonEscapedChar(100);
        assertTrue(result == jsonGenerator);
        assertEquals(100, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        when(mockCharacterEscapes.getEscapeCodesForAscii()).thenReturn(new int[0]);
        JsonGenerator result = jsonGenerator.setCharacterEscapes(mockCharacterEscapes);
        assertTrue(result == jsonGenerator);
        assertArrayEquals(new int[0], jsonGenerator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        jsonGenerator.setCharacterEscapes(mockCharacterEscapes);
        assertTrue(mockCharacterEscapes == jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGenerator result = jsonGenerator.setRootValueSeparator(mockSerializableString);
        assertTrue(result == jsonGenerator);
        assertEquals(mockSerializableString, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        assertNotNull(jsonGenerator.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";

        jsonGenerator.writeFieldName(fieldName);
        jsonGenerator.writeString(value);

        verify(jsonGenerator).writeFieldName(fieldName);
        verify(jsonGenerator).writeString(value);
    }
}