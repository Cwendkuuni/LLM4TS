import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    private SomeClass someMock;

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        InvocationOnMock invocation = Mockito.on(someMock).invoke("someMethod", 1, 2, new String[]{"test"});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testInvokeMatcher_WhenInvokedWithNoMatchers_ThenCreateMatchersFromArguments() {
        List<Matcher> expectedMatchers = Arrays.asList(
                ArgumentMatchers.isA(Integer.class),
                ArgumentMatchers.isA(Integer.class),
                ArgumentMatchers.arrayContaining(ArgumentMatchers.isA(String.class))
        );

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(equalTo(expectedMatchers)));
    }

    @Test
    public void testInvokeMatcher_WhenInvokedWithCustomMatchers_ThenUseProvidedMatchers() {
        List<Matcher> customMatchers = Arrays.asList(
                ArgumentMatchers.isInstanceOf(String[].class),
                ArgumentMatchers.containsString("test")
        );

        InvocationMatcher invocationMatcherWithCustomMatchers = new InvocationMatcher(Mockito.on(someMock).invoke("someMethod", ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), customMatchers.toArray(new Matcher[0])));

        List<Matcher> actualMatchers = invocationMatcherWithCustomMatchers.getMatchers();

        assertThat(actualMatchers, is(equalTo(customMatchers)));
    }

    @Test
    public void testToString_WhenInvokeMatcherIsCreatedWithNoMatchers_ThenPrintEmptyMatcherList() {
        String expectedToString = "InvocationMatcher [method=someMethod(java.lang.Object, java.lang.Object, [Ljava.lang.String;)]";

        String actualToString = invocationMatcher.toString();

        assertThat(actualToString, is(equalTo(expectedToString)));
    }

    @Test
    public void testToString_WhenInvokeMatcherIsCreatedWithCustomMatchers_ThenPrintCustomMatchers() {
        List<Matcher> customMatchers = Arrays.asList(
                ArgumentMatchers.isInstanceOf(String[].class),
                ArgumentMatchers.containsString("test")
        );

        String expectedToString = String.format("InvocationMatcher [method=someMethod(java.lang.Object, java.lang.Object, [Ljava.lang.String;])%n arguments=%n [%s, %s, %s]",
                customMatchers.get(0).toString(),
                customMatchers.get(1).toString(),
                Arrays.toString(new String[]{"test"}));

        invocationMatcher = new InvocationMatcher(Mockito.on(someMock).invoke("someMethod", ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), customMatchers.toArray(new Matcher[0])));

        String actualToString = invocationMatcher.toString();

        assertThat(actualToString, is(equalTo(expectedToString)));
    }

    @Test
    public void testMatches_WhenTwoInvocationsHaveSameMethodAndMock_ThenReturnTrue() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("someMethod", 1, 2, new String[]{"test"});

        boolean result = invocationMatcher.matches(new InvocationMatcher(invocationToCompare));

        assertTrue(result);
    }

    @Test
    public void testMatches_WhenTwoInvocationsHaveDifferentMethods_ThenReturnFalse() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("differentMethod", 1, 2);

        boolean result = invocationMatcher.matches(new InvocationMatcher(invocationToCompare));

        assertFalse(result);
    }

    @Test
    public void testMatches_WhenTwoInvocationsHaveSameMethodAndMockButDifferentArguments_ThenReturnFalse() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("someMethod", 1, "differentString", new String[]{"test", "differentString"});

        boolean result = invocationMatcher.matches(new InvocationMatcher(invocationToCompare));

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WhenTwoInvocationsHaveDifferentMethodsButSameArguments_ThenReturnTrue() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("differentMethod", 1, 2, new String[]{"test"});

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocationToCompare));

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WhenTwoInvocationsHaveSameMethodButDifferentArguments_ThenReturnFalse() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("someMethod", 1, "differentString", new String[]{"test", "differentString"});

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocationToCompare));

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WhenTwoInvocationsHaveSameMethod_ThenReturnTrue() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("someMethod", 1, 2, new String[]{"test"});

        boolean result = invocationMatcher.hasSameMethod(new InvocationMatcher(invocationToCompare));

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WhenTwoInvocationsHaveDifferentMethods_ThenReturnFalse() {
        InvocationOnMock invocationToCompare = Mockito.on(someMock).invoke("differentMethod", 1, 2);

        boolean result = invocationMatcher.hasSameMethod(new InvocationMatcher(invocationToCompare));

        assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom_WhenInvocationHasNoVarargs_ThenCaptureArgumentsOneByOne() {
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.isInstanceOf(String.class),
                ArgumentMatchers.is("test")
        );

        invocationMatcher = new InvocationMatcher(Mockito.on(someMock).invoke("someMethod", "firstArgument", "secondArgument", matchers.toArray(new Matcher[0])));
        invocationMatcher.captureArgumentsFrom(Mockito.on(someMock).invoke("someMethod", "firstArgument1", "secondArgument1", new String[]{"test"}));

        Matcher firstCapturedArgumentMatcher = matchers.get(0);
        Matcher secondCapturedArgumentMatcher = matchers.get(1);

        Matcher firstCapturedArgument = ((CapturesArguments) firstCapturedArgumentMatcher).getValue();
        Matcher secondCapturedArgument = ((CapturesArguments) secondCapturedArgumentMatcher).getValue();

        verify(someMock, VerificationMode.times(1)).someMethod("firstArgument", "secondArgument");
        verifyNoMoreInteractions(someMock);

        assertEquals("firstArgument1", firstCapturedArgument.getValue());
        assertEquals("secondArgument1", secondCapturedArgument.getValue());
    }

    @Test
    public void testCaptureArgumentsFrom_WhenInvocationHasVarargs_ThenCaptureArgumentsInTwoLists() {
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.containsString("test"),
                ArgumentMatchers.containsString("test2")
        );

        invocationMatcher = new InvocationMatcher(Mockito.on(someMock).invoke("someMethod", 1, 2, matchers.toArray(new Matcher[0])));
        invocationMatcher.captureArgumentsFrom(Mockito.on(someMock).invoke("someMethod", 1, 2, new String[]{"test1", "test2", "test3"}));

        List<Matcher> capturedArguments = ((CapturesArguments) matchers.get(0)).getCaptured();
        List<Object> capturedRawArguments = ((CapturesArguments) matchers.get(1)).getCaptured();

        verify(someMock, VerificationMode.times(1)).someMethod(1, 2, (String[]) Arrays.asList("test1", "test2", "test3").toArray(new String[0]));
        verifyNoMoreInteractions(someMock);

        assertEquals(Arrays.asList("test1", "test2", "test3"), capturedArguments);
        assertEquals(Arrays.asList(1, 2, "test3"), capturedRawArguments);
    }

    @Test
    public void testCreateFrom_WhenGivenListOfInvocations_ThenCreateListOfInvokeMatchers() {
        List<Invocation> invocations = Arrays.asList(
                Mockito.on(someMock).invoke("someMethod", 1, 2, new String[]{"test1", "test2"}),
                Mockito.on(someMock).invoke("someMethod", 1, 3, new String[]{"test3", "test1"})
        );

        List<InvocationMatcher> invokeMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invokeMatchers.size());

        InvocationMatcher invokeMatcher1 = invokeMatchers.get(0);
        InvocationMatcher invokeMatcher2 = invokeMatchers.get(1);

        verify(someMock, VerificationMode.times(2)).someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(String[].class));
        verifyNoMoreInteractions(someMock);

        assertEquals("someMethod(java.lang.Object, java.lang.Object, [Ljava.lang.String;])", invokeMatcher1.getMethod().getName());
        assertEquals(Arrays.asList(1, 2, Arrays.asList("test1", "test2")), invokeMatcher1.getMatchers());

        assertEquals("someMethod(java.lang.Object, java.lang.Object, [Ljava.lang.String;])", invokeMatcher2.getMethod().getName());
        assertEquals(Arrays.asList(1, 3, Arrays.asList("test3", "test1")), invokeMatcher2.getMatchers());
    }
}