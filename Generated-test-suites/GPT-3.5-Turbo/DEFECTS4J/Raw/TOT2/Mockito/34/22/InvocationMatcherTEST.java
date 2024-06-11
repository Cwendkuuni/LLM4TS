import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation testInvocation = new Invocation(testMethod, null);
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(testInvocation);
        
        assertEquals(testMethod, testInvocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation testInvocation = new Invocation(testMethod, null);
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(testInvocation);
        
        assertEquals(testInvocation, testInvocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation testInvocation = new Invocation(testMethod, null);
        List<Matcher> testMatchers = new ArrayList<>();
        testMatchers.add(new TestMatcher());
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(testInvocation, testMatchers);
        
        assertEquals(testMatchers, testInvocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation testInvocation = new Invocation(testMethod, null);
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(testInvocation);
        
        assertNotNull(testInvocationMatcher.toString());
    }
    
    // Add more test cases for other methods
    
}