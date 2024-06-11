import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertTrue(reflectionSearch.hasAnnotation("org.junit.Test"));
        assertTrue(reflectionSearch.hasAnnotation("Test*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.methodsThatHave("testHasAnnotation").getMethods());
        assertNull(reflectionSearch.methodsThatHave("unknownMethod").getMethods());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.methodsThatDontHave("unknownMethod").getMethods());
        assertNull(reflectionSearch.methodsThatDontHave("testHasAnnotation").getMethods());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertSame(reflectionSearch, reflectionSearch.and());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertSame(reflectionSearch, reflectionSearch.or());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethodsException() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatDontHave(null).getMethods();
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.and().methodsThatHave("testHasAnnotation").and().methodsThatHave("testMethodsThatHave");
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.or().methodsThatHave("testHasAnnotation").or().methodsThatHave("testMethodsThatHave");
        assertNotNull(reflectionSearch.getMethods());
    }

}