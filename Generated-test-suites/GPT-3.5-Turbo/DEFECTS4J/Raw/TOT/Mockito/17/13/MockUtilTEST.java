import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_typical() {
        CreationValidator validator = mock(CreationValidator.class);
        when(validator.validateType(any(Class.class))).thenReturn(true);
        when(validator.validateExtraInterfaces(any(Class.class), any(Class[].class))).thenReturn(true);
        when(validator.validateMockedType(any(Class.class), any(Object.class))).thenReturn(true);
        
        MockUtil util = new MockUtil(validator);
        
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(settings.getSpiedInstance()).thenReturn(null);
        
        Object mock = util.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
    }
    
    @Test(expected = Exception.class)
    public void testCreateMock_edge() {
        CreationValidator validator = mock(CreationValidator.class);
        when(validator.validateType(any(Class.class))).thenThrow(new Exception());
        
        MockUtil util = new MockUtil(validator);
        
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        util.createMock(classToMock, settings);
    }
    
    @Test(expected = Exception.class)
    public void testCreateMock_error() {
        CreationValidator validator = mock(CreationValidator.class);
        when(validator.validateType(any(Class.class))).thenReturn(true);
        when(validator.validateExtraInterfaces(any(Class.class), any(Class[].class))).thenReturn(false);
        
        MockUtil util = new MockUtil(validator);
        
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        
        util.createMock(classToMock, settings);
    }
    
    // Edge case and typical cases not applicable to the other methods
    
    @Test
    public void testResetMock_typical() {
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockHandler mockHandler2 = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockHandler2.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        
        MockUtil util = new MockUtil();
        
        Object mock = mock(Object.class, withSettings().defaultAnswer(RETURNS_DEFAULTS));
        
        util.resetMock(mock);
        
        verify((Factory) mock).setCallback(0, any(MethodInterceptorFilter.class));
    }
    
    @Test(expected = Exception.class)
    public void testResetMock_error() {
        MockUtil util = new MockUtil();
        
        Object notMock = new Object();
        
        util.resetMock(notMock);
    }
    
    @Test
    public void testGetMockHandler_typical() {
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        Class<?> mockClass = mock(Class.class);
        Object mock = mock(Object.class);
        when(mock.getClass()).thenReturn(mockClass);
        
        MockUtil util = new MockUtil();
        
        MockHandlerInterface result = util.getMockHandler(mock);
        
        assertNotNull(result);
        assertEquals(mockHandler, result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_error1() {
        MockUtil util = new MockUtil();
        
        Object nullMock = null;
        
        util.getMockHandler(nullMock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_error2() {
        MockUtil util = new MockUtil();
        
        Object notMock = new Object();
        
        util.getMockHandler(notMock);
    }
    
    @Test
    public void testIsMock_true() {
        MockUtil util = new MockUtil();
        
        Object mock = mock(Object.class);
        
        boolean result = util.isMock(mock);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsMock_false() {
        MockUtil util = new MockUtil();
        
        Object notMock = new Object();
        
        boolean result = util.isMock(notMock);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetInterceptor_typical() {
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        Factory factory = mock(Factory.class);
        when(factory.getCallback(0)).thenReturn(interceptor);
        
        MockUtil util = new MockUtil();
        
        Object mock = factory;
        
        MethodInterceptorFilter result = util.getInterceptor(mock);
        
        assertNotNull(result);
        assertEquals(interceptor, result);
    }
    
    @Test
    public void testGetInterceptor_null() {
        Factory factory = mock(Factory.class);
        when(factory.getCallback(0)).thenReturn(null);
        
        MockUtil util = new MockUtil();
        
        Object mock = factory;
        
        MethodInterceptorFilter result = util.getInterceptor(mock);
        
        assertNull(result);
    }
    
    @Test
    public void testGetMockName_typical() {
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        
        MockUtil util = new MockUtil();
        
        Object mock = mock(Object.class);
        
        MockName result = util.getMockName(mock);
        
        assertNotNull(result);
        assertEquals(mockName, result);
    }
}