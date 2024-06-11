
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    private Class<?> testClass = MyClass.class;
    private ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);

    @Test
    public void testHasAnnotation_validAnnotation_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotation_validAnnotationQualifiedName_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("com.example.MyAnnotation"));
    }

    @Test
    public void testHasAnnotation_wildcardAnnotation_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
    }

    @Test
    public void testHasAnnotation_annotationNotPresent_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("NotExistingAnnotation"));
    }

    @Test
    public void testHasAnnotation_invalidAnnotation_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("12345"));
    }

    @Test
    public void testHasAnnotation_invalidWildcardFormat_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("*Annot@tion"));
    }

    @Test
    public void testMethodsThatHave_singleMethodWithTerm_returnsSameMethod() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("methodName").getMethods();
        assertEquals(1, methods.size());
        assertEquals("methodName", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatHave_multipleMethodsWithTerm_returnsAllMatchingMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("methodName").and().methodsThatHave("anotherMethodName").getMethods();
        assertEquals(2, methods.size());
        assertEquals("methodName", methods.get(0).getName());
        assertEquals("anotherMethodName", methods.get(1).getName());
    }

    @Test
    public void testMethodsThatHave_methodWithInvalidTerm_returnsEmptyList() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("invalidMethodName").getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testMethodsThatDontHave_singleMethodWithoutTerm_returnsSameMethod() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatDontHave("methodName").getMethods();
        assertEquals(1, methods.size());
        assertEquals("methodName", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave_multipleMethodsWithoutTerm_returnsAllMatchingMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatDontHave("methodName").or().methodsThatDontHave("anotherMethodName").getMethods();
        assertEquals(2, methods.size());
        assertEquals("methodName", methods.get(0).getName());
        assertEquals("anotherMethodName", methods.get(1).getName());
    }

    @Test
    public void testMethodsThatDontHave_methodWithInvalidTerm_returnsAllMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatDontHave("invalidMethodName").getMethods();
        assertEquals(2, methods.size());
        assertEquals("methodName", methods.get(0).getName());
        assertEquals("anotherMethodName", methods.get(1).getName());
    }

    @Test
    public void testAnd_addAndToQuery_incrementsNextConditionalToAND() {
        reflectionSearch.and();
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void testOr_addOrToQuery_incrementsNextConditionalToOR() {
        reflectionSearch.or();
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void testGetMethods_validQuery_returnsListOfMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("methodName").getMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());
    }

    @Test
    public void testGetMethods_emptyQuery_returnsEmptyList() throws BeanBinException {
        reflectionSearch.getQuery().clear(); // Clear any previous query added
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());
    }

    private static class MyClass {
        private void methodName() {}
        private void anotherMethodName() {}
    }
}

