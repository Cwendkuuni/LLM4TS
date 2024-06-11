import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {
    
    private ByteBuddyMockMaker byteBuddyMockMaker;
    
    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ThrowsExceptionWhenSerializableModeAcrossClassloaders() {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        // Act
        byteBuddyMockMaker.createMock(settings, handler);
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testCreateMock_CreatesMockInstanceAndSetMockitoInterceptor() throws Exception {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(MyClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{MyInterface.class});
        
        // Act
        MyClass mockInstance = byteBuddyMockMaker.createMock(settings, handler);
        
        // Assert
        Assert.assertNotNull(mockInstance);
        Assert.assertTrue(mockInstance instanceof MyInterface);
        verify(mockInstance, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ThrowsExceptionWhenClassCastException() {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(InvalidClass.class);
        
        // Act
        byteBuddyMockMaker.createMock(settings, handler);
        
        // Assert
        // Exception is thrown
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ThrowsExceptionWhenInstantiationException() {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(AbstractClass.class);
        
        // Act
        byteBuddyMockMaker.createMock(settings, handler);
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType_ReturnsSameInstanceWhenAssignable() {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MyClass mockInstance = mock(MyClass.class);
        
        // Act
        MyClass result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);
        
        // Assert
        Assert.assertSame(mockInstance, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_ThrowsExceptionWhenNotAssignable() {
        // Arrange
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        InvalidClass mockInstance = mock(InvalidClass.class);
        
        // Act
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testDescribeClass_ReturnsClassNameWithClassLoader() {
        // Arrange
        Class<?> type = MyClass.class;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(type);
        
        // Assert
        Assert.assertEquals("'org.mockito.internal.creation.bytebuddy.MyClass', loaded by classloader : 'org.mockito.internal.creation.bytebuddy.MyClass.class.getClassLoader()'", result);
    }
    
    @Test
    public void testDescribeClass_ReturnsNullWhenTypeIsNull() {
        // Arrange
        Class<?> type = null;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(type);
        
        // Assert
        Assert.assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_ReturnsClassNameWithClassLoaderFromInstance() {
        // Arrange
        MyClass instance = new MyClass();
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);
        
        // Assert
        Assert.assertEquals("'org.mockito.internal.creation.bytebuddy.MyClass', loaded by classloader : 'org.mockito.internal.creation.bytebuddy.MyClass.class.getClassLoader()'", result);
    }
    
    @Test
    public void testDescribeClass_ReturnsNullWhenInstanceIsNull() {
        // Arrange
        MyClass instance = null;
        
        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);
        
        // Assert
        Assert.assertEquals("null", result);
    }
    
    @Test
    public void testGetHandler_ReturnsMockitoInterceptorMockHandlerWhenMockInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler expectedHandler = mock(MockHandler.class);
        when(mock.getMockitoInterceptor().getMockHandler()).thenReturn(expectedHandler);
        
        // Act
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        // Assert
        Assert.assertSame(expectedHandler, result);
    }
    
    @Test
    public void testGetHandler_ReturnsNullWhenMockNotInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        Object mock = new Object();
        
        // Act
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testResetMock_SetsMockitoInterceptorWithNewHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        
        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        
        // Assert
        verify(mock, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMock_ThrowsExceptionWhenNewHandlerNotInstanceOfInternalMockHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        
        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testInitializeClassInstantiator_ReturnsUsingObjenesisClassInstantiator() {
        // Arrange
        
        // Act
        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ThrowsExceptionWhenObjenesisIsMissingFromClasspath() {
        // Arrange
        GlobalConfiguration globalConfigurationMock = mock(GlobalConfiguration.class);
        whenNew(GlobalConfiguration.class).withNoArguments().thenReturn(globalConfigurationMock);
        doThrow(new ClassNotFoundException()).when(Class.class).forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        
        // Act
        ByteBuddyMockMaker.initializeClassInstantiator();
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testAsInternalMockHandler_ReturnsInternalMockHandlerWhenHandlerInstanceOfInternalMockHandler() {
        // Arrange
        InternalMockHandler handler = mock(InternalMockHandler.class);
        
        // Act
        InternalMockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);
        
        // Assert
        Assert.assertSame(handler, result);
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_ThrowsExceptionWhenHandlerNotInstanceOfInternalMockHandler() {
        // Arrange
        MockHandler handler = mock(MockHandler.class);
        
        // Act
        ByteBuddyMockMaker.asInternalMockHandler(handler);
        
        // Assert
        // Exception is thrown
    }
    
    private interface MyInterface {
        void foo();
    }
    
    private class MyClass implements MyInterface {
        public void foo() { }
    }
    
    private class InvalidClass { }
    
    private abstract class AbstractClass { }
}