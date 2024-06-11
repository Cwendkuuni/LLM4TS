import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;

import java.io.Serializable;

import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
        verify(mockUtil.creationValidator).validateType(classToMock);
        verify(mockUtil.creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(mockUtil.creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        verify(mockUtil).newMethodInterceptorFilter(settings);
        verify(settings).getExtraInterfaces();
        verify(settings).isSerializable();
        verify(settings).getSpiedInstance();
        verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptorFilter.class), eq(classToMock), any(Class[].class));
        verify(new LenientCopyTool()).copyToMock(settings.getSpiedInstance(), mock);
    }
    
    @Test
    public void testResetMock() {
        String mock = Mockito.mock(String.class);
        
        mockUtil.resetMock(mock);
        
        MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<String> newMockHandler = new MockHandler<String>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
        ((Factory) mock).setCallback(0, newFilter);
        
        verify(mockUtil).getMockHandler(mock);
        verify(org.mockito.Mockito).withSettings();
        verify(org.mockito.Mockito).RETURNS_DEFAULTS;
    }
    
    @Test
    public void testGetMockHandler() {
        String mock = Mockito.mock(String.class);
        
        mockUtil.getMockHandler(mock);
        
        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil, times(2)).getMockHandler(mock);
        verify(mockUtil.getInterceptor(mock)).getHandler();
    }
    
    @Test
    public void testIsMock() {
        String mock = Mockito.mock(String.class);
        boolean expectedResult = true;
        
        boolean result = mockUtil.isMock(mock);
        
        assertEquals(expectedResult, result);
        verify(mockUtil).isMockitoMock(mock);
    }
    
    @Test
    public void testGetInterceptor() {
        String mock = Mockito.mock(String.class);
        MethodInterceptorFilter interceptor = new MethodInterceptorFilter();
        ((Factory) mock).setCallback(0, interceptor);
        
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        assertEquals(interceptor, result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetInterceptor_NotAMockException() {
        String notMock = "Not a mock";
        
        mockUtil.getInterceptor(notMock);
    }
    
    @Test
    public void testGetMockName() {
        String mock = Mockito.mock(String.class);
        MockHandlerInterface<String> mockHandler = Mockito.mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        MockName mockName = Mockito.mock(MockName.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        
        MockName result = mockUtil.getMockName(mock);
        
        assertEquals(mockName, result);
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_NotAMockException() {
        String notMock = "Not a mock";
        
        mockUtil.getMockName(notMock);
    }
    
}