import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator$UsingObjenesis;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();

        try {
            Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
            Assert.assertNotNull(mock);
            Assert.assertInstanceOf(MockMethodInterceptor.class, mock);
        } catch (MockitoException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateMock_SerializationAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = Mockito.mock(Object.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker.MockAccess mockAccess = (ByteBuddyMockMaker.MockAccess) mock;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(Mockito.internal.MockitoCore.mockHandler(), null));

        Assert.assertNotNull(byteBuddyMockMaker.getHandler(mock));
        Assert.assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = Mockito.mock(Object.class);
        MockHandler newMockHandler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker.MockAccess mockAccess = (ByteBuddyMockMaker.MockAccess) mock;

        byteBuddyMockMaker.resetMock(mock, newMockHandler, null);

        Assert.assertSame(newMockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (Throwable e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiator_ObjenesisMissing() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}