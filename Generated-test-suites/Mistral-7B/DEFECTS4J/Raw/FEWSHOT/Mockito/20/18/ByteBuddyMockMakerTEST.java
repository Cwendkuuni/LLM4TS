package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Test
    public void testCreateMock() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();

        MockHandler handler = mock(MockHandler.class);

        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);

        byteBuddyMockMaker = new ByteBuddyMockMaker();

        settings = new MockCreationSettings.Builder()
                .forClass(String[].class)
                .serializableMode(SerialiableMode.ACROSS_CLASSLOADERS) // Unsupported
                .build();

        try {
            mock = byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException when creating mock with unsupported serializable mode.");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testGetHandler() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = mock(Object.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().build());

        MockHandler oldHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(oldHandler);

        MockHandler newHandlerCheck = byteBuddyMockMaker.getHandler(mock);
        assertSame(newHandler, newHandlerCheck);
    }

    @Test
    public void testDescribeClass() {
        assertNotNull(ByteBuddyMockMaker.describeClass(Object.class));
        assertNotNull(ByteBuddyMockMaker.describeClass(String.class));
        assertNull(ByteBuddyMockMaker.describeClass(null));
    }
}