import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
     
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException was not thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);

        try {
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException was not thrown");
        } catch (ClassCastException e) {
            // Test successful
        }
    }

    @Test
    public void testDescribeClass() {
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", ByteBuddyMockMaker.describeClass(Object.class));
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);
        
        try {
            mockMaker.resetMock(mock, newHandler, settings);
        } catch (MockitoException e) {
            fail("Unexpected MockitoException thrown");
        }
    }
}