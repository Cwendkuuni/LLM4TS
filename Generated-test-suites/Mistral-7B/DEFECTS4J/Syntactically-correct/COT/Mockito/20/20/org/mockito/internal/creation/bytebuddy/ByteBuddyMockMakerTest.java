import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.MockCreationSettings;
import org.mockito.internal.creation.bytebuddy.MockHandler;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMock() {
        MockCreationSettings<String> settings = MockCreationSettings.defaultSettings().forClass(String.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        String mock = byteBuddyMockMaker.createMock(settings, handler);
        Assert.assertNotNull(mock);
        Assert.assertEquals(String.class, mock.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationAcrossClassloaders() {
        MockCreationSettings<String> settings = MockCreationSettings.defaultSettings()
                .forClass(String.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMockWithExceptionThrownDuringInstanceCreation() {
        MockCreationSettings<Integer> settings = MockCreationSettings.defaultSettings().forClass(int[].class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("Unable to create mock instance of type 'class [I'", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        Object mock = Mockito.mock(Object.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, MockCreationSettings.defaultSettings().forClass(Object.class));
        Assert.assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Class<Integer> intType = int[].class;
        MockCreationSettings<Integer> settings = MockCreationSettings.defaultSettings().forClass(intType);
        Object mock = Mockito.mock(intType);
        Integer returnType = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        Assert.assertEquals(intType.cast(mock), returnType);
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithDifferentTypes() {
        Class<String> stringType = String.class;
        MockCreationSettings<Integer> settings = MockCreationSettings.defaultSettings().forClass(int[].class);
        Object mock = Mockito.mock(stringType);
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

}