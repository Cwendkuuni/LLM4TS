package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testNullifyingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(Null.class);

        JsonParser p = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result1 = deserializer.deserialize(p, ctxt);
        assertNull(result1);

        p = mapper.getFactory().createParser("{\"name\":\"John Doe\"}");
        Object result2 = deserializer.deserialize(p, ctxt);
        assertNull(result2);

        p = mapper.getFactory().createParser("{\"@class\":\"com.example.MyClass\"}");
        Object result3 = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(result3);

        p = mapper.getFactory().createParser("{\"name\":\"John Doe\", \"age\":30}");
        Object result4 = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(result4);

        p = mapper.getFactory().createParser("[1, 2, 3]");
        Object result5 = deserializer.deserializeWithType(p, ctxt, mapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
        assertNull(result5);

        p = mapper.getFactory().createParser("{\"name\":\"John Doe\", \"age\":30, \"list\":[1, 2, 3]}");
        Object result6 = deserializer.deserializeWithType(p, ctxt, mapper.getTypeFactory().constructType(MyClass.class));
        assertNull(result6);
    }

    @Test
    public void testNullifyingDeserializer_StartArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;

        JsonParser p = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserializeWithType(p, ctxt, mapper.getTypeDeserializerForArray(Object.class));
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializer_StartObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;

        JsonParser p = mapper.getFactory().createParser("{\"name\":\"John Doe\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserializeWithType(p, ctxt, mapper.getTypeFactory().constructType(MyClass.class));
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializer_FieldName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;

        JsonParser p = mapper.getFactory().createParser("{\"name\":\"John Doe\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserializeWithType(p, ctxt, mapper.getTypeDeserializerForAnnotatedClass(MyClassWithField.class, "name"));
        assertNull(result);
    }

    static class MyClass {
        private String name;
        private Integer age;
        private List<Integer> list;

        // getters and setters
    }

    static class MyClassWithField {
        @JsonProperty("name")
        private String name;

        // other fields and getters/setters
    }
}