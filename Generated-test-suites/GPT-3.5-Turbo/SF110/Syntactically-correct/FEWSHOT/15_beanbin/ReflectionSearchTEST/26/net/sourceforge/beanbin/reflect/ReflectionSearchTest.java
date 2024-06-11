package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.ReflectionCriteria;
import net.sourceforge.beanbin.ReflectionShelf;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("Foo"));
        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertTrue(reflectionSearch.hasWildcard("Test*"));
        assertFalse(reflectionSearch.hasWildcard("Foo"));
        assertFalse(reflectionSearch.hasWildcard(""));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("testHasAnnotation");
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasAnnotation"));
        List<Method> actualResults = reflectionSearch.getMethods();
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatDontHave("testHasAnnotation");
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasWildcard"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatHave"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatDontHave"));
        expectedResults.add(this.getClass().getMethod("testAnd"));
        expectedResults.add(this.getClass().getMethod("testOr"));
        expectedResults.add(this.getClass().getMethod("testGetMethods"));
        expectedResults.add(this.getClass().getMethod("testIntersect"));
        expectedResults.add(this.getClass().getMethod("testUnion"));
        List<Method> actualResults = reflectionSearch.getMethods();
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("testHasAnnotation").and().methodsThatDontHave("testHasWildcard");
        assertTrue(reflectionSearch.getMethods().isEmpty());
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("testHasAnnotation").or().methodsThatDontHave("testHasWildcard");
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasAnnotation"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatDontHave"));
        expectedResults.add(this.getClass().getMethod("testAnd"));
        expectedResults.add(this.getClass().getMethod("testOr"));
        expectedResults.add(this.getClass().getMethod("testGetMethods"));
        expectedResults.add(this.getClass().getMethod("testIntersect"));
        expectedResults.add(this.getClass().getMethod("testUnion"));
        List<Method> actualResults = reflectionSearch.getMethods();
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasAnnotation"));
        expectedResults.add(this.getClass().getMethod("testHasWildcard"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatHave"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatDontHave"));
        expectedResults.add(this.getClass().getMethod("testAnd"));
        expectedResults.add(this.getClass().getMethod("testOr"));
        expectedResults.add(this.getClass().getMethod("testGetMethods"));
        expectedResults.add(this.getClass().getMethod("testIntersect"));
        expectedResults.add(this.getClass().getMethod("testUnion"));
        List<Method> actualResults = reflectionSearch.getMethods();
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> master = new ArrayList<>();
        master.add(this.getClass().getMethod("testHasAnnotation"));
        master.add(this.getClass().getMethod("testHasWildcard"));
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(this.getClass().getMethod("testHasAnnotation"));
        reflectionSearch.intersect(master, toAdd);
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasAnnotation"));
        List<Method> actualResults = master;
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> master = new ArrayList<>();
        master.add(this.getClass().getMethod("testHasAnnotation"));
        master.add(this.getClass().getMethod("testHasWildcard"));
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(this.getClass().getMethod("testMethodsThatDontHave"));
        reflectionSearch.union(master, toAdd);
        List<Method> expectedResults = new ArrayList<>();
        expectedResults.add(this.getClass().getMethod("testHasAnnotation"));
        expectedResults.add(this.getClass().getMethod("testHasWildcard"));
        expectedResults.add(this.getClass().getMethod("testMethodsThatDontHave"));
        List<Method> actualResults = master;
        assertEquals(expectedResults, actualResults);
    }
}