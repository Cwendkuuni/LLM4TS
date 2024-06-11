import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.MockitoMethodInvocation;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Invocation invocation = new MockitoMethodInvocation(MyMockitoClass.class, "methodToTest", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        Assertions.assertEquals("methodToTest", method.getName());
    }
}