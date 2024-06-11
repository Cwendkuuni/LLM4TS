import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation_shouldReturnTrue() {
        boolean result = reflectionSearch.hasAnnotation("Override");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasAnnotation_withWildcard_shouldReturnTrue() {
        boolean result = reflectionSearch.hasAnnotation("java*l.annotation.Retention");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasAnnotation_shouldReturnFalse() {
        boolean result = reflectionSearch.hasAnnotation("NonExistentAnnotation");
        Assert.assertFalse(result);
    }

    @Test
    public void testMethodsThatHave_shouldReturnMethods() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("toString");
        Assert.assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave_shouldReturnMethods() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("hashCode");
        Assert.assertNotNull(result);
    }

    @Test
    public void testAnd_shouldReturnReflectionSearch() {
        ReflectionSearch result = reflectionSearch.and();
        Assert.assertNotNull(result);
    }

    @Test
    public void testOr_shouldReturnReflectionSearch() {
        ReflectionSearch result = reflectionSearch.or();
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetMethods_shouldReturnMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertNotNull(methods);
        Assert.assertFalse(methods.isEmpty());
    }

    // Additional test cases for edge and error scenarios:
    
    @Test (expected = BeanBinException.class)
    public void testMethodsThatHave_withNonExistentMethod_shouldThrowException() throws BeanBinException {
        reflectionSearch.methodsThatHave("nonExistentMethod");
    }

    @Test
    public void testGetMethods_withEmptyQuery_shouldReturnEmptyList() throws BeanBinException {
        ReflectionSearch emptyQueryReflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        List<Method> methods = emptyQueryReflectionSearch.getMethods();
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.isEmpty());
    }
}