import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {}, mockHandler);

        ArgumentCaptor<Class<?>> typeToMockArgumentCaptor = ArgumentCaptor.forClass(Class.class);
        ArgumentCaptor<MockHandler> handlerArgumentCaptor = ArgumentCaptor.forClass(MockHandler.class);

        verify(byteBuddyMockMaker).cachingMockBytecodeGenerator.get(typeToMockArgumentCaptor.capture(), handlerArgumentCaptor.capture());
        Class<?> mockedProxyType = typeToMockArgumentCaptor.getValue();
        MockHandler capturedHandler = handlerArgumentCaptor.getValue();

        Object mockInstance = byteBuddyMockMaker.instantiate(mockedProxyType);
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mockInstance;
        assertEquals(capturedHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithNullHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {}, null);
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {
            @Override
            public Class<? extends MockHandler<Object>> getTypeToMock() {
                return null;
            }
        }, mockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithHandlerNotMockMethodInterceptorAccess() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {}, mock(Object.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeACROSS_CLASSLOADERS() {
        MockHandler mockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {
            @Override
            public Class<? extends MockHandler<Object>> getTypeToMock() {
                return MockHandler.class;
            }

            @Override
            public SerializableMode getSerializableMode() {
                return SerializableMode.ACROSS_CLASSLOADERS;
            }
        }, mockHandler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {}, mockHandler);
        byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), new MockCreationSettings<MockHandler>() {});
        assertNotSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<MockHandler>() {}, mockHandler);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(mockHandler, handler);
    }

    @Test
    public void testDescribeClass() {
        Class<String> stringClass = String.class;
        Object stringInstance = new String("Hello World");

        assertEquals("class 'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", byteBuddyMockMaker.describeClass(stringClass));
        assertEquals("class 'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", byteBuddyMockMaker.describeClass(stringInstance));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        } catch (IllegalStateException e) {
            verify(Mockito.mock(Class.class)).getClassLoader();
            verifyNoMoreInteractions(Mockito.mock(Class.class));
            throw e;
        }
    }

}