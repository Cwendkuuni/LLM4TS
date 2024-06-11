
import org.junit.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void createMock_invalidClass_throwsIllegalArgumentException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test
    public void createMock_validClass_noExceptionsThrown() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void createMock_validClassWithExtraInterfaces_noExceptionsThrown() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().extraInterfaces(MyInterface.class));
        assertNotNull(mock);
    }

    @Test
    public void createMock_validClassWithSpiedInstance_noExceptionsThrown() {
        MockUtil mockUtil = new MockUtil();
        Object spiedInstance = new Object();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(mock);
        // Verify that the spiedInstance is copied to the mock
        // ...
    }

    @Test
    public void resetMock_mockObject_mockResetSuccessfully() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // Verify that the mock has been reset
        // ...
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_nonMockObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        mockUtil.resetMock(nonMock);
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_nullObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void getMockHandler_mockObject_returnsMockHandler() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_nonMockObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        mockUtil.getMockHandler(nonMock);
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_nullObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void isMock_mockObject_returnsTrue() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void isMock_nonMockObject_returnsFalse() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void isMock_nullObject_returnsFalse() {
        MockUtil mockUtil = new MockUtil();
		assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void getMockName_mockObject_returnsMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().name("mockName"));
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("mockName", mockName.toString());
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_nonMockObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        mockUtil.getMockName(nonMock);
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_nullObject_throwsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(null);
    }
}

