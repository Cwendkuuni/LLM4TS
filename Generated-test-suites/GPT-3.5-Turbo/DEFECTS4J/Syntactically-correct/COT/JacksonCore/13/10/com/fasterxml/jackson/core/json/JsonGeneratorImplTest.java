import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        IOContext ioContext = mock(IOContext.class);
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec) {
            @Override
            public JsonGenerator enable(Feature f) {
                return null;
            }

            @Override
            protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures) {
            }

            @Override
            public JsonGenerator setHighestNonEscapedChar(int charCode) {
                return null;
            }

            @Override
            public int getHighestEscapedChar() {
                return 0;
            }

            @Override
            public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
                return null;
            }

            @Override
            public CharacterEscapes getCharacterEscapes() {
                return null;
            }

            @Override
            public JsonGenerator setRootValueSeparator(SerializableString sep) {
                return null;
            }

            @Override
            public Version version() {
                return null;
            }

            @Override
            public void writeStringField(String fieldName, String value) throws IOException {
            }
        };
    }

    @Test
    public void testConstructor() {
        IOContext ioContext = mock(IOContext.class);
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertEquals(ioContext, jsonGenerator._ioContext);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testEnable() {
        JsonGenerator jsonGenerator = jsonGeneratorImpl.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertNull(jsonGenerator);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        jsonGeneratorImpl._checkStdFeatureChanges(0, 0);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGenerator jsonGenerator = jsonGeneratorImpl.setHighestNonEscapedChar(100);
        assertNull(jsonGenerator);
        assertEquals(100, jsonGeneratorImpl._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = Mockito.mock(CharacterEscapes.class);
        JsonGenerator jsonGenerator = jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertNull(jsonGenerator);
        verify(characterEscapes, times(1)).getEscapeCodesForAscii();
    }

    @Test
    public void testGetCharacterEscapes() {
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString serializableString = Mockito.mock(SerializableString.class);
        JsonGenerator jsonGenerator = jsonGeneratorImpl.setRootValueSeparator(serializableString);
        assertNull(jsonGenerator);
        assertEquals(serializableString, jsonGeneratorImpl._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        assertNull(jsonGeneratorImpl.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        jsonGeneratorImpl.writeStringField("fieldName", "value");
        Mockito.verify(jsonGenerator, times(1)).writeStringField("fieldName", "value");
    }
}