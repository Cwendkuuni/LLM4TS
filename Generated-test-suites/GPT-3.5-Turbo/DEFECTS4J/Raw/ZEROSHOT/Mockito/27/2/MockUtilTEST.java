import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Mock
    private MockCreationValidator creationValidator;

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof Object);

        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());

        verify(settings).initiateMockName(classToMock);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Factory.class);

        mockUtil.resetMock(mock);

        verify(creationValidator).validateType(any());
        verify(creationValidator).validateExtraInterfaces(any(), any(Class[].class));
        verify(creationValidator).validateMockedType(any(), any());

        verify(mock, times(1)).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        Object mock = mock(Factory.class);
        MockHandlerInterface<?> mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);

        when(interceptor.getHandler()).thenReturn(mockHandler);

        ((Factory) mock).setCallback(0, interceptor);

        assertSame(mockHandler, mockUtil.getMockHandler(mock));
    }

    @Test
    public void testIsMockWithNullMock() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object mock = new Object();

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithMockitoMock() {
        Object mock = mock(Factory.class);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Factory.class);
        MockName mockName = mock(MockName.class);
        MockHandlerInterface<?> mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);

        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        when(mockHandler.getMockSettings().getMockName()).thenReturn(mockName);
        when(interceptor.getHandler()).thenReturn(mockHandler);

        ((Factory) mock).setCallback(0, interceptor);

        assertSame(mockName, mockUtil.getMockName(mock));
    }
}