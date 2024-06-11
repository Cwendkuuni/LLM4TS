package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null) {
            @Override
            public void writeFieldName(String name) throws IOException {
            }

            @Override
            public void writeString(String text) throws IOException {
            }
        };
    }

    @Test
    public void testWriteStringField() throws IOException {
        // Typical use case
        jsonGeneratorImpl.writeStringField("fieldName1", "value1");
        
        // Edge case - empty field name
        jsonGeneratorImpl.writeStringField("", "value2");
        
        // Edge case - empty field value
        jsonGeneratorImpl.writeStringField("fieldName3", "");
        
        // Error scenario - field name is null
        assertThrows(IOException.class, () -> jsonGeneratorImpl.writeStringField(null, "value4"));
        
        // Error scenario - field value is null
        assertThrows(IOException.class, () -> jsonGeneratorImpl.writeStringField("fieldName5", null));
    }

}