import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setup() {
        reflectionSearch = new ReflectionSearch(Object.class);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("Custom"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(reflectionSearch.hasWildcard("Test*"));
        assertFalse(reflectionSearch.hasWildcard("Test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("toString");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("hashCode");
        assertEquals(0, reflectionSearch.getMethods().size());
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
        reflectionSearch.methodsThatHave("toString").or().methodsThatHave("hashCode");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        master.add(Object.class.getDeclaredMethod("toString"));
        toadd.add(Object.class.getDeclaredMethod("toString"));
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        master.add(Object.class.getDeclaredMethod("toString"));
        toadd.add(Object.class.getDeclaredMethod("hashCode"));
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
    }

}