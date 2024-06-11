import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Test creating a mock with settings
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl(classToMock);
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);

        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        // Test resetting a mock
        ClassToMock mock = mock(ClassToMock.class);
        mockUtil.resetMock(mock);

        // Verify that the mock was reset
        verify(mock, times(1)).reset();
    }

    @Test
    public void testGetMockHandler() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Test getting mock handler for a mock
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);

        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Test when passing null as mock
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotMockitoMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Test when passing a non mockito mock
        ClassToMock mock = new ClassToMock();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        // Test checking if object is mock
        ClassToMock mock = mock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));

        // Test when object is not mock
        Object obj = new Object();
        assertFalse(mockUtil.isMock(obj));
    }

    @Test
    public void testGetMockName() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Test getting mock name for a mock
        ClassToMock mock = mock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }

}