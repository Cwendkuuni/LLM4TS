import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

public class ReflectionSearchTest {
    private Class sampleClass;

    @Before
    public void setUp() {
        sampleClass = SampleClass.class;
    }

    @Test
    public void testHasAnnotation_shouldReturnTrue() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        assertTrue(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasAnnotation_shouldReturnFalse() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        assertFalse(reflectionSearch.hasAnnotation("RandomAnnotation"));
    }

    @Test
    public void testHasWildcard_shouldReturnTrue() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        assertTrue(reflectionSearch.hasWildcard("test*"));
    }

    @Test
    public void testHasWildcard_shouldReturnFalse() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        reflectionSearch.methodsThatHave("test");
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        reflectionSearch.methodsThatDontHave("random");
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        ReflectionSearch result = reflectionSearch.and();
        assertEquals("AND", result.getQuery().getLastConditional());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(sampleClass);
        ReflectionSearch result = reflectionSearch.or();
        assertEquals("OR", result.getQuery().getLastConditional());
    }

    // Add more edge cases and exception scenarios as needed

}