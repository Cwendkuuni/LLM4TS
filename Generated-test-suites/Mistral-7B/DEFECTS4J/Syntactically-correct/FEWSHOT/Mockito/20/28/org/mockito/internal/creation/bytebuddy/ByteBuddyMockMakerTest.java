package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.mock.MockType.ANNOTATED;
import static org.mockito.mock.MockType.JAVA;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerUtils;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockWithAnnotatedType() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalMockHandler = MockMakerUtils.createNiceMock(MockCreationSettings.builder()
                .inClassloader(Thread.currentThread().getContextClassLoader())
                .serializable()
                .build(), ANNOTATED);

        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(Object.class)
                .serializable()
                .extraInterfaces(new Object(){}.getClass())
                .build();

        Object mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);

        assertNotNull(mock);
        assertFalse(mock instanceof Serializable);
    }

    @Test
    public void testCreateMockWithJavaType() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalMockHandler = MockMakerUtils.createNiceMock(MockCreationSettings.builder()
                .inClassloader(Thread.currentThread().getContextClassLoader())
                .serializable()
                .build(), JAVA);

        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(String[].class)
                .serializable()
                .build();

        Object mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);

        assertNotNull(mock);
        assertFalse(mock instanceof Serializable);
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(Object.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, null));
    }

    @Test
    public void testCreateMockWithNullHandler() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(Object.class)
                .build();

        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, null));
    }

    @Test
    public void testGetHandler() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = MockMakerUtils.createMock(MockCreationSettings.builder()
                .typeToMock(Object.class)
                .build(), JAVA);

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = MockMakerUtils.createMock(MockCreationSettings.builder()
                .typeToMock(Object.class)
                .build(), JAVA);

        InternalMockHandler internalMockHandler = MockMakerUtils.createNiceMock(MockCreationSettings.builder()
                .typeToMock(Object.class)
                .build(), ANNOTATED);

        byteBuddyMockMaker.resetMock(mock, internalMockHandler, MockCreationSettings.builder().build());

        MockHandler newHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(newHandler);
        assertSame(internalMockHandler, newHandler);
    }

    @Test
    public void testGetHandlerWithNullMock() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        assertNull(byteBuddyMockMaker.getHandler(null));
    }

    @Test
    public void testResetMockWithNullNewHandler() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = MockMakerUtils.createMock(MockCreationSettings.builder()
                .typeToMock(Object.class)
                .build(), JAVA);

        byteBuddyMockMaker.resetMock(mock, null, MockCreationSettings.builder().build());

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        assertNotNull(byteBuddyMockMaker.initializeClassInstantiator());
    }

    @Test
    public void testInitializeClassInstantiatorWithObjenesisMissing() {
        MockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        assertThrows(IllegalStateException.class, byteBuddyMockMaker::initializeClassInstantiator);
    }

    @Test
    public void testDescribeClass() {
        Class<?> clazz = String[].class;
        assertNotNull(clazz);

        String expected = join("'String[]', loaded by classloader : '", clazz.getClassLoader(), "'");

        assertEquals(expected, ByteBuddyMockMaker.describeClass(clazz));
    }

    @Test
    public void testDescribeClassWithNullClass() {
        String expected = "null";

        assertEquals(expected, ByteBuddyMockMaker.describeClass(null));
    }
}