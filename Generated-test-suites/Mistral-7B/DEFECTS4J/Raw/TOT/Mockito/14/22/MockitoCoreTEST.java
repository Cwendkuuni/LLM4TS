import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalVerifier;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockitoCoreTest {

    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    private T mock;
    private OngoingStubbing stubbing;
    private Answer<String> answer;

    @Before
    public void setup() {
        reporter = Mockito.spy(new Reporter());
        mockUtil = Mockito.spy(new MockUtil());
        mockingProgress = Mockito.spy(new ThreadSafeMockingProgress());
    }

    @Test
    public void testMockCreation() {
        // Create a mock
        Class<String> mockClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        mock = Mockito.mock(mockClass, mockSettings);

        // Verify that the mock was created
        verify(mockUtil).createMock(eq(mockClass), argThat(isA(MockSettingsImpl.class)));
        verify(mockingProgress).mockingStarted(eq(mock), eq(mockClass), argThat(notNullValue()));

        // Verify that the reporter was not called with any errors
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testStubbing() {
        // Given a mock
        mock = Mockito.mock(String.class);

        // When a stubbing is created
        stubbing = Mockito.stub(mock);

        // Then the stubbing should be initialized
        verify(mockingProgress).stubbingStarted();

        // And the stubbing should be returnable as an OngoingStubbing
        verify(stubbing).isInstanceOf(OngoingStubbing.class);
    }

    @Test
    public void testDeprecatedStubbing() {
        // Given a mock
        mock = Mockito.mock(String.class);

        // When a stubbing is created using the deprecated method
        stubbing = Mockito.stub(mock);

        // Then the stubbing should be initialized
        verify(mockingProgress).stubbingStarted();

        // And the stubbing should be returnable as a DeprecatedOngoingStubbing
        verify(stubbing).isInstanceOf(DeprecatedOngoingStubbing.class);
    }

    @Test
    public void testWhenStubbing() {
        // Given a mock and a mocking progress reset
        mock = Mockito.mock(String.class);
        mockingProgress.reset();

        // When a when statement is used
        when(mock).thenReturn("Hello");

        // Then the stubbing should be initialized
        verify(mockingProgress).stubbingStarted();

        // And the stub should be set on the mock
        verify(mockUtil, times(1)).setReturnValue("Hello", eq(mock), any(InvocationOnMock.class));
    }

    @Test
    public void testStubbingMultipleTimes() {
        // Given a mock and a mocking progress reset
        mock = Mockito.mock(String.class);
        mockingProgress.reset();

        // When a when statement is used multiple times
        when(mock).thenReturn("Hello").thenReturn("World");

        // Then the stubbing should be initialized twice
        verify(mockingProgress, times(2)).stubbingStarted();

        // And the stubs should be set on the mock in the order they were defined
        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);
        verify(mockUtil).setAnswer(eq(mock), argThat(isA(Stubber.class)), answerCaptor.capture());
        Answer answer1 = answerCaptor.getValue();
        verify(mockUtil).setAnswer(eq(mock), argThat(isA(Stubber.class)), answerCaptor.capture());
        Answer answer2 = answerCaptor.getValue();
        assertEquals("Hello", answer1.answer(eq(null), any(InvocationOnMock.class)));
        assertEquals("World", answer2.answer(eq(null), any(InvocationOnMock.class)));
    }

    @Test
    public void testAnswer() {
        // Given a mock and a mocking progress reset
        mock = Mockito.mock(String.class);
        mockingProgress.reset();

        // Given an answer
        answer = Mockito.answer(invocation -> "Answer: " + invocation.getArgument(0));

        // When the answer is used
        OngoingStubbing<String> stubbing = Mockito.when(mock).thenAnswer(answer);

        // Then the answer should be set on the stubbing
        verify(stubbing).answers(answer);

        // And the answer should be called when the mock method is invoked
        String result = stubbing.getMock().someMethod("Test");
        assertEquals("Test", result);
        verify(answer).answer(eq(null), argThat(isA(InvocationOnMock.class)));
    }

    @Test
    public void testVerify() {
        // Given a mock and a mocking progress reset
        mock = Mockito.mock(String.class);
        mockingProgress.reset();

        // When a verbose verify is called
        verify(mock, VerificationMode.verifyNoInteractions());

        // Then the verify should be initialized
        verify(mockingProgress).verificationStarted(eq(VerificationMode.verifyNoInteractions()));

        // And no interactions should be reported
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        // Given a non-mock object
        Object nonMock = new Object();

        // When a verify is called on the non-mock object
        verify(nonMock, VerificationMode.verifyNoInteractions());

        // Then a NotAMockException should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNull() {
        // Given a null mock
        mock = null;

        // When a verbose verify is called
        verify(mock, VerificationMode.verifyNoInteractions());

        // Then a NullPointerException should be thrown
    }

    @Test
    public void testVerifyInteractions() {
        // Given a mock and some stubbings
        mock = Mockito.mock(String.class);
        when(mock).thenReturn("Mockito");

        // When a verbose verify is called
        verify(mock, VerificationMode.verifyInteractions(verify(mock).thenReturn("Mocked")));

        // Then the interactions should be verified
        verify(mockUtil, times(1)).setReturnValue("Mocked", eq(mock), any(InvocationOnMock.class));
    }

    @Test
    public void testVerifyOrder() {
        // Given two mocks and an in order context
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        InOrder inOrder = Mockito.inOrder(mock1, mock2);

        // When some interactions occur on the mocks
        doReturn("Mock1").when(mock1).someMethod();
        doReturn("Mock2").when(mock2).someMethod();

        // When a verify in order is called
        verify(mock1).someMethod();
        verify(mock2).someMethod();
        verifyNoMoreInteractions(mock1, mock2);
        verifyInteractionsInOrder(inOrder);
    }

    @Test
    public void testReset() {
        // Given a mock and some stubbings
        mock = Mockito.mock(String.class);
        when(mock).thenReturn("Mockito");

        // When the mock and the mocking progress are reset
        mockingProgress.reset();
        reset(mock);

        // Then the stubbings should be reset
        verify(mockUtil).resetMock(eq(mock));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Given a mock and some interactions
        mock = Mockito.mock(String.class);
        doReturn("Mockito").when(mock).someMethod();

        // When no more interactions are verified
        verifyNoMoreInteractions(mock);

        // Then no interactions should be reported
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        // Given a non-mock object
        Object nonMock = new Object();

        // When no more interactions are verified on the non-mock object
        verifyNoMoreInteractions(nonMock);

        // Then a NotAMockException should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsNull() {
        // Given a null mock
        mock = null;

        // When no more interactions are verified
        verifyNoMoreInteractions(mock);

        // Then a NullPointerException should be thrown
    }

    @Test
    public void testStubVoid() {
        // Given a mock
        mock = Mockito.mock(String.class);

        // When a stubVoid is created
        VoidMethodStubbable<String> stubbing = Mockito.stubVoid(mock);

        // Then the stubbing should be initialized
        verify(mockingProgress).stubbingStarted();

        // And the stub should be set on the mock
        verify(mockUtil).voidMethodStubbable(eq(mock));
    }

    @Test
    public void testValidateMockitoUsage() {
        // Given a mocking progress with no ongoing stubbings
        mockingProgress.reset();

        // When the validateMockitoUsage is called
        MockitoCore.validateMockitoUsage();

        // Then no errors should be reported
        verifyNoMoreInteractions(reporter);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateMockitoUsageWithOngoingStubbing() {
        // Given a mocking progress with an ongoing stubbing
        mockingProgress.stubbingStarted();

        // When the validateMockitoUsage is called
        MockitoCore.validateMockitoUsage();

        // Then an IllegalStateException should be thrown
    }

    @Test
    public void testGetLastInvocation() {
        // Given a mock with some interactions
        mock = Mockito.mock(String.class);
        doReturn("Mockito").when(mock).someMethod();

        // When a last invocation is retrieved
        Invocation lastInvocation = MockitoCore.getLastInvocation();

        // Then the invocation should match the latest interaction
        verify(mockUtil, times(1)).getLastInvocation(eq(mock));
        assertEquals("someMethod", lastInvocation.getMethod().getName());
        assertEquals("[Mockito]", lastInvocation.getArguments().toString());
    }
}