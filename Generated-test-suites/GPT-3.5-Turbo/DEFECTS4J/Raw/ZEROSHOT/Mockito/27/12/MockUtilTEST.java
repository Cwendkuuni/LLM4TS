import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.mockito.Mockito.*;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_shouldReturnMock() {
        // Arrange
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestInterface result = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestInterface);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNullMock_shouldThrowNotAMockException() {
        // Arrange
        TestInterface mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert: Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNonMockitoMock_shouldThrowNotAMockException() {
        // Arrange
        TestInterface mock = new TestInterfaceImpl();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert: Exception expected
    }

    @Test
    public void testGetMockHandler_withMockitoMock_shouldReturnMockHandler() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        MockHandlerInterface<TestInterface> result = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockHandlerInterface);
    }

    @Test
    public void testIsMock_withNull_shouldReturnFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_withNonMockitoMock_shouldReturnFalse() {
        // Arrange
        Object mock = new TestInterfaceImpl();

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_withMockitoMock_shouldReturnTrue() {
        // Arrange
        Object mock = mock(TestInterface.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testResetMock_shouldResetMock() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock, times(1)).dummyMethod();
    }

    @Test
    public void testGetMockName_shouldReturnMockName() {
        // Arrange
        TestInterface mock = mock(TestInterface.class);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    public interface TestInterface {
        void dummyMethod();
    }

    public class TestInterfaceImpl implements TestInterface {
        public void dummyMethod() {
            // Do nothing
        }
    }
}