import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase_forProperty() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.forProperty(null));
    }

    @Test
    public void testTypeDeserializerBase_getTypeInclusion() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.getTypeInclusion());
    }

    @Test
    public void testTypeDeserializerBase_getPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.getPropertyName());
    }

    @Test
    public void testTypeDeserializerBase_getTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.getTypeIdResolver());
    }

    @Test
    public void testTypeDeserializerBase_getDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.getDefaultImpl());
    }

    @Test
    public void testTypeDeserializerBase_toString() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNotNull(base.toString());
    }

    @Test
    public void testTypeDeserializerBase_baseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNotNull(base.baseTypeName());
    }

    @Test
    public void testTypeDeserializerBase_findContextualValueDeserializer() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.findContextualValueDeserializer(null, null));
    }

    @Test
    public void testTypeDeserializerBase_deserialize() {
        TypeDeserializerBase base = new TypeDeserializerBaseDummy();
        assertNull(base.deserialize(null, null));
    }

    private class TypeDeserializerBaseDummy extends TypeDeserializerBase {

        public TypeDeserializerBaseDummy() {
            super(null, null, null, false, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}