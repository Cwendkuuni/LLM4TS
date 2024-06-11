package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeHandler;
import com.fasterxml.jackson.databind.type.TypeIdResolver;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        assertNotNull(listType);
        assertEquals(ArrayList.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(String.class, listType.getContentType().getRawClass());
        assertEquals(String.class, listType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, listType.getBindings());
        assertEquals(String.class, listType.getRawClass().getGenericSuperclass());
        assertNull(listType.getSuperClass());
        assertNull(listType.getSuperInterfaces());
        assertNull(listType.getValueHandler());
        assertNull(listType.getTypeHandler());
        assertFalse(listType.isStatic());

        JavaType listType2 = CollectionType.construct(ArrayList.class, null, null, new JavaType[0], String.class);
        assertNotNull(listType2);
        assertEquals(ArrayList.class, listType2.getRawClass());
        assertNull(listType2.getContentType());
        assertEquals(String.class, listType2.getRawClass().getComponentType());
        assertNull(listType2.getBindings());
        assertNull(listType2.getSuperClass());
        assertNull(listType2.getSuperInterfaces());
        assertNull(listType2.getValueHandler());
        assertNull(listType2.getTypeHandler());
        assertFalse(listType2.isStatic());

        JavaType setType = CollectionType.construct(HashSet.class, new TypeBindings(), null, new JavaType[0], String.class);
        assertNotNull(setType);
        assertEquals(HashSet.class, setType.getRawClass());
        assertEquals(String.class, setType.getContentType());
        assertEquals(String.class, setType.getContentType().getRawClass());
        assertEquals(String.class, setType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, setType.getBindings());
        assertNull(setType.getRawClass().getGenericSuperclass());
        assertNull(setType.getSuperClass());
        assertArrayEquals(new JavaType[]{HashSet.class}, setType.getSuperInterfaces());
        assertNull(setType.getValueHandler());
        assertNull(setType.getTypeHandler());
        assertFalse(setType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(ArrayList.class, String.class);
            fail("Should not reach here, as method is deprecated since 2.7.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            JavaType listType = CollectionType.construct(ArrayList.class);
            fail("Should not reach here, as method is deprecated since 2.7.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        JavaType refinedListType = listType.refine(LinkedList.class, new TypeBindings(), null, new JavaType[0], String.class);
        assertNotNull(refinedListType);
        assertEquals(LinkedList.class, refinedListType.getRawClass());
        assertEquals(String.class, refinedListType.getContentType());
        assertEquals(String.class, refinedListType.getContentType().getRawClass());
        assertEquals(String.class, refinedListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, refinedListType.getBindings());
        assertNull(refinedListType.getRawClass().getGenericSuperclass());
        assertNull(refinedListType.getSuperClass());
        assertArrayEquals(new JavaType[]{LinkedList.class}, refinedListType.getSuperInterfaces());
        assertNull(refinedListType.getValueHandler());
        assertNull(refinedListType.getTypeHandler());
        assertFalse(refinedListType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        JavaType narrowedListType = listType.narrow(LinkedList.class);
        assertNotNull(narrowedListType);
        assertEquals(LinkedList.class, narrowedListType.getRawClass());
        assertEquals(String.class, narrowedListType.getContentType());
        assertEquals(String.class, narrowedListType.getContentType().getRawClass());
        assertEquals(String.class, narrowedListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, narrowedListType.getBindings());
        assertNull(narrowedListType.getRawClass().getGenericSuperclass());
        assertNull(narrowedListType.getSuperClass());
        assertArrayEquals(new JavaType[]{LinkedList.class}, narrowedListType.getSuperInterfaces());
        assertNull(narrowedListType.getValueHandler());
        assertNull(narrowedListType.getTypeHandler());
        assertFalse(narrowedListType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        JavaType withContentTypeListType = listType.withContentType(Integer.class);
        assertNotNull(withContentTypeListType);
        assertEquals(ArrayList.class, withContentTypeListType.getRawClass());
        assertEquals(Integer.class, withContentTypeListType.getContentType());
        assertEquals(Integer.class, withContentTypeListType.getContentType().getRawClass());
        assertEquals(Integer.class, withContentTypeListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withContentTypeListType.getBindings());
        assertNull(withContentTypeListType.getRawClass().getGenericSuperclass());
        assertNull(withContentTypeListType.getSuperClass());
        assertArrayEquals(new JavaType[]{ArrayList.class}, withContentTypeListType.getSuperInterfaces());
        assertNull(withContentTypeListType.getValueHandler());
        assertNull(withContentTypeListType.getTypeHandler());
        assertFalse(withContentTypeListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        TypeHandler<String> stringHandler = new TypeHandler<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String deserializeValue(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String getValueAsString(Object value) {
                return (String) value;
            }
        };
        JavaType withTypeHandlerListType = listType.withTypeHandler(stringHandler);
        assertNotNull(withTypeHandlerListType);
        assertEquals(ArrayList.class, withTypeHandlerListType.getRawClass());
        assertEquals(String.class, withTypeHandlerListType.getContentType());
        assertEquals(String.class, withTypeHandlerListType.getContentType().getRawClass());
        assertEquals(String.class, withTypeHandlerListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withTypeHandlerListType.getBindings());
        assertNull(withTypeHandlerListType.getRawClass().getGenericSuperclass());
        assertNull(withTypeHandlerListType.getSuperClass());
        assertArrayEquals(new JavaType[]{ArrayList.class}, withTypeHandlerListType.getSuperInterfaces());
        assertEquals(stringHandler, withTypeHandlerListType.getTypeHandler());
        assertNull(withTypeHandlerListType.getValueHandler());
        assertFalse(withTypeHandlerListType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        TypeHandler<String> stringHandler = new TypeHandler<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String deserializeValue(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String getValueAsString(Object value) {
                return (String) value;
            }
        };
        TypeDeserializer stringDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }
        };
        JavaType withContentTypeHandlerListType = listType.withContentTypeHandler(stringHandler);
        assertNotNull(withContentTypeHandlerListType);
        assertEquals(ArrayList.class, withContentTypeHandlerListType.getRawClass());
        assertEquals(String.class, withContentTypeHandlerListType.getContentType());
        assertEquals(String.class, withContentTypeHandlerListType.getContentType().getRawClass());
        assertEquals(String.class, withContentTypeHandlerListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withContentTypeHandlerListType.getBindings());
        assertNull(withContentTypeHandlerListType.getRawClass().getGenericSuperclass());
        assertNull(withContentTypeHandlerListType.getSuperClass());
        assertArrayEquals(new JavaType[]{ArrayList.class}, withContentTypeHandlerListType.getSuperInterfaces());
        assertEquals(stringHandler, withContentTypeHandlerListType.getContentTypeHandler());
        assertEquals(stringDeserializer, withContentTypeHandlerListType.getValueHandler());
        assertNull(withContentTypeHandlerListType.getTypeHandler());
        assertFalse(withContentTypeHandlerListType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        TypeHandler<String> stringHandler = new TypeHandler<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String deserializeValue(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String getValueAsString(Object value) {
                return (String) value;
            }
        };
        JavaType withValueHandlerListType = listType.withValueHandler(stringHandler);
        assertNotNull(withValueHandlerListType);
        assertEquals(ArrayList.class, withValueHandlerListType.getRawClass());
        assertEquals(String.class, withValueHandlerListType.getContentType());
        assertEquals(String.class, withValueHandlerListType.getContentType().getRawClass());
        assertEquals(String.class, withValueHandlerListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withValueHandlerListType.getBindings());
        assertNull(withValueHandlerListType.getRawClass().getGenericSuperclass());
        assertNull(withValueHandlerListType.getSuperClass());
        assertArrayEquals(new JavaType[]{ArrayList.class}, withValueHandlerListType.getSuperInterfaces());
        assertNull(withValueHandlerListType.getTypeHandler());
        assertEquals(stringHandler, withValueHandlerListType.getValueHandler());
        assertNull(withValueHandlerListType.getContentTypeHandler());
        assertFalse(withValueHandlerListType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        TypeHandler<String> stringHandler = new TypeHandler<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String deserializeValue(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }

            @Override
            public String getValueAsString(Object value) {
                return (String) value;
            }
        };
        TypeDeserializer stringDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return p.getText();
            }
        };
        JavaType withContentValueHandlerListType = listType.withContentValueHandler(stringHandler);
        assertNotNull(withContentValueHandlerListType);
        assertEquals(ArrayList.class, withContentValueHandlerListType.getRawClass());
        assertEquals(String.class, withContentValueHandlerListType.getContentType());
        assertEquals(String.class, withContentValueHandlerListType.getContentType().getRawClass());
        assertEquals(String.class, withContentValueHandlerListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withContentValueHandlerListType.getBindings());
        assertNull(withContentValueHandlerListType.getRawClass().getGenericSuperclass());
        assertNull(withContentValueHandlerListType.getSuperClass());
        assertArrayEquals(new JavaType[]{ArrayList.class}, withContentValueHandlerListType.getSuperInterfaces());
        assertEquals(stringHandler, withContentValueHandlerListType.getContentValueHandler());
        assertEquals(stringDeserializer, withContentValueHandlerListType.getValueHandler());
        assertNull(withContentValueHandlerListType.getTypeHandler());
        assertNull(withContentValueHandlerListType.getContentTypeHandler());
        assertFalse(withContentValueHandlerListType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, new JavaType[0], String.class);
        JavaType withStaticTypingListType = listType.withStaticTyping();
        assertNotNull(withStaticTypingListType);
        assertEquals(ArrayList.class, withStaticTypingListType.getRawClass());
        assertEquals(String.class, withStaticTypingListType.getContentType());
        assertEquals(String.class, withStaticTypingListType.getContentType().getRawClass());
        assertEquals(String.class, withStaticTypingListType.getRawClass().getComponentType());
        assertEquals(TypeBindings.class, withStaticTypingListType.getBindings());
        assertNull(withStaticTypingListType.getRawClass().getGenericSuperclass());
        assertNull(withStaticTypingListType.getSuperClass());
        assertArrayEquals(new JavaType[0], withStaticTypingListType.getSuperInterfaces());
        assertNull(withStaticTypingListType.getValueHandler());
        assertNull(withStaticTypingListType.getTypeHandler());
        assertTrue(withStaticTypingListType.isStatic());
    }
}