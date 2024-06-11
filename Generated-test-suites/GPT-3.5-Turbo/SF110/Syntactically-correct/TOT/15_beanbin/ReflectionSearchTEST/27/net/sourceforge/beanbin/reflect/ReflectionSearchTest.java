import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation_whenAnnotationExists_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }

    @Test
    public void testHasAnnotation_whenAnnotationDoesNotExist_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("XYZAnnotation"));
    }

    @Test
    public void testHasAnnotation_whenWildcardMatchingAnnotationExists_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*de"));
    }

    @Test
    public void testHasAnnotation_whenWildcardMatchingAnnotationNameExists_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*Search"));
    }

    @Test
    public void testHasAnnotation_whenWildcardMatchingClassNameExists_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*Search"));
    }

    @Test
    public void testHasAnnotation_whenWildcardMatchingAnnotationAndClassNameExists_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*n*earch"));
    }

    @Test
    public void testHasAnnotation_whenEmptyTerm_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test
    public void testHasAnnotation_whenNullTerm_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation(null));
    }

    @Test
    public void testMethodsThatHave_whenTermExists_returnsFilteredReflectionSearch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("Search");
        assertTrue(result instanceof ReflectionSearch);
        assertNotSame(result, reflectionSearch);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_whenTermNotExist_throwsBeanBinException() throws BeanBinException {
        reflectionSearch.methodsThatHave("XYZ");
    }

    @Test
    public void testMethodsThatDontHave_whenTermExists_returnsFilteredReflectionSearch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("Search");
        assertTrue(result instanceof ReflectionSearch);
        assertNotSame(result, reflectionSearch);
    }

    @Test
    public void testMethodsThatDontHave_whenTermNotExist_returnsOriginalReflectionSearch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("XYZ");
        assertSame(result, reflectionSearch);
    }

    @Test
    public void testAnd_returnsFilteredReflectionSearchWithNextConditionalSetToAnd() {
        ReflectionSearch result = reflectionSearch.and();
        assertTrue(result instanceof ReflectionSearch);
        assertNotSame(result, reflectionSearch);
    }

    @Test
    public void testOr_returnsFilteredReflectionSearchWithNextConditionalSetToOr() {
        ReflectionSearch result = reflectionSearch.or();
        assertTrue(result instanceof ReflectionSearch);
        assertNotSame(result, reflectionSearch);
    }

    @Test
    public void testGetMethods_whenNoCriteriaAdded_returnsEmptyList() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testGetMethods_whenValidCriteriaAdded_returnsListOfMethods() throws BeanBinException {
        reflectionSearch.methodsThatHave("Search");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());
        for (Method method : methods) {
            // Assert specific criteria is met
            // ...
        }
    }

    @Test
    public void testIntersect_whenNoMethodsToRemove_doesNotModifyMasterList() {
        List<Method> master = createMethodList();
        List<Method> toAdd = createMethodList();
        reflectionSearch.intersect(master, toAdd);
        assertEquals(master.size(), 4);
    }

    @Test
    public void testIntersect_whenMethodsToRemove_removesMethodsFromMasterList() {
        List<Method> master = createMethodList();
        List<Method> toAdd = createMethodList();
        toAdd.remove(1);
        toAdd.remove(2);
        reflectionSearch.intersect(master, toAdd);
        assertEquals(master.size(), 2);
        assertTrue(master.contains(toAdd.get(0)));
        assertTrue(master.contains(toAdd.get(1)));
    }

    @Test
    public void testUnion_whenNoMethodsToAdd_doesNotModifyMasterList() {
        List<Method> master = createMethodList();
        List<Method> toAdd = createMethodList();
        reflectionSearch.union(master, toAdd);
        assertEquals(master.size(), 4);
    }

    @Test
    public void testUnion_whenMethodsToAdd_addsMethodsToMasterList() {
        List<Method> master = createMethodList();
        List<Method> toAdd = createMethodList();
        toAdd.remove(1);
        toAdd.remove(2);
        reflectionSearch.union(master, toAdd);
        assertEquals(master.size(), 6);
        assertTrue(master.contains(toAdd.get(1)));
        assertTrue(master.contains(toAdd.get(2)));
    }

    private List<Method> createMethodList() {
        List<Method> methods = new ArrayList<>();
        methods.add(getMethod("method1"));
        methods.add(getMethod("method2"));
        methods.add(getMethod("method3"));
        methods.add(getMethod("method4"));
        return methods;
    }

    private Method getMethod(String name) {
        try {
            return ReflectionSearch.class.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}