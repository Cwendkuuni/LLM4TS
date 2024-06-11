package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.util.StringJoiner;

import static org.mockito.internal.util.StringJoiner.join;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {
    
    private ByteBuddyMockMaker mockMaker;
    private GlobalConfiguration globalConfig;
    
    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        globalConfig = new GlobalConfiguration();
    }
    
    @Test
    public void testCreateMock_SerializableModeACROSS_CLASSLOADERS_ThrowsMockitoException() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        settings.serializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        
        Throwable exception = assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
        
        assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", exception.getMessage());
    }
    
    @Test
    public void testCreateMock_SuccessfullyCreatesMockInstance() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        
        String mockInstance = mockMaker.createMock(settings, handler);
        
        assertNotNull(mockInstance);
    }
    
    @Test
    public void testCreateMock_ClassCastExceptionThrown() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<? extends String> mockedProxyType = mock(Class.class);
        
        Throwable exception = assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
        
        assertEquals(StringJoiner.join(
            "ClassCastException occurred while creating the mockito mock :",
            "  class to mock : " + describeClass(mockedProxyType),
            "  created class : " + describeClass(settings.getTypeToMock()),
            "  proxy instance class : null",
            "  instance creation by : ClassInstantiator",
            "",
            "You might experience classloading issues, please ask the mockito mailing-list.",
            ""
        ), exception.getMessage());
    }
    
    @Test
    public void testCreateMock_InstantiationExceptionThrown() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<? extends String> mockedProxyType = mock(Class.class);
        
        Throwable exception = assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
        
        assertEquals("Unable to create mock instance of type 'null'", exception.getMessage());
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType_ReturnsMockInstance() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = "mock";
        
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertEquals(mock, result);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType_ReturnsMockInstanceWithCast() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = "mock";
        
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertEquals(mock, result);
    }
    
    @Test
    public void testDescribeClass_ReturnsClassDescription() {
        Class<?> type = getClass();
        
        String result = mockMaker.describeClass(type);
        
        assertEquals("org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest", result);
    }
    
    @Test
    public void testDescribeClass_ReturnsNull() {
        Class<?> type = null;
        
        String result = mockMaker.describeClass(type);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_Object_ReturnsClassDescription() {
        Object instance = getClass();
        
        String result = mockMaker.describeClass(instance);
        
        assertEquals("org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest", result);
    }
    
    @Test
    public void testDescribeClass_Object_ReturnsNull() {
        Object instance = null;
        
        String result = mockMaker.describeClass(instance);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testGetHandler_ReturnsMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        Object mock = mock(Object.class, CALLS_REAL_METHODS);
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(handler), settings)
        );
        
        MockHandler result = mockMaker.getHandler(mock);
        
        assertEquals(handler, result);
    }
    
    @Test
    public void testGetHandler_ReturnsNull() {
        Object mock = mock(Object.class, CALLS_REAL_METHODS);
        
        MockHandler result = mockMaker.getHandler(mock);
        
        assertNull(result);
    }
    
    @Test
    public void testResetMock_SetsNewHandler() {
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class, CALLS_REAL_METHODS);
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(newHandler), settings)
        );
        
        mockMaker.resetMock(mock, newHandler, settings);
        
        verify(mock, times(1)).setMockitoInterceptor(MockMethodInterceptor.asInternalMockHandler(newHandler));
    }
    
    private String describeClass(Class type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }
    
    private String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }
    
    private InternalMockHandler asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please see the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler) handler;
    }

}