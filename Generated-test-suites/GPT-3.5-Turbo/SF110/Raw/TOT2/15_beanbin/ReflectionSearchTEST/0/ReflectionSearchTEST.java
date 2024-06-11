import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Method;
import net.sourceforge.beanbin.BeanBinException;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        
        // Edge case: Empty term
        assertFalse(reflectionSearch.hasAnnotation(""));
        
        // Exception handling: Invalid term
        assertFalse(reflectionSearch.hasAnnotation("InvalidAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("length");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        
        // Edge case: Non-existing term
        reflectionSearch.methodsThatHave("nonExistingMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        // Exception handling: Empty term
        try {
            reflectionSearch.methodsThatHave("");
            fail("Should have thrown BeanBinException for empty term");
        } catch (BeanBinException e) {
            assertEquals("Term cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("length");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotEquals(1, methods.size());
        
        // Edge case: Non-existing term
        reflectionSearch.methodsThatDontHave("nonExistingMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        
        // Exception handling: Empty term
        try {
            reflectionSearch.methodsThatDontHave("");
            fail("Should have thrown BeanBinException for empty term");
        } catch (BeanBinException e) {
            assertEquals("Term cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(List.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        
        // Edge case: No methods found
        reflectionSearch.methodsThatHave("nonExistingMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        // Exception handling: Query criteria is null
        try {
            reflectionSearch.methodsThatHave(null);
            reflectionSearch.getMethods();
        } catch (BeanBinException e) {
            assertEquals("Query criteria cannot be null", e.getMessage());
        }
    }
}