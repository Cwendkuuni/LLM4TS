import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = Mockito.spy(new MockitoCore());
    }

    @Test
    public void testMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        Object mock = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        
        verify(mockitoCore).mock(classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockitoCore).stub();
        
        IOngoingStubbing returnedStubbing = mockitoCore.stub();
        
        assertNotNull(returnedStubbing);
        assertSame(stubbing, returnedStubbing);
        
        verify(mockitoCore).stub();
    }

    @Test
    public void testStubWithMethodCall() {
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockitoCore).stub();
        
        DeprecatedOngoingStubbing returnedStubbing = mockitoCore.stub(methodCall);
        
        assertNotNull(returnedStubbing);
        assertSame(stubbing, returnedStubbing);
        
        verify(mockitoCore).stub(methodCall);
    }

    @Test
    public void testWhen() {
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockitoCore).stub();
        
        OngoingStubbing returnedStubbing = mockitoCore.when(methodCall);
        
        assertNotNull(returnedStubbing);
        assertSame(stubbing, returnedStubbing);
        
        verify(mockitoCore).when(methodCall);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        Object returnedMock = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, returnedMock);
        
        verify(mockitoCore).verify(mock, mode);
    }

    @Test
    public void testVerifyWithNullMock() {
        try {
            mockitoCore.verify(null, Mockito.mock(VerificationMode.class));
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }

        verify(mockitoCore).verify(null, Mockito.mock(VerificationMode.class));
    }

    @Test
    public void testVerifyWithNotAMock() {
        try {
            mockitoCore.verify(new Object(), Mockito.mock(VerificationMode.class));
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }

        verify(mockitoCore).verify(new Object(), Mockito.mock(VerificationMode.class));
    }

    @Test
    public void testReset() {
        Object[] mocks = {mock(Object.class), mock(Object.class)};
        
        mockitoCore.reset(mocks);
        
        for (Object mock : mocks) {
            verify(mockitoCore).reset(mock);
        }
        
        verify(mockitoCore).reset(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {mock(Object.class), mock(Object.class)};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        for (Object mock : mocks) {
            verify(mockitoCore).verifyNoMoreInteractions(mock);
        }

        verify(mockitoCore).verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        Object[] mocks = {null, mock(Object.class)};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore).verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        Object[] mocks = {new Object(), mock(Object.class)};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore).verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        Object[] mocks = {mock(Object.class), mock(Object.class)};
        
        InOrder returnedInOrder = mockitoCore.inOrder(mocks);
        
        assertNotNull(returnedInOrder);
        verify(mockitoCore).inOrder(mocks);
    }

    @Test
    public void testInOrderWithNullMock() {
        Object[] mocks = {null, mock(Object.class)};
        
        try {
            mockitoCore.inOrder(mocks);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }
        
        verify(mockitoCore).inOrder(mocks);
    }

    @Test
    public void testInOrderWithNotAMock() {
        Object[] mocks = {new Object(), mock(Object.class)};
        
        try {
            mockitoCore.inOrder(mocks);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }
        
        verify(mockitoCore).inOrder(mocks);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);
        doReturn(stubber).when(mockitoCore).doAnswer(answer);
        
        Stubber returnedStubber = mockitoCore.doAnswer(answer);
        
        assertNotNull(returnedStubber);
        assertSame(stubber, returnedStubber);
        
        verify(mockitoCore).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        doReturn(handler).when(mockitoCore).getMockHandler(mock);
        
        VoidMethodStubbable returnedStubbable = mockitoCore.stubVoid(mock);
        
        assertNotNull(returnedStubbable);
        assertSame(handler, returnedStubbable);
        
        verify(mockitoCore).stubVoid(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        doReturn(ongoingStubbing).when(mockitoCore).mockingProgress();
        doReturn(ongoingStubbing).when(mockitoCore).pullOngoingStubbing();
        doReturn(allInvocations).when(ongoingStubbing).getRegisteredInvocations();
        
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, lastInvocation);
        
        verify(mockitoCore).getLastInvocation();
    }
}