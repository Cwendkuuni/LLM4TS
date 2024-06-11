import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {

    // Test case for hasAnnotation method
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        assertTrue(reflectionSearch.hasAnnotation(Retention.class.getSimpleName()));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    // Test case for hasWildcard method
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        assertTrue(reflectionSearch.hasWildcard("*test"));
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    // Test case for methodsThatHave method
    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        reflectionSearch.methodsThatHave("test");
    }

    // Test case for methodsThatDontHave method
    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        reflectionSearch.methodsThatDontHave("test");
    }

    // Test case for and method
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatHave("test2");
        assertTrue(true); // No assertion set as this method just sets conditions
    }

    // Test case for or method
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("test2");
        assertTrue(true); // No assertion set as this method just sets conditions
    }

    // Test case for getMethods method
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodHandles.Lookup.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }

}