package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.exceptions.base.MockitoException;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(PowerMockRunner.class)
public class ByteBuddyMockMakerTest {

    // Testing constructor
    @Test
    public void testByteBuddyMockMakerConstructor() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assert null != byteBuddyMockMaker;
    }

    // Testing createMock method (basic functionality)
    @Test
    public void testCreateMockBasicFunctionality() throws Exception {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assert mock != null;
        verify(handler).handle(any(), eq(settings));
    }

    // Testing createMock method (serialization not supported)
    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationNotSupported() throws Exception {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    // Testing createMock method (ClassCastException)
    @Test(expected = MockitoException.class)
    public void testCreateMockClassCastException() throws Exception {
        Class<?> mockedProxyType = Class.forName("org.mockito.MockitoTestClass");
        Class<?> actualClass = Class.forName("org.mockito.MockitoTestClassProxy");
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(null, handler); // null MockCreationSettings
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType), handler);
        Whitebox.setInternalState(mock, "mockedInstance", actualClass.newInstance());
        ByteBuddyMockMaker byteBuddyMockMakerInstance = new ByteBuddyMockMaker();
        Object mockInstance = byteBuddyMockMakerInstance.createMock(settings, handler);
        assert mockInstance != mock;
    }

    // Testing createMock method (InstantiationException)
    @Test(expected = MockitoException.class)
    public void testCreateMockInstantiationException() throws Exception {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    // Testing getHandler method
    @Test
    public void testGetHandler() throws Exception {
        Object mock = Mockito.mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assert null != handler;
    }

    // Testing getHandler method (non-MockAccess mock)
    @Test
    public void testGetHandlerNonMockAccessMock() throws Exception {
        Object mock = new Object();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assert null == handler;
    }

    // Testing resetMock method (basic functionality)
    @Test
    public void testResetMock() throws Exception {
        Object mock = Mockito.mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, handler, Mockito.mock(MockCreationSettings.class
        ));
        verify(handler).handle(any(), eq(Mockito.any(MockCreationSettings.class)));
    }

    // Testing initializeClassInstantiator method (Objenesis not found)
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorObjenesisNotFound() throws Exception {
        new ByteBuddyMockMaker();
    }

    // Testing asInternalMockHandler method (null MockHandler)
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerNullMockHandler() throws Exception {
        MockHandler mockHandler = null;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }
}