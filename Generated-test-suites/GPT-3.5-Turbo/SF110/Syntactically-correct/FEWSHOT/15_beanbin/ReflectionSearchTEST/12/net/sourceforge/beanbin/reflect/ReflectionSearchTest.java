package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        Assert.assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.search.*"));
        Assert.assertFalse(reflectionSearch.hasAnnotation("java.lang.*"));
        Assert.assertFalse(reflectionSearch.hasAnnotation("org.junit.*"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        List<Method> expectedMethods = Arrays.asList(
            getClass().getMethod("testHasAnnotation")
        );
        
        List<Method> actualMethods = reflectionSearch.methodsThatHave("test").getMethods();
        
        Assert.assertEquals(expectedMethods, actualMethods);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        List<Method> expectedMethods = new ArrayList<>();
        
        List<Method> actualMethods = reflectionSearch.methodsThatDontHave("test").getMethods();
        
        Assert.assertEquals(expectedMethods, actualMethods);
    }
        
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        Assert.assertEquals(reflectionSearch, reflectionSearch.and());
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        Assert.assertEquals(reflectionSearch, reflectionSearch.or());
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        List<Method> expectedMethods = Arrays.asList(
            getClass().getMethod("testHasAnnotation"),
            getClass().getMethod("testMethodsThatHave"),
            getClass().getMethod("testMethodsThatDontHave"),
            getClass().getMethod("testAnd"),
            getClass().getMethod("testOr"),
            getClass().getMethod("testGetMethods")
        );
        
        List<Method> actualMethods = reflectionSearch.getMethods();
        
        Assert.assertEquals(expectedMethods, actualMethods);
    }
    
    @Test
    public void testIntersect() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        List<Method> masterList = Arrays.asList(
            getClass().getMethod("testHasAnnotation"),
            getClass().getMethod("testMethodsThatHave"),
            getClass().getMethod("testMethodsThatDontHave")
        );
        
        List<Method> toAddList = Arrays.asList(
            getClass().getMethod("testAnd"),
            getClass().getMethod("testOr"),
            getClass().getMethod("testGetMethods")
        );
        
        List<Method> expectedIntersection = new ArrayList<>();
        
        List<Method> actualIntersection = reflectionSearch.intersect(masterList, toAddList);
        
        Assert.assertEquals(expectedIntersection, actualIntersection);
    }
    
    @Test
    public void testUnion() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(getClass());
        
        List<Method> masterList = Arrays.asList(
            getClass().getMethod("testHasAnnotation"),
            getClass().getMethod("testMethodsThatHave"),
            getClass().getMethod("testMethodsThatDontHave")
        );
        
        List<Method> toAddList = Arrays.asList(
            getClass().getMethod("testAnd"),
            getClass().getMethod("testOr"),
            getClass().getMethod("testGetMethods")
        );
        
        List<Method> expectedUnion = Arrays.asList(
            getClass().getMethod("testHasAnnotation"),
            getClass().getMethod("testMethodsThatHave"),
            getClass().getMethod("testMethodsThatDontHave"),
            getClass().getMethod("testAnd"),
            getClass().getMethod("testOr"),
            getClass().getMethod("testGetMethods")
        );
        
        List<Method> actualUnion = reflectionSearch.union(masterList, toAddList);
        
        Assert.assertEquals(expectedUnion, actualUnion);
    }

}