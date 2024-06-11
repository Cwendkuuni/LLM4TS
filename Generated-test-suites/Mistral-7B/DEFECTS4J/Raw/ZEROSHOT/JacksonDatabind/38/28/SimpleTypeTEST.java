package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBindings;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(simpleType.getClass(), simpleType.getType());
        assertEquals(TypeSerializerBindings.defaultInstance(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        SimpleType copiedType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedType.getRawClass());
        assertEquals(simpleType.getType(), copiedType.getType());
        assertEquals(simpleType.getBindings(), copiedType.getBindings());
        assertEquals(simpleType.getSuperClass(), copiedType.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), copiedType.getSuperTypes());
        assertEquals(simpleType.getValueHandler(), copiedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), copiedType.getTypeHandler());
        assertEquals(simpleType.isStatic(), copiedType.isStatic());
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        assertNull(simpleType.withContentType(Object.class));
        assertNull(simpleType.withContentType(String.class));
        assertEquals(simpleType.getClass(), simpleType.withContentType(String[].class).getType().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidContentType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        simpleType.withContentType(List.class);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, typeWithHandler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, typeWithHandler);
        assertEquals(handler, typeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafeWithInvalidClass() {
        SimpleType.constructUnsafe(new Object() {}.getClass());
    }

    @Test
    public void testSimpleTypeConstruct() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithInvalidClass() {
        SimpleType.construct(new Object() {}.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSimpleTypeNarrowWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        SimpleType narrowType = simpleType._narrow(int.class);
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        JavaType refinedType = simpleType.refine(stringBuilderClass, TypeBindings.defaultBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testSimpleType_getClassSignature() {
        Class<String> stringClass = String.class;
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testSimpleType_getClassSignatureWithArray() {
        Class<String[]> stringArrayClass = String[].class;
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(stringArrayClass, TypeBindings.defaultBindings());
        simpleType.getErasedSignature(sb);
        assertEquals("String[]", sb.toString());
    }

    @Test
    public void testSimpleType_getClassSignatureWithGenericTypes() {
        Class<List<String>> listClass = List.class;
        TypeSerializer<List<String>> listTypeSerializer = new TypeSerializer() {
            @Override
            public void serialize(List<String> value, TypeSerializer s, JsonGenerator gen) throws IOException {
                gen.writeStartArray();
                for (String valueElement : value) {
                    gen.writeString(valueElement);
                }
                gen.writeEndArray();
            }

            @Override
            public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ArrayList<>();
            }

            @Override
            public void acceptTypeSerializer(TypeSerializer s) {
                // no-op
            }
        };
        TypeDeserializer<List<String>> listTypeDeserializer = new TypeDeserializer() {
            @Override
            public List<String> deserialize(JsonParser p, DeserializationContext context) throws IOException {
                return new ArrayList<>();
            }

            @Override
            public TypeDeserializer withValueHandler(ValueDeserializer vdh) {
                return this;
            }

            @Override
            public TypeDeserializer withValueDeserializer(ValueDeserializer vdh) {
                return this;
            }

            @Override
            public void registerSerializer(TypeSerializer s) {
                // no-op
            }

            @Override
            public void registerDeserializer(TypeDeserializer d) {
                // no-op
            }

            @Override
            public void acceptTypeSerializer(TypeSerializer s) {
                // no-op
            }
        };
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.putBinding(List.class, new TypeSerializer.AlwaysStaticTypeSerializer(listTypeSerializer));
        bindings.putBinding(List.class, new TypeDeserializer.EmptyTypeDeserializer(listTypeDeserializer));

        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(List.class, bindings);
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.util.List;<Ljava.lang.String;>;", sb.toString());
    }
}