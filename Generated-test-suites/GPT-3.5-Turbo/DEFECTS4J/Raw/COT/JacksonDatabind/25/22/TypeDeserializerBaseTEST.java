import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBaseTestImpl();
        assertEquals("java.lang.Object", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBaseTestImpl();
        assertEquals("_typePropertyName", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBaseTestImpl();
        assertNull(base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBaseTestImpl();
        assertNull(base.getDefaultImpl());
    }

    private static class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        private static final long serialVersionUID = 1L;

        public TypeDeserializerBaseTestImpl() {
            super(null, null, "_typePropertyName", false, Object.class);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }

    }
}