
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {

    private MockUtil mockUtil = new MockUtil();

    // Test createMock method
    @Test
    public void testCreateMock() {
        String expectedName = "mock";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(expectedName);
        Object mock = mockUtil.createMock(Object.class, mockSettings);
        assertTrue(mockUtil.isMock(mock));
        assertEquals(expectedName, mockUtil.getMockName(mock).toString());
    }

    // Test resetMock method
    @Test
    public void testResetMock() {
        String expectedName = "mock";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(expectedName);
        Object mock = mockUtil.createMock(Object.class, mockSettings);
        mockUtil.resetMock(mock);
        assertFalse(mockUtil.isMock(mock));
    }

    // Test getMockHandler method with valid mock
    @Test
    public void testGetMockHandlerWithValidMock() {
        String expectedName = "mock";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(expectedName);
        Object mock = mockUtil.createMock(Object.class, mockSettings);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    // Test getMockHandler method with null mock
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    // Test getMockHandler method with non-mock object
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        mockUtil.getMockHandler("not a mock");
    }

    // Test isMock method with valid mock
    @Test
    public void testIsMockWithValidMock() {
        String expectedName = "mock";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(expectedName);
        Object mock = mockUtil.createMock(Object.class, mockSettings);
        assertTrue(mockUtil.isMock(mock));
    }

    // Test isMock method with non-mock object
    @Test
    public void testIsMockWithNonMockObject() {
        assertFalse(mockUtil.isMock("not a mock"));
    }

    // Test getInterceptor method with valid mock
    @Test
    public void testGetInterceptorWithValidMock() {
        String expectedName = "mock";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(expectedName);
        Object mock = mockUtil.createMock(Object.class, mockSettings);
        assertNotNull(mockUtil.getInterceptor(mock));
    }

    // Test getInterceptor method with non-mock object
    @Test
    public void testGetInterceptorWithNonMockObject() {
        assertNull(mockUtil.getInterceptor("not a mock"));
    }

}

