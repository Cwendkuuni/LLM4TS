package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {
    private StringCollectionDeserializer stringCollectionDeserializer;
    private JsonDeserializer<String> valueDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private Collection<String> result;
    private JsonParser p;
    private DeserializationContext ctxt;
    private NullValueProvider nullProvider;
    private Boolean unwrapSingle;

    @Before
    public void setUp() {
        valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return null;
            }

            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return null;
            }

            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return null;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return null;
            }

            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return null;
            }
        };
        delegateDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        result = new ArrayList<String>();
        p = null;
        ctxt = null;
        nullProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        unwrapSingle = null;
        stringCollectionDeserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
    }

    @Test
    public void testDeserialize() throws IOException {
        Collection<String> output = stringCollectionDeserializer.deserialize(p, ctxt);
        assertNull(output);
    }

    @Test
    public void testDeserializeWithCustom() throws IOException {
        Collection<String> output = stringCollectionDeserializer.deserializeUsingCustom(p, ctxt, result, valueDeserializer);
        assertEquals(result, output);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        Object output = stringCollectionDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(output);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        Collection<String> output = stringCollectionDeserializer.handleNonArray(p, ctxt, result);
        assertEquals(result, output);
    }
}