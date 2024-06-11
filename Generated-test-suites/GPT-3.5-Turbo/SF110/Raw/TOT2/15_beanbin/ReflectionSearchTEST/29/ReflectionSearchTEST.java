
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        
        // Edge cases
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("Te*tAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("testMethod");
        assertEquals(1, reflectionSearch.getMethods().size());
        
        // Error scenario
        reflectionSearch.methodsThatHave("nonExistingMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatDontHave("nonExistingMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
        
        // Error scenario
        reflectionSearch.methodsThatDontHave("testMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertEquals(0, reflectionSearch.getMethods().size());
        
        // Edge case
        reflectionSearch.methodsThatHave("testMethod");
        assertEquals(1, reflectionSearch.getMethods().size());
    }
}
