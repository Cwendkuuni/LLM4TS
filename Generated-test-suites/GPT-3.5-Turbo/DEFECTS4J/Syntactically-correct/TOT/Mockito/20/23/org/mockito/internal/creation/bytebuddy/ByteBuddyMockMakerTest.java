import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import org.junit.Test;

public class ByteBuddyMockMakerTest {

    @Test
    public void createMock_SerializableModeAcrossClassloaders_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        // Act & Assert
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void createMock_CastException_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        
        // Act & Assert
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            String expectedMessage = "ClassCastException occurred while creating the mockito mock :\n" +
                    "  class to mock : 'org.mockito.internal.creation.bytebuddy.TestClass', loaded by classloader : 'null'\n" +
                    "  created class : 'org.mockito.internal.creation.bytebuddy.TestClass', loaded by classloader : 'null'\n" +
                    "  proxy instance class : 'null'\n" +
                    "  instance creation by : 'UsingObjenesis', loaded by classloader : 'null'\n" +
                    "\n" +
                    "You might experience classloading issues, please ask the mockito mailing-list.\n" +
                    "";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void createMock_InstantiationException_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        doThrow(new org.mockito.internal.creation.instance.InstantiationException()).when(mockMaker).instantiate(any());
        
        // Act & Assert
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'Object'", e.getMessage());
        }
    }

    @Test
    public void ensureMockIsAssignableToMockedType_ReturnsCastMock() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        
        // Act
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Assert
        assertSame(mock, result);
    }

    @Test
    public void describeClass_ReturnsNull_WhenTypeIsNull() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Act
        String result = mockMaker.describeClass(null);
        
        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_ReturnsClassDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<?> type = TestClass.class;
        
        // Act
        String result = mockMaker.describeClass(type);
        
        // Assert
        assertEquals("'org.mockito.internal.creation.bytebuddy.TestClass', loaded by classloader : 'null'", result);
    }

    @Test
    public void describeClass_ReturnsInstanceDescription_WhenInstanceNotNull() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass instance = new TestClass();
        
        // Act
        String result = mockMaker.describeClass(instance);
        
        // Assert
        assertEquals("'org.mockito.internal.creation.bytebuddy.TestClass', loaded by classloader : 'null'", result);
    }

    @Test
    public void getHandler_ReturnsMockHandler_WhenMockIsInstanceOfMockAccess() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        when(mock.getMockitoInterceptor()).thenReturn(mockHandler);
        
        // Act
        MockHandler result = mockMaker.getHandler(mock);
        
        // Assert
        assertSame(mockHandler, result);
    }

    @Test
    public void getHandler_ReturnsNull_WhenMockIsNotInstanceOfMockAccess() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        
        // Act
        MockHandler result = mockMaker.getHandler(mock);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void resetMock_SetsNewHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);
        MockHandler newHandler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        doNothing().when(mock).setMockitoInterceptor(any());
        
        // Act
        mockMaker.resetMock(mock, newHandler, settings);
        
        // Assert
        verify(mock).setMockitoInterceptor(any());
    }

    private static class TestClass {
        // Some fields and methods for testing purposes
    }
}