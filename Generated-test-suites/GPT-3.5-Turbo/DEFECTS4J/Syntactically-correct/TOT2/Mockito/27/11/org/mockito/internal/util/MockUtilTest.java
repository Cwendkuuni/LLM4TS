import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Test creating a mock with valid inputs
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        ClassToMock classToMock = ClassToMock.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        assertNotNull(mockUtil.createMock(classToMock, settings));
        
    }

    @Test
    public void testResetMock() {
        // Test resetting a mock 
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        mockUtil.resetMock(mock);
        // Verify that the mock has been reset
        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        // Test getMockHandler with null mock
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        // Test getMockHandler with a non-mock object
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMock() {
        // Test isMock with a valid mock object
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        // Test getting mock name
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}