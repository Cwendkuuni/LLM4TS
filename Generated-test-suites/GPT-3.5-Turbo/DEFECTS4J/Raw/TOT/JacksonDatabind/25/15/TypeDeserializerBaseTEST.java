import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {};
        assertEquals("java.lang.Object", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "type", false, null) {};
        assertEquals("type", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new TypeIdResolverImpl();
        TypeDeserializerBase base = new TypeDeserializerBase(null, resolver, null, false, null) {};
        assertEquals(resolver, base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {};
        assertNull(base.getDefaultImpl());

        base = new TypeDeserializerBase(null, null, null, false, String.class) {};
        assertEquals(String.class, base.getDefaultImpl());

        base = new TypeDeserializerBase(null, null, null, false, int.class) {};
        assertEquals(int.class, base.getDefaultImpl());
    }

    @Test
    public void testToString() {
        JavaType baseType = new JavaTypeImpl(null);
        TypeIdResolver resolver = new TypeIdResolverImpl();
        TypeDeserializerBase base = new TypeDeserializerBase(baseType, resolver, "type", false, Object.class) {};

        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:com.fasterxml.jackson.databind.jsontype.impl.JavaTypeImpl; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl]";
        assertEquals(expected, base.toString());
    }

}