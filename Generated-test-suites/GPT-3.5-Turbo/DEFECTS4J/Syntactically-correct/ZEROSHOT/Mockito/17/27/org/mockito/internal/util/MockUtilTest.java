import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

@SuppressWarnings("unchecked")
public class MockUtilTest {
    
    private MockUtil mockUtil;
    private CreationValidator creationValidator;
    
    @Before
    public void setup() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }
    
    @Test
    public void testCreateMock() {
        // Setup
        Class classToMock = MyClass.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.getSpiedInstance()).thenReturn(null);
        
        // Test
        Object result = mockUtil.createMock(classToMock, settings);
        
        // Verify
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, null);
        verify(creationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
    }
    
    @Test
    public void testCreateMockWithExtraInterfaces() {
        // Setup
        Class classToMock = MyClass.class;
        Class[] extraInterfaces = {MyInterface.class};
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        when(settings.getSpiedInstance()).thenReturn(null);
        
        // Test
        Object result = mockUtil.createMock(classToMock, settings);
        
        // Verify
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
        assertTrue(result instanceof MyInterface);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, extraInterfaces);
        verify(creationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
    }
    
    @Test
    public void testCreateMockWithSpiedInstance() {
        // Setup
        Class classToMock = MyClass.class;
        MyClass spiedInstance = mock(MyClass.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        
        // Test
        Object result = mockUtil.createMock(classToMock, settings);
        
        // Verify
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, null);
        verify(creationValidator).validateMockedType(classToMock, spiedInstance);
        verify(settings).initiateMockName(classToMock);
        verify(spiedInstance).getMock();
    }
    
    @Test
    public void testResetMock() {
        // Setup
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);
        when(mock.getCallback(0)).thenReturn(newFilter);
        doNothing().when((Factory) mock).setCallback(0, newFilter);
        
        // Test
        mockUtil.resetMock(mock);
        
        // Verify
        verify(mock).getCallback(0);
        verify((Factory) mock).setCallback(0, newFilter);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullArgument() {
        // Test
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        // Setup
        Object mock = new Object();
        
        // Test
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        // Setup
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        when(mock.getCallback(0)).thenReturn(interceptor);
        when(interceptor.getHandler()).thenReturn(mockHandler);
        
        // Test
        MockHandlerInterface result = mockUtil.getMockHandler(mock);
        
        // Verify
        assertNotNull(result);
        assertEquals(mockHandler, result);
        verify(mock).getCallback(0);
        verify(interceptor).getHandler();
    }
    
    @Test
    public void testIsMock() {
        // Setup
        MyClass mock = mock(MyClass.class);
        
        // Test
        boolean result = mockUtil.isMock(mock);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testGetMockName() {
        // Setup
        MyClass mock = mock(MyClass.class);
        MockName mockName = mock(MockName.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        
        // Test
        MockName result = mockUtil.getMockName(mock);
        
        // Verify
        assertNotNull(result);
        assertEquals(mockName, result);
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }
    
    private interface MyInterface {}
    
    private class MyClass {}
}