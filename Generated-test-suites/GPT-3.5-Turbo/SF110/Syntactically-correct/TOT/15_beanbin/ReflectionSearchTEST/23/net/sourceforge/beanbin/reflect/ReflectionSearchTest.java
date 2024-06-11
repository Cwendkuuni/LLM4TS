import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Test
    public void testHasAnnotation_WithWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        assertTrue(reflectionSearch.hasAnnotation("*Annotation")); // Typical use case
        assertFalse(reflectionSearch.hasAnnotation("*Annotated")); // Edge case
        assertFalse(reflectionSearch.hasAnnotation("*OtherAnnotation")); // Error scenario
    }

    @Test
    public void testHasAnnotation_WithoutWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        assertTrue(reflectionSearch.hasAnnotation("Annotation")); // Typical use case
        assertFalse(reflectionSearch.hasAnnotation("Annotated")); // Edge case
        assertFalse(reflectionSearch.hasAnnotation("OtherAnnotation")); // Error scenario
    }

    @Test
    public void testMethodsThatHave_WithWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.methodsThatHave("*Method");

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testMethodsThatHave_WithoutWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.methodsThatHave("Method");

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testMethodsThatDontHave_WithWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.methodsThatDontHave("*Method");

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testMethodsThatDontHave_WithoutWildcard() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.methodsThatDontHave("Method");

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testAnd() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.and();

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testOr() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        ReflectionSearch result = reflectionSearch.or();

        assertNotNull(result);
        // Perform assertions on the updated ReflectionSearch object
    }

    @Test
    public void testGetMethods() {
        Class<TestClass> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.getMethods();

        assertNotNull(methods);
        // Perform assertions on the list of methods
    }

    // Additional test cases for the protected helper methods, intersect and union, can be added here if necessary

    // Test classes used for testing ReflectionSearch
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface OtherAnnotation {
    }

    @TestAnnotation
    @OtherAnnotation
    private class TestClass {
        public void publicMethod() {
        }

        protected void protectedMethod() {
        }

        private void privateMethod() {
        }
    }

}