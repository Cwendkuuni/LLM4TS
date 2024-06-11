package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private TypeDeserializer typeDeserializer;

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }
}