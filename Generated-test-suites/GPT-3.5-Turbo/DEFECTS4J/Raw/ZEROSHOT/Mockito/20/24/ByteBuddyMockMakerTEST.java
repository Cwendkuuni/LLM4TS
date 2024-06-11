import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.instantiate;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassloaders() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock() throws Exception {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class typeToMock = Object.class;
        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[0]);

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        assertEquals(typeToMock, mock.getClass());
        verify(settings, atLeastOnce()).getSerializableMode();
        verify(settings, times(1)).getTypeToMock();
        verify(settings, times(1)).getExtraInterfaces();
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Class typeToMock = Object.class;
        Object mock = mock(typeToMock);

        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertEquals(typeToMock, result.getClass());
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_DifferentType() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        Class<String> typeToMock = String.class;
        Object mock = mock(Object.class);

        try {
            byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            assertEquals("java.lang.Object cannot be cast to java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testDescribeClass_TypeNull() {
        String result = byteBuddyMockMaker.describeClass(null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_TypeNotNull() {
        Class type = Object.class;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_InstanceNull() {
        String result = byteBuddyMockMaker.describeClass(null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_InstanceNotNull() {
        Object instance = new Object();

        String result = byteBuddyMockMaker.describeClass(instance);

        assertNotNull(result);
    }

    @Test
    public void testGetHandler_NotMockMethodInterceptor_MockHandlerNull() {
        Object mock = new Object();

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandler_NotMockMethodInterceptor() {
        Object mock = new Object();

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandler_MockMethodInterceptor() {
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        when(mockitoInterceptor.getMockHandler()).thenReturn(mockHandler);
        Object mock = mockAccess;

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertEquals(mockHandler, result);
        verify(mockAccess, times(1)).getMockitoInterceptor();
        verify(mockitoInterceptor, times(1)).getMockHandler();
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor newMockitoInterceptor = mock(MockMethodInterceptor.class);
        MockMethodInterceptor oldMockitoInterceptor = mock(MockMethodInterceptor.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(oldMockitoInterceptor);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        Object mock = mockAccess;

        byteBuddyMockMaker.resetMock(mock, newMockHandler, settings);

        verify(mockAccess, times(1)).setMockitoInterceptor(newMockitoInterceptor);
        verify(newMockitoInterceptor, times(1)).setMockHandler(newMockHandler);
        verify(mockAccess, times(1)).getMockitoInterceptor();
        verify(settings, times(1)).getSerializableMode();
    }

    @Test
    public void testInitializeClassInstantiator() throws Exception {
        ByteBuddyMockMaker spy = spy(byteBuddyMockMaker);
        doReturn(ClassInstantiator.class).when(spy).initializeClassInstantiator();
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        Constructor constructor = mock(Constructor.class);
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(constructor);
        when(constructor.getDeclaredConstructor(boolean.class)).thenReturn(constructor);
        when(constructor.newInstance(anyBoolean())).thenReturn(globalConfiguration);
        when(globalConfiguration.enableClassCache()).thenReturn(true);

        ClassInstantiator result = spy.initializeClassInstantiator();

        assertNotNull(result);
        verify(constructor, times(1)).newInstance(anyBoolean());
        verify(globalConfiguration, times(1)).enableClassCache();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Exception() throws Exception {
        ByteBuddyMockMaker spy = spy(byteBuddyMockMaker);
        doReturn(ClassInstantiator.class).when(spy).initializeClassInstantiator();
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        Constructor constructor = mock(Constructor.class);
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(constructor);
        when(constructor.getDeclaredConstructor(boolean.class)).thenThrow(new ClassNotFoundException());
        when(globalConfiguration.enableClassCache()).thenReturn(true);

        ClassInstantiator result = spy.initializeClassInstantiator();

        assertNotNull(result);
        verify(constructor, never()).newInstance(anyBoolean());
        verify(globalConfiguration, never()).enableClassCache();
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NotInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);

        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }

    @Test
    public void testAsInternalMockHandler() {
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);

        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(internalMockHandler);

        assertEquals(internalMockHandler, result);
    }
}