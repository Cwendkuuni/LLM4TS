import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Test case with a matching annotation
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));

        // Test case with a non-matching annotation
        assertFalse(reflectionSearch.hasAnnotation("NonMatchingAnnotation"));
    }

    @Test
    public void testMethodsThatHave() {
        try {
            ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
            reflectionSearch.methodsThatHave("testMethod");
            List<Method> methods = reflectionSearch.getMethods();
            assertEquals(1, methods.size());
            assertEquals("testMethod", methods.get(0).getName());
        } catch (BeanBinException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        try {
            ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
            reflectionSearch.methodsThatDontHave("testMethod");
            List<Method> methods = reflectionSearch.getMethods();
            assertEquals(1, methods.size());
            assertEquals("otherMethod", methods.get(0).getName());
        } catch (BeanBinException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAnd() {
        try {
            ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
            reflectionSearch.methodsThatHave("testMethod").and().methodsThatDontHave("otherMethod");
            List<Method> methods = reflectionSearch.getMethods();
            assertEquals(1, methods.size());
            assertEquals("testMethod", methods.get(0).getName());
        } catch (BeanBinException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testOr() {
        try {
            ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
            reflectionSearch.methodsThatHave("testMethod").or().methodsThatHave("otherMethod");
            List<Method> methods = reflectionSearch.getMethods();
            assertEquals(2, methods.size());
            assertEquals("testMethod", methods.get(0).getName());
            assertEquals("otherMethod", methods.get(1).getName());
        } catch (BeanBinException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Helper class for the tests
    @TestAnnotation
    private static class TestClass {

        @TestAnnotation
        public void testMethod() {
            // Do nothing
        }

        public void otherMethod() {
            // Do nothing
        }
    }
}