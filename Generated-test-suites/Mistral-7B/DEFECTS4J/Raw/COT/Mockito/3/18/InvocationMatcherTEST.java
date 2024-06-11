import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.TypeSafeMatcher;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        // add assertions here
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
            @Override
            public boolean matchesSafely(Object item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("some matcher");
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // add assertions here
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        // add assertions here
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();
        // add assertions here
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
            @Override
            public boolean matchesSafely(Object item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("some matcher");
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> result = invocationMatcher.getMatchers();
        // add assertions here
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
            @Override
            public boolean matchesSafely(Object item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("some matcher");
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();
        // add assertions here
    }

    @Test
    public void testMatchesInvocation() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);
        // add assertions here
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] actualArgs = new Object[0];
        when(invocation.getArguments()).thenReturn(actualArgs);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        // add assertions here
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", Arrays.asList(String.class).toArray(new Class[0])));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        // add assertions here
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);
        // add assertions here
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();
        // add assertions here
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2", "arg3"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        // add assertions here
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCaptureArgumentsFromWithEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        // add assertions here
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        // add assertions here for matchers that have been captured
        Stubber stubber = Mockito.mock(Stubber.class);
        MatcherDecorator decorator = new MatcherDecorator(stubber);
        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).assertCapturedArguments(invocation.getArguments());
            }
            decorator.decore(matcher);
        }
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), "toString", new Class[0]));

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        // add assertions here
    }
}