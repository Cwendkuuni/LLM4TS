import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private CreationValidator creationValidator;

    @Before
    public void setup() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }
    
    @Test
    public void testCreateMock_typicalCase() {
        // Arrange
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(ClassImposterizer.INSTANCE.isProxyClass(mock.getClass()));
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidType() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);
    }
    
    @Test
    public void testCreateMock_extraInterfaces() {
        // Arrange
        Class<?> classToMock = MyClass.class;
        Class<?>[] extraInterfaces = {MyInterface.class};
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(ClassImposterizer.INSTANCE.isProxyClass(mock.getClass()));
        verify(creationValidator).validateExtraInterfaces(classToMock, extraInterfaces);
    }
    
    @Test
    public void testCreateMock_spiedInstance() {
        // Arrange
        Class<?> classToMock = MyClass.class;
        Object spiedInstance = new MyClass();
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(ClassImposterizer.INSTANCE.isProxyClass(mock.getClass()));
        verify(creationValidator).validateMockedType(classToMock, spiedInstance);
    }
    
    @Test
    public void testResetMock() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> oldMockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        
        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(oldMockHandler).getMockSettings();
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Act
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMock() {
        // Arrange
        Object nonMock = new Object();
        
        // Act
        mockUtil.getMockHandler(nonMock);
    }
    
    @Test
    public void testGetMockHandler_mockitoMock() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(interceptor);
        when(interceptor.getHandler()).thenReturn(mockHandler);
        
        // Act
        MockHandlerInterface<MyClass> result = mockUtil.getMockHandler(mock);
        
        // Assert
        assertSame(mockHandler, result);
    }
    
    @Test
    public void testIsMock_true() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        
        // Act
        boolean result = mockUtil.isMock(mock);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsMock_false() {
        // Arrange
        Object nonMock = new Object();
        when(mockUtil.isMockitoMock(nonMock)).thenReturn(false);
        
        // Act
        boolean result = mockUtil.isMock(nonMock);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGetInterceptor_returnInterceptor() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        Factory factory = mock(Factory.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        when(interceptor.getHandler()).thenReturn(mock(MockHandlerInterface.class));
        when(factory.getCallback(0)).thenReturn(interceptor);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when((Factory) mock).thenReturn(factory);
        
        // Act
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        // Assert
        assertSame(interceptor, result);
    }
    
    @Test
    public void testGetInterceptor_returnNull() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        Factory factory = mock(Factory.class);
        Callback callback = mock(Callback.class);
        when(factory.getCallback(0)).thenReturn(callback);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when((Factory) mock).thenReturn(factory);
        
        // Act
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetMockName() {
        // Arrange
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);
        
        // Act
        MockName result = mockUtil.getMockName(mock);
        
        // Assert
        assertSame(mockName, result);
    }
}

class MyClass implements MyInterface {
    
}

interface MyInterface {
    
}