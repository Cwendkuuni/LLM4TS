import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import net.sourceforge.beanbin.reflect.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.*;

public class ReflectionSearchTest {
    
    private Class testClass;
    
    @Before
    public void setUp() {
        testClass = ReflectionSearch.class;
    }
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("NonexistentAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        reflectionSearch.methodsThatHave("getFieldByName").methodsThatHave("setFieldValue");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(2, methods.size());
        assertTrue(methods.contains(testClass.getMethod("getFieldByName")));
        assertTrue(methods.contains(testClass.getMethod("setFieldValue")));
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        reflectionSearch.methodsThatDontHave("getFieldByName").methodsThatDontHave("setFieldValue");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("getFieldByName").and();
        
        assertEquals(reflectionSearch, result);
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("getFieldByName").or();
        
        assertEquals(reflectionSearch, result);
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        // Assume this is a valid test case
    }
    
    @Test
    public void testIntersect() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        Method method1 = testClass.getMethod("getFieldByName");
        Method method2 = testClass.getMethod("setFieldValue");
        
        List<Method> master = new ArrayList<Method>();
        master.add(method1);
        master.add(method2);
        
        List<Method> toAdd = new ArrayList<Method>();
        toAdd.add(method1);
        
        reflectionSearch.intersect(master, toAdd);
        
        assertEquals(1, master.size());
        assertFalse(master.contains(method2));
    }
    
    @Test
    public void testUnion() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        
        Method method1 = testClass.getMethod("getFieldByName");
        Method method2 = testClass.getMethod("setFieldValue");
        
        List<Method> master = new ArrayList<Method>();
        master.add(method1);
        
        List<Method> toAdd = new ArrayList<Method>();
        toAdd.add(method2);
        
        reflectionSearch.union(master, toAdd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
    }
}