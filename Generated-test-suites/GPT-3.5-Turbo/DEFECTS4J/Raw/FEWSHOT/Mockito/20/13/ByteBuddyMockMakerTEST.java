package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;

public class ByteBuddyMockMakerTest {
    private ByteBuddyMockMaker mockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        mockMaker.classInstantiator = classInstantiator;
        mockMaker.cachingMockBytecodeGenerator = cachingMockBytecodeGenerator;
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ThrowsExceptionWhenSerializableModeAcrossClassLoaders() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        settings.serializableMode = SerializableMode.ACROSS_CLASSLOADERS;

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_InvokesCachingMockBytecodeGeneratorAndGet() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Class<?>[] extraInterfaces = new Class<?>[0];
        settings.typeToMock = mockedProxyType;
        settings.extraInterfaces = extraInterfaces;

        mockMaker.createMock(settings, handler);

        verify(cachingMockBytecodeGenerator).get(mockedProxyType, extraInterfaces);
    }

    @Test
    public void testCreateMock_InvokesClassInstantiatorInstantiate() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Class<?>[] extraInterfaces = new Class<?>[0];
        settings.typeToMock = mockedProxyType;
        settings.extraInterfaces = extraInterfaces;
        when(cachingMockBytecodeGenerator.get(mockedProxyType, extraInterfaces)).thenReturn(mockedProxyType);

        mockMaker.createMock(settings, handler);

        verify(classInstantiator).instantiate(mockedProxyType);
    }

    @Test
    public void testCreateMock_InvokesMockAccessSetMockitoInterceptor() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Class<?>[] extraInterfaces = new Class<?>[0];
        Object mockInstance = mock(Object.class);
        settings.typeToMock = mockedProxyType;
        settings.extraInterfaces = extraInterfaces;
        when(cachingMockBytecodeGenerator.get(mockedProxyType, extraInterfaces)).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);

        mockMaker.createMock(settings, handler);

        verify(mockInstance).setMockitoInterceptor(any());
    }

    @Test
    public void testCreateMock_ReturnsMockInstance() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Class<?>[] extraInterfaces = new Class<?>[0];
        Object mockInstance = mock(Object.class);
        settings.typeToMock = mockedProxyType;
        settings.extraInterfaces = extraInterfaces;
        when(cachingMockBytecodeGenerator.get(mockedProxyType, extraInterfaces)).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);

        Object result = mockMaker.createMock(settings, handler);

        assertEquals(mockInstance, result);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_ReturnsMock() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mock = mock(Object.class);
        settings.typeToMock = mockedProxyType;

        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertEquals(mock, result);
    }

    @Test(expected = MockitoException.class)
    public void testEnsureMockIsAssignableToMockedType_ThrowsExceptionWhenClassCastException() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Class<?> mockedProxyType = mock(Class.class);
        String mockInstance = "mockInstance";
        settings.typeToMock = mockedProxyType;

        mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);
    }

    @Test
    public void testDescribeClass_ReturnsClassDescription() {
        Class<?> clazz = mock(Class.class);

        String result = mockMaker.describeClass(clazz);

        assertEquals("'null', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_ReturnsNullWhenTypeIsNull() {
        Class<?> type = null;

        String result = mockMaker.describeClass(type);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_ReturnsInstanceClassDescription() {
        Object instance = mock(Object.class);

        String result = mockMaker.describeClass(instance);

        assertEquals("'java.lang.Object', loaded by classloader : 'java.lang.ClassLoader'", result);
    }

    @Test
    public void testGetHandler_ReturnsMockitoInterceptorMockHandler() {
        Object mock = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        InternalMockHandler handler = mock(InternalMockHandler.class);
        mockAccess.mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(handler);
        when(mockAccess instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(mock).thenReturn(mockAccess);

        MockHandler result = mockMaker.getHandler(mock);

        assertEquals(handler, result);
    }

    @Test
    public void testGetHandler_ReturnsNullWhenNotMockitoInterceptorMockAccess() {
        Object mock = mock(Object.class);

        MockHandler result = mockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testResetMock_InvokesMockAccessSetMockitoInterceptor() {
        Object mock = mock(Object.class);
        InternalMockHandler newHandler = mock(InternalMockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        mockAccess.mockitoInterceptor = mockitoInterceptor;
        settings.setTypeToMock(mock(Class.class));
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        when(mockAccess instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(mock).thenReturn(mockAccess);

        mockMaker.resetMock(mock, newHandler, settings);

        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
}