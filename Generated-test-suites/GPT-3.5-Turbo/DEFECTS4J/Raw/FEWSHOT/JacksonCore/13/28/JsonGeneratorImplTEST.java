import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import java.io.IOException;

public class JsonGeneratorImplTest {
    
    @Mock
    private IOContext ioContext;
    
    @Mock
    private ObjectCodec codec;
    
    @Test
    public void testConstructor() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        assertEquals(ioContext, generator._ioContext);
        assertEquals(0, generator.getFeatureMask());
        assertNull(generator._characterEscapes);
    }
    
    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        generator._checkStdFeatureChanges(1, 1);
        
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        generator.setHighestNonEscapedChar(127);
        
        assertEquals(127, generator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        CharacterEscapes esc = new CharacterEscapes() {
            public int[] getEscapeCodesForAscii() {
                return null;
            }
        };
        
        generator.setCharacterEscapes(esc);
        
        assertNull(generator._characterEscapes);
    }
    
    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        SerializableString sep = () -> " ";
        
        generator.setRootValueSeparator(sep);
        
        assertEquals(sep, generator._rootValueSeparator);
    }
    
    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        assertNotNull(generator.version());
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, codec);
        
        generator.writeStringField("fieldName", "value");
        
        assertEquals("\"fieldName\"", generator.getFeatureMask());
        assertEquals("\"value\"", generator.getFeatureMask());
    }
}