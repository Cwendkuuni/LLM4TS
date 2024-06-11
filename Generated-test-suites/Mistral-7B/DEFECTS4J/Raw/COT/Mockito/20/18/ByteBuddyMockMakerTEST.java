import org.junit.Test;
import org.junit.Assert;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        Class<?> mockedType = Mockito.mock(String.class);
        Type mockType = Mockito.typeCapture(Mockito.mock(Type.class));

        try {
            Object mockInstance = mockMaker.createMock(new MockCreationSettings<>((Class<?>) mockedType, mockType), mockHandler);
            Assert.assertNotNull(mockInstance);
            Assert.assertInstanceOf(mockedType, mockInstance);
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }

        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class, Mockito.mock(Type.class));

        try {
            Object mockInstance = mockMaker.createMock(settings, mockHandler);
            Assert.assertNotNull(mockInstance);
            Assert.assertInstanceOf(String.class, mockInstance);
        } catch (ClassCastException e) {
            Assert.fail("Unexpected ClassCastException");
        } catch (InstantiationException e) {
            Assert.fail("Unexpected InstantiationException");
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        Class<?> mockedType = Mockito.mock(String.class);
        Type mockType = Mockito.typeCapture(Mockito.mock(Type.class));
        Object mockInstance = new Object() {};

        Assert.assertSame(mockedType, mockMaker.ensureMockIsAssignableToMockedType(new MockCreationSettings<>((Class<?>) mockedType, mockType), mockInstance));
    }

    @Test
    public void testDescribeClass() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        Class<?> mockedType = Mockito.mock(String.class);
        Object instance = new Object() {};

        String expected = "'" + Mockito.class.getCanonicalName() + "', loaded by classloader : 'mockito.internal.ReflectionUtils$ClassLoaderUtil@7852e9b9'";
        Assert.assertEquals(expected, mockMaker.describeClass(mockedType));

        expected = "null";
        Assert.assertEquals(expected, mockMaker.describeClass(null));

        expected = "" + instance.getClass().getCanonicalName() + ", loaded by classloader : 'sun.misc.Launcher$AppClassLoader@7852e9b9'";
        Assert.assertEquals(expected, mockMaker.describeClass(instance));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);

        MockHandler handler = Mockito.mock(MockHandler.class);
        Mockito.doAnswer(invocation -> handler).when(mock).setMockitoInterceptor(Mockito.any());

        Assert.assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);

        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(Object.class, Mockito.mock(Type.class));

        mockMaker.resetMock(mock, newHandler, settings);

        MockMethodInterceptor oldMockAccess = (MockMethodInterceptor) mock;
        MockMethodInterceptor newMockAccess = (MockMethodInterceptor) mock;

        Assert.assertNotSame(oldMockAccess.getMockitoInterceptor().getMockHandler(), newMockAccess.getMockitoInterceptor().getMockHandler());
        Assert.assertEquals(newHandler, newMockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        try {
            mockMaker.initializeClassInstantiator();
            // Pass
        } catch (ClassNotFoundException e) {
            Assert.fail("Unexpected ClassNotFoundException");
        } catch (InstantiationException e) {
            Assert.fail("Unexpected InstantiationException");
        } catch (IllegalAccessException e) {
            Assert.fail("Unexpected IllegalAccessException");
            Assert.fail("Unexpected IllegalAccessException");
        } catch (InvocationTargetException e) {
            Assert.fail("Unexpected InvocationTargetException");
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        Class<?> mockedType = Mockito.mock(String.class);
        Type mockType = Mockito.typeCapture(Mockito.mock(Type.class));
        MockCreationSettings<String> settings = new MockCreationSettings<>(mockedType, mockType);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        mockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        Class<?> mockedType = Mockito.mock(String[].class);
        Type mockType = Mockito.typeCapture(Mockito.mock(Type.class));
        MockCreationSettings<String[]> settings = new MockCreationSettings<>(mockedType, mockType);

        mockMaker.createMock(settings, mockHandler);
    }
}