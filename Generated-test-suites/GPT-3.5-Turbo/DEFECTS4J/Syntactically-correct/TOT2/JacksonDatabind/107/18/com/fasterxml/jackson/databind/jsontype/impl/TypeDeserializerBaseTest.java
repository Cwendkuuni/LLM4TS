import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", base.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertEquals("_typePropertyName", base.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertNotNull(base.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertNull(base.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertNotNull(base.baseType());
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase base = new TypeDeserializerBase();
        assertTrue(base.toString().startsWith("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
    }
}