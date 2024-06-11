import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        // Test case for InvocationMatcher constructor with empty matcher list
        Invocation invocation = new Invocation(mock, new MethodTest(), new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertThat(method, is(notNullValue()));
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherWithCustomMatcherList() {
        // Test case for InvocationMatcher constructor with custom matcher list
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.matches(new InvocationOnMock(mock, method, arguments)), is(true));
    }

    @Test
    public void testInvocationMatcherWithNullMatcherList() {
        // Test case for InvocationMatcher constructor with null matcher list
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        List<Matcher> matchers = null;
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher, is(nullValue()));
    }

    @Test
    public void testInvocationMatcherWithNullInvocation() {
        // Test case for InvocationMatcher constructor with null invocation
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        Invocation invocation = null;
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher, is(nullValue()));
    }

    @Test
    public void testToStringMethod() {
        // Test case for toString method
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = String.format("InvocationMatcher[%s, %s, %s]", method.getName(), matchers, invocation);

        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void testMatchesMethod() {
        // Test case for matches method
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        Invocation invocationToTest = new InvocationOnMock(mock, method, arguments);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.matches(invocationToTest);
        boolean expected = true;

        assertThat(result, is(expected));
    }

    @Test
    public void testSafelyArgumentsMatchMethod() {
        // Test case for safelyArgumentsMatch method
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        Invocation invocationToTest = new InvocationOnMock(mock, method, new Object[]{2, "test2"});
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.safelyArgumentsMatch(invocationToTest.getArguments());
        boolean expected = false;

        assertThat(result, is(expected));
    }

    @Test
    public void testHasSimilarMethodMethod() {
        // Test case for hasSimilarMethod method
        Method method1 = new MethodTest();
        Method method2 = new MethodTest();
        Invocation invocation = new Invocation(mock, method1, new Object[]{1, "test"});
        Invocation invocationToTest = new InvocationOnMock(mock, method2, new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationOnMock(mock, method2, new Object[]{1, "test"}));
        boolean expected = true;

        assertThat(result, is(expected));
    }

    @Test
    public void testHasSameMethodMethod() {
        // Test case for hasSameMethod method
        Method method1 = new MethodTest();
        Method method2 = new MethodTest();
        Invocation invocation = new Invocation(mock, method1, new Object[]{1, "test"});
        Invocation invocationToTest = new InvocationOnMock(mock, method2, new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentMatcher1()), Mockito.argThat(new ArgumentMatcher2()));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.hasSameMethod(new InvocationOnMock(mock, method2, new Object[]{1, "test"}));
        boolean expected = false;

        assertThat(result, is(expected));
    }

    @Test
    public void testCaptureArgumentsFromMethod() {
        // Test case for captureArgumentsFrom method
        Method method = new MethodTest();
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, method, arguments);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentCaptor<Integer>() {
            @Override
            public void verify(Integer value) {
                assertThat(value, is(1));
            }
        }), Mockito.argThat(new ArgumentCaptor<String>() {
            @Override
            public void verify(String value) {
                assertThat(value, is("test"));
            }
        }));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock, method, new Object[]{1, "test"}));

        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(mock).method(integerCaptor.capture(), stringCaptor.capture());

        assertThat(integerCaptor.getValue(), is(1));
        assertThat(stringCaptor.getValue(), is("test"));
    }

    private static class MethodTest {
        public void method(Integer i, String s) {
        }
    }

    private static class ArgumentMatcher1 implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument instanceof Integer;
        }
    }

    private static class ArgumentMatcher2 implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument instanceof String;
        }
    }
}