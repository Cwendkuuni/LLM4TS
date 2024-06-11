
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        assertNotNull(mockitoCore);

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        assertNotNull(mockedCore);
    }

    @Test
    public void testMockWithSettingsAndResetOngoingStubbing() {
        boolean shouldResetOngoingStubbing = true;
        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        assertNotNull(mockedCore);
        MockitoCore mockitoCore = mockedCore.mock(MockitoCore.class, Mockito.withSettings(), shouldResetOngoingStubbing);
        assertNotNull(mockitoCore);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNull(stubbing);

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        IOngoingStubbing stubbing2 = mockedCore.stub();
        assertNull(stubbing2);

        MockitoCore mockitoCore2 = mock(MockitoCore.class);
        assertNotNull(mockitoCore2);
        int methodCall = 5;
        DeprecatedOngoingStubbing<Integer> stubbing3 = mockitoCore2.stub(methodCall);
        assertNotNull(stubbing3);

        MockitoCore mockedCore2 = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        Integer methodCall2 = 10;
        OngoingStubbing<Integer> stubbing4 = mockedCore2.when(methodCall2);
        assertNotNull(stubbing4);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Mockito.when(mockitoCore.verify(null, Mockito.times(1))).thenThrow(new Exception("Null mock"));
        try {
            mockitoCore.verify(null, Mockito.times(1));
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Null mock", e.getMessage());
        }

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        try {
            mockedCore.verify(new Object(), Mockito.times(1));
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Not a mock passed to verify", e.getMessage());
        }

        MockitoCore mockitoCore2 = mock(MockitoCore.class);
        assertNotNull(mockitoCore2);
        Object mockObject = mock(Object.class);
        Mockito.when(mockObject.toString()).thenReturn("Test");
        Object result = mockitoCore2.verify(mockObject, Mockito.times(1));
        assertEquals(mockObject, result);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        mockitoCore.reset();

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.reset();

        Object[] mocks = new Object[] { mockitoCore, mockedCore };
        MockitoCore mockitoCore2 = mock(MockitoCore.class);
        assertNotNull(mockitoCore2);
        assertArrayEquals(mocks, new Object[] { mockitoCore, mockedCore });
        mockitoCore2.reset(mockitoCore, mockedCore);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        try {
            mockitoCore.verifyNoMoreInteractions();
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Mocks have to be passed to verifyNoMoreInteractions", e.getMessage());
        }

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        Object mock = new Object();
        mockedCore.verifyNoMoreInteractions(mock);

        try {
            mockedCore.verifyNoMoreInteractions(null);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Null passed to verifyNoMoreInteractions", e.getMessage());
        }

        Object[] mocks = new Object[] { mock };
        MockitoCore mockitoCore2 = mock(MockitoCore.class);
        assertNotNull(mockitoCore2);
        mockitoCore2.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        try {
            mockitoCore.inOrder();
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Mocks have to be passed when creating InOrder", e.getMessage());
        }

        Object mock = new Object();
        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.inOrder(mock);
        try {
            mockedCore.inOrder(null);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Null passed when creating InOrder", e.getMessage());
        }

        Object[] mocks = new Object[] { mock };
        MockitoCore mockitoCore2 = mock(MockitoCore.class);
        assertNotNull(mockitoCore2);
        InOrder inOrder = mockitoCore2.inOrder(mocks);
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        mockitoCore.doAnswer(null);

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.doAnswer(null);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = new Object();
        mockitoCore.stubVoid(mock);

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.stubVoid(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        mockitoCore.validateMockitoUsage();

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        mockitoCore.getLastInvocation();

        MockitoCore mockedCore = Mockito.mock(MockitoCore.class, Mockito.withSettings());
        mockedCore.getLastInvocation();
    }

}
