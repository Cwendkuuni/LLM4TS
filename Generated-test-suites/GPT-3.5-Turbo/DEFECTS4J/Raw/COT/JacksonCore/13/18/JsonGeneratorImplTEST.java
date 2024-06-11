import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private int features;
    private ObjectCodec codec;

    @Before
    public void setUp() {
        ioContext = mock(IOContext.class);
        features = 0;
        codec = mock(ObjectCodec.class);
        jsonGenerator = new JsonGeneratorImpl(ioContext, features, codec) {
            @Override
            public Version version() {
                return VersionUtil.versionFor(getClass());
            }
        };
    }

    @Test
    public void testEnable() {
        JsonGenerator result = jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        
        assertEquals(jsonGenerator, result);
        assertEquals(false, jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testCheckStdFeatureChanges() {
        jsonGenerator._checkStdFeatureChanges(0, 0);
        
        assertEquals(true, jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGenerator result = jsonGenerator.setHighestNonEscapedChar(127);
        
        assertEquals(jsonGenerator, result);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        int result = jsonGenerator.getHighestEscapedChar();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = mock(CharacterEscapes.class);
        JsonGenerator result = jsonGenerator.setCharacterEscapes(esc);
        
        assertEquals(jsonGenerator, result);
        assertEquals(esc, jsonGenerator._characterEscapes);
    }
    
    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes result = jsonGenerator.getCharacterEscapes();
        
        assertEquals(null, result);
    }
    
    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = mock(SerializableString.class);
        JsonGenerator result = jsonGenerator.setRootValueSeparator(sep);
        
        assertEquals(jsonGenerator, result);
        assertEquals(sep, jsonGenerator._rootValueSeparator);
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";
        
        jsonGenerator.writeStringField(fieldName, value);
        
        verify(jsonGenerator).writeFieldName(fieldName);
        verify(jsonGenerator).writeString(value);
    }
}