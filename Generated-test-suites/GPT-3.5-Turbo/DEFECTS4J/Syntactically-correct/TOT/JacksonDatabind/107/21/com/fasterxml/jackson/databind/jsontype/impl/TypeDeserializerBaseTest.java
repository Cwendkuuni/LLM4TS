
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeDeserializerBaseTest {

    private static class DummyTypeDeserializer extends TypeDeserializerBase {

        protected DummyTypeDeserializer(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        protected DummyTypeDeserializer(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new DummyTypeDeserializer(this, prop);
        }

        @Override
        public JavaType baseType() {
            return _baseType;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
            throw new IOException("Missing TypeId");
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
            throw new IOException("Unknown TypeId");
        }

        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private DummyTypeDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        deserializer = new DummyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @After
    public void tearDown() {
        deserializer = null;
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializer result = deserializer.forProperty(prop);
        assertNotNull(result);
        assertTrue(result instanceof DummyTypeDeserializer);

        prop = new BeanProperty() {
            @Override
            public String getName() {
                return "testProperty";
            }

            @SuppressWarnings("rawtypes")
            @Override
            public JavaType getType() {
                return TypeFactory.defaultInstance().constructType(Map.class);
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> arg0) {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> boolean hasAnnotation(Class<A> arg0) {
                return false;
            }

            @Override
            public boolean isShapeWrittenUsingIndex() {
                return false;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        };

        result = deserializer.forProperty(prop);
        assertNotNull(result);
        assertTrue(result instanceof DummyTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(null, deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.String", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = deserializer.baseType();
        assertEquals(String.class, baseType.getRawClass());
    }

    @Test
    public void testToString() {
        String expected = "[DummyTypeDeserializer; base-type:class java.lang.String; id-resolver: null]";
        String result = deserializer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = deserializer.deserializeWithType(jp, ctxt);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = null;
        deserializer._handleMissingTypeId(ctxt, "Missing Type Id Desc");
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        deserializer._handleUnknownTypeId(ctxt, "Unknown Type Id");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = "type_id";
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId_Success() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNotNull(result);
        assertNull(result);
    }
}
