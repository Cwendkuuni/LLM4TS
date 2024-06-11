import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        boolean result1 = rs.hasAnnotation("TestAnnotation1");
        assertTrue(result1);
        
        // Test case 2
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        boolean result2 = rs.hasAnnotation("TestAnnotation2");
        assertFalse(result2);
        
        // Test case 3
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        boolean result3 = rs.hasAnnotation("TestAnnotation");
        assertFalse(result3);
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result1 = rs.methodsThatHave("testMethod1");
        assertNotNull(result1);
        
        // Test case 2
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result2 = rs.methodsThatHave("testMethod2");
        assertNotNull(result2);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result1 = rs.methodsThatDontHave("testMethod1");
        assertNotNull(result1);
        
        // Test case 2
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result2 = rs.methodsThatDontHave("testMethod2");
        assertNotNull(result2);
    }
    
    @Test
    public void testAnd() {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result1 = rs.and();
        assertNotNull(result1);
    }
    
    @Test
    public void testOr() {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        ReflectionSearch result1 = rs.or();
        assertNotNull(result1);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        // Test case 1
        ReflectionSearch rs = new ReflectionSearch(TestClass.class);
        List<Method> result1 = rs.getMethods();
        assertNotNull(result1);
    }
    
    // Test helper class
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation1 {}
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2 {}
    
    public class TestClass {
        
        @TestAnnotation1
        public void testMethod1() {}
        
        @TestAnnotation2
        public void testMethod2() {}
        
        public void testMethod3() {}
    }
}