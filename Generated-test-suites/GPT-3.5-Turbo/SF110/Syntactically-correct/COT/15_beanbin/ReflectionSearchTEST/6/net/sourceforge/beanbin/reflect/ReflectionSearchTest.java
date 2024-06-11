import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.query.SearchType;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        // Test case for annotation present
        assertTrue(rs.hasAnnotation("Override"));
        
        // Test case for annotation not present
        assertFalse(rs.hasAnnotation("NotNull"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        rs.methodsThatHave("annotation")
            .and()
            .methodsThatHave("term");
        
        List<Method> methods = rs.getMethods();
        
        // Test case for methods that have the specified terms
        for (Method method : methods) {
            assertTrue(method.getName().contains("annotation") 
                       || method.getName().contains("term"));
        }
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        rs.methodsThatDontHave("annotation")
            .and()
            .methodsThatDontHave("term");
        
        List<Method> methods = rs.getMethods();
        
        // Test case for methods that don't have the specified terms
        for (Method method : methods) {
            assertFalse(method.getName().contains("annotation") 
                       || method.getName().contains("term"));
        }
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = rs.and();
        
        // Test case for and() method
        assertSame(result, rs);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = rs.or();
        
        // Test case for or() method
        assertSame(result, rs);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testHasAnnotationWildcard() {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        // Test case for wildcard in annotation term
        assertTrue(rs.hasAnnotation("*Override"));
        
        // Test case for wildcard in class name term
        assertTrue(rs.hasAnnotation("*ReflectionSearch"));
    }
    
    @Test(expected = BeanBinException.class)
    public void testMethodsThatHaveInvalidTerm() throws BeanBinException {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        // Test case for invalid term in methodsThatHave
        rs.methodsThatHave("invalid_term");
    }
    
    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHaveInvalidTerm() throws BeanBinException {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        // Test case for invalid term in methodsThatDontHave
        rs.methodsThatDontHave("invalid_term");
    }
    
    @Test
    public void testNoMethodsReturned() throws BeanBinException {
        ReflectionSearch rs = new ReflectionSearch(ReflectionSearch.class);
        
        rs.methodsThatHave("nonexistent_method");
        
        List<Method> methods = rs.getMethods();
        
        // Test case for no methods returned
        assertEquals(0, methods.size());
    }

}