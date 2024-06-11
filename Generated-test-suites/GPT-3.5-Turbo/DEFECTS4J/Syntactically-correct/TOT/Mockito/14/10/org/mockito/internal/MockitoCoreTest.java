import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.*;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        ClassToMock mock = mockitoCore.mock(ClassToMock.class, mockSettings);
        assertNotNull(mock);
        assertTrue(MockUtil.isMock(mock));
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        // Case 1: stubbing is null
        assertNull(stubbing);
        
        // Case 2: stubbing exists
        when(mockitoCore.stub()).thenReturn(mockitoCore.mock(IOngoingStubbing.class));
        assertNotNull(mockitoCore.stub());
    }

    @Test
    public void testStubWithMethodCall() {
        DeprecatedOngoingStubbing<T> stubbing = mockitoCore.stub(methodCall);

        // Case 1: stubbing is null
        assertNull(stubbing);
        
        // Case 2: stubbing exists
        when(mockitoCore.stub(methodCall)).thenReturn(mockitoCore.mock(DeprecatedOngoingStubbing.class));
        assertNotNull(mockitoCore.stub(methodCall));
    }

    @Test
    public void testWhen() {
        OngoingStubbing<T> stubbing = mockitoCore.when(methodCall);

        // Case 1: stubbing is null
        assertNull(stubbing);
        
        // Case 2: stubbing exists
        when(mockitoCore.when(methodCall)).thenReturn(mockitoCore.mock(OngoingStubbing.class));
        assertNotNull(mockitoCore.when(methodCall));
    }

    @Test
    public void testVerify() {
        // Case 1: mock is null
        try {
            mockitoCore.verify(null, VerificationModeFactory.times(1));
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Null passed to verify", e.getMessage());
        }

        // Case 2: mock is not a mock
        ClassToMock notMock = new ClassToMock();
        try {
            mockitoCore.verify(notMock, VerificationModeFactory.times(1));
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Not a mock passed to verify", e.getMessage());
        }

        // Case 3: valid verify
        ClassToMock mock = mockitoCore.mock(ClassToMock.class, mockSettings);
        mockitoCore.verify(mock, VerificationModeFactory.times(1));
    }

    @Test
    public void testReset() {
        // Case 1: reset with null mocks
        mockitoCore.reset();
        
        // Case 2: reset with valid mocks
        ClassToMock mock1 = mockitoCore.mock(ClassToMock.class);
        ClassToMock mock2 = mockitoCore.mock(ClassToMock.class);
        mockitoCore.reset(mock1, mock2);
        verify(mock1).reset();
        verify(mock2).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Case 1: null mock
        mockitoCore.verifyNoMoreInteractions(null);
        
        // Case 2: not a mock
        ClassToMock notMock = new ClassToMock();
        try {
            mockitoCore.verifyNoMoreInteractions(notMock);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Not a mock passed to verifyNoMoreInteractions", e.getMessage());
        }

        // Case 3: verify no more interactions
        ClassToMock mock = mockitoCore.mock(ClassToMock.class);
        when(mock.methodCall()).thenReturn("test");
        mock.methodCall();
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Case 1: empty mocks list
        mockitoCore.verifyNoMoreInteractionsInOrder(new ArrayList<Object>(), inOrderContext);
        
        // Case 2: valid mocks list
        List<Object> mocks = new ArrayList<Object>();
        ClassToMock mock1 = mockitoCore.mock(ClassToMock.class);
        ClassToMock mock2 = mockitoCore.mock(ClassToMock.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        // Define method calls and verify in order
        when(mock1.methodCall()).thenReturn("test");
        when(mock2.methodCall()).thenReturn("test");
        mock1.methodCall();
        mock2.methodCall();
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        // Case 1: empty mocks
        try {
            InOrder inOrder = mockitoCore.inOrder();
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertEquals("Mocks have to be passed when creating InOrder", e.getMessage());
        }

        // Case 2: valid mocks
        ClassToMock mock1 = mockitoCore.mock(ClassToMock.class);
        ClassToMock mock2 = mockitoCore.mock(ClassToMock.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).methodCall();
        inOrder.verify(mock2).methodCall();
    }

    @Test
    public void testDoAnswer() {
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Case 1: stubbing started
        assertNotNull(stubber);
        verify(mockingProgress).stubbingStarted();

        // Case 2: ongoing stubbing reset
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        ClassToMock mock = mockitoCore.mock(ClassToMock.class);
        VoidMethodStubbable<ClassToMock> stubbable = mockitoCore.stubVoid(mock);

        // Case 1: stubbing started
        assertNotNull(stubbable);
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        // Verify that validateMockitoUsage calls validateState method
        mockitoCore.validateMockitoUsage();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        // Case 1: ongoing stubbing is null
        assertNull(mockitoCore.getLastInvocation());

        // Case 2: ongoing stubbing is not null
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(new Invocation());
        OngoingStubbingImpl ongoingStubbing = mockitoCore.mock(OngoingStubbingImpl.class);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);

        Invocation invocation = mockitoCore.getLastInvocation();
        assertNotNull(invocation);
        assertEquals(invocations.get(0), invocation);
    }
}