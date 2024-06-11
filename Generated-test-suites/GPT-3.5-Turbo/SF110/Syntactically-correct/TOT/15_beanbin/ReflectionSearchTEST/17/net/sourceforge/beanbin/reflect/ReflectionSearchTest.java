import org.junit.*;
import static org.junit.Assert.*;
import net.sourceforge.beanbin.reflect.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_typicalUseCase() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("Test");
        assertTrue(result);
    }

    @Test
    public void testHasAnnotation_edgeCase1() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("NonExistingAnnotation");
        assertFalse(result);
    }

    @Test
    public void testHasAnnotation_edgeCase2() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("");
        assertFalse(result);
    }

    @Test
    public void testHasAnnotation_errorScenario() {
        Class clazz = null; // Invalid class parameter
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("Test");
        assertFalse(result);
    }

    @Test
    public void testMethodsThatHave_typicalUseCase() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatHave("getValue");
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatHave_edgeCase1() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatHave("NonExistingMethod");
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatHave_edgeCase2() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatHave("");
        assertNotNull(result);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_errorScenario() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave(null); // Invalid method name parameter
    }

    @Test
    public void testMethodsThatDontHave_typicalUseCase() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("setValue");
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave_edgeCase1() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("NonExistingMethod");
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave_edgeCase2() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("");
        assertNotNull(result);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave_errorScenario() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave(null); // Invalid method name parameter
    }

    @Test
    public void testAnd_typicalUseCase() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }

    @Test
    public void testOr_typicalUseCase() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }

    @Test
    public void testGetMethods_typicalUseCase() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethods_errorScenario() throws BeanBinException {
        Class clazz = null; // Invalid class parameter
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }

    @Test
    public void testIntersect_typicalUseCase() {
        // Create two lists of methods for intersection
        List<Method> list1 = new ArrayList<>();
        list1.add(MyClass.class.getMethod("getValue"));
        list1.add(MyClass.class.getMethod("setValue"));

        List<Method> list2 = new ArrayList<>();
        list2.add(MyClass.class.getMethod("setValue"));

        // Perform intersection
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.intersect(list1, list2);

        // Validate intersection result
        assertEquals(1, list1.size());
        assertTrue(list1.contains(MyClass.class.getMethod("setValue")));
    }

    @Test
    public void testUnion_typicalUseCase() {
        // Create two lists of methods for union
        List<Method> list1 = new ArrayList<>();
        list1.add(MyClass.class.getMethod("getValue"));

        List<Method> list2 = new ArrayList<>();
        list2.add(MyClass.class.getMethod("setValue"));

        // Perform union
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.union(list1, list2);

        // Validate union result
        assertEquals(2, list1.size());
        assertTrue(list1.contains(MyClass.class.getMethod("getValue")));
        assertTrue(list1.contains(MyClass.class.getMethod("setValue")));
    }
}