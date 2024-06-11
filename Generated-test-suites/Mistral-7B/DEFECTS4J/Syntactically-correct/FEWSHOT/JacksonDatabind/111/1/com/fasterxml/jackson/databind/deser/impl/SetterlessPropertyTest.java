package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    private static class TestClass {
        @JsonProperty
        private List<String> list;

        @JsonProperty
        private Map<String, Integer> map;
    }

    @Test
    public void testList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestClass tc = new TestClass();
        Method getter = TestClass.class.getMethod("getList");
        SetterlessProperty property = new SetterlessProperty(
                BeanPropertyDefinition.standardMethod("list", TestClass.class, getter),
                JavaType.ofCollectionType(List.class, String.class),
                null, null, new AnnotatedMethod(getter)
        );

        JsonParser p = mapper.getFactory().createParser("[]");

        property.deserializeAndSet(p, null, tc);

        assertEquals(Collections.emptyList(), tc.list);

        p = mapper.getFactory().createParser("[1, 2, 3]");
        property.deserializeAndSet(p, null, tc);

        List<String> expectedList = Arrays.asList("1", "2", "3");
        assertEquals(expectedList, tc.list);
    }

    @Test
    public void testMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestClass tc = new TestClass();
        Method getter = TestClass.class.getMethod("getMap");
        SetterlessProperty property = new SetterlessProperty(
                BeanPropertyDefinition.standardMethod("map", TestClass.class, getter),
                JavaType.ofCollectionType(Map.class, String.class, Integer.class),
                null, null, new AnnotatedMethod(getter)
        );

        JsonParser p = mapper.getFactory().createParser("{}");

        property.deserializeAndSet(p, null, tc);

        assertEquals(Collections.emptyMap(), tc.map);

        p = mapper.getFactory().createParser("{\"key\":1, \"anotherKey\":2}");
        property.deserializeAndSet(p, null, tc);

        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("key", 1);
        expectedMap.put("anotherKey", 2);
        assertEquals(expectedMap, tc.map);
    }

    @Test
    public void testGetMethodNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestClass tc = new TestClass();
        SetterlessProperty property = new SetterlessProperty(
                null, null, null, null, null
        );

        JsonParser p = mapper.getFactory().createParser("[]");

        try {
            property.deserializeAndSet(p, null, tc);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetMethodThrowException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestClass tc = new TestClass();
        Method getter = TestClass.class.getMethod("getList");
        SetterlessProperty property = new SetterlessProperty(
                BeanPropertyDefinition.standardMethod("list", TestClass.class, getter),
                JavaType.ofCollectionType(List.class, String.class),
                null, null, new AnnotatedMethod(getter)
        );

        doThrow(InvocationTargetException.class)
                .when(getter)
                .invoke(tc, (Object[]) null);

        JsonParser p = mapper.getFactory().createParser("[]");

        try {
            property.deserializeAndSet(p, null, tc);
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}