import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation() {
        assertFalse(reflectionSearch.hasAnnotation("test"));
    }

    @Test
    public void testHasAnnotationWildcard() {
        assertTrue(reflectionSearch.hasAnnotation("*"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(reflectionSearch.hasWildcard("*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("test");
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("test");
        assertNotNull(result);
    }

    @Test
    public void testAnd() {
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }

    @Test
    public void testOr() {
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        reflectionSearch.intersect(master, toadd);
        assertTrue(master.isEmpty());
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        reflectionSearch.union(master, toadd);
        assertTrue(master.isEmpty());
    }
}