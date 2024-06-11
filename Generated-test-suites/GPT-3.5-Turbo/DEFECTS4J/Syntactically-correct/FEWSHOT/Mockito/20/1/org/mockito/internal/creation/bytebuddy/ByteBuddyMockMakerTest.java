package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.caching.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.invocation.MockHandler;
import org.mockito.internal.invocation.mockref.MockReference;

@SuppressWarnings("unchecked")
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings<Object> mockSettings;
    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        mockSettings = (MockCreationSettings<Object>) mock(MockCreationSettings.class);
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() {
        Object mock = mockMaker.createMock(mockSettings, mockHandler);

        assertNotNull(mock);
        assertSame(mockSettings.getTypeToMock(), mock.getClass());

        verify(mockHandler).setMockitoInterceptor(Mockito.any());
    }

    @Test
    public void testCreateMock_serializationAcrossClassLoadersNotSupported() {
        mockSettings = (MockCreationSettings<Object>) mock(MockCreationSettings.class);
        mockSettings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(mockSettings, mockHandler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        String mock = mockMaker.ensureMockIsAssignableToMockedType(mockSettings, "mock");

        assertNotNull(mock);
        assertSame(mockSettings.getTypeToMock(), mock.getClass());
    }

    @Test
    public void testDescribeClass() {
        String description = ByteBuddyMockMaker.describeClass(String.class);

        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testDescribeClassNull() {
        String description = ByteBuddyMockMaker.describeClass(null);

        assertEquals("null", description);
    }

    @Test
    public void testDescribeClassFromInstance() {
        String description = ByteBuddyMockMaker.describeClass("instance");

        assertEquals("'java.lang.String', loaded by classloader : 'java.net.URLClassLoader@2a139a55'", description);
    }

    @Test(expected = MockitoException.class)
    public void testGetHandlerInvalidMock() {
        Object mock = "mock";
        mockMaker.getHandler(mock);
    }

    @Test
    public void testGetHandler() {
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(mockHandler, mockSettings));
        Object mock = mockAccess;

        MockHandler handler = mockMaker.getHandler(mock);

        assertNotNull(handler);
        assertSame(mockHandler, handler);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(mockHandler, mockSettings));
        Object mock = mockAccess;

        mockMaker.resetMock(mock, mockHandler, mockSettings);

        verify(mockAccess).setMockitoInterceptor(Mockito.any());
    }

    @Test(expected = InstantiationException.class)
    public void testInitializeClassInstantiatorMissingObjenesis() {
        ClassInstantiator classInstantiator = ByteBuddyMockMaker.initializeClassInstantiator();
        classInstantiator.instantiate(Object.class);
    }

}