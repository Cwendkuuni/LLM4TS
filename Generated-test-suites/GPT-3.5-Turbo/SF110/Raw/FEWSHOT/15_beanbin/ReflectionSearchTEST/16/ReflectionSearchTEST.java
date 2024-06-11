package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.query.Query;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }

    @Test
    public void testHasAnnotation() {
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Retention"));
        assertTrue(reflectionSearch.hasAnnotation("Retention"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("test");
        Query query = reflectionSearch.getQuery();
        List<Criteria> criterias = query.getCriterias();

        assertEquals(1, criterias.size());
        assertTrue(criterias.get(0) instanceof MethodReflectionCriteria);
        MethodReflectionCriteria methodCriteria = (MethodReflectionCriteria) criterias.get(0);
        assertEquals(SearchType.EQUALS, methodCriteria.getSearchType());
        assertEquals("test", methodCriteria.getSearchTerm());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("nonExistent");
        Query query = reflectionSearch.getQuery();
        List<Criteria> criterias = query.getCriterias();

        assertEquals(1, criterias.size());
        assertTrue(criterias.get(0) instanceof MethodReflectionCriteria);
        MethodReflectionCriteria methodCriteria = (MethodReflectionCriteria) criterias.get(0);
        assertEquals(SearchType.DOESNOTEQUAL, methodCriteria.getSearchType());
        assertEquals("nonExistent", methodCriteria.getSearchTerm());
    }

    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("test").and();
        Query query = reflectionSearch.getQuery();
        assertEquals(Conditional.AND, query.getNextConditional());
    }

    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("test").or();
        Query query = reflectionSearch.getQuery();
        assertEquals(Conditional.OR, query.getNextConditional());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());
    }

    @Test
    public void testIntersect() throws NoSuchMethodException {
        List<Method> masterList = new ArrayList<>();
        masterList.add(ReflectionSearch.class.getMethod("testMethodsThatHave"));
        masterList.add(ReflectionSearch.class.getMethod("testIntersect"));

        List<Method> toAddList = new ArrayList<>();
        toAddList.add(ReflectionSearch.class.getMethod("testMethodsThatHave"));

        reflectionSearch.intersect(masterList, toAddList);

        assertEquals(1, masterList.size());
        assertTrue(masterList.contains(ReflectionSearch.class.getMethod("testMethodsThatHave")));
    }

    @Test
    public void testUnion() throws NoSuchMethodException {
        List<Method> masterList = new ArrayList<>();
        masterList.add(ReflectionSearch.class.getMethod("testMethodsThatHave"));

        List<Method> toAddList = new ArrayList<>();
        toAddList.add(ReflectionSearch.class.getMethod("testIntersect"));

        reflectionSearch.union(masterList, toAddList);

        assertEquals(2, masterList.size());
        assertTrue(masterList.contains(ReflectionSearch.class.getMethod("testMethodsThatHave")));
        assertTrue(masterList.contains(ReflectionSearch.class.getMethod("testIntersect")));
    }
}