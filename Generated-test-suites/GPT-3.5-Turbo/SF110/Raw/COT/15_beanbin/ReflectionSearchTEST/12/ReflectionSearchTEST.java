import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("Deprecated"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("search");
        assertEquals(3, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatDontHave("search");
        assertEquals(6, reflectionSearch.getMethods().size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("search").and().methodsThatDontHave("list");
        assertEquals(2, reflectionSearch.getMethods().size());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("search").or().methodsThatDontHave("list");
        assertEquals(7, reflectionSearch.getMethods().size());
    }
}

