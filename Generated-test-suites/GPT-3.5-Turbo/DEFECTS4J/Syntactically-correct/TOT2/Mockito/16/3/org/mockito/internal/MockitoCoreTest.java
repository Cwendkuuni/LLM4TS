import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class<?> classToMock = Object.class;
        assertNotNull(mockitoCore.mock(classToMock, mockitoCore.mockSettings, true));
    }

    @Test
    public void testStub() {
        assertNotNull(mockitoCore.stub());
    }

    @Test
    public void testWhen() {
        assertNotNull(mockitoCore.when(null));
    }

    @Test
    public void testVerify() {
        assertNotNull(mockitoCore.verify(mockitoCore.mock(Object.class), mockitoCore.mockSettings));
    }

    @Test
    public void testReset() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mockitoCore.reset(mock1, mock2);
        verify(mock1, times(1)).reset();
        verify(mock2, times(1)).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mock, times(1)).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        assertNotNull(mockitoCore.inOrder(mock1, mock2));
    }

    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer(null));
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        assertNotNull(mockitoCore.stubVoid(mock));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        assertNotNull(mockitoCore.getLastInvocation());
    }
}