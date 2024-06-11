import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = null;
        mockUtil.resetMock(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock_True() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_False() {
        MockUtil mockUtil = new MockUtil();
        String mock = "Not a mock";
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}