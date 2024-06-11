import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    // Test createMock method with valid input
    @Test
    public void testCreateMock_ValidInput() {
        MockUtil mockUtil = new MockUtil();
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MyClass mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
    }

    // Test resetMock method with valid input
    @Test
    public void testResetMock_ValidInput() {
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);

        mockUtil.resetMock(mock);

        // Verify that reset is successful
        verify(mock, times(1)).reset();
    }

    // Test getMockHandler method with null input
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullInput() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }

    // Test getMockHandler method with invalid input
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_InvalidInput() {
        MockUtil mockUtil = new MockUtil();
        MyClass notMock = new MyClass();

        mockUtil.getMockHandler(notMock);
    }

    // Test isMock method with valid mock object
    @Test
    public void testIsMock_ValidMockObject() {
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);

        assertTrue(mockUtil.isMock(mock));
    }

    // Test isMock method with null object
    @Test
    public void testIsMock_NullObject() {
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(null));
    }

    // Test getMockName method with valid mock object
    @Test
    public void testGetMockName_ValidMockObject() {
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }
}