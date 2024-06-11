import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandlerInvocation;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import org.mockito.plugins.MockMakerRegistry;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockSuccess() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(MockMakerRegistry.getDefaultMockMaker(), null, null, handler, null);
        Type typeToMock = String.class;
        Class<? extends Object> mockedProxyType = mockMaker.createMock(settings, handler).getClass();
        Assert.assertNotNull(mockedProxyType);
        Assert.assertInstanceOf(MockMethodInterceptor.class, Whitebox.getInternalState(mockedProxyType.newInstance()));
        verify(handler).handle(MockHandlerInvocation.create(mockedProxyType, "testMethod", new Object[0]));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(MockMakerRegistry.getDefaultMockMaker(), null, null, handler, null);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(String.class);
        MockHandler handler = mockMaker.getHandler(mock);
        Assert.assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(String.class);
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(MockMakerRegistry.getDefaultMockMaker(), null, null, handler, null);
        mockMaker.resetMock(mock, handler, settings);
        verifyNoInteractions(handler);
        verify(handler).handle(MockHandlerInvocation.create(mock, "testMethod", new Object[0]));
    }

    @Test
    public void testInitializeClassInstantiatorSuccess() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<? extends ClassInstantiator> classInstantiatorType = mockMaker.getClass().getClassLoader().loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<? extends ClassInstantiator> constructor = classInstantiatorType.getDeclaredConstructor(boolean.class);
        ClassInstantiator classInstantiator = (ClassInstantiator) constructor.newInstance(true);
        Assert.assertNotNull(classInstantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeClassInstantiatorFail() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<? extends ClassInstantiator> classInstantiatorType = mockMaker.getClass().getClassLoader().loadClass("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        try {
            Constructor<? extends ClassInstantiator> constructor = classInstantiatorType.getDeclaredConstructor();
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Initialization of ClassInstantiator failed", e);
        }
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        InternalMockHandler internalMockHandler = (InternalMockHandler) ByteBuddyMockMaker.asInternalMockHandler(handler);
        Assert.assertNotNull(internalMockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerInvalidHandler() {
        MockHandler handler = mock(Object.class);
        ByteBuddyMockMaker.asInternalMockHandler(handler);
    }
}