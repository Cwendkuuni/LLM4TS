package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setup() {
        jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null) {
            public void writeFieldName(String fieldName) throws IOException {}
            public void writeString(String value) throws IOException {}
        };
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";

        jsonGeneratorImpl.writeStringField(fieldName, value);

        verify(jsonGeneratorImpl, times(1)).writeFieldName(fieldName);
        verify(jsonGeneratorImpl, times(1)).writeString(value);
    }
}