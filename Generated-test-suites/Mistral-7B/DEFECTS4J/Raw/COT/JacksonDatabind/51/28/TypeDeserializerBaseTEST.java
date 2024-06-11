package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        testee = new TypeDeserializerBase(
                new JavaType(String.class),
                new TypeIdResolver() {
                    @Override
                    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                        return ctxt.getTypeFactory().constructType(typeId);
                    }
                },
                "",
                false,
                String.class
        );
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("test", String.class);
        TypeDeserializer actual = testee.forProperty(property);
        TypeDeserializer expected = new TypeDeserializerBase(
                new JavaType(String.class),
                testee._idResolver,
                property.getName(),
                false,
                String.class
        );
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() throws Exception {
        testee.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As actual = testee.getTypeInclusion();
        JsonTypeInfo.As expected = JsonTypeInfo.As.EXISTING_PROPERTY;
        assertEquals(expected, actual);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String actual = testee.baseTypeName();
        String expected = "java.lang.String";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String actual = testee.getPropertyName();
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver actual = testee.getTypeIdResolver();
        TypeIdResolver expected = testee._idResolver;
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> actual = testee.getDefaultImpl();
        Class<?> expected = String.class;
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void testToString() throws Exception {
        String actual = testee.toString();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@1b6d39f4]";
        assertEquals(expected, actual);
    }

    @Test
    public void testFindDeserializerNullDeserializerMap() throws Exception {
        DeserializationContext context = createContext();
        String typeId = "java.util.Map";
        JsonDeserializer<Object> actual = testee._findDeserializer(context, typeId);
        assertNull(actual);
    }

    @Test
    public void testFindDeserializerEmptyDeserializerMap() throws Exception {
        DeserializationContext context = createContext();
        String typeId = "java.util.Map";
        TestDeserializer testDeserializer = new TestDeserializer();
        testee._deserializers.put(typeId, testDeserializer);
        JsonDeserializer<Object> actual = testee._findDeserializer(context, typeId);
        assertEquals(testDeserializer.getClass(), actual.getClass());
    }

    @Test
    public void testFindDeserializerTypeIdNotFound() throws Exception {
        DeserializationContext context = createContext();
        String typeId = "java.util.List";
        testee._deserializers.put("java.util.Map", new TestDeserializer());
        JsonDeserializer<Object> actual = testee._findDeserializer(context, typeId);
        assertNull(actual);
    }

    @Test
    public void testFindDeserializerTypeFound() throws Exception {
        DeserializationContext context = createContext();
        String typeId = "java.util.Map";
        TestDeserializer testDeserializer = new TestDeserializer();
        testee._deserializers.put(typeId, testDeserializer);
        JsonDeserializer<Object> actual = testee._findDeserializer(context, typeId);
        assertEquals(testDeserializer.getClass(), actual.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws Exception {
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(null);
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerEmptyDeserializerMap() throws Exception {
        DeserializationContext context = createContext();
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(context);
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerDefaultImplNotSet() throws Exception {
        DeserializationContext context = createContext();
        testee._defaultImpl = null;
        testee._defaultImplDeserializer = null;
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(context);
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerSetDefaultImpl() throws Exception {
        DeserializationContext context = createContext();
        JsonDeserializer<Object> defaultImplDeserializer = new TestDeserializer();
        testee._defaultImpl = null;
        testee._defaultImplDeserializer = defaultImplDeserializer;
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer.getClass(), actual.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerSetDefaultImplWithContextualDeserializer() throws Exception {
        DeserializationContext context = createContext();
        JsonDeserializer<Object> defaultImplDeserializer = new TestDeserializer();
        TestDeserializer contextualDeserializer = new TestDeserializer();
        JavaType defaultImplType = new JavaType(String.class);
        context.findValueDeserializer(defaultImplType, "");
        testee._defaultImpl = null;
        testee._defaultImplDeserializer = defaultImplDeserializer;
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer.getClass(), actual.getClass());
        JsonDeserializer<Object> actualContextual = context.findContextualValueDeserializer(defaultImplType, "");
        assertEquals(contextualDeserializer.getClass(), actualContextual.getClass());
        assertEquals(defaultImplDeserializer, actualContextual);
    }

    @Test
    public void testHandleUnknownTypeIdNullIdResolver() throws Exception {
        DeserializationContext context = createContext();
        JavaType actual = testee._handleUnknownTypeId(context, null, null, testee._baseType);
        assertNull(actual);
    }

    @Test
    public void testHandleUnknownTypeIdNotNullIdResolver() throws Exception {
        DeserializationContext context = createContext();
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                throw new IOException("Type id resolution failed");
            }
        };
        JavaType actual = testee._handleUnknownTypeId(context, "java.util.List", idResolver, testee._baseType);
        assertNull(actual);
    }

    @Test
    public void testHandleUnknownTypeIdKnownType() throws Exception {
        DeserializationContext context = createContext();
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return ctxt.getTypeFactory().constructType(typeId);
            }
        };
        JavaType actual = testee._handleUnknownTypeId(context, "java.util.List", idResolver, testee._baseType);
        assertNotNull(actual);
        assertNotNull(actual.getRawClass());
        assertEquals(JavaType.class, actual.getClass());
        assertEquals(List.class, actual.getRawClass());
    }

    @Test
    public void testHandleUnknownTypeIdUnknownType() throws Exception {
        DeserializationContext context = createContext();
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                throw new IOException("Type id resolution failed");
            }
        };
        JavaType actual = testee._handleUnknownTypeId(context, "com.example.MyClass", idResolver, testee._baseType);
        assertNull(actual);
    }

    private DeserializationContext createContext() {
        return new TestDeserializationContext();
    }

    private static class TestDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public JsonFactory _rootValueFactory() {
            return null;
        }

        @Override
        public JavaType handleUnknownTypeId(JavaType baseType, String typeId, TypeIdResolver idResolver, String[] typeIds) throws IOException {
            throw new IOException("Type id resolution failed");
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(JavaType type, BeanProperty property) {
            return null;
        }

        @Override
        public Converter<String, ?> findValueConverter(Class<String> type) {
            return null;
        }

        @Override
        public void findValueDeserializer(Class<?> type, BeanProperty property) throws IOException {
        }
    }
}