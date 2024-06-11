package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StringCollectionDeserializerTest {
    
    private StringCollectionDeserializer stringCollectionDeserializer;
    
    @Mock
    private JsonDeserializer<String> valueDeserializer;
    
    @Mock
    private ValueInstantiator valueInstantiator;
    
    @Mock
    private JsonDeserializer<Object> delegateDeserializer;

    @Before
    public void setup() {
        JavaType collectionType = mock(JavaType.class);
        stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator);
        stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeserializer,
                valueDeserializer, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = stringCollectionDeserializer.deserialize(p, ctxt);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(p, ctxt)).thenReturn(null);
        Object result = stringCollectionDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithCustom() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = stringCollectionDeserializer.deserializeUsingCustom(p, ctxt, null, valueDeserializer);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testDeserializeWithNonArray() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = stringCollectionDeserializer.handleNonArray(p, ctxt, null);
        assertNull(result);
    }
}