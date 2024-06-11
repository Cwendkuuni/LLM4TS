
package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
    
    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        
        // typical use case
        StringWriter stringWriter = new StringWriter();
        jsonGenerator.setCodec(new ObjectMapper());
        jsonGenerator.setHighestNonEscapedChar(0);
        jsonGenerator.setCharacterEscapes(CharacterEscapes.ESCAPE_NONE);
        jsonGenerator.setRootValueSeparator(new SerializedString("/"));
        
        jsonGenerator.writeStringField("hello", "world");
        jsonGenerator.flush();
        
        assertEquals("{\"hello\":\"world\"}", stringWriter.toString());
        
        // edge case - empty field name and null value
        StringWriter emptyStringWriter = new StringWriter();
        jsonGenerator.writeStringField("", null);
        jsonGenerator.flush();
        
        assertEquals("{\"\":\"null\"}", emptyStringWriter.toString());
        
        // error scenario - field name contains special characters
        StringWriter specialCharacterStringWriter = new StringWriter();
        jsonGenerator.writeStringField("special!@#$%^&*()_+}{|\":?><", "value");
        jsonGenerator.flush();
        
        assertEquals("{\"special!@#$%^&*()_+}{|\":?><\":\"value\"}", specialCharacterStringWriter.toString());
    }
}
