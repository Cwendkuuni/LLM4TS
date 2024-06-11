package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.*;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    private JavaType collectionType;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private Boolean unwrapSingle;
    private Collection<Object> result;
    private JsonParser p;
    private DeserializationContext ctxt;
    private JsonDeserializer<Object> valueDeser;
    private TypeDeserializer typeDeser;

    @Before
    public void setUp() {
        collectionType = mock(JavaType.class);
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        unwrapSingle = true;

        collectionDeserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        
        result = new ArrayList<>();
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        valueDeser = mock(JsonDeserializer.class);
        typeDeser = mock(TypeDeserializer.class);
    }

    @Test
    public void testIsCachable() {
        assertEquals(true, collectionDeserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);   

        assertEquals(collectionDeserializer,
                collectionDeserializer.createContextual(ctxt, property));

    }

    @Test
    public void testGetContentType() {
        collectionDeserializer.getContentType();
        verify(collectionType, times(1)).getContentType();
    }

    @Test
    public void testGetContentDeserializer() {
        collectionDeserializer.getContentDeserializer();
        verify(valueDeserializer, times(1)).getNullValue(ctxt);
    }    

    @Test
    public void testDeserialize() throws IOException {
        when(p.isExpectedStartArrayToken()).thenReturn(true);
        when(p.nextToken()).thenReturn(JsonToken.END_ARRAY);

        assertEquals(result, collectionDeserializer.deserialize(p, ctxt));

    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonToken t = mock(JsonToken.class);
        when(p.getCurrentToken()).thenReturn(t);
        when(typeDeser.deserializeTypedFromArray(jp, ctxt)).thenReturn("value");

        assertEquals("value", collectionDeserializer.deserializeWithType(p, ctxt, typeDeser));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonToken t = mock(JsonToken.class);
        when(p.getCurrentToken()).thenReturn(t);
        when(_unwrapSingle.equals(Boolean.TRUE)).thenReturn(true);
        when(valueDeser.deserialize(p, ctxt)).thenReturn("value");

        assertEquals(result, collectionDeserializer.handleNonArray(p, ctxt, result));
    }
}