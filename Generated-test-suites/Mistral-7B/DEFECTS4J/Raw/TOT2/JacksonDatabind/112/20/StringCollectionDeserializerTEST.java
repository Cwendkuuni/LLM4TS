package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.test.BaseMapTest;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionDeserializerTest extends BaseMapTest {

    private static final String JSON_STRING_COLLECTION =
            "[\"alpha\",\"beta\",\"gamma\",\"delta\"]";
    private static final String JSON_STRING_ELEMENT = "\"element\"";
    private static final String JSON_NULL = "null";

    private ObjectMapper mapper;
    private StringCollectionDeserializer deserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;

    @BeforeClass
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                valueInstantiator, null, delegateDeserializer, null, null);
    }

    @Test
    public void testDeserializeStandard() throws Exception {
        // Given
        Collection<String> expected = new ArrayList<>();
        expected.add("alpha");
        expected.add("beta");
        expected.add("gamma");
        expected.add("delta");

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(JSON_STRING_COLLECTION), null);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeUsingCustomDeserializer() throws Exception {
        // Given
        String valueDeserializerClassName = "com.example.CustomStringDeserializer";
        JsonDeserializer<String> customValueDeserializer = new ObjectMapper().findValueDeserializer(mapper.getFactory(), String.class);
        when(valueInstantiator.createUsingCustomValueDeserializer(any(DeserializationContext.class), eq(String.class), any(Class.class)))
                .thenReturn(customValueDeserializer);

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(JSON_STRING_COLLECTION), null);

        // Then
        verify(valueInstantiator).createUsingCustomValueDeserializer(any(DeserializationContext.class), eq(String.class), eq(StringCollectionDeserializer.class));
        assertEquals(4, actual.size());
    }

    @Test
    public void testDeserializeEmpty() throws Exception {
        // Given
        String emptyJSON = "[]";

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(emptyJSON), null);

        // Then
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testDeserializeNull() throws Exception {
        // Given
        String JSON_NULL_COLLECTION = JSON_NULL;

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(JSON_NULL_COLLECTION), null);

        // Then
        assertNull(actual);
    }

    @Test
    public void testDeserializeSingleElement() throws Exception {
        // Given
        String JSON_SINGLE_ELEMENT = "[\"" + JSON_STRING_ELEMENT + "\"]";

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(JSON_SINGLE_ELEMENT), null);

        // Then
        assertEquals(1, actual.size());
        assertEquals(JSON_STRING_ELEMENT, actual.iterator().next());
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        // Given
        when(delegateDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("element");

        // When
        Collection<String> actual = deserializer.deserialize(mapper.getFactory().createParser(JSON_STRING_COLLECTION), null);

        // Then
        assertEquals(4, actual.size());
        for (int i = 0; i < 4; i++) {
            assertEquals("element", actual.get(i));
        }

        verify(delegateDeserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test(expectedExceptions = IOException.class)
    public void testDeserializeInvalidJSON() throws Exception {
        // Given
        String invalidJSON = "{";

        // When & Then
        deserializer.deserialize(mapper.getFactory().createParser(invalidJSON), null);

        fail("Expected IOException, but it was not thrown.");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws Exception {
        // Given
        String unexpectedTokenJSON = "[1, \"alpha\", 2.5]";

        // When & Then
        deserializer.deserialize(mapper.getFactory().createParser(unexpectedTokenJSON), null);

        fail("Expected JsonMappingException, but it was not thrown.");
    }
}