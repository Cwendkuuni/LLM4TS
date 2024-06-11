import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;

    @Test
    public void testHasAnnotation() {
        reflectionSearch = new ReflectionSearch(TestClass.class);

        // Test case 1: Annotation exists
        boolean result1 = reflectionSearch.hasAnnotation("MyAnnotation");
        assertTrue(result1);

        // Test case 2: Annotation does not exist
        boolean result2 = reflectionSearch.hasAnnotation("SomeAnnotation");
        assertFalse(result2);

        // Test case 3: Annotation with wildcard exists in name
        boolean result3 = reflectionSearch.hasAnnotation("*Annotation");
        assertTrue(result3);

        // Test case 4: Annotation with wildcard exists in class name
        boolean result4 = reflectionSearch.hasAnnotation("MyAnn*");
        assertTrue(result4);
    }

    @Test
    public void testMethodsThatHaveAndMethodsThatDontHave() throws Exception {
        reflectionSearch = new ReflectionSearch(TestClass.class);

        // Test case 1: Methods that have term
        List<Method> methods1 = reflectionSearch.methodsThatHave("term").getMethods();
        assertEquals(2, methods1.size());
        // verify methods1 contains the expected methods

        // Test case 2: Methods that don't have term
        List<Method> methods2 = reflectionSearch.methodsThatDontHave("term").getMethods();
        assertEquals(1, methods2.size());
        // verify methods2 contains the expected method
    }

    @Test
    public void testAndAndOr() throws Exception {
        reflectionSearch = new ReflectionSearch(TestClass.class);

        // Test case 1: Method chaining with and()
        List<Method> methods1 = reflectionSearch.methodsThatHave("term1").and().methodsThatHave("term2").getMethods();
        assertEquals(1, methods1.size());
        // verify methods1 contains the expected method

        // Test case 2: Method chaining with or()
        List<Method> methods2 = reflectionSearch.methodsThatHave("term1").or().methodsThatHave("term2").getMethods();
        assertEquals(3, methods2.size());
        // verify methods2 contains the expected methods
    }

    @Test
    public void testGetMethods() throws Exception {
        reflectionSearch = new ReflectionSearch(TestClass.class);

        // Test case 1: Get all methods
        List<Method> allMethods = reflectionSearch.getMethods();
        assertEquals(3, allMethods.size());
        // verify allMethods contains all the methods of the TestClass
    }

    class TestClass {
        @MyAnnotation
        public void method1() {}

        @MyOtherAnnotation
        public void method2() {}

        public void method3() {}
    }
}