import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        String baseTypeName = deserializer.baseTypeName();
        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        String propertyName = deserializer.getPropertyName();
        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = mock(TypeIdResolver.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, resolver, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        TypeIdResolver result = deserializer.getTypeIdResolver();
        assertEquals(resolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        Class<?> result = deserializer.getDefaultImpl();
        assertEquals(defaultImpl.getRawClass(), result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        JsonParser jp = mock(JsonParser.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return deser;
            }
        };

        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdFound() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        JsonParser jp = mock(JsonParser.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return deser;
            }
        };

        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, "typeName");
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NoDefaultValue() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };

        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, "typeName");
        assertNull(result);
    }
}