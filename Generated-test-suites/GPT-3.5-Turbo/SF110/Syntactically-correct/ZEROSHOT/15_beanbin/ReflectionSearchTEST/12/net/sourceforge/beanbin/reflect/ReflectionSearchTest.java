import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.reflect.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = TestClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        // Test for annotation with wildcard
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.*"));
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation.*"));
        assertFalse(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.*"));

        // Test for annotation without wildcard
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Retention"));
        assertTrue(reflectionSearch.hasAnnotation("TestClassAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.Annotation"));
    }
    
    @Test
    public void testMethodsThatHave() {
        Class clazz = TestClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        // Test for method that contains specified term
        try {
            reflectionSearch.methodsThatHave("MethodWithParam").methodsThatHave("Void").methodsThatHave("public");
            assertEquals(1, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected exception thrown");
        }
        
        // Test for method that does not contain specified term
        try {
            reflectionSearch.methodsThatHave("MethodWithParam").methodsThatDontHave("Void").methodsThatHave("public");
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testAnd() {
        Class clazz = TestClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        // Test for AND combination of method search criteria
        try {
            reflectionSearch.methodsThatHave("MethodWithParam").and().methodsThatHave("Void");
            assertEquals(1, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testOr() {
        Class clazz = TestClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        // Test for OR combination of method search criteria
        try {
            reflectionSearch.methodsThatHave("MethodWithParam").or().methodsThatHave("Void");
            assertEquals(2, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    class TestClass {
        
        @Retention(value = RetentionPolicy.RUNTIME)
        @interface TestAnnotation {}
        
        @TestAnnotation
        public void MethodWithParam(int param) {}
        
        @TestAnnotation
        public void MethodWithParamVoid(int param) {}
        
        @TestAnnotation
        public void MethodWithoutParam() {}
    }
}