import org.junit.Before;
import org.junit.Test;
import org.mockito.MockCreationSettings;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMockWithSerializableMode() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithValidSettings() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class typeToMock = String.class;
        Class<?> mockedProxyType = typeToMock;

        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);

        Object mockInstance = byteBuddyMockMaker.createMock(settings, handler);

        assertTrue(mockInstance instanceof String);
        assertSame(handler, byteBuddyMockMaker.getHandler(mockInstance));
        assertTrue(byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance) instanceof String);
    }

    @Test
    public void testCreateMockWithInvalidSettings() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class typeToMock = Integer.class;
        
        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().startsWith("ClassCastException occurred while creating the mockito mock :"));
        }
    }

    @Test
    public void testDescribeClassWithNull() {
        String description = ByteBuddyMockMaker.describeClass(null);
        assertEquals("null", description);
    }

    @Test
    public void testDescribeClassWithValidClass() {
        String description = ByteBuddyMockMaker.describeClass(String.class);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testDescribeClassWithValidInstance() {
        String description = ByteBuddyMockMaker.describeClass("test");
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testGetHandlerWithValidMock() {
        MockHandler handler = mock(MockHandler.class);
        Object mockInstance = mock(Object.class);
        when(mockInstance instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(handler);
        when(mockInstance).thenReturn(mockAccess);

        assertSame(handler, byteBuddyMockMaker.getHandler(mockInstance));
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        Object mockInstance = mock(Object.class);
        when(mockInstance instanceof MockMethodInterceptor.MockAccess).thenReturn(false);

        assertNull(byteBuddyMockMaker.getHandler(mockInstance));
    }

    @Test
    public void testResetMock() {
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mockInstance = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MethodInterceptor.class));
        when(mockInstance).thenReturn(mockAccess);

        byteBuddyMockMaker.resetMock(mockInstance, newHandler, settings);

        verify(mockAccess).setMockitoInterceptor(argThat(interceptor -> interceptor.getMockHandler() == newHandler));
    }
}