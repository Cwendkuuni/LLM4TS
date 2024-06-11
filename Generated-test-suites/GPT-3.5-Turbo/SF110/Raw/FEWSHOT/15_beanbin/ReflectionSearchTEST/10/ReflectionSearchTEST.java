import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);

        assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
        assertFalse(reflectionSearch.hasAnnotation("Annotation2"));
        assertTrue(reflectionSearch.hasAnnotation("*Annotation*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("method1");
        List<Method> expected = new ArrayList<>();
        expected.add(TestClass.class.getMethod("method1"));
        List<Method> actual = reflectionSearch.getMethods();
        assertEquals(expected, actual);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatDontHave("method1");
        List<Method> expected = new ArrayList<>();
        expected.add(TestClass.class.getMethod("method2"));
        expected.add(TestClass.class.getMethod("method3"));
        List<Method> actual = reflectionSearch.getMethods();
        assertEquals(expected, actual);
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("method1").and().methodsThatHave("method2");
        List<Method> expected = new ArrayList<>();
        expected.add(TestClass.class.getMethod("method1"));
        expected.add(TestClass.class.getMethod("method2"));
        List<Method> actual = reflectionSearch.getMethods();
        assertEquals(expected, actual);
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("method1").or().methodsThatHave("method2");
        List<Method> expected = new ArrayList<>();
        expected.add(TestClass.class.getMethod("method1"));
        expected.add(TestClass.class.getMethod("method2"));
        expected.add(TestClass.class.getMethod("method3"));
        List<Method> actual = reflectionSearch.getMethods();
        assertEquals(expected, actual);
    }

    private static class TestClass {
        @Annotation1
        public void method1() {}

        @Annotation2
        public void method2() {}

        public void method3() {}
    }
}