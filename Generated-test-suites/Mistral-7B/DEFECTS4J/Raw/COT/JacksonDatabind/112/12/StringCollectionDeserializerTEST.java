package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class StringCollectionDeserializerTest {

    private JsonDeserializer<Object> delegateDeserializer;
    private JsonDeserializer<String> valueDeserializer;
    private JsonParser jsonParserMock;
    private DeserializationContext contextMock;
    private ValueInstantiator valueInstantiatorMock;
    private NullValueProvider nullValueProviderMock;
    private TypeDeserializer typeDeserializerMock;
    private ObjectNode testObjectNode;

    @Before
    public void setUp() {
        delegateDeserializer = mock(JsonDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        jsonParserMock = mock(JsonParser.class);
        contextMock = mock(DeserializationContext.class);
        valueInstantiatorMock = mock(ValueInstantiator.class);
        nullValueProviderMock = mock(NullValueProvider.class);
        typeDeserializerMock = mock(TypeDeserializer.class);
        testObjectNode = mock(ObjectNode.class);
    }

    // Test constructor
    @Test
    public void testStringCollectionDeserializerConstructor() {
        JavaType collectionType = mock(JavaType.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiatorMock, delegateDeserializer, nullValueProviderMock, null);
        Assert.assertEquals(collectionType, stringCollectionDeserializer.getContainerType());
        Assert.assertEquals(valueDeserializer, stringCollectionDeserializer.getValueDeserializer());
        Assert.assertEquals(valueInstantiatorMock, stringCollectionDeserializer.getValueInstantiator());
        Assert.assertEquals(delegateDeserializer, stringCollectionDeserializer.getDelegateDeserializer());
        Assert.assertEquals(nullValueProviderMock, stringCollectionDeserializer.getNullProvider());
        Assert.assertEquals(false, stringCollectionDeserializer.isCachable());
    }

    // Test isCachable method
    @Test
    public void testIsCachable() {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Assert.assertFalse(stringCollectionDeserializer.isCachable());

        StringCollectionDeserializer stringCollectionDeserializerWithValueDeserializer = new StringCollectionDeserializer(null, valueDeserializer, null, null, null, null);
        Assert.assertFalse(stringCollectionDeserializerWithValueDeserializer.isCachable());

        StringCollectionDeserializer stringCollectionDeserializerWithDelegateDeserializer = new StringCollectionDeserializer(null, null, null, delegateDeserializer, null, null);
        Assert.assertFalse(stringCollectionDeserializerWithDelegateDeserializer.isCachable());

        StringCollectionDeserializer stringCollectionDeserializerWithBoth = new StringCollectionDeserializer(null, valueDeserializer, null, delegateDeserializer, null, null);
        Assert.assertFalse(stringCollectionDeserializerWithBoth.isCachable());
    }

    // Test createContextual method
    @Test
    public void testCreateContextual() throws JsonProcessingException {
        when(valueDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn("test");

        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn(testObjectNode);

        when(valueInstantiatorMock.getDelegateCreator()).thenReturn(null);
        when(valueInstantiatorMock.getDelegateType(contextMock.getConfig())).thenReturn(Collection.class);
        when(contextMock.findContextualValueDeserializer(Collection.class, any()))
                .thenReturn(valueDeserializer);
        when(contextMock.findContextualValueDeserializer(any(), any()))
                .thenReturn(valueDeserializer);

        when(contextMock.handleSecondaryContextualization(valueDeserializer, any(), any()))
                .thenReturn(valueDeserializer);

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiatorMock, delegateDeserializer, nullValueProviderMock, null);
        Collection<String> result = stringCollectionDeserializer.createContextual(contextMock, null);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test", result.get(0));

        verify(valueDeserializer, times(1)).deserialize(jsonParserMock, contextMock);
        verifyNoMoreInteractions(delegateDeserializer);
    }

    // Test deserialize method with no delegate deserializer
    @Test
    public void testDeserializeWithNoDelegateDeserializer() throws JsonProcessingException {
        when(valueDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn("test");

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiatorMock, null, nullValueProviderMock, null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParserMock, contextMock);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test", result.get(0));
        verify(valueDeserializer, times(1)).deserialize(jsonParserMock, contextMock);
    }

    // Test deserialize method with delegate deserializer
    @Test
    public void testDeserializeWithDelegateDeserializer() throws JsonProcessingException {
        when(valueDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn("test");
        when(delegateDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn(testObjectNode);

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, delegateDeserializer, nullValueProviderMock, null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParserMock, contextMock);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test", result.get(0));
        verify(delegateDeserializer, times(1)).deserialize(jsonParserMock, contextMock);
    }

    // Test deserializeUsingCustom method
    @Test
    public void testDeserializeUsingCustom() throws JsonProcessingException {
        when(valueDeserializer.deserialize(jsonParserMock, contextMock)).thenThrow(JsonMappingException.class);

        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(customDeserializer.deserialize(jsonParserMock, contextMock)).thenReturn("test");

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, nullValueProviderMock, null);

        Collection<String> result = stringCollectionDeserializer.deserializeUsingCustom(jsonParserMock, contextMock, new ArrayList<>(), customDeserializer);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test", result.get(0));
        verify(customDeserializer, times(1)).deserialize(jsonParserMock, contextMock);
        verifyNoMoreInteractions(valueDeserializer);
    }

    // Test deserializeWithType method
    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        when(typeDeserializerMock.deserializeTypedFromArray(jsonParserMock, contextMock)).thenReturn(new ArrayList<>());

        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, nullValueProviderMock, null);

        Collection<String> result = stringCollectionDeserializer.deserializeWithType(jsonParserMock, contextMock, typeDeserializerMock);

        verify(typeDeserializerMock, times(1)).deserializeTypedFromArray(jsonParserMock, contextMock);
        Assert.assertEquals(0, result.size());
    }
}