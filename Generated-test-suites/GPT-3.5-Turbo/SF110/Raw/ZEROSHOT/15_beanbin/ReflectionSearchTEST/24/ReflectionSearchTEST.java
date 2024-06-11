import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_shouldReturnTrue() {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        assertTrue(search.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testHasAnnotation_shouldReturnFalse() {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        assertFalse(search.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testMethodsThatHave_shouldAddMethodReflectionCriteria() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = search.methodsThatHave("testMethod");
        assertTrue(result instanceof ReflectionSearch);
        assertEquals(1, result.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave_shouldAddMethodReflectionCriteria() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = search.methodsThatDontHave("testMethod");
        assertTrue(result instanceof ReflectionSearch);
        assertEquals(2, result.getMethods().size());
    }

    @Test
    public void testAnd_shouldSetNextConditionalToAND() {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = search.and();
        assertTrue(result instanceof ReflectionSearch);
        assertEquals(Conditional.AND, result.query.getNextConditional());
    }

    @Test
    public void testOr_shouldSetNextConditionalToOR() {
        ReflectionSearch search = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = search.or();
        assertTrue(result instanceof ReflectionSearch);
        assertEquals(Conditional.OR, result.query.getNextConditional());
    }

    private static class TestClass {

        @TestAnnotation
        public void testMethod() {}

        @AnotherAnnotation
        public void anotherTestMethod() {}

        public void nonAnnotatedMethod() {}
    }
}