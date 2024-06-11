package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.ReflectionShelf;
import net.sourceforge.beanbin.criteria.Criteria;
import net.sourceforge.beanbin.criteria.ReflectionCriteria;
import net.sourceforge.beanbin.criteria.MethodReflectionCriteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Query;
import net.sourceforge.beanbin.search.SearchType;

public class ReflectionSearchTest {
    
    private Class<?> clazz;
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("TestMethod");
        
        Query query = reflectionSearch.getQuery();
        List<Criteria> criterias = query.getCriterias();
        assertEquals(1, criterias.size());
        
        MethodReflectionCriteria criteria = (MethodReflectionCriteria) criterias.get(0);
        assertEquals(clazz, criteria.getClazz());
        assertEquals("TestMethod", criteria.getTerm());
        assertEquals(SearchType.EQUALS, criteria.getSearchType());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("TestMethod");
        
        Query query = reflectionSearch.getQuery();
        List<Criteria> criterias = query.getCriterias();
        assertEquals(1, criterias.size());
        
        MethodReflectionCriteria criteria = (MethodReflectionCriteria) criterias.get(0);
        assertEquals(clazz, criteria.getClazz());
        assertEquals("TestMethod", criteria.getTerm());
        assertEquals(SearchType.DOESNOTEQUAL, criteria.getSearchType());
    }

    @Test
    public void testAnd() {
        reflectionSearch.and();
        
        Query query = reflectionSearch.getQuery();
        assertEquals(Conditional.AND, query.getNextConditional());
    }

    @Test
    public void testOr() {
        reflectionSearch.or();
        
        Query query = reflectionSearch.getQuery();
        assertEquals(Conditional.OR, query.getNextConditional());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = new ArrayList<>();
        Method method1 = null;
        Method method2 = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("TestMethod1")) {
                method1 = method;
            } else if (method.getName().equals("TestMethod2")) {
                method2 = method;
            }
        }
        methods.add(method1);
        methods.add(method2);
        
        ReflectionShelf.getInstance().put(reflectionSearch.getQuery(), methods);
        
        List<Method> returnedMethods = reflectionSearch.getMethods();
        
        assertEquals(2, returnedMethods.size());
        assertTrue(returnedMethods.contains(method1));
        assertTrue(returnedMethods.contains(method2));
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        Method method1 = new Method();
        Method method2 = new Method();
        
        master.add(method1);
        toadd.add(method1);
        toadd.add(method2);
        
        reflectionSearch.intersect(master, toadd);
        
        assertEquals(1, master.size());
        assertTrue(master.contains(method1));
        assertFalse(master.contains(method2));
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        Method method1 = new Method();
        Method method2 = new Method();
        
        master.add(method1);
        toadd.add(method1);
        toadd.add(method2);
        
        reflectionSearch.union(master, toadd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
    }
    
    // Test class for testing purposes
    private static class TestClass {
        @TestAnnotation
        public void TestMethod1() {}
        
        public void TestMethod2() {}
    }
}