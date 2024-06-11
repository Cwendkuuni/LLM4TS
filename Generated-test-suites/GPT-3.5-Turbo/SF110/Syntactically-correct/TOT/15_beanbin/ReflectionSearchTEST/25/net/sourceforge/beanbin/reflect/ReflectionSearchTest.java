import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.*;

import static org.junit.Assert.*;
import org.junit.*;

public class ReflectionSearchTest {

    @Test
    public void hasAnnotation_shouldReturnTrue_whenClassHasAnnotation() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Act
        boolean result = reflectionSearch.hasAnnotation("Test");

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAnnotation_shouldReturnFalse_whenClassDoesNotHaveAnnotation() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Act
        boolean result = reflectionSearch.hasAnnotation("Deprecated");

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasAnnotation_shouldReturnTrue_whenClassHasWildcardAnnotation() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Act
        boolean result = reflectionSearch.hasAnnotation("T*");

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAnnotation_shouldReturnFalse_whenClassDoesNotHaveWildcardAnnotation() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Act
        boolean result = reflectionSearch.hasAnnotation("D*");

        // Assert
        assertFalse(result);
    }

    @Test
    public void methodsThatHave_shouldAddMethodReflectionCriteriaToQuery() throws BeanBinException {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        MethodReflectionCriteria expectedCriteria = new MethodReflectionCriteria(clazz, "dummyMethod", SearchType.EQUALS);

        // Act
        reflectionSearch.methodsThatHave("dummyMethod");

        // Assert
        Query query = reflectionSearch.getQuery();
        assertTrue(query.getCriterias().contains(expectedCriteria));
    }

    @Test
    public void methodsThatDontHave_shouldAddMethodReflectionCriteriaToQuery() throws BeanBinException {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        MethodReflectionCriteria expectedCriteria = new MethodReflectionCriteria(clazz, "dummyMethod", SearchType.DOESNOTEQUAL);

        // Act
        reflectionSearch.methodsThatDontHave("dummyMethod");

        // Assert
        Query query = reflectionSearch.getQuery();
        assertTrue(query.getCriterias().contains(expectedCriteria));
    }

    @Test
    public void and_shouldSetNextConditionalToAnd() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        Conditional expectedConditional = Conditional.AND;

        // Act
        reflectionSearch.and();

        // Assert
        Query query = reflectionSearch.getQuery();
        assertEquals(expectedConditional, query.getNextConditional());
    }

    @Test
    public void or_shouldSetNextConditionalToOr() {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        Conditional expectedConditional = Conditional.OR;

        // Act
        reflectionSearch.or();

        // Assert
        Query query = reflectionSearch.getQuery();
        assertEquals(expectedConditional, query.getNextConditional());
    }

    @Test
    public void getMethods_shouldReturnListOfMethodsMatchingQuery() throws BeanBinException {
        // Arrange
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(clazz.getDeclaredMethod("dummyMethod"));

        // Act
        List<Method> methods = reflectionSearch.getMethods();

        // Assert
        assertEquals(expectedMethods, methods);
    }

    private static class DummyClass {

        @Test
        public void dummyMethod() {
            // Dummy method for testing
        }
    }

}