import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;

    @Before
    public void setup() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("NotNull"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Deprecated"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(reflectionSearch.hasWildcard("com.*"));
        assertFalse(reflectionSearch.hasWildcard("org.junit.Test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());

        reflectionSearch.methodsThatHave("hashCode");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("hashCode", methods.get(1).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("equals");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(3, methods.size());
        assertEquals("toString", methods.get(0).getName());
        assertEquals("hashCode", methods.get(1).getName());
        assertEquals("getMethods", methods.get(2).getName());

        reflectionSearch.methodsThatDontHave("getClass");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("toString", methods.get(0).getName());
        assertEquals("hashCode", methods.get(1).getName());
    }

    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("toString").and().methodsThatHave("hashCode");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("hashCode", methods.get(0).getName());
    }

    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("toString").or().methodsThatHave("hashCode");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("toString", methods.get(0).getName());
        assertEquals("hashCode", methods.get(1).getName());
    }
}