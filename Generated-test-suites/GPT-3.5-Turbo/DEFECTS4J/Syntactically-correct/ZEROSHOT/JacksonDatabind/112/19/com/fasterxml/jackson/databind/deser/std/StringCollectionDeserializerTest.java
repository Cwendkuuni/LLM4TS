package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializer typeDeserializer = null;
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }
}