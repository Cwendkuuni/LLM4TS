import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock_SimpleSettings_SuccessfulMockCreation() {
        // Given
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenReturn(new ByteBuddyMockMaker());
        // When
        ByteBuddyMockMaker result = (ByteBuddyMockMaker) mockMaker.createMock(new MockCreationSettings<ByteBuddyMockMaker>() {
        }, classInstantiator);
        // Then
        assertNotNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassloaders_ExceptionThrown() {
        // Given
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        // When
        mockMaker.createMock(settings, mock(MockHandler.class));
        // Then
        // Exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException_ExceptionThrown() {
        // Given
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenReturn("InvalidInstance");
        // When
        mockMaker.createMock(settings, classInstantiator);
        // Then
        // Exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException_ExceptionThrown() {
        // Given
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(new InstantiationException("Unable to create instance"));
        // When
        mockMaker.createMock(settings, classInstantiator);
        // Then
        // Exception should be thrown
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_ConversionSuccess() {
        // Given
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(ByteBuddyMockMaker.class);
        ByteBuddyMockMaker mock = new ByteBuddyMockMaker();
        // When
        ByteBuddyMockMaker result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        // Then
        assertEquals(mock, result);
    }

    @Test
    public void testDescribeClass_TypeNotNull_ValidDescriptionReturned() {
        // Given
        Class type = String.class;
        // When
        String description = mockMaker.describeClass(type);
        // Then
        assertNotNull(description);
    }

    @Test
    public void testDescribeClass_InstanceNotNull_ValidDescriptionReturned() {
        // Given
        Object instance = new String();
        // When
        String description = mockMaker.describeClass(instance);
        // Then
        assertNotNull(description);
    }

    @Test
    public void testGetHandler_ValidMock_ValidHandlerReturned() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(ByteBuddyMockMaker.MockMethodInterceptor.MockAccess.class);
        // When
        MockHandler handler = mockMaker.getHandler(mock);
        // Then
        assertNotNull(handler);
    }

    @Test
    public void testResetMock_ValidMock_ValidReset() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(ByteBuddyMockMaker.MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        // When
        mockMaker.resetMock(mock, newHandler, settings);
        // Then
        // No exception should be thrown
    }
}