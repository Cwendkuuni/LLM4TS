import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Typical use case: Class has the specified annotation
        assertTrue(reflectionSearch.hasAnnotation("Test"));

        // Edge case: Class does not have any annotation
        assertFalse(reflectionSearch.hasAnnotation("Override"));

        // Error scenario: Invalid annotation name
        assertFalse(reflectionSearch.hasAnnotation("InvalidAnnotation"));
    }

    @Test
    public void testMethodsThatHave() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        try {
            // Typical use case: Class has methods with the specified term
            reflectionSearch.methodsThatHave("test");
            assertEquals(4, reflectionSearch.getMethods().size());

            // Edge case: Class does not have any methods with the specified term
            reflectionSearch.methodsThatHave("invalid");
            assertEquals(0, reflectionSearch.getMethods().size());

            // Error scenario: Null term provided
            reflectionSearch.methodsThatHave(null);
            fail("Expected BeanBinException");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        try {
            // Typical use case: Class does not have methods with the specified term
            reflectionSearch.methodsThatDontHave("invalid");
            assertEquals(4, reflectionSearch.getMethods().size());

            // Edge case: Class has all methods with the specified term
            reflectionSearch.methodsThatDontHave("test");
            assertEquals(0, reflectionSearch.getMethods().size());

            // Error scenario: Null term provided
            reflectionSearch.methodsThatDontHave(null);
            fail("Expected BeanBinException");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testAnd() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Typical use case: Querying methods using "and" conditional
        try {
            reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("invalid");
            assertEquals(4, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException");
        }

        // Edge case: Query with only one criteria
        try {
            reflectionSearch.methodsThatHave("test").and();
            fail("Expected BeanBinException");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testOr() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Typical use case: Querying methods using "or" conditional
        try {
            reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("invalid");
            assertEquals(4, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException");
        }

        // Edge case: Query with only one criteria
        try {
            reflectionSearch.methodsThatHave("test").or();
            fail("Expected BeanBinException");
        } catch (BeanBinException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMethods() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        try {
            // Typical use case: Get all methods of the class
            assertEquals(5, reflectionSearch.getMethods().size());

            // Edge case: Class does not have any methods
            clazz = ReflectionSearch.class;
            reflectionSearch = new ReflectionSearch(clazz);
            assertEquals(0, reflectionSearch.getMethods().size());

            // Error scenario: Null class provided
            reflectionSearch = new ReflectionSearch(null);
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected BeanBinException");
        }
    }

    // Helper methods for testing intersection and union methods
    
    @Test
    public void testIntersect() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Typical use case: Intersecting two lists of methods
        List<Method> master = reflectionSearch.getMethods();
        List<Method> toadd = reflectionSearch.getMethods();
        reflectionSearch.intersect(master, toadd);
        assertEquals(master.size(), reflectionSearch.getMethods().size());

        // Edge case: No common methods between the two lists
        master.clear();
        reflectionSearch.intersect(master, toadd);
        assertEquals(0, reflectionSearch.getMethods().size());
    }

    @Test
    public void testUnion() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Typical use case: Creating a union of two lists of methods
        List<Method> master = reflectionSearch.getMethods();
        List<Method> toadd = reflectionSearch.getMethods();
        reflectionSearch.union(master, toadd);
        assertEquals(master.size(), reflectionSearch.getMethods().size());

        // Edge case: No duplicate methods in the union
        master.addAll(toadd);
        reflectionSearch.union(master, toadd);
        assertEquals(master.size(), reflectionSearch.getMethods().size());
    }
}