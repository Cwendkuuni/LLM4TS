
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationDataImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockHandler = new MockHandler();
        mockingProgress = new ThreadSafeMockingProgress();
    }

    @Test
    public void handle_withInvocationContainerImplHasAnswersForStubbing_invocationMatcherBoundSuccessfully() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        mockHandler.invocationContainerImpl.setMethodForStubbing(mock(InvocationMatcher.class));

        assertNull(mockHandler.handle(invocation));

        // Verify that invocationContainerImpl has the correct method for stubbing
        assertEquals(mockHandler.invocationContainerImpl.getMethodForStubbing(), invocationMatcher);
    }

    @Test
    public void handle_withVerificationModeVerifyDataCalledSuccessfully() throws Throwable {
        VerificationMode verificationMode = mock(VerificationMode.class);
        Invocation invocation = mock(Invocation.class);
        mockHandler.mockingProgress.pushVerificationMode(verificationMode);

        assertNull(mockHandler.handle(invocation));

        // Verify that verificationMode.verify() was called with the correct data
        verify(verificationMode).verify(new VerificationDataImpl(mockHandler.invocationContainerImpl.getInvocations(), invocationMatcher));
    }

    @Test
    public void handle_withStubbedInvocationExists_stubbedInvocationAnswerCalledSuccessfully() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        mockHandler.invocationContainerImpl.addAnswer(mock(StubbedInvocationMatcher.class));

        assertNotNull(mockHandler.handle(invocation));

        // Verify that stubbedInvocation.answer() was called with the correct invocation
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void handle_withNoStubbedInvocation_defaultAnswerCalledSuccessfully() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        mockHandler.mockSettings.setDefaultAnswer(mock(Answer.class));

        assertNotNull(mockHandler.handle(invocation));

        // Verify that defaultAnswer.answer() was called with the correct invocation
        verify(defaultAnswer).answer(invocation);

        // Verify that invocationContainerImpl.resetInvocationForPotentialStubbing() was called with the correct invocationMatcher
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void voidMethodStubbable_returnsNewVoidMethodStubbableImpl() {
        Object mock = new Object();

        VoidMethodStubbableImpl stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
        assertEquals(mock, stubbable.getMock());
        assertEquals(mockHandler.invocationContainerImpl, stubbable.getInvocationContainer());
    }

    @Test
    public void getMockSettings_returnsMockSettingsImpl() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();

        assertNotNull(mockSettings);
        assertEquals(mockHandler.mockSettings, mockSettings);
    }

    @Test
    public void setAnswersForStubbing_setsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void getInvocationContainer_returnsInvocationContainerImpl() {
        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();

        assertNotNull(invocationContainer);
        assertEquals(mockHandler.invocationContainerImpl, invocationContainer);
    }
}

