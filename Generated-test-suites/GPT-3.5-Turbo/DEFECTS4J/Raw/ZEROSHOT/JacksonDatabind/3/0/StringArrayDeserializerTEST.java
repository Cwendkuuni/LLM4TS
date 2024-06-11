package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.*;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        String[] result = stringArrayDeserializer.deserialize(null, null);
        assertArrayEquals(null, result);
        JsonParser jsonParser = new JsonParser(null);
        DeserializationContext deserializationContext = new DeserializationContext(null);
        result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(null, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = new JsonParser(null);
        DeserializationContext deserializationContext = new DeserializationContext(null);
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jsonParser = new JsonParser(null);
        DeserializationContext deserializationContext = new DeserializationContext(null);
        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        assertArrayEquals(null, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext deserializationContext = new DeserializationContext(null);
        BeanProperty property = new BeanProperty();
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);
        assertNotNull(result);
    }

}