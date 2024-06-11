import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationDataImpl;

public class MockHandlerTest {

    private MockHandler<TestObject> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);

        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void handle_WhenInvocationContainerHasAnswersForStubbing_ShouldSetMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        assertNull(result);
    }

    @Test
    public void handle_WhenVerificationModeIsNotNull_ShouldVerifyDataAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(data.getMatchers()).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(data).getInvocation();
        verify(invocationContainerImpl).getInvocations();
        verify(verificationMode).verify(data);
        assertNull(result);
    }

    @Test
    public void handle_WhenStubbedInvocationIsNotNull_ShouldCaptureArgumentsAndReturnAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<TestObject> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        TestObject expectedResult = new TestObject();

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(expectedResult);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void handle_WhenStubbedInvocationIsNull_ShouldReturnDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        TestObject expectedResult = new TestObject();
        Answer defaultAnswer = mock(Answer.class);

        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn(expectedResult);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void voidMethodStubbable_ShouldReturnVoidMethodStubbableInstance() {
        // Arrange
        TestObject mock = mock(TestObject.class);
        VoidMethodStubbableImpl<TestObject> stubbable = mock(VoidMethodStubbableImpl.class);

        when(stubbable.voidMethodStubbable(mock)).thenReturn(stubbable);

        // Act
        VoidMethodStubbable<TestObject> result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertEquals(stubbable, result);
    }

    @Test
    public void getMockSettings_ShouldReturnMockSettings() {
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void setAnswersForStubbing_ShouldSetAnswersForStubbingInInvocationContainer() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_ShouldReturnInvocationContainer() {
        // Act
        InvocationContainerImpl result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainerImpl, result);
    }
}

class TestObject {
    // Test class
}