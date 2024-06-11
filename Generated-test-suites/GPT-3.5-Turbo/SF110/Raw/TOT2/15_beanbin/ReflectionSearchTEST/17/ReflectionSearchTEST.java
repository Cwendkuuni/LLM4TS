import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    private Class clazz;
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        clazz = this.getClass();
        reflectionSearch = new ReflectionSearch(clazz);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertTrue(reflectionSearch.hasAnnotation("Deprecated"));
        assertFalse(reflectionSearch.hasAnnotation("NotNull"));
        assertFalse(reflectionSearch.hasAnnotation("Nullable"));
    }

    @Test
    public void testWildcardSearch() {
        assertTrue(reflectionSearch.hasWildcard("test*"));
        assertFalse(reflectionSearch.hasWildcard("test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
    }

    @Test
    public void testAnd() {
        reflectionSearch.and();
        assertNotNull(reflectionSearch);
    }

    @Test
    public void testOr() {
        reflectionSearch.or();
        assertNotNull(reflectionSearch);
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
        Method method = clazz.getDeclaredMethods()[0];
        toadd.add(method);
        reflectionSearch.union(master, toadd);
        assertEquals(1, master.size());
    }
}