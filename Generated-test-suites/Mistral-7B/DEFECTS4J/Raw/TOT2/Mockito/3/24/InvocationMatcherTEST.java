import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationWithMocks;
import org.mockito.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new InvocationWithMocks(new Object(), new Method(MockitoTestUtils.class.getDeclaredMethod("testMethod", String.class)), new Object[0]);
        List<Matcher> matchers = Arrays.asList(Mockito.eq("test"), Mockito.eq(123));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Basic functionality check
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());

        // Test edge cases and exception handling
        try {
            new InvocationMatcher(null, matchers);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            new InvocationMatcher(invocation, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            new InvocationMatcher(new Object(), null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            new InvocationMatcher(null, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}