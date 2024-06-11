import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.internal.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);

        // Act
        Object mock = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(mock);
        assertTrue(mockitoCore.mockUtil.isMock(mock));
    }

    @Test
    public void testStub() {
        // Arrange
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);

        // Act
        IOngoingStubbing stubbing = mockitoCore.stub();

        // Assert
        assertEquals(ongoingStubbing, stubbing);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_withMethodCall() {
        // Arrange
        Object methodCall = mock(Object.class);

        // Act
        DeprecatedOngoingStubbing<Integer> stubbing = mockitoCore.stub(methodCall);

        // Assert
        assertNotNull(stubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        // Arrange
        Object methodCall = mock(Object.class);

        // Act
        OngoingStubbing<Integer> stubbing = mockitoCore.when(methodCall);

        // Assert
        assertNotNull(stubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }

    @Test
    public void testVerify() {
        // Arrange
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        // Act
        Object result = mockitoCore.verify(mock, mode);

        // Assert
        assertEquals(mock, result);
        verify(mockitoCore.reporter).nullPassedToVerify();
        verify(mockitoCore.reporter).notAMockPassedToVerify();
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        // Arrange
        Object[] mocks = {mock(Object.class)};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        for (Object mock : mocks) {
            verify(mockitoCore.mockUtil).resetMock(mock);
        }
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Arrange
        Object[] mocks = {mock(Object.class)};
        List<Invocation> invocations = Arrays.asList(new Invocation(), new Invocation());
        when(mockitoCore.mockUtil.getMockHandler(any())).thenReturn(mock(MockHandlerInterface.class));
        when(mockitoCore.mockito.allInvocationsFinder.find(any())).thenReturn(invocations);

        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);

        // Assert
        verify(mockitoCore.reporter).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockito.allInvocationsFinder).find(mocks);
        verify(mockitoCore.mockito.verificationModeFactory).noMoreInteractions();
        verify(mockitoCore.mockito.noMoreInteractionsVerificationMode).verify(any());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Arrange
        List<Object> mocks = Arrays.asList(mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);

        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Assert
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockito.allInvocationsFinder).find(mocks);
        verify(mockitoCore.mockito.verificationModeFactory).noMoreInteractions();
        verify(mockitoCore.mockito.noMoreInteractionsVerificationMode).verifyInOrder(any());
    }

    @Test
    public void testAssertMocksNotEmpty() {
        // Arrange
        Object[] mocks = {};

        // Act
        mockitoCore.assertMocksNotEmpty(mocks);

        // Assert
        verify(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        // Arrange
        Object[] mocks = {mock(Object.class)};

        // Act
        InOrder inOrder = mockitoCore.inOrder(mocks);

        // Assert
        assertNotNull(inOrder);
        verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();
        verify(mockitoCore.mockUtil, times(mocks.length)).isMock(any());
        verify(mockitoCore.mockingProgress, times(mocks.length)).validateState();
        verify(mockitoCore.mockingProgress, times(mocks.length)).reset();
    }

    @Test
    public void testDoAnswer() {
        // Arrange
        Answer answer = mock(Answer.class);

        // Act
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(stubber);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        verify(mockitoCore.mockito.stubberImpl).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handler);

        // Act
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(stubbable);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        // Assert
        assertEquals(invocations.get(invocations.size() - 1), lastInvocation);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    }
}