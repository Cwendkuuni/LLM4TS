
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class);
        assertNotNull(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidType() {
        mockUtil.createMock(null);
    }

    @Test
    public void testCreateMockWithSettings() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class);
        mockUtil.resetMock(mock);
        verify(mock).clear();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNull() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        ClassToMock mock = new ClassToMock();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNull() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNull() {
        mockUtil.getMockName(null);
    }

    private static class ClassToMock implements Serializable {
        public void clear() {
            // Clear implementation
        }
    }
}
