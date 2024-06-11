
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        assertNotNull(mockUtil.createMock(Object.class, new MockSettingsImpl()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidType() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidExtraInterfaces() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(Object.class, new MockSettingsImpl().extraInterfaces(null));
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // Verify that the mock has been reset successfully
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMock() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockName(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(null);
    }
}
