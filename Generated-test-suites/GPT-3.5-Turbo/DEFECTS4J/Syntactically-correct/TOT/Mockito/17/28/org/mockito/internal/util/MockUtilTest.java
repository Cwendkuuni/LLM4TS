import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.MockSettingsImpl;
import org.mockito.internal.util.creation.MethodInterceptorFilter;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    // Test createMock method
    
    @Test
    public void testCreateMock_typicalUseCase() {
        // Given
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(Arrays.asList(Runnable.class, Comparable.class).toArray(new Class<?>[0]));
        
        // When
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertTrue(Runnable.class.isInstance(mock));
        assertTrue(Comparable.class.isInstance(mock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidType() {
        // Given
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // When
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        // Exception should be thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidExtraInterfaces() {
        // Given
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(Arrays.asList(Runnable.class, Integer.class).toArray(new Class<?>[0]));
        
        // When
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        // Exception should be thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidSpiedInstance() {
        // Given
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(10);
        
        // When
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        // Exception should be thrown
    }
    
    // Test resetMock method
    
    @Test
    public void testResetMock_typicalUseCase() {
        // Given
        Runnable mock = Mockito.mock(Runnable.class);
        MockHandlerInterface<Runnable> mockHandler = mockUtil.getMockHandler(mock);
        Mockito.when(mockHandler.getHandler()).thenReturn(new MockHandler<Runnable>(mockHandler));
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        verify(mock, atLeastOnce()).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_nullMock() {
        // Given
        Object mock = null;
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        // Exception should be thrown
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_nonMockObject() {
        // Given
        Object mock = "not a mock";
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        // Exception should be thrown
    }
    
    // Test getMockHandler method
    
    @Test
    public void testGetMockHandler_typicalUseCase() {
        // Given
        Runnable mock = Mockito.mock(Runnable.class);
        
        // When
        MockHandlerInterface<Runnable> mockHandler = mockUtil.getMockHandler(mock);
        
        // Then
        assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Given
        Object mock = null;
        
        // When
        mockUtil.getMockHandler(mock);
        
        // Then
        // Exception should be thrown
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockObject() {
        // Given
        Object mock = "not a mock";
        
        // When
        mockUtil.getMockHandler(mock);
        
        // Then
        // Exception should be thrown
    }
    
    // Test isMock method
    
    @Test
    public void testIsMock_typicalUseCase() {
        // Given
        Runnable mock = Mockito.mock(Runnable.class);
        
        // When
        boolean result = mockUtil.isMock(mock);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void testIsMock_nonMockObject() {
        // Given
        Object mock = "not a mock";
        
        // When
        boolean result = mockUtil.isMock(mock);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void testIsMock_nullObject() {
        // Given
        Object mock = null;
        
        // When
        boolean result = mockUtil.isMock(mock);
        
        // Then
        assertFalse(result);
    }
    
    // Test getMockName method
    
    @Test
    public void testGetMockName_typicalUseCase() {
        // Given
        Runnable mock = Mockito.mock(Runnable.class, "testMock");
        
        // When
        MockName mockName = mockUtil.getMockName(mock);
        
        // Then
        assertNotNull(mockName);
        assertEquals("testMock", mockName.toString());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullMock() {
        // Given
        Object mock = null;
        
        // When
        mockUtil.getMockName(mock);
        
        // Then
        // Exception should be thrown
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_nonMockObject() {
        // Given
        Object mock = "not a mock";
        
        // When
        mockUtil.getMockName(mock);
        
        // Then
        // Exception should be thrown
    }
    
    @Test
    public void testGetInterceptor() {
        // Given
        Factory factory = Mockito.mock(Factory.class);
        Callback callback = Mockito.mock(MethodInterceptorFilter.class);
        when(factory.getCallback(0)).thenReturn(callback);
        
        // When
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(factory);
        
        // Then
        assertNotNull(interceptor);
        assertEquals(callback, interceptor);
    }
    
    @Test
    public void testGetInterceptor_noInterceptor() {
        // Given
        Factory factory = Mockito.mock(Factory.class);
        Callback callback = Mockito.mock(Callback.class);
        when(factory.getCallback(0)).thenReturn(callback);
        
        // When
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(factory);
        
        // Then
        assertNull(interceptor);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetInterceptor_nullFactory() {
        // Given
        Factory factory = null;
        
        // When
        mockUtil.getInterceptor(factory);
        
        // Then
        // Exception should be thrown
    }
    
}