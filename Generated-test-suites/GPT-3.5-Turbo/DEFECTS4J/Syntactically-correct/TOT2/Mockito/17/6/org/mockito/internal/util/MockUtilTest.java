import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockName;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Typical use case
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        // Typical use case
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }

    @Test
    public void testGetMockHandler() {
        // Typical use case
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        MockHandler<String> returnedMockHandler = mockUtil.getMockHandler(mock);
        assertEquals(mockHandler, returnedMockHandler);
    }

    @Test
    public void testIsMock() {
        // Typical use case
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        // Typical use case
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}