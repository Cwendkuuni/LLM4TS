import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerStubber;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockHandlerTest {

    @Test
    public void testHandleMethodWithNullArguments() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        Invocation invocationWithNullArgs = new Invocation(null, new Object[0]);

        try {
            spy.handle(invocationWithNullArgs);
            fail("Expected InvocationException");
        } catch (InvocationException e) {
            // Expected
        }

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testHandleMethodWithExceptionDuringMatcherBinding() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        Exception exception = new Exception("Matcher binding exception");

        doThrow(exception).when(spy.matchersBinder).bindMatchers(Mockito.any(ThreadSafeMockingProgress.class), ArgumentMatchers.eq(invocation));

        try {
            spy.handle(invocation);
            fail("Expected MatchersException.none");
        } catch (MatchersException.None e) {
            // Expected
        }

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testHandleMethodWithExceptionDuringVerificationModePulling() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        Exception exception = new Exception("Verification mode pulling exception");

        doThrow(exception).when(spy.mockingProgress).pullVerificationMode();

        try {
            spy.handle(invocation);
            fail("Expected VerificationException.none");
        } catch (VerificationException.None e) {
            // Expected
        }

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testHandleMethodWithExceptionDuringSettingInvocationForPotentialStubbing() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Exception exception = new Exception("Setting invocation for potential stubbing exception");

        doThrow(exception).when(spy.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);

        try {
            spy.handle(invocation);
            fail("Expected InvocationException");
        } catch (InvocationException e) {
            // Expected
        }

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testHandleMethodWithVerificationModeNullAndWrongMockObject() throws Throwable {
        MockHandler<MockitoTest> spy1 = mock(MockHandler.class);
        MockHandler<MockitoTest> spy2 = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = null;

        doReturn(verificationMode).when(spy1).mockingProgress.pullVerificationMode();

        try {
            spy1.handle(invocation);
            fail("Expected VerificationException.none");
        } catch (VerificationException.None e) {
            // Expected
        }

        try {
            spy2.handle(invocation);
            verify(spy2).handle(ArgumentMatchers.notNull(invocation));
        } catch (VerificationException.None e) {
            // Expected
        }

        verify(spy1, never()).handle(invocation);
    }

    @Test
    public void testHandleMethodWithNoMatchingStubbedInvocation() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);

        try {
            spy.handle(invocation);
            fail("Expected InvocationException");
        } catch (InvocationException e) {
            // Expected
        }

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testHandleMethodWithMultipleMatchingStubbedInvocations() throws Throwable {
        MockHandler<MockitoTest> spy = mock(MockHandler.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        AnswerStubber answerStubber = mock(AnswerStubber.class);

        when(spy.matchersBinder.bindMatchers(Mockito.any(ThreadSafeMockingProgress.class), ArgumentMatchers.eq(invocation)))
            .thenReturn(invocationMatcher);

        StubbedInvocation stubbedInvocation1 = mock(StubbedInvocation.class);
        StubbedInvocation stubbedInvocation2 = mock(StubbedInvocation.class);

        doReturn(stubbedInvocation1).when(invocationMatcher).answer(ArgumentMatchers.any());
        doReturn(stubbedInvocation2).when(invocationMatcher).answer(ArgumentMatchers.any());

        assertEquals(stubbedInvocation1.answer(invocation), spy.handle(invocation));
        assertEquals(stubbedInvocation2.answer(invocation), spy.handle(invocation));

        verify(spy).handle(ArgumentMatchers.notNull(invocation));
    }

    @Test
    public void testSetAnswersForStubbingWithEmptyList() {
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(new MockSettingsImpl());

        try {
            mockHandler.setAnswersForStubbing(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<Answer> emptyAnswersList = Collections.emptyList();

        mockHandler.setAnswersForStubbing(emptyAnswersList);

        assertEquals(emptyAnswersList, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testMockHandlerConstructorWithMockSettingsImpl() {
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(new MockSettingsImpl());

        assertNotNull(mockHandler);
    }

    @Test
    public void testMockHandlerConstructorWithOldMockHandler() {
        MockHandler<MockitoTest> oldMockHandler = mock(MockHandler.class);
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(oldMockHandler);

        assertSame(oldMockHandler, mockHandler.getMockSettings());
    }

    @Test
    public void testGetMockSettings() {
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(new MockSettingsImpl());

        MockSettingsImpl mockSettings = mockHandler.getMockSettings();

        assertNotNull(mockSettings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(new MockSettingsImpl());
        List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<MockitoTest> mockHandler = new MockHandler<>(new MockSettingsImpl());

        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        assertNotNull(invocationContainer);
    }
}