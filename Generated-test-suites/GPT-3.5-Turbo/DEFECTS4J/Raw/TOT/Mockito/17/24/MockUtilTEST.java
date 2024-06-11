import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    // Test createMock method
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyClass testMock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        assertNotNull(testMock);

        // Edge case: Class with extra interfaces
        MyClassWithInterface testMockWithInterface = mockUtil.createMock(MyClassWithInterface.class, new MockSettingsImpl().extraInterfaces(MyInterface.class));
        assertNotNull(testMockWithInterface);

        // Error scenario: Invalid class to mock
        try {
            InvalidClass testMockInvalid = mockUtil.createMock(InvalidClass.class, new MockSettingsImpl());
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    // Test resetMock method
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyClass testMock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        mockUtil.resetMock(testMock);
        assertTrue(true); // No exception should be thrown

        // Error scenario: Null mock
        try {
            mockUtil.resetMock(null);
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    // Test getMockHandler method
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyClass testMock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(testMock);
        assertNotNull(mockHandler);

        // Error scenario: Null mock
        try {
            mockUtil.getMockHandler(null);
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Error scenario: Not a mock
        MyClass myClass = new MyClass();
        try {
            mockUtil.getMockHandler(myClass);
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: MyClass", e.getMessage());
        }
    }

    // Test isMock method
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyClass testMock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(testMock));

        // Edge case: Not a mock
        MyClass myClass = new MyClass();
        assertFalse(mockUtil.isMock(myClass));
    }

    // Test getMockName method
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyClass testMock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(testMock);
        assertNotNull(mockName);

        // Error scenario: Null mock
        try {
            mockUtil.getMockName(null);
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Error scenario: Not a mock
        MyClass myClass = new MyClass();
        try {
            mockUtil.getMockName(myClass);
            fail("Should throw exception");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: MyClass", e.getMessage());
        }
    }
}