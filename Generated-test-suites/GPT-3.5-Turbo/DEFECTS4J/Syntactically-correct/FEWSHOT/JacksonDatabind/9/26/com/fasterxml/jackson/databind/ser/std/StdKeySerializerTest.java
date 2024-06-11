
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        keySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_Date() throws IOException {
        Date date = new Date();
        keySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
        verify(jsonGenerator, never()).writeFieldName(anyString());
    }

    @Test
    public void testSerialize_OtherObject() throws IOException {
        String value = "test";
        keySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type type = String.class;
        keySerializer.getSchema(serializerProvider, type);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        keySerializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), Mockito.mock(JavaType.class));
    }
}
