import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.creation.bytebuddy.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.Class;
import java.lang.reflect.Constructor;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withSerializableMode(SerializableMode.DEFAULT)
                .build();

        ByteBuddyMockMaker.MockHandler actualMockHandler = byteBuddyMockMaker.createMock(settings, mockHandler);
        Assert.assertNotNull(actualMockHandler);
        Assert.assertEquals(mockHandler.getClass(), actualMockHandler.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testCreateMockClassCastException() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(String.class)
                .withSerializableMode(SerializableMode.DEFAULT)
                .build();

        Object mockInstance = byteBuddyMockMaker.createMock(settings, mockHandler);
        Assert.assertNotNull(mockInstance);

        Mockito.verify(mockHandler).mockUsing(Mockito.isA(Object.class));

        try {
            ByteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, (Object) new Integer(1));
            Assert.fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            Assert.assertEquals(Integer.class, e.getSourceClass());
            Assert.assertEquals(Object.class, e.getTargetType());
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockInstanceException() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withSerializableMode(SerializableMode.DEFAULT)
                .build();

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            Assert.fail("Expected InstantiationException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InstantiationException);
        }
    }

    @Test
    public void testGetHandler() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);

        MockHandler mockHandler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertNotNull(mockHandler);
        Assert.assertNull(mockHandler.getMockName());
    }

    @Test
    public void testResetMock() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = Mockito.mock(Object.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .build();

        byteBuddyMockMaker.resetMock(mock, mockHandler, settings);
        Mockito.verify(mock).setMockitoInterceptor(Mockito.eq(byteBuddyMockMaker.getHandler(mock)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetMockWithNullMock() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .build();

        byteBuddyMockMaker.resetMock(null, mockHandler, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetMockWithNullMockHandler() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = Mockito.mock(Object.class);
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .build();

        byteBuddyMockMaker.resetMock(mock, null, settings);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorClassNotFound() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorClassNotFoundObjenesis() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testDescribeClass() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Class<?> clazz1 = Object.class;
        Class<?> clazz2 = Mockito.class;
        Object obj1 = new Object();
        Object obj2 = new Object();

        String expected1 = "'" + Object.class.getCanonicalName() + "', loaded by classloader : '" + Object.class.getClassLoader() + "'";
        String expected2 = "'" + Mockito.class.getCanonicalName() + "', loaded by classloader : '" + Mockito.class.getClassLoader() + "'";
        String expected3 = "'" + Object.class.getName() + "', loaded by classloader : '" + Object.class.getClassLoader() + "'";
        String expected4 = "'" + Object.class.getName() + "', loaded by classloader : '" + Mockito.class.getClassLoader() + "'";

        String result1 = byteBuddyMockMaker.describeClass(clazz1);
        String result2 = byteBuddyMockMaker.describeClass(clazz2);
        String result3 = byteBuddyMockMaker.describeClass(obj1);
        String result4 = byteBuddyMockMaker.describeClass(obj2);

        Assert.assertEquals(expected1, result1);
        Assert.assertEquals(expected2, result2);
        Assert.assertEquals(expected3, result3);
        Assert.assertEquals(expected4, result4);
    }
}