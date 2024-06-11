package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<MockitoTestObject> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainer invocationContainer;
    private MatchersBinder matchersBinder;
    private ThreadSafeMockingProgress mockingProgress;

    @Before
    public void setUp() {
        this.mockSettings = new MockSettingsImpl();
        this.mockHandler = new MockHandler<MockitoTestObject>(this.mockSettings);
        this.matchersBinder = this.mockHandler.matchersBinder;
        this.mockingProgress = this.mockHandler.mockingProgress;
        this.invocationContainer = this.mockHandler.invocationContainerImpl;
    }

    @Test
    public void testHandleVoidMethodInvocation() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Answer";
            }
        }).when(isA(MockitoTestObject.class)).voidMethod();

        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "voidMethod", new Object[0], 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress, never()).pushVerificationMode(eq(VerificationMode.Times.NEVER()));
        verify(mockingProgress).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verifyZeroInteractions(invocationContainer.getInvocations());
        verify(mockSettings.getDefaultAnswer()).answer(isA(InvocationOnMock.class));

        assertNull(result);
    }

    @Test
    public void testHandleNonVoidMethodInvocationWithAnswer() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Answer";
            }
        }).when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param"));

        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress, never()).pushVerificationMode(eq(VerificationMode.Times.NEVER()));
        verify(mockingProgress).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verify(invocationContainer.getInvocations()).contains(invocation);
        verify(mockSettings.getDefaultAnswer()).answer(isA(InvocationOnMock.class));

        assertSame("Answer", result);
    }

    @Test
    public void testHandleNonVoidMethodInvocationWithoutAnswer() throws Throwable {
        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress, never()).pushVerificationMode(eq(VerificationMode.Times.NEVER()));
        verify(mockingProgress).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verifyZeroInteractions(invocationContainer.getInvocations());
        verify(mockSettings.getDefaultAnswer()).answer(isA(InvocationOnMock.class));

        assertNull(result);
    }

    @Test
    public void testHandleInvocationWithVerificationMode() throws Throwable {
        VerificationMode verificationMode = VerificationMode.times(1);
        this.mockingProgress.pushVerificationMode(verificationMode);

        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).pushVerificationMode(eq(verificationMode));
        verify(mockingProgress).popVerificationMode();
        verify(mockingProgress).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verify(invocationContainer.getInvocations()).contains(invocation);
        verify(mockSettings.getDefaultAnswer()).answer(isA(InvocationOnMock.class));

        assertNull(result);
    }

    @Test
    public void testHandleInvocationWithMultipleAnswers() throws Throwable {
        List<Answer> answers = Arrays.asList(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return "Answer1";
                    }
                },
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return "Answer2";
                    }
                });
        this.mockHandler.setAnswersForStubbing(answers);

        Object result1 = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param1"}, 0));
        Object result2 = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param2"}, 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress, never()).pushVerificationMode(eq(VerificationMode.Times.NEVER()));
        verify(mockingProgress, times(2)).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation1));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation2));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verify(invocationContainer.getInvocations()).contains(invocation1).contains(invocation2);
        verifyZeroInteractions(mockSettings.getDefaultAnswer());

        assertSame("Answer1", result1);
        assertSame("Answer2", result2);
    }

    @Test
    public void testHandleInvocationWithStubbing() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doReturn("Answer").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param"));

        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress, never()).pushVerificationMode(eq(VerificationMode.Times.NEVER()));
        verify(mockingProgress).reportOngoingStubbing(isA(OngoingStubbingImpl.class));
        verify(matchersBinder).bindMatchers(eq(mockingProgress.getArgumentMatcherStorage()), eq(invocation));
        verify(invocationContainer).setInvocationForPotentialStubbing(isA(StubbedInvocationMatcher.class));
        verify(invocationContainer).findAnswerFor(isA(InvocationOnMock.class));
        verify(invocationContainer.getInvocations()).contains(invocation);
        verify(mockSettings.getDefaultAnswer()).answer(isA(InvocationOnMock.class));

        assertSame("Answer", result);
    }

    @Test
    public void testHandleInvocationWithVerify() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doReturn("Answer").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param"));

        verify(this.mockHandler.invocationContainerImpl.getInvocations()).size();

        this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(this.mockHandler.mockingProgress).pushVerificationMode(VerificationMode.times(1));
        verify(this.mockHandler.mockingProgress).popVerificationMode();
        verifyZeroInteractions(this.mockHandler.invocationContainerImpl.getInvocations());

        assertSame("Answer", verify(mock(MockitoTestObject.class)).nonVoidMethod("param"));
    }

    @Test
    public void testHandleInvocationWithVerifyZeroInteractions() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doReturn("Answer").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param"));

        this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verifyZeroInteractions(this.mockHandler.invocationContainerImpl.getInvocations());

        verifyZeroInteractions(mock(MockitoTestObject.class));

        assertSame("Answer", verifyZeroInteractions(mock(MockitoTestObject.class)));
    }

    @Test
    public void testHandleInvocationWithVerifyNever() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doReturn("Answer").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param"));

        this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(this.mockHandler.mockingProgress, never()).pushVerificationMode(VerificationMode.times(1));

        verifyNever(mock(MockitoTestObject.class)).nonVoidMethod("param");

        assertSame("Answer", verify(mock(MockitoTestObject.class)).nonVoidMethod("param"));
    }

    @Test
    public void testHandleInvocationWithVerifyAllInteractions() throws Throwable {
        Stubber stubber = mockingProgress.getStubber();
        stubber.doReturn("Answer1").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param1"));
        stubber.doReturn("Answer2").when(isA(MockitoTestObject.class)).nonVoidMethod(eq("param2"));

        this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param1"}, 0));
        this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param2"}, 0));

        verify(this.mockHandler.mockingProgress).pushVerificationMode(VerificationMode.times(1));
        verify(this.mockHandler.mockingProgress).popVerificationMode();
        verify(this.mockHandler.invocationContainerImpl.getInvocations()).size();

        verify(mock(MockitoTestObject.class)).nonVoidMethod("param1");
        verify(mock(MockitoTestObject.class)).nonVoidMethod("param2");

        assertSame("Answer1", verify(mock(MockitoTestObject.class)).nonVoidMethod("param1"));
        assertSame("Answer2", verify(mock(MockitoTestObject.class)).nonVoidMethod("param2"));
    }

    @Test
    public void testHandleInvocationWithSpy() throws Throwable {
        MockitoTestObject spyObject = spy(new MockitoTestObject());
        this.mockHandler = new MockHandler<>(spyObject);

        Object result = this.mockHandler.handle(new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param"}, 0));

        verify(spyObject).nonVoidMethod("param");

        assertSame("Result from the real method", result);
    }

    private InvocationOnMock invocation1 = new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param1"}, 0);
    private InvocationOnMock invocation2 = new InvocationOnMock(MockitoTestObject.class, "nonVoidMethod", new Object[]{"param2"}, 0);
}

class MockitoTestObject {
    public void nonVoidMethod(String param) {
        // some implementation
    }

    public void voidMethod() {
        // some void implementation
    }
}