import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_TypeWithNoGenerics_Success() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("java.lang.String");
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
    }
    
    @Test
    public void testParse_TypeWithGenerics_Success() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertNotNull(type.getBindings());
        assertEquals(1, type.getBindings().size());
        JavaType parameterType = type.getBindings().getTypeParameters().get(0);
        assertEquals(String.class, parameterType.getRawClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParse_InvalidType_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.lang.String<");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParse_UnexpectedTokenAfterCompleteType_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util.List<java.lang.String> extraToken");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse_UnexpectedEndOfString_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse_UnexpectedTokenInTypeList_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util.List<java.lang.String and java.lang.Integer>");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse_CannotLocateClass_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("com.nonexistent.Type");
    }
}