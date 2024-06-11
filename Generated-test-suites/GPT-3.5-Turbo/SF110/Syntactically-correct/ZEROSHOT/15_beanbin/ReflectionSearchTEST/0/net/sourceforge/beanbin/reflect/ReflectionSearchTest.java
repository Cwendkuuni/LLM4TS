package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import java.util.*;

public class ReflectionSearchTest {

    private Class clazz;
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setup() {
        clazz = ReflectionSearch.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation_whenNoAnnotations_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("testAnnotation"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationMatchesName_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Override"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationMatchesClassName_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationWildcardMatchesName_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("java.lang.*"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationWildcardMatchesClassName_returnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationDoesNotMatchName_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("test"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationDoesNotMatchClassName_returnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("Test"));
    }
    
    @Test
    public void testMethodsThatHave_addsMethodReflectionCriteriaToQuery() throws BeanBinException {
        reflectionSearch.methodsThatHave("test");
        List<Criteria> criterias = reflectionSearch.query.getCriterias();
        assertTrue(criterias.get(0) instanceof MethodReflectionCriteria);
    }
    
    @Test
    public void testMethodsThatDontHave_addsMethodReflectionCriteriaToQueryWithDoesNotEqualSearchType() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("test");
        List<Criteria> criterias = reflectionSearch.query.getCriterias();
        assertTrue(criterias.get(0) instanceof MethodReflectionCriteria);
        assertEquals(SearchType.DOESNOTEQUAL, ((MethodReflectionCriteria)criterias.get(0)).getSearchType());
    }
    
    @Test
    public void testAnd_setsNextConditionalToAND() {
        assertSame(reflectionSearch, reflectionSearch.and());
        assertEquals(Conditional.AND, reflectionSearch.query.getNextConditional());
    }
    
    @Test
    public void testOr_setsNextConditionalToOR() {
        assertSame(reflectionSearch, reflectionSearch.or());
        assertEquals(Conditional.OR, reflectionSearch.query.getNextConditional());
    }
    
    @Test
    public void testGetMethods_returnsListOfMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertTrue(methods instanceof List);
    }
    
    @Test
    public void testGetMethods_whenResultsInReflectionShelf_returnsStoredList() throws BeanBinException {
        Method method = clazz.getDeclaredMethods()[0];
        List<Method> storedList = new ArrayList<>();
        storedList.add(method);
        ReflectionShelf.getInstance().put(reflectionSearch.query, storedList);
        
        List<Method> methods = reflectionSearch.getMethods();
        assertSame(storedList, methods);
    }
    
    @Test
    public void testIntersect_removesMethodsNotInToaddFromMasterList() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Method method3 = Mockito.mock(Method.class);
        
        master.add(method1);
        master.add(method2);
        toadd.add(method2);
        toadd.add(method3);
        
        reflectionSearch.intersect(master, toadd);
        
        assertFalse(master.contains(method1));
        assertTrue(master.contains(method2));
        assertFalse(master.contains(method3));
    }
    
    @Test
    public void testUnion_addsMethodsNotInMasterListToMasterList() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Method method3 = Mockito.mock(Method.class);
        
        master.add(method1);
        toadd.add(method2);
        toadd.add(method3);
        
        reflectionSearch.union(master, toadd);
        
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
        assertTrue(master.contains(method3));
    }
}