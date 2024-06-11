import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockHandler;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

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
        ClassToMock classToMock = Mockito.mock(ClassToMock.class);
        assertNotNull(classToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        ClassToMock classToMock = null;
        mockUtil.createMock(classToMock, null);
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }

    @Test
    public void testResetMockWithNullMock() {
        ClassToMock mock = null;
        mockUtil.resetMock(mock);
        assertNull(mock);
    }

    @Test
    public void testGetMockHandler() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        MockHandler handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        ClassToMock mock = null;
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNullMock() {
        ClassToMock mock = null;
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMockObject() {
        Object nonMockObject = new Object();
        assertFalse(mockUtil.isMock(nonMockObject));
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        ClassToMock mock = null;
        mockUtil.getMockName(mock);
    }

    private interface ClassToMock {}
}