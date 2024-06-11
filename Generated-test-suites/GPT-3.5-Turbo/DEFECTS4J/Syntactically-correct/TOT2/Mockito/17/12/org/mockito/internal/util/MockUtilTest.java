import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock_basic() {
        // Create a basic mock with default settings
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        
        assertNotNull(mock);
    }

    @Test
    public void testCreateMock_extraInterfaces() {
        // Create a mock with extra interfaces
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(ExtraInterface.class);
        
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mockUtil.createMock(MyClass.class, settings);
        
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullArgument() {
        // Passing null as argument should throw NotAMockException
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    // Add more test cases here for other methods and edge cases

}