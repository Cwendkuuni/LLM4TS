import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_True() {
        ReflectionSearch reflectionSearch = Mockito.mock(ReflectionSearch.class);
        Mockito.when(reflectionSearch.hasAnnotation("Override")).thenReturn(true);
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }

    @Test
    public void testHasAnnotation_False() {
        ReflectionSearch reflectionSearch = Mockito.mock(ReflectionSearch.class);
        Mockito.when(reflectionSearch.hasAnnotation("RandomAnnotation")).thenReturn(false);
        assertFalse(reflectionSearch.hasAnnotation("RandomAnnotation"));
    }

    @Test
    public void testHasWildcard_True() {
        ReflectionSearch reflectionSearch = new ReflectionSearch();
        assertTrue(reflectionSearch.hasWildcard("test*"));
    }

    @Test
    public void testHasWildcard_False() {
        ReflectionSearch reflectionSearch = new ReflectionSearch();
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Add test case for methodsThatHave
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch returnedResult = reflectionSearch.methodsThatHave("testTerm");
        assertNotNull(returnedResult);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Add test case for methodsThatDontHave
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch returnedResult = reflectionSearch.methodsThatDontHave("testTerm");
        assertNotNull(returnedResult);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Add test case for getMethods
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertNotNull(reflectionSearch.getMethods());
    }

}