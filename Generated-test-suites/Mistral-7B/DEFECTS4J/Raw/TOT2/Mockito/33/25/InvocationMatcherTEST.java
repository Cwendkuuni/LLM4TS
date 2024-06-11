import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = mockMethod();
        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testInvocationMatcherWithArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.any(), Mockito.eq(2), Mockito.anyInt()));
        Method method = mockMethod();

        assertEquals(method, matcher.getMethod());
        assertArrayEquals(arguments, matcher.getInvocation().getArguments());
    }

    @Test(expected = RuntimeException.class)
    public void testInvocationMatcherWithExceptionThrowingMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = Mockito.mock(Method.class);
        when(method.getName()).thenReturn("methodThrowingException");
        doThrow(new RuntimeException()).when(method).invoke(any(), anyArray());

        assertFalse(matcher.matches(new Invocation(method, invocation.getMock(), new Object[]{}, new Location(1, 1, "testFile.java"))));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation expectedInvocation = Mockito.mock(Invocation.class);
        assertEquals(expectedInvocation, matcher.getInvocation());
    }

    @Test
    public void testGetInvocationWhenNull() {
        InvocationMatcher matcher = new InvocationMatcher(null);
        assertNull(matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.any(), Mockito.eq(2), Mockito.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = Arrays.asList(Mockito.any(), Mockito.eq(2), Mockito.anyInt());
        assertEquals(expectedMatchers, matcher.getMatchers());
    }

    @Test
    public void testGetMatchersWhenEmpty() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Collections.emptyList();
        assertEquals(expectedMatchers, matcher.getMatchers());
    }

    private Method mockMethod() {
        // mock method here
    }
}