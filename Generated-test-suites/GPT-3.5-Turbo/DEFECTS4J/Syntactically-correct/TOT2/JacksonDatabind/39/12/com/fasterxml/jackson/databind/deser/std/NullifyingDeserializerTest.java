package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    JsonParser jsonParser;

    @Mock
    DeserializationContext deserializationContext;

    @Mock
    TypeDeserializer typeDeserializer;

    private NullifyingDeserializer nullifier = NullifyingDeserializer.instance;

    @Test
    public void testDeserialize_normalCase_returnsNull() throws Exception {
        Object result = nullifier.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_startArray_returnsNull() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[]");
        Object result = nullifier.deserializeWithType(parser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_startObject_returnsNull() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        Object result = nullifier.deserializeWithType(parser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_fieldName_returnsNull() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("\"field\"");
        Object result = nullifier.deserializeWithType(parser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_otherToken_returnsNull() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("123");
        Object result = nullifier.deserializeWithType(parser, deserializationContext, typeDeserializer);
        assertNull(result);
    }
}