import org.junit.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        // Typical case: Class has the given annotation
        ClassWithAnnotation classWithAnnotation = new ClassWithAnnotation();
        ReflectionSearch reflectionSearch = new ReflectionSearch(classWithAnnotation.getClass());
        boolean result = reflectionSearch.hasAnnotation("MyAnnotation");
        Assert.assertTrue(result);
        
        // Typical case: Class does not have the given annotation
        ClassWithoutAnnotation classWithoutAnnotation = new ClassWithoutAnnotation();
        reflectionSearch = new ReflectionSearch(classWithoutAnnotation.getClass());
        result = reflectionSearch.hasAnnotation("MyAnnotation");
        Assert.assertFalse(result);
        
        // Edge case: Empty string as annotation term
        result = reflectionSearch.hasAnnotation("");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Typical case: Methods containing the given term exist
        ClassWithMethods classWithMethods = new ClassWithMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(classWithMethods.getClass().getMethod("methodOne"));
        expectedMethods.add(classWithMethods.getClass().getMethod("methodTwo"));
        expectedMethods.add(classWithMethods.getClass().getMethod("methodThree"));
        ReflectionSearch reflectionSearch = new ReflectionSearch(classWithMethods.getClass());
        List<Method> methods = reflectionSearch.methodsThatHave("method").getMethods();
        Assert.assertEquals(expectedMethods, methods);
        
        // Typical case: Methods containing the given term don't exist
        ClassWithoutMethods classWithoutMethods = new ClassWithoutMethods();
        expectedMethods.clear();
        reflectionSearch = new ReflectionSearch(classWithoutMethods.getClass());
        methods = reflectionSearch.methodsThatHave("method").getMethods();
        Assert.assertTrue(methods.isEmpty());
        
        // Edge case: Empty string as method term
        methods = reflectionSearch.methodsThatHave("").getMethods();
        Assert.assertTrue(methods.isEmpty());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Typical case: Methods not containing the given term exist
        ClassWithMethods classWithMethods = new ClassWithMethods();
        List<Method> expectedMethods = new ArrayList<>();
        ReflectionSearch reflectionSearch = new ReflectionSearch(classWithMethods.getClass());
        List<Method> methods = reflectionSearch.methodsThatDontHave("method").getMethods();
        Assert.assertEquals(expectedMethods, methods);
        
        // Typical case: Methods containing the given term don't exist
        ClassWithoutMethods classWithoutMethods = new ClassWithoutMethods();
        expectedMethods.add(classWithoutMethods.getClass().getMethod("methodOne"));
        expectedMethods.add(classWithoutMethods.getClass().getMethod("methodTwo"));
        expectedMethods.add(classWithoutMethods.getClass().getMethod("methodThree"));
        reflectionSearch = new ReflectionSearch(classWithoutMethods.getClass());
        methods = reflectionSearch.methodsThatDontHave("method").getMethods();
        Assert.assertEquals(expectedMethods, methods);
        
        // Edge case: Empty string as method term
        methods = reflectionSearch.methodsThatDontHave("").getMethods();
        Assert.assertTrue(methods.isEmpty());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.and();
        // There is no specific behavior for the 'and' method other than setting the next conditional
        // So, we can just test that there are no exceptions thrown
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.or();
        // There is no specific behavior for the 'or' method other than setting the next conditional
        // So, we can just test that there are no exceptions thrown
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        // Typical case: Methods are returned based on the applied criteria
        ClassWithMethods classWithMethods = new ClassWithMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(classWithMethods.getClass().getMethod("methodOne"));
        expectedMethods.add(classWithMethods.getClass().getMethod("methodTwo"));
        expectedMethods.add(classWithMethods.getClass().getMethod("methodThree"));
        ReflectionSearch reflectionSearch = new ReflectionSearch(classWithMethods.getClass());
        List<Method> methods = reflectionSearch.methodsThatHave("method").and().methodsThatDontHave("Two").getMethods();
        Assert.assertEquals(expectedMethods, methods);
        
        // Edge case: No criteria applied, all methods should be returned
        methods = reflectionSearch.getMethods();
        Assert.assertTrue(methods.containsAll(expectedMethods));
        
        // Error scenario: BeanBinException is thrown
        // We can create a class with methods that throw BeanBinException, but for simplicity, we can just use a mock object/functionality
    }
    
    // Additional test cases for 'intersect' and 'union' methods can also be added, but for simplicity, we can skip those here
    
}