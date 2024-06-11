import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // Typical use case
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);
        MockHandler handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        when(mock.getClass()).thenReturn(TestClass.class);
        when(settings.getTypeToMock().cast(mock)).thenReturn(mock);

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        try {
            byteBuddyMockMaker.createMock(settings, handler);
        } catch (Exception e) {
            fail("An exception occurred");
        }

        // Edge case
        settings = mock(MockCreationSettings.class);
        mock = mock(TestClass.class);
        handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        when(mock.getClass()).thenReturn(TestClass.class);
        when(settings.getTypeToMock().cast(mock)).thenReturn(mock);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }

        // Error scenario
        settings = mock(MockCreationSettings.class);
        mock = mock(TestClass.class);
        handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenThrow(NullPointerException.class);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        when(mock.getClass()).thenReturn(TestClass.class);
        when(settings.getTypeToMock().cast(mock)).thenReturn(mock);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("null", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Typical use case
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);

        when(settings.getTypeToMock()).thenReturn(TestClass.class);

        TestClass mock = mock(TestClass.class);

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        TestClass result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertNotNull(result);
        assertEquals(mock, result);

        // Error scenario
        settings = mock(MockCreationSettings.class);

        when(settings.getTypeToMock()).thenThrow(NullPointerException.class);

        try {
            byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetHandler() {
        // Typical use case
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mock(MockHandler.class);

        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(handler);

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler result = byteBuddyMockMaker.getHandler(mockAccess);

        assertEquals(handler, result);

        // Error scenario
        Object mock = new Object();

        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        // Typical use case
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        byteBuddyMockMaker.resetMock(mockAccess, handler, settings);

        // Error scenario
        Object mock = new Object();

        try {
            byteBuddyMockMaker.resetMock(mock, handler, settings);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Typical use case
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        assertNotNull(result);
        assertEquals(ClassInstantiator.UsingObjenesis.class, result.getClass());

        // Error scenario
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        when(globalConfiguration.enableClassCache()).thenThrow(NullPointerException.class);

        try {
            byteBuddyMockMaker.initializeClassInstantiator();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAsInternalMockHandler() {
        // Typical use case
        InternalMockHandler handler = mock(InternalMockHandler.class);

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(handler);

        assertEquals(handler, result);

        // Error scenario
        MockHandler mockHandler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.asInternalMockHandler(mockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(true);
        }
    }
}