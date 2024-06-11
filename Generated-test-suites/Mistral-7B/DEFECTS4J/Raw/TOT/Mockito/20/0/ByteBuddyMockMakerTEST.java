import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.NONE);
        }};

        String mockedProxyType = byteBuddyMockMaker.createMock(settings, mockHandler).getClass();
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) byteBuddyMockMaker.getHandler(mockedProxyType);
        assertEquals(mockHandler, mockAccess.getMockitoInterceptor().getMockHandler());

        // Test with a mock that implements multiple interfaces
        settings = new MockCreationSettings<DummyClass>() {{
            setTypeToMock(DummyClass.class);
            setSerializableMode(SerializableMode.NONE);
            addExtraInterfaces(DummyInterface1.class, DummyInterface2.class);
        }};

        DummyClass mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        verify(mockHandler).handle(eq(DummyInterface1.class), any(), any());
        verify(mockHandler).handle(eq(DummyInterface2.class), any(), any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        }};

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(Object.class);
            setSerializableMode(SerializableMode.NONE);
        }};

        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        byteBuddyMockMaker.resetMock(mock, mockHandler, settings);
        byteBuddyMockMaker.getHandler(mock); // This will cause a ClassCastException
    }

    @Test(expected = InstantiationException.class)
    public void testInstantiationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(Object.class);
            setSerializableMode(SerializableMode.NONE);
        }};

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
        } catch (MockitoException e) {
            throw new InstantiationException("Unable to create mock instance of type '" + Object.class.getSuperclass().getSimpleName() + "'", e);
        }
    }

    @Test
    public void testResetMock() {
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.NONE);
        }};

        String mockedProxyType = byteBuddyMockMaker.createMock(settings, mockHandler).getClass();
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) byteBuddyMockMaker.getHandler(mockedProxyType);
        ArgumentCaptor<MockHandler> newHandlerCaptor = ArgumentCaptor.forClass(MockHandler.class);

        byteBuddyMockMaker.resetMock(mockedProxyType, newHandler(1), settings);
        verify(mockHandler).handle(eq(MockCreationSettings.class), eq(settings), newHandlerCaptor.capture());
        MockHandler newMockHandler = newHandlerCaptor.getValue();
        assertEquals(newMockHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    private MockHandler newHandler(int i) {
        return invocation -> System.out.println("New handler called with : " + i);
    }

    private static class DummyClass {
        public void dummyMethod() {}
    }

    private interface DummyInterface1 {
        void dummyMethod1();
    }

    private interface DummyInterface2 {
        void dummyMethod2();
    }
}