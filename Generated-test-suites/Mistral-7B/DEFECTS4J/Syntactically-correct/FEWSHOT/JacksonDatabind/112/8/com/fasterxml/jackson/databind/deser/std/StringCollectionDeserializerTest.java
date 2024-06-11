package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test cases for {@link StringCollectionDeserializer}.
 */
public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializer() {
        JavaType javaType = MapperFactory.getMapper().getTypeFactory().constructCollectionType(String.class, String.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        ContextualDeserializer delegateDeserializer = Mockito.mock(ContextualDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueDeserializer, valueInstantiator, delegateDeserializer, null, null);

        // Test empty array
        Collection<String> result = deserializer.deserialize(null, Mockito.mock(ObjectMapper.class), null);
        assertEquals(Collections.emptyList(), result);

        // Test deserializing a JSON array with strings
        ArrayNode jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("foo");
        jsonArray.add("bar");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        assertEquals(Arrays.asList("foo", "bar"), result);

        // Test deserializing a JSON array with null values
        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add(JsonNull.getInstance());
        jsonArray.add("baz");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        assertEquals(Arrays.asList((String) JsonNull.getInstance(), "baz"), result);

        // Test deserializing a JSON array with an empty string
        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        assertEquals(Collections.singletonList(""), result);

        // Test deserializing a JSON array with a single string
        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("qux");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        assertEquals(Collections.singletonList("qux"), result);

        // Test deserializing a JSON array with a single null value
        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add(JsonNull.getInstance());
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        assertEquals(Collections.singletonList(JsonNull.getInstance()), result);

        // Test deserializing a JSON array with custom deserializer for strings
        JsonDeserializer<String> customDeserializer = Mockito.mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(customDeserializer);
        result = deserializer.deserialize(null, Mockito.mock(ObjectMapper.class), null);
        verify(valueDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        verifyNoMoreInteractions(valueDeserializer);
        assertEquals(Collections.emptyList(), result);

        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("quux");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        verify(valueDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList("quux"), result);

        // Test deserializing a JSON array with custom deserializer for strings and null values
        JsonDeserializer<String> customNullDeserializer = Mockito.mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(customNullDeserializer);
        when(customNullDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(JsonNull.getInstance());
        result = deserializer.deserialize(null, Mockito.mock(ObjectMapper.class), null);
        verify(valueDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        verify(customNullDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList(JsonNull.getInstance()), result);

        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("quuz");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        verify(valueDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList("quuz"), result);

        // Test deserializing a JSON array with a delegate deserializer
        when(delegateDeserializer.createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class))).thenReturn(customDeserializer);
        result = deserializer.deserialize(null, Mockito.mock(ObjectMapper.class), null);
        verify(delegateDeserializer).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        verify(customDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        verifyNoMoreInteractions(valueDeserializer, customNullDeserializer);
        assertEquals(Collections.emptyList(), result);

        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("quux");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        verify(delegateDeserializer).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        verify(customDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList("quux"), result);

        // Test deserializing a JSON array with a delegate deserializer and null values
        when(delegateDeserializer.createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class))).thenReturn(customNullDeserializer);
        result = deserializer.deserialize(null, Mockito.mock(ObjectMapper.class), null);
        verify(delegateDeserializer).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        verify(customNullDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList(JsonNull.getInstance()), result);

        jsonArray = MapperFactory.getMapper().createArrayNode();
        jsonArray.add("quuz");
        result = deserializer.deserialize(MapperFactory.getMapper().treeToValues(jsonArray), Mockito.mock(ObjectMapper.class), null);
        verify(delegateDeserializer).createContextual(Mockito.any(DeserializationContext.class), Mockito.any(BeanProperty.class));
        verify(customDeserializer).deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class));
        assertEquals(Collections.singletonList("quuz"), result);
    }
}