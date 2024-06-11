package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private static final ByteBuddyMockMaker MOCK_MAKER = new ByteBuddyMockMaker();

    @Test
    public void testCreateMockThrowsExceptionWhenSerializableModeIsAcrossClassLoaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Integer.class).withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        assertThrows(MockitoException.class, () -> MOCK_MAKER.createMock(settings, mockitoStub -> {}));
    }

    @Test
    public void testCreateMockSuccess() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().withTypeToMock(String.class).build();
        String mock = MOCK_MAKER.createMock(settings, mockitoStub -> {});
        assertNotNull(mock);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Class<?> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().withTypeToMock(typeToMock).build();
        String mock = MOCK_MAKER.createMock(settings, mockitoStub -> {});

        assertInstanceOf(String.class, MOCK_MAKER.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test
    public void testDescribeClass() {
        Class<?> simpleClass = String.class;
        assertEquals("class 'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", MOCK_MAKER.describeClass(simpleClass));

        Class<?> complexClass = Class.forName("org.apache.commons.cli.Util");
        assertEquals("class 'org.apache.commons.cli.Util', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", MOCK_MAKER.describeClass(complexClass));
    }

    @Test
    public void testGetHandler() {
        Class<?> mockedType = String.class;
        Object mock = MOCK_MAKER.createMock(new MockCreationSettings.Builder().withTypeToMock(mockedType).build(), mockitoStub -> {});
        MockHandler handler = MOCK_MAKER.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        Class<?> mockedType = String.class;
        Object mock = MOCK_MAKER.createMock(new MockCreationSettings.Builder().withTypeToMock(mockedType).build(), mockitoStub -> {});
        MockHandler newHandler = mockitoStub -> {};
        MOCK_MAKER.resetMock(mock, newHandler, new MockCreationSettings.Builder().withTypeToMock(mockedType).build());
        MockHandler oldHandler = MOCK_MAKER.getHandler(mock);
        assertNotSame(newHandler, oldHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertDoesNotThrow(() -> MOCK_MAKER.initializeClassInstantiator());
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = mockitoStub -> {};
        assertInstanceOf(InternalMockHandler.class, MOCK_MAKER.asInternalMockHandler(mockHandler));
    }
}