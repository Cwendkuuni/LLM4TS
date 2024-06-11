import org.junit.Test;
import org.mockito.HamcrestMatchers;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Stubber stubber = mockito3(InvocationMatcher.class);
        InvocationMatcher invocationMatcher = stubber.createMock(InvocationMatcher.class);
        Method method = new MockMethod("testMethod", new Class[]{String.class, int.class});
        Invocation invocation = new InvocationOnMock(invocationMatcher, method, new Object[]{new String("test"), 1});
        InvocationMatcher invocationMatcherWithInvocation = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertThat(invocationMatcherWithInvocation.getMethod(), is(notNullValue()));
        assertThat(invocationMatcherWithInvocation.getMethod().getName(), is("testMethod"));
    }

    @Test
    public void testGetMethod_whenInvocationIsNull_thenReturnsNull() {
        Invocation invocation = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertNull(invocationMatcher.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethod_whenMethodNameIsNull_thenThrowsIllegalArgumentException() {
        String methodName = null;
        Method method = new MockMethod(methodName, new Class[0], new Class[0], new Class[0]);
        Invocation invocation = new InvocationOnMock(new Object(), method, new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        invocationMatcher.getMethod();
    }

    @Test
    public void testToString() {
        // Test for toString() method
    }

    @Test
    public void testMatches() {
        // Test for matches() method
    }

    @Test
    public void testHasSimilarMethod() {
        // Test for hasSimilarMethod() method
    }

    @Test
    public void testHasSameMethod() {
        // Test for hasSameMethod() method
    }

    @Test
    public void testGetLocation() {
        // Test for getLocation() method
    }

    @Test
    public void testToString_PrintSettingsIsNull_thenUsesDefaultPrintSettings() {
        // Test for toString(PrintSettings printSettings) method when PrintSettings is null
    }

    @Test
    public void testToString_PrintSettingsIsNotNull_thenUsesProvidedPrintSettings() {
        // Test for toString(PrintSettings printSettings) method when PrintSettings is not null
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Test for captureArgumentsFrom(Invocation i) method
    }
}