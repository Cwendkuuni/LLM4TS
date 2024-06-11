import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    private Invocation invocation1;
    private Invocation invocation2;
    private List<Matcher> matchers1;
    private List<Matcher> matchers2;

    @Before
    public void setUp() {
        Method method1 = SomeClass.class.getMethod("method1", String.class);
        Method method2 = SomeClass.class.getMethod("method2", int.class);
        invocation1 = new Invocation(mock(SomeClass.class), method1, new Object[] {"test"});
        invocation2 = new Invocation(mock(SomeClass.class), method2, new Object[] {123});
        matchers1 = Arrays.asList(any(String.class), eq("test"));
        matchers2 = Collections.emptyList();
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers1);
        assertEquals("Method should be equal", invocation1.getMethod(), matcher1.getMethod());
        
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers2);
        assertEquals("Method should be equal", invocation2.getMethod(), matcher2.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers1);
        assertEquals("Invocation should be equal", invocation1, matcher1.getInvocation());
        
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers2);
        assertEquals("Invocation should be equal", invocation2, matcher2.getInvocation());
    }

    // Add more test cases for other methods

}