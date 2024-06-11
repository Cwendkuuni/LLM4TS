import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertFalse(reflectionSearch.hasAnnotation("test"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertEquals(reflectionSearch, reflectionSearch.methodsThatHave("test"));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertEquals(reflectionSearch, reflectionSearch.methodsThatDontHave("test"));
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertEquals(reflectionSearch, reflectionSearch.and());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertEquals(reflectionSearch, reflectionSearch.or());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        assertNotNull(reflectionSearch.getMethods());
    }
}