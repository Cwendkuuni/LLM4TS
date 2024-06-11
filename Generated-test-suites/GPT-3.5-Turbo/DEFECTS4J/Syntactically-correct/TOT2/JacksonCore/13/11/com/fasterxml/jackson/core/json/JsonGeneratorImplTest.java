import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        // Setup
        JsonGeneratorImpl jsonGenerator = mock(JsonGeneratorImpl.class);
        
        // Test
        jsonGenerator.writeStringField("fieldName", "value");
        
        // Verify
        verify(jsonGenerator, times(1)).writeFieldName("fieldName");
        verify(jsonGenerator, times(1)).writeString("value");
    }
    
    @Test
    public void testEnableQuoteFieldNames() {
        // Setup
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        
        // Test
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        
        // Verify
        assertFalse(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        // Setup
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        
        // Test
        jsonGenerator.setHighestNonEscapedChar(100);
        
        // Verify
        assertEquals(100, jsonGenerator._maximumNonEscapedChar);
    }
    
    @Test
    public void testSetCharacterEscapes() {
        // Setup
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        CharacterEscapes characterEscapes = mock(CharacterEscapes.class);

        // Test
        jsonGenerator.setCharacterEscapes(characterEscapes);

        // Verify
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
        verify(characterEscapes, times(1)).getEscapeCodesForAscii();
    }
    
    @Test
    public void testSetRootValueSeparator() {
        // Setup
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        SerializableString separator = mock(SerializableString.class);
        
        // Test
        jsonGenerator.setRootValueSeparator(separator);
        
        // Verify
        assertEquals(separator, jsonGenerator._rootValueSeparator);
    }
    
}