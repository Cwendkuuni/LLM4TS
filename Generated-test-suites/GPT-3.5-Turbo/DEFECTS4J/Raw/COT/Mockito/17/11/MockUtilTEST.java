
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // TODO: Verify that the mock is reset
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNonMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock nonMock = new ClassToMock();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockNonMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock nonMock = new ClassToMock();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

}
