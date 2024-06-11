package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.StringJoiner.join;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockWithNullSettingsAndHandler() {
        assertNull(new ByteBuddyMockMaker().createMock(null, null));
    }

    @Test
    public void testCreateMockWithNullHandler() {
        assertThrows(MockitoException.class, () -> new ByteBuddyMockMaker().createMock(new MockCreationSettings<>(), null));
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        assertThrows(NullPointerException.class, () -> new ByteBuddyMockMaker().createMock(new MockCreationSettings<>(), null));
    }

    @Test
    public void testCreateMockWithNullExtraInterfaces() {
        Class<?> mockType = MockitoTestUtils.mockType();
        assertThrows(NullPointerException.class, () -> new ByteBuddyMockMaker().createMock(new MockCreationSettings<>(mockType), null));
    }

    @Test
    public void testCreateMockWithNullSerializableMode() {
        Class<?> mockType = MockitoTestUtils.mockType();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        settings.setSerializableMode(null);
        assertThrows(NullPointerException.class, () -> new ByteBuddyMockMaker().createMock(settings, null));
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        Class<?> mockType = MockitoTestUtils.mockType();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        assertThrows(MockitoException.class, () -> new ByteBuddyMockMaker().createMock(settings, null));
    }

    @Test
    public void testCreateMock() throws InstantiationException, IllegalAccessException {
        Class<?> mockType = MockitoTestUtils.mockType();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        MockHandler handler = MockitoTestUtils.mockHandler();
        Object mock = new ByteBuddyMockMaker().createMock(settings, handler);
        assertNotNull(mock);
        assertSame(mockType, mock.getClass());
        assertSame(handler, new ByteBuddyMockMaker().getHandler(mock));
    }

    @Test
    public void testCreateMockWithClassCastException() {
        Class<?> mockType = MockitoTestUtils.mockType();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        Object mock = new ByteBuddyMockMaker().createMock(settings, null);
        assertThrows(ClassCastException.class, () -> mockType.cast(mock));
    }

    @Test
    public void testCreateMockWithInstantiationException() {
        Class<?> mockType = MockitoTestUtils.mockType();
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        assertThrows(org.mockito.internal.creation.instance.InstantiationException.class, () -> new ByteBuddyMockMaker().createMock(settings, null));
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockType = MockitoTestUtils.mockType();
        assertEquals("'org.mockito.test.TestType', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", new ByteBuddyMockMaker().describeClass(mockType));
        assertEquals("null", new ByteBuddyMockMaker().describeClass(null));
    }

    @Test
    public void testResetMock() {
        Class<?> mockType = MockitoTestUtils.mockType();
        Object mock = MockitoTestUtils.mock(mockType);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(mockType);
        MockHandler handler = MockitoTestUtils.mockHandler();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
        mockMaker.resetMock(mock, handler, settings);
        assertSame(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertNotNull(new ByteBuddyMockMaker().initializeClassInstantiator());
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = MockitoTestUtils.mockHandler();
        assertSame(handler, new ByteBuddyMockMaker().asInternalMockHandler(handler));
    }
}