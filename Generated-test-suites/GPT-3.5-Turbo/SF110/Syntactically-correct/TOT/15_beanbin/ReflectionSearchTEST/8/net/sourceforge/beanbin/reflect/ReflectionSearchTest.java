import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class ReflectionSearchTest {

    ReflectionSearch reflectionSearch;
    Class testClass;

    @Before
    public void setUp() {
        testClass = Mockito.mock(Class.class);
        reflectionSearch = new ReflectionSearch(testClass);
    }

    @Test
    public void hasAnnotation_shouldReturnTrue_whenClassHasAnnotation() {
        // Arrange
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{annotation});
        String term = "TestAnnotation";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAnnotation_shouldReturnFalse_whenClassDoesNotHaveAnnotation() {
        // Arrange
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[0]);
        String term = "TestAnnotation";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasAnnotation_shouldMatchWildcardAnnotationName() {
        // Arrange
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.annotationType()).thenReturn(TestAnnotation.class);
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{annotation});
        String term = "*Annotation";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAnnotation_shouldMatchWildcardClassName() {
        // Arrange
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.annotationType()).thenReturn(TestAnnotation.class);
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{annotation});
        String term = "Test*";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAnnotation_shouldNotMatchWildcardAnnotationName() {
        // Arrange
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.annotationType()).thenReturn(TestAnnotation.class);
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{annotation});
        String term = "*Test";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasAnnotation_shouldNotMatchWildcardClassName() {
        // Arrange
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.annotationType()).thenReturn(TestAnnotation.class);
        Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{annotation});
        String term = "Annotation*";

        // Act
        boolean result = reflectionSearch.hasAnnotation(term);

        // Assert
        assertFalse(result);
    }

    @Test(expected = BeanBinException.class)
    public void methodsThatHave_shouldThrowException_whenBeanBinExceptionOccurs() throws BeanBinException {
        // Arrange
        String term = "InvalidTerm";

        // Act
        reflectionSearch.methodsThatHave(term);

        // Assert
        // Expecting BeanBinException to be thrown
    }

    @Test
    public void methodsThatHave_shouldAddMethodReflectionCriteriaToList() throws BeanBinException {
        // Arrange
        String term = "TestMethod";

        // Act
        ReflectionSearch result = reflectionSearch.methodsThatHave(term);

        // Assert
        assertNotNull(result);
        assertTrue(reflectionSearch.getQuery().getCriterias().get(0) instanceof MethodReflectionCriteria);
    }

    @Test(expected = BeanBinException.class)
    public void methodsThatDontHave_shouldThrowException_whenBeanBinExceptionOccurs() throws BeanBinException {
        // Arrange
        String term = "InvalidTerm";

        // Act
        reflectionSearch.methodsThatDontHave(term);

        // Assert
        // Expecting BeanBinException to be thrown
    }

    @Test
    public void methodsThatDontHave_shouldAddMethodReflectionCriteriaToList() throws BeanBinException {
        // Arrange
        String term = "TestMethod";

        // Act
        ReflectionSearch result = reflectionSearch.methodsThatDontHave(term);

        // Assert
        assertNotNull(result);
        assertTrue(reflectionSearch.getQuery().getCriterias().get(0) instanceof MethodReflectionCriteria);
    }

    @Test
    public void and_shouldSetNextConditionalToAnd() {
        // Arrange

        // Act
        ReflectionSearch result = reflectionSearch.and();

        // Assert
        assertNotNull(result);
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void or_shouldSetNextConditionalToOr() {
        // Arrange

        // Act
        ReflectionSearch result = reflectionSearch.or();

        // Assert
        assertNotNull(result);
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void getMethods_shouldReturnEmptyList_whenReflectionShelfHasNoMethods() throws BeanBinException {
        // Arrange
        Mockito.when(ReflectionShelf.getInstance().get(reflectionSearch.getQuery())).thenReturn(null);

        // Act
        List<Method> result = reflectionSearch.getMethods();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getMethods_shouldReturnListFromReflectionShelf_whenReflectionShelfHasMethods() throws BeanBinException {
        // Arrange
        List<Method> methodList = new ArrayList<>();
        methodList.add(Mockito.mock(Method.class));
        Mockito.when(ReflectionShelf.getInstance().get(reflectionSearch.getQuery())).thenReturn(methodList);

        // Act
        List<Method> result = reflectionSearch.getMethods();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(methodList, result);
    }

    @Test
    public void intersect_shouldRemoveMethodsNotPresentInToaddList() {
        // Arrange
        List<Method> masterList = new ArrayList<>();
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Method method3 = Mockito.mock(Method.class);
        masterList.add(method1);
        masterList.add(method2);
        masterList.add(method3);
        List<Method> toaddList = new ArrayList<>();
        toaddList.add(method1);

        // Act
        reflectionSearch.intersect(masterList, toaddList);

        // Assert
        assertTrue(masterList.contains(method1));
        assertFalse(masterList.contains(method2));
        assertFalse(masterList.contains(method3));
    }

    @Test
    public void union_shouldAddMethodsNotPresentInMasterToList() {
        // Arrange
        List<Method> masterList = new ArrayList<>();
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Method method3 = Mockito.mock(Method.class);
        masterList.add(method1);
        masterList.add(method2);
        List<Method> toaddList = new ArrayList<>();
        toaddList.add(method2);
        toaddList.add(method3);

        // Act
        reflectionSearch.union(masterList, toaddList);

        // Assert
        assertTrue(masterList.contains(method1));
        assertTrue(masterList.contains(method2));
        assertTrue(masterList.contains(method3));
    }
}