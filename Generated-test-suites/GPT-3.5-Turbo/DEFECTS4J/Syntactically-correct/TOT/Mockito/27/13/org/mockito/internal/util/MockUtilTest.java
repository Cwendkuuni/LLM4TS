import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.ArrayUtils;
import org.mockito.internal.util.LenientCopyTool;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_typicalCase() {
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(false);
        when(settings.getSpiedInstance()).thenReturn(null);
        doNothing().when(settings).initiateMockName(classToMock);

        mockUtil.createMock(classToMock, settings);

        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, null);
        verify(mockCreationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
        verify(settings).getExtraInterfaces();
        verify(settings).isSerializable();
        verify(settings).getSpiedInstance();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(null);
    }

    @Test
    public void testResetMock_typicalCase() {
        MockHandlerInterface<ClassToMock> oldMockHandler = mock(MockHandlerInterface.class);
        ClassToMock mock = mock(ClassToMock.class);
        Factory factory = mock(Factory.class);

        when(factory.getCallback(0)).thenReturn(null, new MethodInterceptorFilter<>(mock));
        when(factory).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                factory.setCallback(0, (MethodInterceptorFilter<?>) invocation.getArguments()[1]);
                return null;
            }
        });
        when((ClassToMock) ((Object) mock)).thenReturn(mock);

        reset(mock);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        verify(factory, times(1)).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock() {
        MockUtil mockUtil = new MockUtil();

        Object mock = new Object();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(null));

        Object mock = new Object();
        assertFalse(mockUtil.isMock(mock));

        MockHandlerInterface<ClassToMock> oldMockHandler = mock(MockHandlerInterface.class);
        ClassToMock classToMock = mock(ClassToMock.class);
        when((ClassToMock) ((Object) classToMock)).thenReturn(classToMock);

        assertTrue(mockUtil.isMock(classToMock));
    }

    @Test
    public void testGetMockName() {
        MockHandlerInterface<ClassToMock> oldMockHandler = mock(MockHandlerInterface.class);
        ClassToMock mock = mock(ClassToMock.class);
        MockName mockName = mock(MockName.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(oldMockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);

        MockUtil mockUtil = new MockUtil();

        MockName result = mockUtil.getMockName(mock);

        assertSame(result, mockName);
    }

    private interface ClassToMock extends Serializable { }
}