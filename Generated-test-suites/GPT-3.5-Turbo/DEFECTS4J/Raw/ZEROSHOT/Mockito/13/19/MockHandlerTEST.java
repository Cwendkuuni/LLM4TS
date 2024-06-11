import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.verification.VerificationDataImpl;

import java.util.List;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandle_InvocationContainerHasAnswersForStubbing_ReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_VerificationModeNotNull_VerifyCalled() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockingProgress = mock(MockingProgress.class);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(verificationData).verify(any());
    }

    @Test
    public void testHandle_VerificationModeNotNull_DifferentMock_VerifyNotCalled() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockingProgress = mock(MockingProgress.class);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(mock(Invocation.class));
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(verificationData, never()).verify(any());
    }

    @Test
    public void testHandle_StubbedInvocationNotNull_ReturnsAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer answer = mock(Answer.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(null);
        when(stubbedInvocation.answer(invocation)).thenReturn(answer);

        Object result = mockHandler.handle(invocation);

        assertSame(answer, result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(invocationContainer, never()).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_StubbedInvocationNull_ReturnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbable() {
        Object mock = mock(Object.class);

        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        assertNotNull(result);
        assertEquals(mockHandler.invocationContainerImpl, result.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettingsImpl() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler.mockSettings = mockSettings;

        MockSettingsImpl result = mockHandler.getMockSettings();

        assertSame(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertSame(invocationContainer, result);
    }
}