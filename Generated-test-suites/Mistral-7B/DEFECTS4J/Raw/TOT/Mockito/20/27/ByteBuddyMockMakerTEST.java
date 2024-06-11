import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalMockHandler;
import org.mockito.MockCreationSettings;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerResolver;
import org.mockito.plugins.MockMakerUtil;
import org.mockito.invocation.MockMethodInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private InternalMockHandler handler;
    private MockMaker mockMaker;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        handler = mock(InternalMockHandler.class);
        mockMaker = spy(byteBuddyMockMaker);
        MockMakerResolver.getInstance().registerMockMaker(ByteBuddyMockMaker.class, mockMaker);
    }

    @Test
    public void testCreateMockSuccess() {
        Type typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<String>(typeToMock, null);
        MockHandler handler = mock(MockHandler.class);

        String mock = byteBuddyMockMaker.createMock(settings, handler);
        assertEquals(String.class, mock.getClass());

        verify(handler).handle(ArgumentMatchers.anyInvocation());
    }

    @Test
    public void testCreateMockFailureSerializationAcrossClassLoaders() {
        Type typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<String>(typeToMock, null);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockFailureInstantiationException() {
        Type typeToMock = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(typeToMock, null);
        MockHandler handler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            ArgumentCaptor<String> exceptionMessageCaptor = ArgumentCaptor.forClass(String.class);
            assertEquals("Unable to create mock instance of type '" + Object.class.getSuperclass().getSimpleName() + "'", exceptionMessageCaptor.getValue(e.getCause().getMessage()));
            verify(handler).handle(ArgumentMatchers.anyInvocation());
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(String.class);
        assertEquals(handler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(String.class);
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<String>(String.class, null));
        assertNull(byteBuddyMockMaker.getHandler(mock));
        verify(handler).handle(ArgumentMatchers.anyInvocation());
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            Class<?> classInstantiatorClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> constructor = classInstantiatorClass.getDeclaredConstructor(boolean.class);
            ClassInstantiator classInstantiator = (ClassInstantiator) constructor.newInstance(true);
            // Test the ClassInstantiator instance here
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Expected initialization of ClassInstantiator to not throw an exception");
        }
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerFailure() {
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.asInternalMockHandler(null);
    }
}