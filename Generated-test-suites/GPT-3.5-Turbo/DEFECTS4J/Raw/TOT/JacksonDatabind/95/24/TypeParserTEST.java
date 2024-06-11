import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {
    
    private TypeFactory typeFactory;
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse_withEmptyString_shouldThrowIllegalArgumentException() {
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_withInvalidClass_shouldThrowIllegalArgumentException() {
        try {
            typeParser.parse("InvalidClass");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidClass' (remaining: 'InvalidClass'): Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testParse_withUndeterminedClassName_shouldThrowIllegalArgumentException() {
        try {
            typeParser.parse("com.foo.");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.foo.' (remaining: 'com.foo.'): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_withNonGenericType_shouldReturnJavaType() {
        try {
            JavaType javaType = typeParser.parse("java.util.List");
            assertEquals(java.util.List.class, javaType.getRawClass());
            assertNull(javaType.getBindings());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_withGenericType_shouldReturnJavaType() {
        try {
            JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
            assertEquals(java.util.Map.class, javaType.getRawClass());
            assertNotNull(javaType.getBindings());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
}