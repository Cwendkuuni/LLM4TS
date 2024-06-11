import org.junit.Test;
import org.mockito.internal.*;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Class<MockSettings> classToMock = MockSettings.class;
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        // When
        when(mockitoCore.mock(classToMock, mockSettings)).thenReturn(mockSettings);

        // Then
        MockSettings result = mockitoCore.mock(classToMock, mockSettings);
        assertEquals(mockSettings, result);
        verify(mockitoCore, times(2)).mock(classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);

        // When
        when(mockitoCore.stub()).thenReturn(stubbing);

        // Then
        IOngoingStubbing result = mockitoCore.stub();
        assertEquals(stubbing, result);
        verify(mockitoCore).stub();
    }

    @Test
    public void testStub_T() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String methodCall = "methodCall";
        DeprecatedOngoingStubbing<String> stubbing = mock(DeprecatedOngoingStubbing.class);

        // When
        when(mockitoCore.stub(methodCall)).thenReturn(stubbing);

        // Then
        DeprecatedOngoingStubbing<String> result = mockitoCore.stub(methodCall);
        assertEquals(stubbing, result);
        verify(mockitoCore).stub(methodCall);
    }

    @Test
    public void testWhen() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String methodCall = "methodCall";
        OngoingStubbing<String> stubbing = mock(OngoingStubbing.class);

        // When
        when(mockitoCore.when(methodCall)).thenReturn(stubbing);

        // Then
        OngoingStubbing<String> result = mockitoCore.when(methodCall);
        assertEquals(stubbing, result);
        verify(mockitoCore).when(methodCall);
    }

    @Test
    public void testVerify() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        String mock = "mock";
        VerificationMode mode = mock(VerificationMode.class);

        // When
        when(mockitoCore.verify(mock, mode)).thenReturn(mock);

        // Then
        String result = mockitoCore.verify(mock, mode);
        assertEquals(mock, result);
        verify(mockitoCore).verify(mock, mode);
    }

    @Test
    public void testReset() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // When
        doNothing().when(mockitoCore).reset(mock1, mock2);

        // Then
        mockitoCore.reset(mock1, mock2);
        verify(mockitoCore).reset(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);

        // When
        doNothing().when(mockitoCore).verifyNoMoreInteractions(mock);

        // Then
        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        List<Object> mocks = mock(List.class);
        InOrderContext inOrderContext = mock(InOrderContext.class);

        // When
        doNothing().when(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Then
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);

        // When
        when(mockitoCore.inOrder(mock)).thenReturn(mock(InOrder.class));

        // Then
        InOrder result = mockitoCore.inOrder(mock);
        assertNotNull(result);
        verify(mockitoCore).inOrder(mock);
    }

    @Test
    public void testDoAnswer() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Answer answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);

        // When
        when(mockitoCore.doAnswer(answer)).thenReturn(stubber);

        // Then
        Stubber result = mockitoCore.doAnswer(answer);
        assertEquals(stubber, result);
        verify(mockitoCore).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);

        // When
        when(mockitoCore.stubVoid(mock)).thenReturn(stubbable);

        // Then
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        assertEquals(stubbable, result);
        verify(mockitoCore).stubVoid(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);

        // When
        doNothing().when(mockitoCore).validateMockitoUsage();

        // Then
        mockitoCore.validateMockitoUsage();
        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        // Given
        MockitoCore mockitoCore = mock(MockitoCore.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);

        // When
        when(mockitoCore.getLastInvocation()).thenReturn(invocation);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Then
        Invocation result = mockitoCore.getLastInvocation();
        assertEquals(invocation, result);
        verify(mockitoCore).getLastInvocation();
    }
}