
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Check if method correctly identifies annotations
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasAnnotation("SuppressWarnings"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Check if method correctly filters methods that have specific term
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("toString");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Check if method correctly filters methods that don't have specific term
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("hashCode");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    // Add more test cases for and(), or(), getMethods(), intersect(), union() methods if needed

}
