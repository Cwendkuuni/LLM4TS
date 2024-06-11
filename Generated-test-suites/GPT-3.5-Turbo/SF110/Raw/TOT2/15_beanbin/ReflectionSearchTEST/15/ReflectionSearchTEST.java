import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        assertFalse(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        assertTrue(reflectionSearch.hasWildcard("Test*"));
        assertFalse(reflectionSearch.hasWildcard("Test"));
    }

    @Test
    public void testMethodsThatHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        try {
            reflectionSearch.methodsThatHave("methodName");
        } catch (BeanBinException e) {
            fail("Exception thrown when it shouldn't have");
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        try {
            reflectionSearch.methodsThatDontHave("methodName");
        } catch (BeanBinException e) {
            fail("Exception thrown when it shouldn't have");
        }
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.and();
        assertEquals(result, reflectionSearch);
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.or();
        assertEquals(result, reflectionSearch);
    }

    @Test
    public void testGetMethods() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        try {
            assertTrue(reflectionSearch.getMethods().isEmpty());
        } catch (BeanBinException e) {
            fail("Exception thrown when it shouldn't have");
        }
    }

    @Test
    public void testIntersection() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> list1 = new ArrayList<>();
        List<Method> list2 = new ArrayList<>();
        Method method1 = Object.class.getMethod("hashCode");
        list1.add(method);
        list2.add(method);
        reflectionSearch.intersect(list1, list2);
        assertTrue(list1.contains(method1));
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> list1 = new ArrayList<>();
        List<Method> list2 = new ArrayList<>();
        Method method1 = Object.class.getMethod("toString");
        list1.add(method1);
        reflectionSearch.union(list1, list2);
        assertTrue(list1.contains(method1));
    }
}