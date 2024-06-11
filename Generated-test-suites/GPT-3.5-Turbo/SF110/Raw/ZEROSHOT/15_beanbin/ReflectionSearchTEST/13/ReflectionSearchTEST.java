import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    private Class clazz;
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        clazz = MyClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation() {
        boolean result = reflectionSearch.hasAnnotation("TestAnnotation");
        assertTrue(result);
    }
    
    @Test
    public void testHasAnnotation_Invalid() {
        boolean result = reflectionSearch.hasAnnotation("InvalidAnnotation");
        assertFalse(result);
    }
    
    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch search = reflectionSearch.methodsThatHave("getName");
        List<Method> methods = search.getMethods();
        assertEquals(1, methods.size());
        assertEquals("getName", methods.get(0).getName());
    }
    
    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch search = reflectionSearch.methodsThatDontHave("getName");
        List<Method> methods = search.getMethods();
        for(Method method : methods) {
            assertNotEquals("getName", method.getName());
        }
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch search = reflectionSearch.methodsThatHave("getName").and().methodsThatHave("setName");
        List<Method> methods = search.getMethods();
        assertEquals(1, methods.size());
        assertEquals("getName", methods.get(0).getName());
        assertEquals("setName", methods.get(0).getName());
    }
    
    @Test
    public void testOr() {
        ReflectionSearch search = reflectionSearch.methodsThatHave("getName").or().methodsThatHave("getAge");
        List<Method> methods = search.getMethods();
        assertEquals(2, methods.size());
        assertEquals("getName", methods.get(0).getName());
        assertEquals("getAge", methods.get(1).getName());
    }
    
    private class MyClass {
        @TestAnnotation
        public void getName() {}
        
        public void setName() {}
        
        public void getAge() {}
    }
}