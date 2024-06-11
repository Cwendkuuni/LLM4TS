import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockHandler mockHandler;
    private Class<Object> mockableClass;
    private Class<Object> mockedProxyType;

    @Rule
    public MockMaker mockMaker = new MockitoJUnitRunnerMockMaker();

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockHandler = mock(MockHandler.class);
        mockableClass = Mockito.bytebuddy().bytebuddyAgent().install();
        mockedProxyType = mockableClass;
    }

    @Test
    public void testCreateMockSuccess() {
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockedProxyType, mockHandler);
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        verify(mockHandler).handle(Mockito.eq(mock), Mockito.eq(settings));
    }

    @Test
    public void testCreateMockSerializableModeAcrossClassloaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockedProxyType, mockHandler);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            assert false : "Expected MockitoException to be thrown.";
        } catch (MockitoException e) {
            assert e.getMessage().contains("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType, mockHandler), mockHandler);
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType, mockHandler), mockHandler);
        byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(mockedProxyType, mockHandler));

        MockMethodInterceptor.MockAccess newMockAccess = (MockMethodInterceptor.MockAccess) mock;
        MockMethodInterceptor oldMockAccess = newMockAccess.getMockitoInterceptor().getMockHandler().getMockAccess();

        assertNotNull(newMockAccess);
        assertNull(newMockAccess.getMockitoInterceptor());
        assertNotNull(oldMockAccess);
        assertNotNull(oldMockAccess.getMockitoInterceptor());
        verify(mockHandler).handle(Mockito.eq(mock), Mockito.eq(new MockCreationSettings<>(mockedProxyType, mockHandler)));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (IllegalStateException e) {
            verify(mockMaker).mockMaker(Mockito.eq(ByteBuddyMockMaker.class));
            verify(mockMaker).getMockCreator(Mockito.eq(mockedProxyType), Mockito.any());
            verify(mockMaker).getMockHandler(Mockito.eq(mockedProxyType));
            return;
        }

        Mockito.fail("Expected IllegalStateException to be thrown.");
    }

    @Test
    public void testDescribeClass() {
        assertNotNull(byteBuddyMockMaker.describeClass(mockedProxyType));
    }

    @Test
    public void testCreateMockClassCastException() {
        Class<?> unrelatedClass = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings<>(unrelatedClass, mockHandler);

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            assert false : "Expected ClassCastException to be thrown.";
        } catch (ClassCastException e) {
            verify(mockHandler).handle(Mockito.eq(null), Mockito.eq(settings));
            verify(mockHandler, never()).handle(Mockito.eq(Mockito.any()), Mockito.eq(settings));
            verify(mockHandler, never()).handle(Mockito.eq(Mockito.any()), Mockito.any());
        }
    }

    @Test
    public void testCreateMockInstantiationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockedProxyType, mockHandler);

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            assert false : "Expected InstantiationException to be thrown.";
        } catch (InstantiationException e) {
            verify(mockHandler).handle(Mockito.eq(null), Mockito.eq(settings));
            verify(mockHandler, never()).handle(Mockito.any(), Mockito.any());
        }

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            assert false : "Expected InstantiationException to be thrown.";
        } catch (UndeclaredThrowableException e) {
            InstantiationException innerException = e.getRootCause();
            verify(mockHandler).handle(Mockito.eq(null), Mockito.eq(settings));
            verify(mockHandler, never()).handle(Mockito.any(), Mockito.any());
            assertNotNull(innerException);
        }
    }

    @Test
    public void testMockStaticMockito() {
        mockStatic(Mockito.class);
        when(Mockito.mock(Mockito.class)).thenReturn(mock(MockHandler.class));

        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType, mockHandler), mockHandler);
            assert false : "Expected MockitoException to be thrown.";
        } catch (MockitoException e) {
            verify(mockHandler).handle(Mockito.eq(mock(mockedProxyType), Mockito.eq(settings)));
        }

        Mockito.reset(Mockito.class);
    }

    @Captor
    private ArgumentCaptor<InvocationOnMock> invocationCaptor;

    @Test
    public void testMockHandlerInvoked() {
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType, mockHandler), mockHandler);

        when(mockHandler).thenAnswer(invocation -> {
            Object answer = invocation.getArguments()[0];
            answer.getClass(); // This forces the answer to be casted to the mocked type.
            return answer;
        });

        Object testObject = new Object();
        mock.someMethod(testObject);

        verify(mockHandler).handle(Mockito.eq(mock), Mockito.eq(new ArgumentCaptor<MockCreationSettings<Object>>(){}.getValue()));
        verify(mockHandler).handle(Mockito.eq(mock), Mockito.eq(new ArgumentCaptor<InvocationOnMock>(){}.getValue()));
        verify(mockHandler).handle(Mockito.eq(mock), Mockito.eq(testObject));
    }
}