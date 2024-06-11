package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.exceptions.base.MockitoException.classCastError;
import static org.mockito.internal.util.StringJoiner.join;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, null);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(settings, null);
        Class<?> clazz = mock.getClass();
        throw classCastError(join(
                "ClassCastException occurred while creating the mockito mock :",
                "  class to mock : ",
                describeClass(settings.getTypeToMock()),
                "  created class : ",
                describeClass(clazz),
                "  proxy instance class : ",
                describeClass(Object.class),
                "  instance creation by : ",
                ByteBuddyMockMaker.class.getSimpleName()
        ));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        throw new MockitoException("Unable to create mock instance of type '" + Object.class.getSuperclass().getSimpleName() + "'", new InstantiationException());
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().build(), null);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().build(), null);
        MockHandler handler = Mockito.mock(MockHandler.class);
        mockMaker.resetMock(mock, handler, null);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        assertNull(mockAccess.getMockitoInterceptor());
        assertNotNull(mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testDescribeClass() {
        assertNull(ByteBuddyMockMaker.describeClass(null));
        assertNull(ByteBuddyMockMaker.describeClass(String.class));
        assertNull(ByteBuddyMockMaker.describeClass(Integer.class));
        assertNull(ByteBuddyMockMaker.describeClass(Object.class));
        assertNull(ByteBuddyMockMaker.describeClass(ByteBuddyMockMaker.class));
    }
}