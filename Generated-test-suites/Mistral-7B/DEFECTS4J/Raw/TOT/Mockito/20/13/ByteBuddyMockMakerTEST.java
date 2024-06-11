import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.MockCreationSettings;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@PrepareForTest({ByteBuddyMockMaker.class, ClassInstantiator.class, CachingMockBytecodeGenerator.class})
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void init() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = PowerMockito.spy(byteBuddyMockMaker.initializeClassInstantiator());
        cachingMockBytecodeGenerator = PowerMockito.spy(new CachingMockBytecodeGenerator());
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        Type typeToMock = Mockito.class;

        MockCreationSettings<Object> settings = new MockCreationSettings<>(typeToMock, new SerializableMode(false), mockHandler);
        Object mockInstance = byteBuddyMockMaker.createMock(settings, mockHandler);

        assertNotNull(mockInstance);
        assertNull(byteBuddyMockMaker.getHandler(mockInstance));

        ByteBuddyMockMaker.resetMock(mockInstance, mockHandler, settings);
        MockHandler resetHandler = byteBuddyMockMaker.getHandler(mockInstance);
        assertEquals(mockHandler, resetHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockHandler mockHandler = mock(MockHandler.class);
        Type typeToMock = Mockito.class;

        MockCreationSettings<Object> settings = new MockCreationSettings<>(typeToMock, new SerializableMode(true), mockHandler);
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testCreateMockWithInvalidTypeToMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        Type typeToMock = Object.class;

        MockCreationSettings<Object> settings = new MockCreationSettings<>(typeToMock, new SerializableMode(false), mockHandler);
        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            assertFalse("Expected MockitoException", true);
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'java.lang.Object'", e.getMessage());
        }
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithMismatchedTypes() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        MockHandler mockHandler = mock(MockHandler.class);
        Type mockedProxyType = ByteBuddyMockMaker.class;
        Class<?> classToMock = String.class;

        MockCreationSettings<String> settings = new MockCreationSettings<>(classToMock, new SerializableMode(false), mockHandler);
        byteBuddyMockMaker.createMock(settings, mockHandler);

        ByteBuddyMockMaker.resetMock(ByteBuddyMockMaker.class, mockHandler, settings);
        Object resetMock = byteBuddyMockMaker.getHandler(ByteBuddyMockMaker.class);
        assertNotNull(resetMock);
        assertFalse(mockedProxyType.isInstance(resetMock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        byteBuddyMockMaker.initializeClassInstantiator();
        assertNotNull(classInstantiator);
    }
}