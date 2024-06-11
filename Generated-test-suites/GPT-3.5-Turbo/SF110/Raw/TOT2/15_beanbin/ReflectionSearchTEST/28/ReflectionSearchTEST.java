import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Test case to check if hasAnnotation method correctly identifies annotations
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        boolean result = reflectionSearch.hasAnnotation("Override");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasWildcard() {
        // Test case to check the hasWildcard method with a term containing wildcard
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        boolean result = reflectionSearch.hasWildcard("String*");
        Assert.assertTrue(result);
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Test case to check the methodsThatHave method with a specific term
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("toString");
        Assert.assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Test case to check the methodsThatDontHave method with a specific term
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("toString");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Test case to check the getMethods method returns a list of methods
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertNotNull(methods);
    }

}