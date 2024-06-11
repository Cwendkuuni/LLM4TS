import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        assertNotNull(mockUtil.createMock(String.class, new MockSettingsImpl()));
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        assertTrue(mockHandler == mockUtil.getMockHandler(mockHandler));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        assertNotNull(mockUtil.getMockName(mockHandler));
    }
}