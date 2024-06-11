import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.internal.MockitoCore;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Object mockObject = mockitoCore.mock(Object.class);
        assertNotNull(mockObject);
    }

    @Test
    public void testWhen() {
        Object mockObject = mock(Object.class);
        OngoingStubbing stubbing = mockitoCore.when(mockObject);
        assertNotNull(stubbing);
    }

    @Test
    public void testVerify() {
        Object mockObject = mock(Object.class);
        Object verifiedObject = mockitoCore.verify(mockObject, times(1));
        assertEquals(mockObject, verifiedObject);
    }

    @Test
    public void testReset() {
        Object mockObject = mock(Object.class);
        mockitoCore.reset(mockObject);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mockObject = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mockObject);
    }

    @Test
    public void testInOrder() {
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);
        assertNotNull(mockitoCore.inOrder(mockObject1, mockObject2));
    }

    @Test
    public void testDoAnswer() {
        Object mockObject = mock(Object.class);
        assertNotNull(mockitoCore.doAnswer(invocation -> null));
    }

    @Test
    public void testStubVoid() {
        Object mockObject = mock(Object.class);
        assertNotNull(mockitoCore.stubVoid(mockObject));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        assertNotNull(mockitoCore.getLastInvocation());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNullMock() {
        mockitoCore.verify(null, times(1));
    }
}