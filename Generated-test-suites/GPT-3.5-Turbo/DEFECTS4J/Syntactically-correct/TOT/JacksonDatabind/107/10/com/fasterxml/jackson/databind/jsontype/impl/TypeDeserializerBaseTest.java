
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Test
    public void testForProperty() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertNull(typeDeserializer.forProperty(null));
    }

    @Test
    public void testGetTypeInclusion() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertNotNull(typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertEquals("java.lang.String", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertNotNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertNotNull(typeDeserializer.getDefaultImpl());
        assertEquals(String.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        typeDeserializer = new TypeDeserializerBaseMock();
        assertNotNull(typeDeserializer.baseType());
        assertEquals(String.class, typeDeserializer.baseType());
    }

    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {

        protected TypeDeserializerBaseMock() {
            super(null, null, "type", true, null);
        }

        @Override
        public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public JsonDeserializer<Object> forProperty(org.codehaus.jackson.map.BeanProperty prop) {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
            return null;
        }
    }
}
