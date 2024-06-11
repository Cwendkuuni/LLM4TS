package org.mockito.internal.creation.bytebuddy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandlerInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockMakerRepository mockMakerRepository;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        mockMakerRepository = new MockMakerRepositoryImpl();
        Mockito.initMocks(this);
    }

    @Test
    public void testCreateMockWithValidSettings() {
        Class<DummyClass> dummyClass = DummyClass.class;
        MockHandler mockHandler = Mockito.mock(DummyInterface.class);
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder()
                .withTypeToMock(dummyClass)
                .withSerializableMode(SerializableMode.mode0())
                .build();

        DummyClass mock = mockMaker.createMock(settings, mockHandler);

        MockHandler handler = mockMaker.getHandler(mock);
        Mockito.verify(mock, Mockito.times(1)).methodCall();

        handler.handle(MockHandlerInvocation.mocked(mock));
        Assert.assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithUnsupportedSerializableMode() {
        Class<DummyClass> dummyClass = DummyClass.class;
        MockHandler mockHandler = Mockito.mock(DummyInterface.class);
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder()
                .withTypeToMock(dummyClass)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        mockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithMismatchedTypes() {
        Class<DummyClass> dummyClass = DummyClass.class;
        MockHandler mockHandler = Mockito.mock(String.class);
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder()
                .withTypeToMock(DummyClass.class)
                .withSerializableMode(SerializableMode.mode0())
                .build();

        mockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithFailedInstantiation() {
        Class<DummyClass> dummyClass = DummyClass.class;
        MockHandler mockHandler = Mockito.mock(DummyInterface.class);
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder()
                .withTypeToMock(dummyClass)
                .withSerializableMode(SerializableMode.mode0())
                .build();

        mockMaker.createMock(settings, mockHandler);

        try {
            Constructor<?> constructor = dummyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    @Test
    public void testResetMock() {
        Class<DummyClass> dummyClass = DummyClass.class;
        MockHandler mockHandler = Mockito.mock(DummyInterface.class);
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder()
                .withTypeToMock(dummyClass)
                .withSerializableMode(SerializableMode.mode0())
                .build();

        DummyClass mock = mockMaker.createMock(settings, mockHandler);
        MockHandler originalHandler = mockMaker.getHandler(mock);

        mockMaker.resetMock(mock, Mockito.mock(DummyInterface.class), settings);

        MockHandler newHandler = mockMaker.getHandler(mock);
        Assert.assertNotSame(originalHandler, newHandler);
    }
}

class DummyClass {
    public void methodCall() {

    }
}

interface DummyInterface {
    void methodCall();
}