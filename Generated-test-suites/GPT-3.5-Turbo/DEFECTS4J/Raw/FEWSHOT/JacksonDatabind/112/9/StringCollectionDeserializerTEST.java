package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class StringCollectionDeserializerTest {
    private StringCollectionDeserializer deserializer;
    private DeserializationContext ctxt;
    private TypeDeserializer typeDeserializer;
    private BeanProperty beanProperty;

    @Before
    public void setUp() {
        JsonDeserializer<Object> delegateDeser = null;
        NullValueProvider nuller = null;
        Boolean unwrapSingle = null;
        ValueInstantiator valueInstantiator = null;
        deserializer = new StringCollectionDeserializer(null, valueInstantiator, delegateDeser, null, nuller, unwrapSingle);

        ctxt = null;
        typeDeserializer = null;
        beanProperty = null;
    }

    @Test
    public void testDeserialize() throws IOException {
        MappingIterator<Collection<String>> it = new MappingIterator<Collection<String>>(null, ctxt, deserializer, true, null, typeDeserializer, null);
        Collection<String> result = deserializer.deserialize(null, ctxt, null);
        assertTrue(result instanceof Collection);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");

        assertEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testHandleNonArray() throws IOException {
        deserializer.handleNonArray(null, ctxt, null);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<Object> valueDeser = null;
        NullValueProvider nuller = null;
        Boolean unwrapSingle = null;
        Boolean expected = deserializer.isCachable();

        JsonDeserializer<Object> contextualValueDeserializer = deserializer.createContextual(ctxt, beanProperty);
        assertTrue(expected == contextualValueDeserializer.isCachable());

        NullValueProvider result = deserializer.findContentNullProvider(ctxt, beanProperty, valueDeser);
        assertEquals(nuller, result);

        Boolean unwrapSingleResult = deserializer.findFormatFeature(ctxt, beanProperty, Collection.class, null);
        assertEquals(unwrapSingle, unwrapSingleResult);

        JsonDeserializer<Object> valueDeserResult = deserializer.findConvertingContentDeserializer(ctxt, beanProperty, valueDeser);
        assertEquals(valueDeser, valueDeserResult);

        assertTrue(deserializer.isDefaultDeserializer(valueDeser));
    }
}