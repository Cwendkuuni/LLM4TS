
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.junit.Before;
import org.junit.Test;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings mockSettings;
    private MockHandler mockHandler;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        mockSettings = mock(MockCreationSettings.class);
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock_SerializableModeAcrossClassloadersThrowsException() {
        when(mockSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(mockSettings, mockHandler);
            fail("MockitoException was not thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ClassCastException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(classInstantiator.instantiate(any())).thenThrow(new ClassCastException());

        try {
            mockMaker.createMock(mockSettings, mockHandler);
            fail("MockitoException was not thrown");
        } catch (MockitoException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :\n  class to mock : 'java.lang.Object', loaded by classloader : 'null'\n  created class : 'null', loaded by classloader : 'null'\n  proxy instance class : 'null', loaded by classloader : 'null'\n  instance creation by : ClassInstantiator$UsingObjenesis\n\nYou might experience classloading issues, please ask the mockito mailing-list.\n", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(classInstantiator.instantiate(any())).thenThrow(new org.mockito.internal.creation.instance.InstantiationException());

        try {
            mockMaker.createMock(mockSettings, mockHandler);
            fail("MockitoException was not thrown");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'Object'", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Object mock = mock(Object.class);
        Class<Object> mockedType = Object.class;
        mockSettings = mock(MockCreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mockedType);

        Object result = mockMaker.ensureMockIsAssignableToMockedType(mockSettings, mock);

        assertSame(result, mock);
        verify(mockSettings).getTypeToMock();
    }

    @Test
    public void testDescribeClass_TypeIsNull() {
        String result = ByteBuddyMockMaker.describeClass(null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_TypeIsNotNull() {
        Class<String> type = String.class;
        String result = ByteBuddyMockMaker.describeClass(type);

        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNull() {
        Object instance = null;
        String result = ByteBuddyMockMaker.describeClass(instance);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNotNull() {
        Object instance = new Object();
        String result = ByteBuddyMockMaker.describeClass(instance);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandler_MockIsNotInstanceOfMockAccess() {
        Object mock = new Object();
        MockHandler result = mockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandler_MockIsInstanceOfMockAccess() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(mockHandler);

        MockHandler result = mockMaker.getHandler(mock);

        assertSame(mockHandler, result);
        verify(mockAccess, times(2)).getMockitoInterceptor();
    }

    @Test
    public void testResetMock() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));

        mockMaker.resetMock(mock, mockHandler, mockSettings);

        verify(mockAccess).setMockitoInterceptor(any());
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_MockHandlerIsNotInternalMockHandler() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);

        mockMaker.resetMock(mock, mock(MockHandler.class), mockSettings);

        fail("MockitoException was not thrown");
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisClassLoaderFound() throws Throwable {
        GlobalConfiguration configuration = mock(GlobalConfiguration.class);
        ByteBuddyMockMaker mockMakerSpy = spy(mockMaker);
        doReturn(configuration).when(mockMakerSpy).createGlobalConfiguration();
        when(configuration.enableClassCache()).thenReturn(true);
        Class<?> objenesisClassLoader = byteBuddyMockMaker.getClass().getClassLoader().loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);

        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator(mockMakerSpy);

        assertNotNull(result);
        verify(mockMakerSpy).createGlobalConfiguration();
        verify(configuration).enableClassCache();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisClassLoaderNotFound() throws Throwable {
        GlobalConfiguration configuration = mock(GlobalConfiguration.class);
        ByteBuddyMockMaker mockMakerSpy = spy(mockMaker);
        doReturn(configuration).when(mockMakerSpy).createGlobalConfiguration();
        when(configuration.enableClassCache()).thenReturn(true);
        Class<?> objenesisClassLoader = byteBuddyMockMaker.getClass().getClassLoader().loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);

        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator(mockMakerSpy);

        fail("IllegalStateException was not thrown");
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_HandlerIsNotInternalMockHandler() {
        mockMaker.asInternalMockHandler(mock(MockHandler.class));

        fail("MockitoException was not thrown");
    }

    @Test
    public void testAsInternalMockHandler_HandlerIsInternalMockHandler() {
        InternalMockHandler mockInternalHandler = mock(InternalMockHandler.class);

        InternalMockHandler result = mockMaker.asInternalMockHandler(mockInternalHandler);

        assertSame(mockInternalHandler, result);
    }
}
