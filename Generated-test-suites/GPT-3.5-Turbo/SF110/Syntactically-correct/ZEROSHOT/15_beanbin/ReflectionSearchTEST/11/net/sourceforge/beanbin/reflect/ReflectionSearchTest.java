import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    private ReflectionSearch reflectionSearch;
    private Class clazz;
    
    @Before
    public void setUp() {
        clazz = ReflectionSearch.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation_withMatchingAnnotation_shouldReturnTrue() {
        boolean result = reflectionSearch.hasAnnotation("Override");
        assertTrue(result);
    }
    
    @Test
    public void testHasAnnotation_withNonMatchingAnnotation_shouldReturnFalse() {
        boolean result = reflectionSearch.hasAnnotation("NonExistentAnnotation");
        assertFalse(result);
    }
    
    @Test
    public void testMethodsThatHave_withMatchingTerm_shouldAddMethodReflectionCriteria() throws BeanBinException {
        reflectionSearch.methodsThatHave("testMethod");
        List<Criteria> criteriaList = reflectionSearch.getQuery().getCriterias();
        assertTrue(criteriaList.size() == 1);
        assertTrue(criteriaList.get(0) instanceof MethodReflectionCriteria);
    }
    
    @Test
    public void testMethodsThatDontHave_withNonMatchingTerm_shouldAddMethodReflectionCriteria() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("testMethod");
        List<Criteria> criteriaList = reflectionSearch.getQuery().getCriterias();
        assertTrue(criteriaList.size() == 1);
        assertTrue(criteriaList.get(0) instanceof MethodReflectionCriteria);
    }
    
    @Test
    public void testAnd_shouldSetNextConditionalToAnd() {
        reflectionSearch.and();
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }
    
    @Test
    public void testOr_shouldSetNextConditionalToOr() {
        reflectionSearch.or();
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }
    
    @Test
    public void testGetMethods_shouldReturnListOfMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertTrue(methods instanceof List);
    }
    
    @Test
    public void testIntersect_shouldIntersectTwoLists() {
        List<Method> master = new ArrayList<>();
        master.add(clazz.getMethod("hasAnnotation", String.class));
        master.add(clazz.getMethod("methodsThatHave", String.class));
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(clazz.getMethod("methodsThatHave", String.class));
        toadd.add(clazz.getMethod("methodsThatDontHave", String.class));
        
        reflectionSearch.intersect(master, toadd);
        assertTrue(master.size() == 1);
        assertTrue(master.contains(clazz.getMethod("methodsThatHave", String.class)));
    }
    
    @Test
    public void testUnion_shouldUnionTwoLists() {
        List<Method> master = new ArrayList<>();
        master.add(clazz.getMethod("hasAnnotation", String.class));
        master.add(clazz.getMethod("methodsThatHave", String.class));
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(clazz.getMethod("methodsThatHave", String.class));
        toadd.add(clazz.getMethod("methodsThatDontHave", String.class));
        
        reflectionSearch.union(master, toadd);
        assertTrue(master.size() == 4);
        assertTrue(master.contains(clazz.getMethod("hasAnnotation", String.class)));
        assertTrue(master.contains(clazz.getMethod("methodsThatHave", String.class)));
        assertTrue(master.contains(clazz.getMethod("methodsThatDontHave", String.class)));
    }
}