import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        ClassToMock mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(classToMock, times(1));
        verifyZeroInteractions(settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidClassToMock() {
        ClassToMock classToMock = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidExtraInterfaces() {
        ClassToMock classToMock = mock(ClassToMock.class);
        Class<?>[] extraInterfaces = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);

        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_WithSpiedInstance() {
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Object spiedInstance = new Object();
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);

        ClassToMock mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(classToMock, times(1));
        verify(settings, times(1)).getSpiedInstance();
        verifyZeroInteractions(spiedInstance);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        Object mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockitoMock() {
        ClassToMock classToMock = new ClassToMock();

        mockUtil.getMockHandler(classToMock);
    }

    @Test
    public void testGetMockHandler_ValidMock() {
        ClassToMock classToMock = mock(ClassToMock.class);

        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(classToMock);

        assertNotNull(mockHandler);
        verify(classToMock, times(1));
    }

    @Test
    public void testIsMock_ValidMock() {
        ClassToMock classToMock = mock(ClassToMock.class);

        boolean isMock = mockUtil.isMock(classToMock);

        assertTrue(isMock);
    }

    @Test
    public void testIsMock_NullMock() {
        Object mock = null;

        boolean isMock = mockUtil.isMock(mock);

        assertFalse(isMock);
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = mock(ClassToMock.class);

        mockUtil.resetMock(mock);

        verify(mock, times(1)).resetMock();
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = mock(ClassToMock.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }

    private static class ClassToMock {
        public void resetMock() {
            // Reset mock logic
        }
    }
}