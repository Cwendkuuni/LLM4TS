
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();

        // Basic test case
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithInterfaces() {
        MockUtil mockUtil = new MockUtil();

        // Test case with interfaces
        Class<?>[] interfaces = {Serializable.class};
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().extraInterfaces(interfaces));
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        // Test case with reset
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();

        // Test case with mock handler
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        // Test case with is mock
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();

        // Test case with mock name
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

}
