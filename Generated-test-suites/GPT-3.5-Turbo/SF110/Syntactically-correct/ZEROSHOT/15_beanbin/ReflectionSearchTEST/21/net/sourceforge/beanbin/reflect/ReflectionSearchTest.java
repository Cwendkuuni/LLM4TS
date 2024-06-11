import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("SuppressWarnings"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        assertFalse(reflectionSearch.hasWildcard("Test"));
        assertTrue(reflectionSearch.hasWildcard("Test*"));
        assertTrue(reflectionSearch.hasWildcard("*Test*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("equals");
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("equals");
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.and();
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        ReflectionSearch result = reflectionSearch.or();
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        master.add(Object.class.getDeclaredMethods()[0]);
        toadd.add(Object.class.getDeclaredMethods()[1]);
        reflectionSearch.intersect(master, toadd);
        assertTrue(master.isEmpty());
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        master.add(Object.class.getDeclaredMethods()[0]);
        toadd.add(Object.class.getDeclaredMethods()[1]);
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
    }
}