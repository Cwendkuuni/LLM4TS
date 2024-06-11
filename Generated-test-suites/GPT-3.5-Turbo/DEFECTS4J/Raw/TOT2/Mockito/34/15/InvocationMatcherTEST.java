import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(testMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        List<Matcher> matchers = Arrays.asList(Any.ANY, Equals.eq(1), NotNull.NOT_NULL);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("TestClass.testMethod()", invocationMatcher.toString());
    }

    @Test
    public void testSimilarMethod() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new Invocation(TestClass.class, testMethod, null);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
        
        Method differentMethod = TestClass.class.getDeclaredMethod("differentMethod");
        Invocation differentInvocation = new Invocation(TestClass.class, differentMethod, null);
        
        assertFalse(invocationMatcher.hasSimilarMethod(differentInvocation));
    }

    @Test
    public void testSameMethod() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new Invocation(TestClass.class, testMethod, null);
        
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
        
        Method differentMethod = TestClass.class.getDeclaredMethod("differentMethod");
        Invocation differentInvocation = new Invocation(TestClass.class, differentMethod, null);
        
        assertFalse(invocationMatcher.hasSameMethod(differentInvocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method testMethod = TestClass.class.getDeclaredMethod("testMethod");
        Invocation invocation = new Invocation(TestClass.class, testMethod, new Object[]{1, "test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation argumentInvocation = new Invocation(TestClass.class, testMethod, new Object[]{1, "new"});
        
        invocationMatcher.captureArgumentsFrom(argumentInvocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        
        assertEquals("1", ((Equals) matchers.get(0)).toString());
        assertEquals("new", ((Equals) matchers.get(1)).toString());
    }

    private static class TestClass {
        public void testMethod() {}
        public void differentMethod() {}
    }
}