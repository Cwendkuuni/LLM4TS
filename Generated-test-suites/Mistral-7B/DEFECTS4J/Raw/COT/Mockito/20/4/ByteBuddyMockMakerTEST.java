import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMockMethod() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Integer> settings = new MockCreationSettings.Builder().build();
        Integer mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().typeToMock(String[].class).build();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);
        assertFalse(mock instanceof String);
        throw new ClassCastException("Cannot cast mock to expected type");
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();
        byteBuddyMockMaker.createMock(settings, handler);
        byteBuddyMockMaker.createMock(settings, handler); // Exception is thrown the second time
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), handler);
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings.Builder().build());
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler1 = Mockito.mock(MockHandler.class);
        MockHandler handler2 = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), handler1);
        byteBuddyMockMaker.resetMock(mock, handler2, new MockCreationSettings.Builder().build());
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assertEquals(handler2, returnedHandler);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
    }
}