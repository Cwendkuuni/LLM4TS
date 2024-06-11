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
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@JacksonStdImpl
public final class StringCollectionDeserializerTest extends TestBase {

    private static final String JSON_STRING_ARRAY = "[\"foo\", \"bar\", \"baz\"]";
    private static final String JSON_STRING_LIST = "[\"foo\", \"bar\", \"baz\"]";
    private static final String JSON_NULL = "null";
    private static final String JSON_EMPTY_ARRAY = "[]";

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        JavaType javaType = mapper().constructType(Collection.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper().getFactory().getDeserializerFactory().findValueDeserializer(String.class, null);
        StringCollectionDeserializer underTest = new StringCollectionDeserializer(javaType, null, null, stringDeserializer, null, null);
        Collection<String> result = underTest.deserialize(parser(JSON_EMPTY_ARRAY), context(), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeStringArray() throws Exception {
        JavaType javaType = mapper().constructType(Collection.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper().getFactory().getDeserializerFactory().findValueDeserializer(String.class, null);
        StringCollectionDeserializer underTest = new StringCollectionDeserializer(javaType, null, null, stringDeserializer, null, null);
        Collection<String> result = underTest.deserialize(parser(JSON_STRING_ARRAY), context(), null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringArrayWithCustomValueDeserializer() throws Exception {
        JavaType javaType = mapper().constructType(Collection.class, String.class);
        JsonDeserializer<String> customStringDeserializer = createMock(JsonDeserializer.class);
        StringCollectionDeserializer underTest = new StringCollectionDeserializer(javaType, null, null, customStringDeserializer, null, null);
        Collection<String> result = underTest.deserialize(parser(JSON_STRING_ARRAY), context(), null);

        // The custom deserializer should be used to deserialize each element
        verify(customStringDeserializer).deserialize(eq(parser(JSON_STRING_ARRAY)), eq(context()), any());
        assertEquals(Collections.singletonList("foo, bar, baz"), result);
    }

    @Test
    public void testDeserializeStringList() throws Exception {
        JavaType javaType = mapper().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper().getFactory().getDeserializerFactory().findValueDeserializer(String.class, null);
        StringCollectionDeserializer underTest = new StringCollectionDeserializer(javaType, null, null, stringDeserializer, null, null);
        Collection<String> result = underTest.deserialize(parser(JSON_STRING_LIST), context(), null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeNull() throws Exception {
        JavaType javaType = mapper().constructType(Collection.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper().getFactory().getDeserializerFactory().findValueDeserializer(String.class, null);
        StringCollectionDeserializer underTest = new StringCollectionDeserializer(javaType, null, null, stringDeserializer, null, null);
        Collection<String> result = underTest.deserialize(parser(JSON_NULL), context(), null);
        assertNull(result);
    }
}