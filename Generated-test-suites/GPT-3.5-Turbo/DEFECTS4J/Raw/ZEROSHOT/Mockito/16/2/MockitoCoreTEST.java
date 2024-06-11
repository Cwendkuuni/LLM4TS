import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class<Foo> classToMock = Foo.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        Foo mockFoo = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(mockFoo);
        assertEquals(classToMock, mockFoo.getClass());
    }

    @Test
    public void testMock_withMockSettings() {
        Class<Foo> classToMock = Foo.class;
        MockSettings mockSettings = mock(MockSettings.class);

        Foo mockFoo = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mockFoo);
        assertEquals(classToMock, mockFoo.getClass());
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        assertNull(stubbing);
    }

    @Test
    public void testStub_deprecated() {
        Foo methodCall = mock(Foo.class);

        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);

        assertNull(stubbing);
    }

    @Test
    public void testWhen() {
        Foo methodCall = mock(Foo.class);

        OngoingStubbing stubbing = mockitoCore.when(methodCall);

        assertNull(stubbing);
    }

    @Test
    public void testVerify() {
        Foo mock = mock(Foo.class);
        VerificationMode mode = mock(VerificationMode.class);

        Foo verifiedMock = mockitoCore.verify(mock, mode);

        assertNotNull(verifiedMock);
        assertEquals(mock, verifiedMock);
    }

    @Test
    public void testReset() {
        Foo mock1 = mock(Foo.class);
        Foo mock2 = mock(Foo.class);

        mockitoCore.reset(mock1, mock2);

        verify(mock1).reset();
        verify(mock2).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Foo mock = mock(Foo.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mock).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Foo mock1 = mock(Foo.class);
        Foo mock2 = mock(Foo.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        Foo mock = mock(Foo.class);

        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertNull(lastInvocation);
    }

    private static class Foo {

        public void reset() {
            // reset method for testing purposes
        }

        public void verifyNoMoreInteractions() {
            // verifyNoMoreInteractions method for testing purposes
        }
    }
}