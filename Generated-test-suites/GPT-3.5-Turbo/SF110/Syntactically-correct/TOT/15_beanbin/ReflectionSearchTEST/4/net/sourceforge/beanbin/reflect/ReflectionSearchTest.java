import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {
    ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
    }

    @Test
    public void testHasAnnotation_WhenAnnotationExists_ShouldReturnTrue() {
        assertTrue(reflectionSearch.hasAnnotation(RetentionPolicy.class.getName()));
    }

    @Test
    public void testHasAnnotation_WhenAnnotationDoesNotExist_ShouldReturnFalse() {
        assertFalse(reflectionSearch.hasAnnotation("nonexistent.Annotation"));
    }

    @Test
    public void testHasAnnotation_WhenWildcardExistsInTermName_ShouldReturnTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*"));
    }

    @Test
    public void testHasAnnotation_WhenWildcardDoesNotExistInTermName_ShouldReturnFalse() {
        assertFalse(reflectionSearch.hasAnnotation("RetentionPolicy"));
    }

    @Test
    public void testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance() throws BeanBinException {
        assertNotNull(reflectionSearch.methodsThatHave("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"));
    }

    @Test
    public void testMethodsThatHave_WhenMethodDoesNotExist_ShouldThrowBeanBinException() throws BeanBinException {
        try {
            reflectionSearch.methodsThatHave("nonexistentMethod");
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Method not found", e.getMessage());
        }
    }

    @Test
    public void testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance() throws BeanBinException {
        assertNotNull(reflectionSearch.methodsThatDontHave("setUp"));
    }

    @Test
    public void testMethodsThatDontHave_WhenMethodDoesNotExist_ShouldThrowBeanBinException() throws BeanBinException {
        try {
            reflectionSearch.methodsThatDontHave("nonexistentMethod");
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertEquals("Method not found", e.getMessage());
        }
    }

    @Test
    public void testAnd_WhenCalled_ShouldSetNextConditionalToAnd() {
        assertSame(Conditional.AND, reflectionSearch.and().getNextConditional());
    }

    @Test
    public void testOr_WhenCalled_ShouldSetNextConditionalToOr() {
        assertSame(Conditional.OR, reflectionSearch.or().getNextConditional());
    }

    @Test
    public void testGetMethods_WhenNoCriteriasAdded_ShouldReturnEmptyList() throws BeanBinException {
        reflectionSearch.getMethods();
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testGetMethods_WhenCriteriasAdded_ShouldReturnMatchingMethods() throws BeanBinException {
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("setUp").and().methodsThatDontHave("tearDown");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance")));
    }

    @Test
    public void testIntersect_WhenNoCommonMethods_ShouldEmptyMasterList() throws NoSuchMethodException {
        List<Method> master = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        List<Method> toAdd = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        reflectionSearch.intersect(master, toAdd);
        assertTrue(master.isEmpty());
    }

    @Test
    public void testIntersect_WhenCommonMethodsExist_ShouldRemoveNonCommonMethodsFromMaster() throws NoSuchMethodException {
        List<Method> master = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"), ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        List<Method> toAdd = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        reflectionSearch.intersect(master, toAdd);
        assertEquals(1, master.size());
        assertTrue(master.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance")));
    }

    @Test
    public void testUnion_WhenNoCommonMethods_ShouldAddAllMethodsFromToAddToMaster() throws NoSuchMethodException {
        List<Method> master = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        List<Method> toAdd = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        reflectionSearch.union(master, toAdd);
        assertEquals(2, master.size());
        assertTrue(master.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance")));
        assertTrue(master.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance")));
    }

    @Test
    public void testUnion_WhenCommonMethodsExist_ShouldAddOnlyNonCommonMethodsFromToAddToMaster() throws NoSuchMethodException {
        List<Method> master = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        List<Method> toAdd = createMethodList(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance"), ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance"));
        reflectionSearch.union(master, toAdd);
        assertEquals(2, master.size());
        assertTrue(master.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave_WhenMethodExists_ShouldReturnNonNullInstance")));
        assertTrue(master.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave_WhenMethodExists_ShouldReturnNonNullInstance")));
    }

    private List<Method> createMethodList(Method... methods) {
        return Arrays.asList(methods);
    }
}