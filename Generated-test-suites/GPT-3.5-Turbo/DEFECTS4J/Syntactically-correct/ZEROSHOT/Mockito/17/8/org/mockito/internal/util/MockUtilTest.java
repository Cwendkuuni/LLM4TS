import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Setup
        Class<T> classToMock = T.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Method to test
        T mock = mockUtil.createMock(classToMock, settings);

        // Verification
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        // Setup
        Class<T> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Method to test
        T mock = mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() {
        // Setup
        T mock = mock(T.class);

        // Method to test
        mockUtil.resetMock(mock);

        // Verification
        // No exceptions should be thrown
    }

    @Test
    public void testGetMockHandler() {
        // Setup
        T mock = mock(T.class);

        // Method to test
        MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);

        // Verification
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        // Setup
        T mock = null;

        // Method to test
        MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        // Setup
        T mock = new T();

        // Method to test
        MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        // Setup
        T mock = mock(T.class);

        // Method to test
        boolean isMock = mockUtil.isMock(mock);

        // Verification
        assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockObject() {
        // Setup
        T obj = new T();

        // Method to test
        boolean isMock = mockUtil.isMock(obj);

        // Verification
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName() {
        // Setup
        T mock = mock(T.class);
        MockName mockName = new MockName();
        when(mockUtil.getMockHandler(mock).getMockSettings().getMockName()).thenReturn(mockName);

        // Method to test
        MockName result = mockUtil.getMockName(mock);

        // Verification
        assertEquals(mockName, result);
    }
}

class T {
    // Empty class for testing purposes
}