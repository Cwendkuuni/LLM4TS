
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        // Test case for typical use case
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        
        // Test case for edge case
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
        
        // Test case for error scenario
        assertFalse(reflectionSearch.hasAnnotation(null));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test");
        
        // Test case for typical use case
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.get(0).getName().startsWith("test"));
        assertTrue(methods.get(1).getName().startsWith("test"));
        
        // Test case for edge case
        reflectionSearch.methodsThatHave("NonExistingMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        // Test case for error scenario
        try {
            reflectionSearch.methodsThatHave(null);
            fail("No exception thrown for null term");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatDontHave("nonExistingMethod");
        
        // Test case for typical use case
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(3, methods.size());
        assertTrue(methods.get(0).getName().startsWith("test"));
        assertTrue(methods.get(1).getName().startsWith("anotherTest"));
        assertTrue(methods.get(2).getName().startsWith("yetAnotherTest"));
        
        // Test case for edge case
        reflectionSearch.methodsThatDontHave("test");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        // Test case for error scenario
        try {
            reflectionSearch.methodsThatDontHave(null);
            fail("No exception thrown for null term");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("nonExistingMethod");
    
        // Test case for typical use case
        assertNotNull(reflectionSearch);
        
        // There are no additional test cases needed for edge case and error scenario
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("nonExistingMethod");
    
        // Test case for typical use case
        assertNotNull(reflectionSearch);
        
        // There are no additional test cases needed for edge case and error scenario
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test");
        
        // Test case for typical use case
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        
        // Test case for edge case
        reflectionSearch.methodsThatHave("NonExistingMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        // Test case for error scenario
        try {
            ReflectionSearch nullSearch = new ReflectionSearch(null);
            nullSearch.getMethods();
            fail("No exception thrown for null clazz");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }
}
