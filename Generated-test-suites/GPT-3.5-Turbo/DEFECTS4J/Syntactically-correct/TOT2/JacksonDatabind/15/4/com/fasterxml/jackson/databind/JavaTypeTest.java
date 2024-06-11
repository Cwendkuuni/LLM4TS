import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test withStaticTyping method
        JavaType result = javaType.withStaticTyping();
        
        // Verify the result
        assertTrue(result.useStaticType());
    }

    @Test
    public void testNarrowBy_sameClass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test narrowBy method with same class
        JavaType result = javaType.narrowBy(getClass());
        
        // Verify the result
        assertEquals(javaType, result);
    }

    @Test
    public void testNarrowBy_validSubclass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test narrowBy method with valid subclass
        JavaType result = javaType.narrowBy(JavaType.class);
        
        // Verify the result
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowBy_invalidSubclass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test narrowBy method with invalid subclass
        javaType.narrowBy(String.class);
    }

    @Test
    public void testForcedNarrowBy_sameClass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test forcedNarrowBy method with same class
        JavaType result = javaType.forcedNarrowBy(getClass());
        
        // Verify the result
        assertEquals(javaType, result);
    }

    @Test
    public void testForcedNarrowBy_validSubclass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test forcedNarrowBy method with valid subclass
        JavaType result = javaType.forcedNarrowBy(JavaType.class);
        
        // Verify the result
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForcedNarrowBy_invalidSubclass() {
        // Create a JavaType instance
        JavaType javaType = new ConcreteJavaType(getClass(), 0, null, null, false);
        
        // Test forcedNarrowBy method with invalid subclass
        javaType.forcedNarrowBy(String.class);
    }
}