import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        String baseTypeName = deserializer.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        String propertyName = deserializer.getPropertyName();
        assertEquals("_typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        TypeIdResolver resolver = deserializer.getTypeIdResolver();
        assertEquals(null, resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        Class<?> defaultImpl = deserializer.getDefaultImpl();
        assertEquals(null, defaultImpl);
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        JavaType baseType = deserializer.baseType();
        assertEquals(null, baseType);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseImpl();
        String str = deserializer.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", str);
    }
}