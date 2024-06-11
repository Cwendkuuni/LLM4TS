package net.sourceforge.beanbin.reflect;

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
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        boolean result = reflectionSearch.hasAnnotation("Override");
        assertFalse(result);

        result = reflectionSearch.hasAnnotation("Test");
        assertTrue(result);
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        boolean result = reflectionSearch.hasWildcard("term*");
        assertTrue(result);

        result = reflectionSearch.hasWildcard("term");
        assertFalse(result);
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        ReflectionSearch result = reflectionSearch.methodsThatHave("test");
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        ReflectionSearch result = reflectionSearch.methodsThatDontHave("test");
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        List<Method> result = reflectionSearch.getMethods();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        List<Method> master = new ArrayList<>();
        master.add(ReflectionSearchTest.class.getDeclaredMethods()[0]);
        List<Method> toadd = new ArrayList<>();
        toadd.add(ReflectionSearchTest.class.getDeclaredMethods()[1]);

        reflectionSearch.intersect(master, toadd);
        assertEquals(0, master.size());
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        List<Method> master = new ArrayList<>();
        master.add(ReflectionSearchTest.class.getDeclaredMethods()[0]);
        List<Method> toadd = new ArrayList<>();
        toadd.add(ReflectionSearchTest.class.getDeclaredMethods()[1]);

        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
    }
}