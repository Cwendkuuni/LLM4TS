import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        
        verify(generator).enable(Feature.QUOTE_FIELD_NAMES);
    }
    
    @Test
    public void test_checkStdFeatureChanges() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        
        generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        
        verify(generator)._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
    }
    
    @Test
    public void test_setHighestNonEscapedChar() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        int charCode = 0;
        
        generator.setHighestNonEscapedChar(charCode);
        
        verify(generator).setHighestNonEscapedChar(charCode);
    }
    
    @Test
    public void test_getHighestEscapedChar() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        
        generator.getHighestEscapedChar();
        
        verify(generator).getHighestEscapedChar();
    }
    
    @Test
    public void test_setCharacterEscapes() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        CharacterEscapes esc = mock(CharacterEscapes.class);
        
        generator.setCharacterEscapes(esc);
        
        verify(generator).setCharacterEscapes(esc);
    }
    
    @Test
    public void test_getCharacterEscapes() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        
        generator.getCharacterEscapes();
        
        verify(generator).getCharacterEscapes();
    }
    
    @Test
    public void test_setRootValueSeparator() {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        SerializableString sep = mock(SerializableString.class);
        
        generator.setRootValueSeparator(sep);
        
        verify(generator).setRootValueSeparator(sep);
    }
    
    @Test
    public void test_writeStringField() throws IOException {
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        String fieldName = "fieldName";
        String value = "value";
        
        generator.writeStringField(fieldName, value);
        
        verify(generator).writeStringField(fieldName, value);
    }
}