
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.characterEscapes.CharacterEscapes;
import com.fasterxml.jackson.core.json.JsonGeneratorImpl;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class JsonGeneratorImplTest {

    @Mock
    private SerializableString mockSerializableString;
    @Mock
    private CharacterEscapes mockCharacterEscapes;

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            @Override
            public void flush() throws IOException {
                // do nothing
            }

            @Override
            protected void _releaseBuffers() {
                // do nothing
            }
        };
    }

    @Test
    public void testEnable() {
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        jsonGenerator._checkStdFeatureChanges(0, 0);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void test_setHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void test_setCharacterEscapes() {
        when(mockCharacterEscapes.getEscapeCodesForAscii()).thenReturn(new int[]{1, 2, 3});
        jsonGenerator.setCharacterEscapes(mockCharacterEscapes);
        assertArrayEquals(new int[]{1, 2, 3}, jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        for (int i = 0; i < jsonGenerator._outputEscapes.length; i++) {
            assertEquals(JsonGeneratorImpl.sOutputEscapes[i], jsonGenerator._outputEscapes[i]);
        }
    }

    @Test
    public void test_setRootValueSeparator() {
        jsonGenerator.setRootValueSeparator(mockSerializableString);
        assertSame(mockSerializableString, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void test_version() {
       assertNotNull(jsonGenerator.version());
    }
}

