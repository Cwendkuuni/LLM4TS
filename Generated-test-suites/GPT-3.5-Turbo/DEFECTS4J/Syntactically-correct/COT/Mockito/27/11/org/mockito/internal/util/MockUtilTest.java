
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Test valid classToMock and settings
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MyClass mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        // Test invalid classToMock
        classToMock = null;
        assertThrows(NotAMockException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });

        // Test invalid extraInterfaces
        classToMock = MyClass.class;
        settings.setExtraInterfaces(null);
        assertThrows(NotAMockException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });

        // Test valid spiedInstance
        classToMock = MyClass.class;
        settings.setSpiedInstance(new MyClass());
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        // Test invalid spiedInstance
        settings.setSpiedInstance(null);
        assertThrows(NotAMockException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });
    }

    @Test
    public void testResetMock() {
        // Test resetting a valid mock
        MyClass mock = mock(MyClass.class);
        mockUtil.resetMock(mock);
        // Verify reset behaviour

        // Test resetting null mock
        mock = null;
        assertThrows(NotAMockException.class, () -> {
            mockUtil.resetMock(mock);
        });
    }

    @Test
    public void testGetMockHandler() {
        // Test getting the mock handler of a valid mock
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        // Test getting the mock handler of a null object
        mock = null;
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockHandler(mock);
        });

        // Test getting the mock handler of a non-mock object
        Object nonMock = new Object();
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockHandler(nonMock);
        });
    }

    @Test
    public void testIsMock() {
        // Test checking a valid mock object
        MyClass mock = mock(MyClass.class);
        assertTrue(mockUtil.isMock(mock));

        // Test checking a null object
        mock = null;
        assertFalse(mockUtil.isMock(mock));

        // Test checking a non-mock object
        Object nonMock = new Object();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetMockName() {
        // Test getting the mock name of a valid mock
        MyClass mock = mock(MyClass.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);

        // Test getting the mock name of a null object
        mock = null;
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockName(mock);
        });

        // Test getting the mock name of a non-mock object
        Object nonMock = new Object();
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockName(nonMock);
        });
    }
}
