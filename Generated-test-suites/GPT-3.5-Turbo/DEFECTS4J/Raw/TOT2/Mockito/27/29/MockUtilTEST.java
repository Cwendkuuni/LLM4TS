import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        mockUtil.resetMock(mock);

        // Verify that the mock has been reset
        verify(mock, times(1)).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }
}