import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);

        assertNotNull(deserializer);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.forProperty(null));
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNull(deserializer.baseType());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        assertNotNull(deserializer.toString());
    }

    @Test
    public void testFindDeserializer() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        try {
            deserializer._findDeserializer(null, null);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        try {
            deserializer._findDefaultImplDeserializer(null);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        try {
            deserializer._deserializeWithNativeTypeId(null, null);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testHandleUnknownTypeId() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        try {
            deserializer._handleUnknownTypeId(null, null);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testHandleMissingTypeId() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        try {
            deserializer._handleMissingTypeId(null, null);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    private class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
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