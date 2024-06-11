package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    

    @Test
    public void testTypeDeserializerBaseConstructor() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return JavaType.construct(Integer.class);
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Integer.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertNotNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testTypeDeserializerBaseForProperty() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Integer.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", Integer.class);
        TypeDeserializer expectedTypeDeserializer = new IntDeserializer(); // assume IntDeserializer is a valid deserializer
        TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(expectedTypeDeserializer.getClass(), actualTypeDeserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testGetTypeInclusionThrowsIOException() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        typeDeserializerBase.getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Integer.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        DeserializationContext context = new TestingDeserializationContext();
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, null);
        assertNull(actualDeserializer);
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return JavaType.construct(Integer.class);
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        DeserializationContext context = new TestingDeserializationContext();
        JsonDeserializer<Object> expectedDeserializer = new IntDeserializer(); // assume IntDeserializer is a valid deserializer
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, "intTypeId");
        assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        DeserializationContext context = new TestingDeserializationContext();
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(actualDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Integer.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestingDeserializationContext();
        JsonDeserializer<Object> expectedDeserializer = new IntDeserializer(); // assume IntDeserializer is a valid deserializer
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithNullTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"propertyName\": null}");
        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestingDeserializationContext(), null);
        fail("Expected JsonProcessingException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"propertyName\": \"unknownTypeId\"}");
        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestingDeserializationContext(), "unknownTypeId");
        fail("Expected JsonProcessingException");
    }

    @Test
    public void testDeserializeWithKnownTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return JavaType.construct(Integer.class);
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"propertyName\": 5}");
        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestingDeserializationContext(), "int");
        assertEquals(5, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = JavaType.construct(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"propertyName\": \"unknownTypeId\"}");
        Object actualResult = typeDeserializerBase._handleUnknownTypeId(parser.getCodec(), "unknownTypeId", idResolver, baseType);
        fail("Expected JsonMappingException");
    }

    

    private static class TestingDeserializationContext extends DeserializationContext {
        private TestingDeserializationContext() {
            super(new JsonFactory(), null);
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }
    }

    private static class IntDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getIntValue();
        }
    }
}