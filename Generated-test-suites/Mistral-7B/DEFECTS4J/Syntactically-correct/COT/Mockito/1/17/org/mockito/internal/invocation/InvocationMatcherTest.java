import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.onCall;
import static org.mockito.Mockito.stub;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
        assertThat(invocationMatcher.getMatchers(), is(nullValue()));
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(InvocationOnMock.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method;
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertThat(invocationMatcher.getMethod(), is(sameInstance(method)));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String printSettings = Mockito.mock(PrintSettings.class);
        String result = invocationMatcher.toString(printSettings);
        
        onCall(printSettings).print(Mockito.anyListOf(Matcher.class), Mockito.any(Invocation.class));
        OngoingStubbing<String> stub = stub(printSettings.toString(Mockito.eq(matchers), Mockito.eq(invocation)));
        stub.thenReturn("Printed Result");
        
        assertThat(result, is("Printed Result"));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, new ArrayList<>());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, new ArrayList<>());

        stub(invocation1.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));
        stub(invocation2.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));

        stub(invocation1.getMethod()).getMockControl().setReturnValue(method);
        stub(invocation2.getMethod()).getMockControl().setReturnValue(method);

        stub(invocation1.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));
        stub(invocation2.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));

        onCall(method).getName().whenCallRealMethod().thenReturn("testMethod");

        stub(invocation1.isVerified()).when().thenReturn(false);
        stub(invocation2.isVerified()).when().thenReturn(false);

        stub(invocation1.getMock()).when().thenReturn(Mockito.mock(Object.class));
        stub(invocation2.getMock()).when().thenReturn(Mockito.mock(Object.class));

        assertThat(invocationMatcher.matches(invocationMatcher2), is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), new ArrayList<>());
        
        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);
        
        assertThat(result, is(true));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), new ArrayList<>());
        
        invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, new Exception()});
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, new ArrayList<>());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, new ArrayList<>());

        stub(invocation1.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));
        stub(invocation2.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));

        stub(invocation1.getMethod()).getMockControl().setReturnValue(method1);
        stub(invocation2.getMethod()).getMockControl().setReturnValue(method2);

        stub(method1.getName()).whenCallRealMethod().thenReturn("testMethod1");
        stub(method2.getName()).whenCallRealMethod().thenReturn("testMethod2");

        stub(invocation1.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));
        stub(invocation2.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));

        stub(invocation1.isVerified()).when().thenReturn(false);
        stub(invocation2.isVerified()).when().thenReturn(false);

        stub(invocation1.getMock()).when().thenReturn(Mockito.mock(Object.class));
        stub(invocation2.getMock()).when().thenReturn(Mockito.mock(Object.class));

        assertThat(invocationMatcher.hasSimilarMethod(invocationMatcher2), is(false));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, new ArrayList<>());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, new ArrayList<>());

        stub(invocation1.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));
        stub(invocation2.getMock()).getMockControl().setReturnValue(Mockito.mock(Object.class));

        stub(invocation1.getMethod()).getMockControl().setReturnValue(method);
        stub(invocation2.getMethod()).getMockControl().setReturnValue(method);

        stub(invocation1.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));
        stub(invocation2.getLocation()).getMockControl().setReturnValue(Mockito.mock(Location.class));

        stub(invocation1.isVerified()).when().thenReturn(false);
        stub(invocation2.isVerified()).when().thenReturn(false);

        stub(invocation1.getMock()).when().thenReturn(Mockito.mock(Object.class));
        stub(invocation2.getMock()).when().thenReturn(Mockito.mock(Object.class));

        onCall(method).getName().whenCallRealMethod().thenReturn("testMethod");

        assertThat(invocationMatcher.hasSameMethod(invocationMatcher2), is(true));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, new ArrayList<>());
        
        stub(invocation.getLocation()).getMockControl().setReturnValue(location);
        
        assertThat(invocationMatcher.getLocation(), is(sameInstance(location)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object arg1 = Mockito.mock(Object.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        onCall(invocation).getArgumentAt(Mockito.anyInt(), Mockito.eq(Object.class)).when().thenReturn(arg1);

        Matcher matcher = Mockito.mock(CapturesArguments.class);
        matchers.add(matcher);

        invocationMatcher.captureArgumentsFrom(invocation);

        onCall(matcher).captureFrom(arg1);
    }
}