import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));

        reflectionSearch = new ReflectionSearch(MyClass.class);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatHave("methodName");
        // Add assertions
        
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatDontHave("methodName");
        // Add assertions
        
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.and();
        // Add assertions
        
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.or();
        // Add assertions
        
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        List<Method> methods = reflectionSearch.getMethods();
        // Add assertions
        
    }
}