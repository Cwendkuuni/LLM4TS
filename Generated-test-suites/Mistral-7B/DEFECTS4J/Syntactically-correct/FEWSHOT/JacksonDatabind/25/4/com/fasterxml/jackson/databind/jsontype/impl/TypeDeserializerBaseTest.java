package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Test the base class with default values
        TypeDeserializerBase td = new TypeDeserializerBase(
                null, null, null, false, null);
        assertNotNull(td);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", td.toString());
        assertNull(td.getPropertyName());
        assertNull(td.getTypeIdResolver());
        assertNull(td.getDefaultImpl());

        // Test the constructor with non-null arguments
        Class<?> baseType = Object.class;
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase td2 = new TypeDeserializerBase(
                baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(td2);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", td2.toString());
        assertEquals(baseType.getName(), td2.baseTypeName());
        assertEquals(idResolver, td2.getTypeIdResolver());
        assertEquals(typePropertyName, td2.getPropertyName());
        assertNull(td2.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if ("StringDeserializer".equals(typeId)) {
                    return StringDeserializer.instance;
                } else if ("IntegerDeserializer".equals(typeId)) {
                    return IntegerDeserializer.instance;
                }
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return "String, Integer";
            }
        };
        TypeDeserializerBase td = new TypeDeserializerBase(
                Object.class, idResolver, null, true, null);

        // Test with known type ids
        JsonDeserializer<Object> deserializer = td.findDeserializer(new DeserializationContext(), "String");
        assertNotNull(deserializer);
        assertEquals(StringDeserializer.class, deserializer.getClass());
        deserializer = td.findDeserializer(new DeserializationContext(), "Integer");
        assertNotNull(deserializer);
        assertEquals(IntegerDeserializer.class, deserializer.getClass());

        // Test with unknown type id
        deserializer = td.findDeserializer(new DeserializationContext(), "UnknownType");
        assertNull(deserializer);

        // Test with null typeId
        deserializer = td.findDeserializer(new DeserializationContext(), null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase td = new TypeDeserializerBase(
                Object.class, null, null, true, null);

        // Test with null defaultImpl
        JsonDeserializer<Object> deserializer = td.findDefaultImplDeserializer(new DeserializationContext());
        assertNull(deserializer);

        // Test with a defaultImpl that can be deserialized
        td = new TypeDeserializerBase(
                Object.class, null, null, true, String.class);
        deserializer = td.findDefaultImplDeserializer(new DeserializationContext());
        assertNotNull(deserializer);
        assertEquals(StringDeserializer.class, deserializer.getClass());

        // Test with a null defaultImpl
        td = new TypeDeserializerBase(
                Object.class, null, null, true, null);
        deserializer = td.findDefaultImplDeserializer(new DeserializationContext());
        assertNull(deserializer);
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        public static final JsonDeserializer<String> instance = new StringDeserializer();

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class IntegerDeserializer extends JsonDeserializer<Integer> {
        public static final JsonDeserializer<Integer> instance = new IntegerDeserializer();

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    static class TypeIdResolverBase implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }
    }
}