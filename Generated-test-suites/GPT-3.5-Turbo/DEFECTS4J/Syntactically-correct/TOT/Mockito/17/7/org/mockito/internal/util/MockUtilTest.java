import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_validClass_validSettings() {
        // Initialize a valid class to mock
        Class<TestClass> classToMock = TestClass.class;

        // Create valid settings for the mock object
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{ExtraInterface.class});
        settings.setSpiedInstance(null);

        // Create the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        TestClass mock = mockUtil.createMock(classToMock, settings);

        // Verify that the mock object is not null
        assertNotNull(mock);

        // Verify the mockHandler is properly initiated
        MockHandlerInterface<TestClass> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        assertEquals(mockHandler.getMockSettings(), settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidClass() {
        // Initialize an invalid class to mock
        Class<InvalidClass> classToMock = InvalidClass.class;

        // Create valid settings for the mock object
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{ExtraInterface.class});
        settings.setSpiedInstance(null);

        // Create the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidExtraInterfaces() {
        // Initialize a valid class to mock
        Class<TestClass> classToMock = TestClass.class;

        // Create invalid settings with invalid extra interfaces
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{InvalidInterface.class});
        settings.setSpiedInstance(null);

        // Create the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_validSpiedInstance() {
        // Initialize a valid class to mock
        Class<TestSpiedClass> classToMock = TestSpiedClass.class;

        // Create valid settings with valid spied instance
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(null);
        settings.setSpiedInstance(new TestSpiedClass());

        // Create the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        TestSpiedClass mock = mockUtil.createMock(classToMock, settings);

        // Verify that the mock object is not null
        assertNotNull(mock);

        // Verify the lenient copy is performed correctly
        assertEquals(mock.getSpiedData(), mock.getOriginalData());
    }

    @Test
    public void testResetMock_validMock() {
        // Create a valid mock object using Mockito
        TestClass mock = mock(TestClass.class);

        // Call resetMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        // Verify that the mock object is reset
        verifyZeroInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock() {
        // Create null mock object
        TestClass mock = null;

        // Call resetMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_invalidMock() {
        // Create an invalid mock object
        TestClass mock = new TestClass();

        // Call resetMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler_validMock() {
        // Create a valid mock object using Mockito
        TestClass mock = mock(TestClass.class);

        // Call getMockHandler method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<TestClass> mockHandler = mockUtil.getMockHandler(mock);

        // Verify that the mockHandler is not null
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Create null mock object
        TestClass mock = null;

        // Call getMockHandler method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_invalidMock() {
        // Create an invalid mock object
        TestClass mock = new TestClass();

        // Call getMockHandler method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock_validMock() {
        // Create a valid mock object using Mockito
        TestClass mock = mock(TestClass.class);

        // Call isMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(mock);

        // Verify that the result is true
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_nullMock() {
        // Create null mock object
        TestClass mock = null;

        // Call isMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(mock);

        // Verify that the result is false
        assertFalse(isMock);
    }

    @Test
    public void testIsMock_invalidMock() {
        // Create an invalid mock object
        TestClass mock = new TestClass();

        // Call isMock method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(mock);

        // Verify that the result is false
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_validMock() {
        // Create a valid mock object using Mockito
        TestClass mock = mock(TestClass.class);

        // Call getMockName method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mock);

        // Verify that the mockName is not null
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullMock() {
        // Create null mock object
        TestClass mock = null;

        // Call getMockName method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_invalidMock() {
        // Create an invalid mock object
        TestClass mock = new TestClass();

        // Call getMockName method on the mock object using MockUtil
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(mock);
    }

}