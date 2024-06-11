import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.SerializableMode;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings(), mockHandler);
        MockMethodInterceptor mockMethodInterceptor = (MockMethodInterceptor) mock;
        MockHandler actualMockHandler = mockMethodInterceptor.getMockitoInterceptor().getMockHandler();
        Mockito.verify(mockHandler).handle(ArgumentMatchers.any());
        Mockito.verify(actualMockHandler).handle(ArgumentMatchers.any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerialization() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Mockito.doThrow(new MockitoException("Test serialization exception")).when(byteBuddyMockMaker).createMock(
                MockCreationSettings.defaultSettings().serializableMode(SerializableMode.ACROSS_CLASSLOADERS), Mockito.mock(MockHandler.class)
        );
        ByteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings().serializableMode(SerializableMode.ACROSS_CLASSLOADERS), Mockito.mock(MockHandler.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidClassType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings(), Mockito.mock(InvalidClassType.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithNullMockHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings(), null);
    }

    @Test
    public void testCreateMockWithSerializableMockHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings().serializableMode(SerializableMode.SERIALIZABLE), Mockito.mock(MockHandler.class));
        MockMethodInterceptor mockMethodInterceptor = (MockMethodInterceptor) mock;
        MockHandler actualMockHandler = mockMethodInterceptor.getMockitoInterceptor().getMockHandler();
        Mockito.verify(Mockito.mock(MockHandler.class)).handle(ArgumentMatchers.any());
        Mockito.verify(actualMockHandler).handle(ArgumentMatchers.any());
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings(), Mockito.mock(MockHandler.class));
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        Mockito.assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.defaultSettings(), Mockito.mock(MockHandler.class));
        Mockito.mockStatic(ByteBuddyMockMaker.class);
        MockHandler newMockHandler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker.resetMock(mock, newMockHandler, MockCreationSettings.defaultSettings());
        Mockito.verify(mock).setMockitoInterceptor(Mockito.argThat(m -> m instanceof MockMethodInterceptor.MockAccess));
        Mockito.verify(newMockHandler).handle(ArgumentMatchers.any());
    }
}