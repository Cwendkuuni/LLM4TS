import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockUtilTest {

    // Test for createMock method
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Test typical use case
        YourClass obj = mockUtil.createMock(YourClass.class, mockSettings);
        assertNotNull(obj);

        // Test for null classToMock
        assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(null, mockSettings));

        // Test for invalid extra interfaces
        assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(YourClass.class, null));
    }

    // Test for resetMock method
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        YourClass mock = mock(YourClass.class);
        
        // Test typical use case
        mockUtil.resetMock(mock);

        // Test for null mock
        assertThrows(NotAMockException.class, () -> mockUtil.resetMock(null));
    }
    
    // Test for getMockHandler method
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        YourClass mock = mock(YourClass.class);

        // Test typical use case
        MockHandlerInterface<YourClass> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);

        // Test for null mock
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(null));
    }

    // Test for isMock method
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        YourClass mock = mock(YourClass.class);

        // Test typical use case
        assertTrue(mockUtil.isMock(mock));

        // Test for non-mock object
        assertFalse(mockUtil.isMock(new Object()));
    }

    // Test for getMockName method
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        YourClass mock = mock(YourClass.class);

        // Test typical use case
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);

        // Test for null mock
        assertThrows(NotAMockException.class, () -> mockUtil.getMockName(null));
    }
}