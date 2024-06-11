import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.creation.ByteBuddyMockMaker;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.StringJoiner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> mockCreationSettings = Mockito.settings().plusAnnotations(SerializableMode.PROXY);

        Object mock = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
        Assert.assertNotNull(mock);

        Type mockType = mockCreationSettings.getTypeToMock();
        Assert.assertEquals(mockType.getClass(), mock.getClass());

        MockHandler actualHandler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertEquals(mockHandler, actualHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> mockCreationSettings = Mockito.settings().serializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testCreateMockWithInstantiationException() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> mockCreationSettings = Mockito.settings().plusAnnotations(SerializableMode.PROXY);

        Class<?> mockType = Class.forName("java.lang.String");
        ClassInstantiator classInstantiator = Mockito.spy(byteBuddyMockMaker.classInstantiator);
        Mockito.doThrow(new InstantiationException("Test Instantiation Exception")).when(classInstantiator).instantiate(mockType);

        Assert.assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler));
    }

    @Test
    public void testCreateMockWithClassCastException() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<String> mockCreationSettings = Mockito.settings().plusAnnotations(SerializableMode.PROXY).forClass(String.class);

        Class<String> mockedProxyType = Class.forName("java.lang.String");
        Object mockInstance = new String("Test String");

        Assert.assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler, mockInstance));
    }

    @Test
    public void testGetHandler() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);

        MockHandler actualHandler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertNull(actualHandler);

        MockMethodInterceptor mockAccess = Mockito.spy(mock);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.setMockitoInterceptor(mockAccess, mockHandler);

        actualHandler = byteBuddyMockMaker.getHandler(mockAccess);
        Assert.assertEquals(mockHandler, actualHandler);
    }

    @Test
    public void testResetMock() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockHandler oldHandler = Mockito.mock(MockHandler.class);

        byteBuddyMockMaker.setMockitoInterceptor(mock, oldHandler);
        byteBuddyMockMaker.resetMock(mock, newHandler, null);

        MockHandler actualHandler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertEquals(newHandler, actualHandler);
    }

    @Test
    public void testInitializeClassInstantiator() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        ClassInstantiator classInstantiator = byteBuddyMockMaker.classInstantiator;

        Assert.assertEquals(ClassInstantiator.class, classInstantiator.getClass());
        Assert.assertTrue(((Boolean) usingClassCacheConstructor.getParameterTypes()[0].getDefaultValue()).equals(byteBuddyMockMaker.new GlobalConfiguration().enableClassCache()));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithObjenesisMissing() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testAsInternalMockHandler() throws Exception {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        InternalMockHandler internalMockHandler = Mockito.mock(InternalMockHandler.class);
        Mockito.when(internalMockHandler.getMockHandler()).thenReturn(mockHandler);

        InternalMockHandler actualHandler = ByteBuddyMockMaker.asInternalMockHandler(mockHandler);

        Assert.assertEquals(internalMockHandler, actualHandler);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithInvalidMockHandler() throws Exception {
        Object invalidMockHandler = new Object();

        Assert.assertThrows(MockitoException.class, () -> ByteBuddyMockMaker.asInternalMockHandler(invalidMockHandler));
    }
}