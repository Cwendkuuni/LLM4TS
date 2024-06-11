import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("testHasAnnotation", method.getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch newSearch = reflectionSearch.and();
        assertSame(reflectionSearch, newSearch);
        assertEquals(Conditional.AND, reflectionSearch.getNextConditional());
    }

    @Test
    public void testOr() {
        ReflectionSearch newSearch = reflectionSearch.or();
        assertSame(reflectionSearch, newSearch);
        assertEquals(Conditional.OR, reflectionSearch.getNextConditional());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(7, methods.size());
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toAdd = new ArrayList<>();
        master.add(getMethod("test1"));
        master.add(getMethod("test2"));
        toAdd.add(getMethod("test2"));
        toAdd.add(getMethod("test3"));

        reflectionSearch.intersect(master, toAdd);

        assertEquals(1, master.size());
        assertSame(getMethod("test2"), master.get(0));
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toAdd = new ArrayList<>();
        master.add(getMethod("test1"));
        toAdd.add(getMethod("test2"));
        toAdd.add(getMethod("test3"));

        reflectionSearch.union(master, toAdd);

        assertEquals(3, master.size());
        assertTrue(master.contains(getMethod("test1")));
        assertTrue(master.contains(getMethod("test2")));
        assertTrue(master.contains(getMethod("test3")));
    }

    private Method getMethod(String methodName) {
        try {
            return ReflectionSearch.class.getMethod(methodName);
        } catch (Exception e) {
            fail("Failed to get method: " + methodName);
            return null;
        }
    }
}