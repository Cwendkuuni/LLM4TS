
package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import net.sourceforge.beanbin.BeanBinException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(Object.class);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Deprecated"));
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("*Annotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("get");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(3, methods.size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("get");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
    }

    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("get").and().methodsThatHave("set");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
    }

    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("get").or().methodsThatHave("set");
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(4, methods.size());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());
    }

    @Test
    public void testIntersect() {
        List<Method> master = reflectionSearch.getMethods();
        assertNotNull(master);

        List<Method> toadd = null;
        reflectionSearch.intersect(master, toadd);
        assertEquals(0, master.size());

        toadd = reflectionSearch.getMethods();
        reflectionSearch.intersect(master, toadd);
        assertEquals(0, master.size());

        master.add(null);
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());

        master.add(null);
        toadd.add(null);
        reflectionSearch.intersect(master, toadd);
        assertEquals(2, master.size());

        toadd.add(null);
        reflectionSearch.intersect(master, toadd);
        assertEquals(2, master.size());

        toadd.add(null);
        reflectionSearch.intersect(master, toadd);
        assertEquals(2, master.size());
    }

    @Test
    public void testUnion() {
        List<Method> master = reflectionSearch.getMethods();
        assertNotNull(master);

        List<Method> toadd = null;
        reflectionSearch.union(master, toadd);
        assertEquals(0, master.size());

        toadd = reflectionSearch.getMethods();
        reflectionSearch.union(master, toadd);
        assertEquals(0, master.size());

        toadd.add(null);
        reflectionSearch.union(master, toadd);
        assertEquals(1, master.size());

        toadd.add(null);
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());

        master.add(null);
        toadd.add(null);
        reflectionSearch.union(master, toadd);
        assertEquals(3, master.size());

        toadd.add(null);
        reflectionSearch.union(master, toadd);
        assertEquals(3, master.size());
    }
}
