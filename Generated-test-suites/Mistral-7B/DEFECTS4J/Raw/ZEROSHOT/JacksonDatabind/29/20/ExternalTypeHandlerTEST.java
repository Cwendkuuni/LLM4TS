package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws JsonProcessingException, IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("{\"name\":\"John Doe\", \"type\":\"Person\", \"age\":null}");
        ObjectReader reader = factory.getObjectReader();

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("name", String.class), null)
                .addExternal(new SettableBeanProperty("type", String.class), new SimpleTypeDeserializer(Person.class))
                .addExternal(new SettableBeanProperty("age", Integer.class), new SimpleTypeDeserializer(Integer.class))
                .build());

        Map<String, Object> map = handler.complete(jp, factory.getDeserializationContext(), null);
        assertEquals("John Doe", map.get("name"));
        assertEquals("Person", map.get("type"));
        assertNull(map.get("age"));

        jp.nextToken(); // consume root object end
        handler.complete(jp, factory.getDeserializationContext(), null);

        map = handler.complete(jp, factory.getDeserializationContext(), null);
        assertEquals("John Doe", map.get("name"));
        assertEquals("Person", map.get("type"));
        assertNull(map.get("age"));

        jp.nextValue(); // consume null value of "age"
        handler.complete(jp, factory.getDeserializationContext(), null);

        map = handler.complete(jp, factory.getDeserializationContext(), null);
        assertEquals("John Doe", map.get("name"));
        assertEquals("Person", map.get("type"));
        assertEquals(0, map.get("age"));

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("name", "Jane Doe", "type", "Animal", "animalType", "Dog"));
        list.add(Map.of("name", "Bob Smith", "type", "Person"));

        ObjectReader readerList = factory.getObjectReader(new TypeReference<Object>() {});
        Object readValue = readerList.readValue(jp);
        List<Map<String, Object>> resultList = readerList.readValue(jp);

        assertEquals(2, resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> resultMap = resultList.get(i);
            Map<String, Object> expectedMap = list.get(i);
            assertEquals("Jane Doe", resultMap.get("name"));
            assertEquals("Animal", resultMap.get("type"));
            assertEquals("Dog", resultMap.get("animalType"));

            assertEquals("Bob Smith", resultMap.get(i + 1).get("name"));
            assertEquals("Person", resultMap.get(i + 1).get("type"));
            assertNull(resultMap.get(i + 1).get("animalType"));
        }

        jp.nextToken(); // consume end of list
        handler.complete(jp, factory.getDeserializationContext(), null);

        resultList = handler.complete(jp, factory.getDeserializationContext(), null);
        assertEquals(2, resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> resultMap = resultList.get(i);
            Map<String, Object> expectedMap = list.get(i);
            assertEquals("Jane Doe", resultMap.get("name"));
            assertEquals("Animal", resultMap.get("type"));
            assertEquals("Dog", resultMap.get("animalType"));

            assertEquals("Bob Smith", resultMap.get(i + 1).get("name"));
            assertEquals("Person", resultMap.get(i + 1).get("type"));
            assertNull(resultMap.get(i + 1).get("animalType"));
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private final Class<?> _valueClass;

        public SimpleTypeDeserializer(Class<?> valueClass) {
            super(null, null);
            _valueClass = valueClass;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object result = ctxt.readValueAs(_valueClass, p);
            ctxt.handleUnexpectedValue(null, null, "simple", p);
            return result;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd)
                throws IOException {
            Object result = ctxt.readValueAs(_valueClass, p);
            ctxt.handleUnexpectedValue(null, null, "simple", p);
            return result;
        }

        @Override
        public String getTypeDescriptor() {
            return _valueClass.getName();
        }

        @Override
        public String getPropertyName() {
            return "simple";
        }
    }

    private static class Person {
    }
}