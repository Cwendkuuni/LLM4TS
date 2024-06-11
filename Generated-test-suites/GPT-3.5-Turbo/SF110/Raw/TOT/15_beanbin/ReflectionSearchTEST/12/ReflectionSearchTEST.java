import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Typical use case - class has annotation
        ReflectionSearch classWithAnnotation = new ReflectionSearch(ClassWithAnnotation.class);
        assertTrue(classWithAnnotation.hasAnnotation("MyAnnotation"));

        // Typical use case - class does not have annotation
        ReflectionSearch classWithoutAnnotation = new ReflectionSearch(ClassWithoutAnnotation.class);
        assertFalse(classWithoutAnnotation.hasAnnotation("MyAnnotation"));

        // Error scenario - term is null
        ReflectionSearch nullTerm = new ReflectionSearch(ClassWithAnnotation.class);
        assertFalse(nullTerm.hasAnnotation(null));

        // Error scenario - class is null
        ReflectionSearch nullClass = new ReflectionSearch(null);
        assertFalse(nullClass.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() {
        // Typical use case - class has method with matching term
        ReflectionSearch classWithMethod = new ReflectionSearch(ClassWithMethod.class);
        classWithMethod.methodsThatHave("methodName");
        List<Method> methods = classWithMethod.getMethods();
        assertEquals(1, methods.size());
        assertEquals("methodName", methods.get(0).getName());

        // Typical use case - class does not have any method with matching term
        ReflectionSearch classWithoutMethod = new ReflectionSearch(ClassWithoutMethod.class);
        classWithoutMethod.methodsThatHave("methodName");
        List<Method> methods = classWithoutMethod.getMethods();
        assertEquals(0, methods.size());

        // Error scenario - term is null
        ReflectionSearch nullTerm = new ReflectionSearch(ClassWithMethod.class);
        try {
            nullTerm.methodsThatHave(null);
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Term cannot be null", e.getMessage());
        }

        // Error scenario - class is null
        ReflectionSearch nullClass = new ReflectionSearch(null);
        try {
            nullClass.methodsThatHave("methodName");
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Class cannot be null", e.getMessage());
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        // Typical use case - class has method without matching term
        ReflectionSearch classWithMethod = new ReflectionSearch(ClassWithMethod.class);
        classWithMethod.methodsThatDontHave("nonExistentMethod");
        List<Method> methods = classWithMethod.getMethods();
        assertEquals(1, methods.size());
        assertEquals("methodName", methods.get(0).getName());

        // Typical use case - class has method with matching term
        ReflectionSearch classWithoutMethod = new ReflectionSearch(ClassWithoutMethod.class);
        classWithoutMethod.methodsThatDontHave("nonExistentMethod");
        List<Method> methods = classWithoutMethod.getMethods();
        assertEquals(0, methods.size());

        // Error scenario - term is null
        ReflectionSearch nullTerm = new ReflectionSearch(ClassWithMethod.class);
        try {
            nullTerm.methodsThatDontHave(null);
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Term cannot be null", e.getMessage());
        }

        // Error scenario - class is null
        ReflectionSearch nullClass = new ReflectionSearch(null);
        try {
            nullClass.methodsThatDontHave("nonExistentMethod");
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Class cannot be null", e.getMessage());
        }
    }

    @Test
    public void testAnd() {
        // Typical use case
        ReflectionSearch search = new ReflectionSearch(ClassWithMethod.class);
        search.methodsThatHave("method1").and().methodsThatHave("method2");
        List<Method> methods = search.getMethods();
        assertEquals(1, methods.size());
        assertEquals("method1", methods.get(0).getName());

        // Error scenario - calling 'and' without previous method call
        ReflectionSearch searchWithoutMethod = new ReflectionSearch(ClassWithMethod.class);
        try {
            searchWithoutMethod.and();
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("No method criteria specified", e.getMessage());
        }
    }

    @Test
    public void testOr() {
        // Typical use case
        ReflectionSearch search = new ReflectionSearch(ClassWithMethod.class);
        search.methodsThatHave("method1").or().methodsThatHave("method2");
        List<Method> methods = search.getMethods();
        assertEquals(2, methods.size());
        assertEquals("method1", methods.get(0).getName());
        assertEquals("method2", methods.get(1).getName());

        // Error scenario - calling 'or' without previous method call
        ReflectionSearch searchWithoutMethod = new ReflectionSearch(ClassWithMethod.class);
        try {
            searchWithoutMethod.or();
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("No method criteria specified", e.getMessage());
        }
    }

    @Test
    public void testGetMethods() {
        // Typical use case - class with multiple methods
        ReflectionSearch classWithMethods = new ReflectionSearch(ClassWithMethods.class);
        List<Method> methods = classWithMethods.getMethods();
        assertEquals(3, methods.size());

        // Typical use case - class with no methods
        ReflectionSearch classWithoutMethods = new ReflectionSearch(ClassWithoutMethods.class);
        List<Method> methods = classWithoutMethods.getMethods();
        assertEquals(0, methods.size());
    }

    private static class ClassWithAnnotation {
        @MyAnnotation
        public static void method() {}
    }

    private static class ClassWithoutAnnotation {
        public static void method() {}
    }

    private static class ClassWithMethod {
        public static void methodName() {}
    }

    private static class ClassWithoutMethod {}

    private static class ClassWithMethods {
        public static void method1() {}
        public static void method2() {}
        public static void method3() {}
    }

    private static class ClassWithoutMethods {}

}