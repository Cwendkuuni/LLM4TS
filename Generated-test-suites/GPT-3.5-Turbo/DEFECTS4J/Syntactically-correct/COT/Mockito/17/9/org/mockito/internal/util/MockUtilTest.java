
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_basic() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        verifyNoInteractions(mock);
    }

    @Test
    public void testCreateMock_withSpiedInstance() {
        Class<?> classToMock = Object.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertSame(spiedInstance, settings.getSpiedInstance());
        verifyNoInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullArgument() {
        Object mock = null;
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_notMock() {
        Object mock = new Object();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandler_mock() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verifyNoInteractions(mock);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test
    public void testResetMock() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verifyNoInteractions(mock);
        mockUtil.resetMock(mock);
    }

    @Test
    public void testIsMock_true() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verifyNoInteractions(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_false() {
        Object notMock = new Object();
        assertFalse(mockUtil.isMock(notMock));
    }

    @Test
    public void testGetMockName() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verifyNoInteractions(mock);
        assertNotNull(mockUtil.getMockName(mock));
    }
}
