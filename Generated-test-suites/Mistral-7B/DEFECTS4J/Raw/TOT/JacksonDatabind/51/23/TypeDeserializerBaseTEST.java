package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_INSTANCE = new TypeDeserializerBaseBase(
            JsonTypeFactory.constructSimpleType(Object.class),
            new StdTypeIdResolver(),
            "",
            true,
            JsonTypeFactory.constructSimpleType(String.class)
    );

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = TEST_INSTANCE.forProperty(property);
        assertTrue(deserializer instanceof TypeDeserializerBase);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = TEST_INSTANCE.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = TEST_INSTANCE.baseTypeName();
        assertEquals("class java.lang.Object", baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String propertyName = TEST_INSTANCE.getPropertyName();
        assertEquals("", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver idResolver = TEST_INSTANCE.getTypeIdResolver();
        assertNotNull(idResolver);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> defaultImpl = TEST_INSTANCE.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = TEST_INSTANCE.findDeserializer(context, typeId);
        assertNull(deserializer);

        context.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        context.findValueIdHandler().setAnnotationIntrospector(context.getDefaultAnnotationIntrospector());

        context.findValueIdHandler().setTypeIdResolver(new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("TestType")) {
                    return JsonTypeFactory.constructSimpleType(TestType.class);
                }
                return null;
            }
        });

        deserializer = TEST_INSTANCE.findDeserializer(context, typeId);
        assertTrue(deserializer instanceof TestTypeDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = TEST_INSTANCE.findDefaultImplDeserializer(context);
        assertNull(deserializer);

        context.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        context.findValueIdHandler().setAnnotationIntrospector(context.getDefaultAnnotationIntrospector());

        context.findValueIdHandler().setTypeIdResolver(new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("TestType")) {
                    return JsonTypeFactory.constructSimpleType(TestType.class);
                }
                return null;
            }
        });

        deserializer = TEST_INSTANCE.findDefaultImplDeserializer(context);
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"TestType\"}";
        JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        Object result = TEST_INSTANCE.deserializeWithNativeTypeId(parser, new TestDeserializationContext());
        assertTrue(result instanceof TestType);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws Exception {
        String json = "{\"typeId\":\"UnknownType\"}";
        JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        TEST_INSTANCE.deserializeWithNativeTypeId(parser, new TestDeserializationContext());
    }
}

class TestTypeDeserializer extends JsonDeserializer<TestType> {
    @Override
    public TestType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new TestType();
    }
}

class TestType {
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
        return null;
    }

    @Override
    public JavaType findTypeDeserializer(JavaType type, BeanProperty property) throws IOException {
        return null;
    }

    @Override
    public JavaType handleUnknownTypeId(JavaType existingType, String typeId, TypeIdResolver idResolver, String description)
            throws IOException {
        throw new JsonMappingException("Test error");
    }
}