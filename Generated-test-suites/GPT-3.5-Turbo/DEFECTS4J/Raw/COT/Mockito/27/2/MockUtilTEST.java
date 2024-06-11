
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        String mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMockWithNullMock() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object mock = new Object();

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithMockitoMock() {
        Object mock = mock(String.class);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);

        mockUtil.resetMock(mock);

        // Verify that mock has been reset
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockName mockName = new MockName();
        when(mockUtil.getMockHandler(mock)).thenReturn(mockName);

        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockName, result);
    }
}
