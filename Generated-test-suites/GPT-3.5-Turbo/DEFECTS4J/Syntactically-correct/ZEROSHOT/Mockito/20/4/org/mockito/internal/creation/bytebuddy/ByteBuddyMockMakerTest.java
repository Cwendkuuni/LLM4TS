package org.mockito.internal.creation.bytebuddy;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.StringJoiner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock() throws MockitoException, ClassCastException, org.mockito.internal.creation.instance.InstantiationException {
        // Create mock objects
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        String mockedProxyType = "MockedProxyType";
        String mockInstance = "MockInstance";
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);

        // Stub method calls
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});
        when(classInstantiator.instantiate(String.class)).thenReturn(mockInstance);
        when(mockAccess.getMockitoInterceptor()).thenReturn(handler);

        // Set up dependencies
        byteBuddyMockMaker.classInstantiator = classInstantiator;

        // Call method
        String result = byteBuddyMockMaker.createMock(settings, handler);

        // Verify interactions
        verify(settings, times(1)).getTypeToMock();
        verify(settings, times(1)).getExtraInterfaces();
        verify(classInstantiator, times(1)).instantiate(String.class);
        verify(mockAccess, times(1)).getMockitoInterceptor();

        // Verify result
        assertNotNull(result);
        assertEquals(mockInstance, result);
    }

    @Test(expected = MockitoException.class)
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Integer> settings = mock(MockCreationSettings.class);
        Integer mock = mock(Integer.class);

        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testDescribeClass() {
        Class<Integer> type = Integer.class;
        String expected = "'java.lang.Integer', loaded by classloader : 'java.lang.ClassLoader@71f4ddc3'";

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals(expected, result);
    }

    @Test
    public void testDescribeClassWithNullType() {
        Class type = null;
        String expected = "null";

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals(expected, result);
    }

    @Test
    public void testDescribeClassWithNullInstance() {
        Object instance = null;
        String expected = "null";

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals(expected, result);
    }

    @Test
    public void testGetHandler() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler expected = mock(MockHandler.class);
        when(mock.getMockitoInterceptor()).thenReturn(new MockMethodInterceptor(expected, null));

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertEquals(expected, result);
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        String mock = "Mock";

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockMethodInterceptor intercept = new MockMethodInterceptor(newHandler, settings);
        when(mock.getMockitoInterceptor()).thenReturn(intercept);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        assertEquals(newHandler, intercept.getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithInvalidHandler() {
        MockHandler handler = mock(MockHandler.class);

        byteBuddyMockMaker.asInternalMockHandler(handler);
    }

    @Test
    public void testAsInternalMockHandler() {
        InternalMockHandler expected = mock(InternalMockHandler.class);
        MockHandler handler = expected;

        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(handler);

        assertEquals(expected, result);
    }
}