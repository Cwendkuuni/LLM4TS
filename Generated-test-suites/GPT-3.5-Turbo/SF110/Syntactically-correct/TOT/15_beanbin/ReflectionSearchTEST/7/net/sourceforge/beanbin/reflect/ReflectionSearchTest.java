import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import net.sourceforge.beanbin.reflect.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        // Typical use case: Class has the specified annotation
        assertTrue(reflectionSearch.hasAnnotation("Test"));

        // Edge case: Class does not have the specified annotation
        assertFalse(reflectionSearch.hasAnnotation("Deprecated"));
    }

    @Test
    public void testHasAnnotation_Error() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        // Error scenario: Invalid term with wildcard
        assertFalse(reflectionSearch.hasAnnotation("*Test"));
    }

    @Test
    public void testMethodsThatHave() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        try {
            // Typical use case: Method with the specified term exists
            reflectionSearch.methodsThatHave("testHasAnnotation");
            assertEquals(1, reflectionSearch.getMethods().size());

            // Typical use case: Method with the specified term does not exist
            reflectionSearch.methodsThatHave("invalidMethod");
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException occurred");
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        try {
            // Typical use case: Method with the specified term does not exist
            reflectionSearch.methodsThatDontHave("invalidMethod");
            assertEquals(1, reflectionSearch.getMethods().size());

            // Typical use case: Method with the specified term exists
            reflectionSearch.methodsThatDontHave("testHasAnnotation");
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException occurred");
        }
    }

    @Test
    public void testAnd() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        reflectionSearch.methodsThatHave("testHasAnnotation").and();
        reflectionSearch.methodsThatHave("testMethodsThatHave");

        try {
            // Typical use case: Methods match both criteria (AND condition)
            assertEquals(1, reflectionSearch.getMethods().size());

            // Edge case: Only one method matches the second criteria
            reflectionSearch.methodsThatHave("testMethodsThatDontHave");
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException occurred");
        }
    }

    @Test
    public void testOr() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        reflectionSearch.methodsThatHave("testHasAnnotation").or();
        reflectionSearch.methodsThatHave("testMethodsThatHave");

        try {
            // Typical use case: At least one method matches either criteria (OR condition)
            assertEquals(testClass.getDeclaredMethods().length, reflectionSearch.getMethods().size());

            // Edge case: No methods match both criteria
            reflectionSearch.methodsThatHave("testMethodsThatDontHave");
            assertEquals(testClass.getDeclaredMethods().length, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException occurred");
        }
    }

    @Test
    public void testGetMethods() {
        Class testClass = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

        try {
            // Typical use case: Get all methods of the class
            assertEquals(testClass.getDeclaredMethods().length, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException occurred");
        }
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();

        // Typical use case: Intersect two lists of methods
        try {
            Method method1 = ReflectionSearchTest.class.getDeclaredMethod("testIntersect");
            Method method2 = ReflectionSearchTest.class.getDeclaredMethod("testUnion");
            Method method3 = ReflectionSearchTest.class.getDeclaredMethod("testGetMethods");
            master.add(method1);
            master.add(method2);
            toadd.add(method2);
            toadd.add(method3);

            reflectionSearch.intersect(master, toadd);

            assertEquals(1, master.size());
            assertTrue(master.contains(method2));
        } catch (NoSuchMethodException e) {
            fail("Unexpected NoSuchMethodException occurred");
        }
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();

        // Typical use case: Union two lists of methods
        try {
            Method method1 = ReflectionSearchTest.class.getDeclaredMethod("testIntersect");
            Method method2 = ReflectionSearchTest.class.getDeclaredMethod("testUnion");
            Method method3 = ReflectionSearchTest.class.getDeclaredMethod("testGetMethods");
            master.add(method1);
            toadd.add(method2);
            toadd.add(method3);

            reflectionSearch.union(master, toadd);

            assertEquals(3, master.size());
            assertTrue(master.containsAll(toadd));
        } catch (NoSuchMethodException e) {
            fail("Unexpected NoSuchMethodException occurred");
        }
    }
}