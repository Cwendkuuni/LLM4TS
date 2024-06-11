import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.NotAMockException;
import org.mockito.internal.MockUtil;
import org.mockito.internal.mock.MockHandlerInterface;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.internal.verification.api.Stubbing.inOrder;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettings();

        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mock = mockitoCore.mock(mockitoCoreClass, mockSettings);

        assertNotNull(mock);
        assertEquals(mockitoCore, mock.getMock());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNonMockObject() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nonMockObject = new Object();

        mockitoCore.verify(nonMockObject, VerificationMode.times(1));
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();

        try {
            mockitoCore.verifyNoMoreInteractions(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNonMockObject() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nonMockObject = new Object();

        try {
            mockitoCore.verifyNoMoreInteractions(nonMockObject);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyNoInteractionsWithMockObject() {
        MockitoCore mockitoCore = new MockitoCore();
        MockHandlerInterface<Object> mockHandler = MockUtil.createMock(Object.class);
        Object mock = mockitoCore.mock(Object.class, new MockSettings());
        mockitoCore.verifyNoInteractions(mock);
    }

    @Test
    public void testVerifyInOrderWithMocks() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(Object.class, new MockSettings());
        Object mock2 = mockitoCore.mock(Object.class, new MockSettings());

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettings());
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertNotNull(lastInvocation);
    }
}

//