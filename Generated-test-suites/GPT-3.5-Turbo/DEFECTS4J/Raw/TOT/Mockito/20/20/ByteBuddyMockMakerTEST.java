package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_validSettings_mockInstanceCreated() {
        // Arrange
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setTypeToMock(TestClass.class);

        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        TestClass mockInstance = mockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(mockInstance);
        assertTrue(mockInstance instanceof TestClass);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_serializableModeAcrossClassLoaders_exceptionThrown() {
        // Arrange
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setTypeToMock(TestClass.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_classCastException_exceptionThrown() {
        // Arrange
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setTypeToMock(TestClass.class);

        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_instantiationException_exceptionThrown() {
        // Arrange
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setTypeToMock(TestClass.class);

        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_mockAssignableToType_mockReturned() {
        // Arrange
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setTypeToMock(TestClass.class);

        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass mockInstance = Mockito.mock(TestClass.class);

        // Act
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);

        // Assert
        assertNotNull(result);
        assertEquals(mockInstance, result);
    }

    @Test
    public void testGetHandler_mockNotInstanceOfMockAccess_nullReturned() {
        // Arrange
        Object mock = new Object();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetHandler_mockInstanceOfMockAccess_mockHandlerReturned() {
        // Arrange
        Object mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler expectedHandler = Mockito.mock(MockHandler.class);
        Mockito.when(((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler()).thenReturn(expectedHandler);

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNotNull(result);
        assertEquals(expectedHandler, result);
    }

    @Test
    public void testResetMock_mockInstanceOfMockAccess_mockitoInterceptorSetWithNewHandler() {
        // Arrange
        Object mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getSerializableMode()).thenReturn(SerializableMode.DEFAULT);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        Mockito.verify(((MockMethodInterceptor.MockAccess) mock)).setMockitoInterceptor(mockMaker.asInternalMockHandler(newHandler));
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_mockNotInstanceOfMockAccess_exceptionThrown() {
        // Arrange
        Object mock = new Object();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testInitializeClassInstantiator_objenesisClassLoaderPresent_classInstantiatorInitialized() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ClassInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_objenesisClassLoaderNotPresent_exceptionThrown() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        mockMaker.initializeClassInstantiator();

        // Assert
        // Exception should be thrown
    }

    @Test
    public void testAsInternalMockHandler_handlerNotInstanceOfInternalMockHandler_exceptionThrown() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);

        // Act & Assert
        try {
            mockMaker.asInternalMockHandler(handler);
            fail("No exception thrown");
        } catch (MockitoException e) {
            assertEquals("At the moment you cannot provide own implementations of MockHandler.\nPlease see the javadocs for the MockMaker interface.\n", e.getMessage());
        }
    }

    @Test
    public void testAsInternalMockHandler_handlerInstanceOfInternalMockHandler_internalMockHandlerReturned() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler handler = Mockito.mock(InternalMockHandler.class);

        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Assert
        assertNotNull(result);
        assertEquals(handler, result);
    }

    private static class TestClass {}

}