import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.stubs.Answer;
import org.mockito.stubs.Stub;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstruction() {
        Invocation invocation = new Stub(new Answer<Object>() {
            @Override
            public Object answer(Invocation invocation) {
                return null;
            }
        }).when(() -> someMockObject.someMethod(1, 2, 3));

        List<Matcher> matchers = Arrays.asList(is(1), is(2), is(3));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        // Check method getters
        Method method = im.getMethod();
        Invocation actualInvocation = im.getInvocation();
        List<Matcher> actualMatchers = im.getMatchers();
        String description = im.toString();

        // Assertions
        assertSame(invocation.getMethod(), method);
        assertSame(invocation, actualInvocation);
        assertSame(matchers, actualMatchers);
        assertEquals(description, "InvocationMatcher:\n" +
                "Invocation: org.mockito.stubs.Stub@12345678\n" +
                "Method: public void someMockObject.someMethod(int, int, int)\n" +
                "Matchers: [1, 2, 3]\n");
    }
}