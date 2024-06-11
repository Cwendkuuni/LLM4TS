
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.ArrayUtils;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl();
        
        ClassToMock mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        verify(creationValidator).validateType(ClassToMock.class);
        verify(creationValidator).validateExtraInterfaces(ClassToMock.class, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(ClassToMock.class, settings.getSpiedInstance());
        verify(mockUtil).MethodInterceptorFilter(settings);
        verify(classToMock).setCallback(0, newFilter);
        verify(mockUtil).copyToMock(settings.getSpiedInstance(), mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        
        mockUtil.resetMock(mock);
        
        verify(mockUtil).getMethodInterceptorFilter(settings);
        verify(mock).setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        MockUtil mockUtil = new MockUtil();
        Object obj = new Object();
        
        mockUtil.getMockHandler(obj);
    }

    @Test
    public void testIsMockWithMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
        verify(mockUtil).isMockitoMock(mock);
    }

    @Test
    public void testIsMockWithNonMock() {
        MockUtil mockUtil = new MockUtil();
        Object obj = new Object();
        
        boolean isMock = mockUtil.isMock(obj);
        
        assertFalse(isMock);
        verify(mockUtil).isMockitoMock(obj);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);
        
        MockName result = mockUtil.getMockName(mock);
        
        assertEquals(mockName, result);
        verify(mockHandler).getMockSettings();
        verify(settings).getMockName();
    }
}
