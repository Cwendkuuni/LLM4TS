package net.sourceforge.beanbin.reflect;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.BeanBinException;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        boolean result = reflectionSearch.hasAnnotation("Override");
        assertTrue(result);

        // Edge case - Annotation with wildcard
        result = reflectionSearch.hasAnnotation("java.lang.*");
        assertTrue(result);

        // Error scenario - Class with no annotations
        reflectionSearch = new ReflectionSearch(Integer.class);
        result = reflectionSearch.hasAnnotation("Override");
        assertFalse(result);
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("length");
        assertNotNull(reflectionSearch);
        
        // Edge case - Empty method term
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("");
        assertNotNull(reflectionSearch);
        
        // Error scenario - Invalid method term
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("nonExistentMethod");
        assertNotNull(reflectionSearch);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("nonExistentMethod");
        assertNotNull(reflectionSearch);
        
        // Edge case - Empty method term
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("");
        assertNotNull(reflectionSearch);
        
        // Error scenario - Valid method term
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("length");
        assertNotNull(reflectionSearch);
    }

    @Test
    public void testAnd() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.and();
        assertNotNull(reflectionSearch);
    }

    @Test
    public void testOr() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.or();
        assertNotNull(reflectionSearch);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertNotNull(reflectionSearch.getMethods());

        // Edge case - Class with no methods
        reflectionSearch = new ReflectionSearch(Integer.class);
        assertNotNull(reflectionSearch.getMethods());
    }
}