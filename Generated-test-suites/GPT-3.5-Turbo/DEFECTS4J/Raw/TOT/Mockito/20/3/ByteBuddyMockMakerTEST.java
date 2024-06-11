import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_WithSerializableModeAcrossClassloaders_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = mock(MockHandler.class);

        try {
            // Act
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            // Assert
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_WithValidSettings_CreatesMockInstanceAndSetsMockitoInterceptor() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{ExtraInterface.class});
        TestClass mock = mock(TestClass.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);

        // Act
        TestClass result = mockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
        assertEquals(TestClass.class, result.getClass());
        assertNotNull(result.getMockitoInterceptor());
        assertEquals(handler, result.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testCreateMock_WithClassCastExceptionThrown_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<InvalidTestClass> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(InvalidTestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{ExtraInterface.class});
        MockHandler handler = mock(MockHandler.class);

        try {
            // Act
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            // Assert
            assertTrue(e.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void testCreateMock_WithInstantiationExceptionThrown_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(InvalidTestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{ExtraInterface.class});
        MockHandler handler = mock(MockHandler.class);

        try {
            // Act
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            // Assert
            assertTrue(e.getMessage().contains("Unable to create mock instance of type"));
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_WithValidMock_ReturnsMockOfCorrectType() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);

        // Act
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_WithInvalidMock_ThrowsClassCastException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        InvalidTestClass mock = mock(InvalidTestClass.class);

        // Act
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testDescribeClass_WithNull_ReturnsNullString() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = null;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertNotNull(result);
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_WithValidClass_ReturnsClassDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = TestClass.class;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(TestClass.class.getCanonicalName()));
        assertTrue(result.contains(TestClass.class.getClassLoader().toString()));
    }

    @Test
    public void testDescribeClass_WithValidInstance_ReturnsInstanceClassDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass instance = new TestClass();

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(TestClass.class.getCanonicalName()));
        assertTrue(result.contains(TestClass.class.getClassLoader().toString()));
    }

    @Test
    public void testGetHandler_WithMockMethodInterceptorMockAccess_ReturnsMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockInterceptor = mock(MockMethodInterceptor.class);
        MockHandler handler = mock(MockHandler.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockInterceptor);
        when(mockInterceptor.getMockHandler()).thenReturn(handler);

        // Act
        MockHandler result = mockMaker.getHandler(mockAccess);

        // Assert
        assertNotNull(result);
        assertEquals(handler, result);
    }

    @Test
    public void testGetHandler_WithInvalidObject_ReturnsNull() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object obj = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(obj);

        // Assert
        assertNull(result);
    }

    @Test
    public void testResetMock_WithMockMethodInterceptorMockAccess_SetsMockitoInterceptorWithNewHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockInterceptor = mock(MockMethodInterceptor.class);
        MockHandler handler = mock(MockHandler.class);
        MockHandler newHandler = mock(MockHandler.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockInterceptor);

        // Act
        mockMaker.resetMock(mockAccess, newHandler, null);

        // Assert
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verify(mockInterceptor).setMockHandler(newHandler);
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_WithInvalidMockAccess_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);

        // Act
        mockMaker.resetMock(mock, newHandler, null);
    }

    @Test
    public void testInitializeClassInstantiator_WithObjenesisClassLoader_ReturnsClassInstantiatorUsingObjenesis() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Assert
        assertNotNull(result);
        assertEquals(ClassInstantiator.UsingObjenesis.class, result.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_WithoutObjenesisClassLoader_ThrowsIllegalStateException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        // Set global configuration to disable class cache
        GlobalConfiguration.enableClassCache(false);

        // Act
        mockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler_WithInternalMockHandler_ReturnsInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler handler = mock(InternalMockHandler.class);

        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Assert
        assertNotNull(result);
        assertEquals(handler, result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_WithInvalidMockHandler_ThrowsMockitoException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);

        // Act
        mockMaker.asInternalMockHandler(handler);
    }
}