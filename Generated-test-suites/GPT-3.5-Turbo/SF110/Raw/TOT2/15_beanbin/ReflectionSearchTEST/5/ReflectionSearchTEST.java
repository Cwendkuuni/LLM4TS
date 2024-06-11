import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.lang.reflect.*;

public class ReflectionSearchTest {

    // Test case for hasAnnotation method
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    // Test case for hasWildcard method
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasWildcard("name*"));
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    // Test case for methodsThatHave method
    @Test
    public void testMethodsThatHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        try {
            reflectionSearch.methodsThatHave("testMethod");
        } catch (BeanBinException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for methodsThatDontHave method
    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        try {
            reflectionSearch.methodsThatDontHave("nonExistentMethod");
        } catch (BeanBinException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for getMethods method
    @Test
    public void testGetMethods() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        try {
            List<Method> methods = reflectionSearch.getMethods();
            assertNotNull(methods);
            assertEquals(1, methods.size());
        } catch (BeanBinException e) {
            fail("Exception should not be thrown");
        }
    }

    // Testing intersection of two lists for intersect method
    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        List<Method> list1 = new ArrayList<>();
        List<Method> list2 = new ArrayList<>();
        Method method = TestClass.class.getDeclaredMethods()[1];
        list1.add(method);
        list2.add(method);
        reflectionSearch.intersect(list1, list2);
        assertEquals(1, list1.size());
    }

    // Testing union of two lists for union method
    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        List<Method> list1 = new ArrayList<>();
        List<Method> list2 = new ArrayList<>();
        Method method = TestClass.class.getDeclaredMethods()[1];
        list1.add(method);
        reflectionSearch.union(list1, list2);
        assertEquals(1, list1.size());
    }
}