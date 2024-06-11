import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockCreationSettings;
import org.mockito.internal.creation.bytebuddy.MockHandler;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(String.class).build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(settings, handler);
        verify(handler).handle(Mockito.any(), Mockito.eq(settings));
        verifyZeroInteractions(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withType(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, null);
    }

    @Test
    public void testCreateMockForUnknownClass() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withType(Class.forName("UnknownClass"))
                .build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        try {
            mockMaker.createMock(settings, null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testUnableToCreateMockInstance() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withType(String.class)
                .build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<?> clazz = Class.forName("UninstantiatableClass");
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        try {
            Object mock = classInstantiator.instantiate(clazz);
            mockMaker.createMock(settings, null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testGetHandlerReturnsProvidedMockHandler() {
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler actualHandler = mockMaker.getHandler(mock);
        assertEquals(handler, actualHandler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.resetMock(mock, handler, new MockCreationSettings.Builder().build());
        MockHandler actualHandler = mockMaker.getHandler(mock);
        assertEquals(handler, actualHandler);
    }
}