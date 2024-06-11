package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        assertFalse(reflectionSearch.hasAnnotation("term"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        assertFalse(reflectionSearch.hasWildcard("term"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("term");
        
        assertNotNull(result);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("term");
        
        assertNotNull(result);
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        ReflectionSearch result = reflectionSearch.and();
        
        assertNotNull(result);
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        ReflectionSearch result = reflectionSearch.or();
        
        assertNotNull(result);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        List<Method> result = reflectionSearch.getMethods();
        
        assertNotNull(result);
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        reflectionSearch.intersect(master, toadd);
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        reflectionSearch.union(master, toadd);
    }
}