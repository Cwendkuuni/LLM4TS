import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = null;
        MockCreationSettings<Object> mockCreationSettings = new GlobalConfiguration().getDefaultMockingSettings();
        try {
            mockMaker.createMock(mockCreationSettings, mockHandler);
            fail("Expected MockitoException was not thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> mockCreationSettings = new GlobalConfiguration<Object>().getDefaultMockingSettings();
        Object mock = new Object();
        try {
            mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
            fail("Expected ClassCastException was not thrown");
        } catch (ClassCastException e) {
            assertEquals("java.lang.Object cannot be cast to java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testDescribeClass() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertEquals("'java.lang.Class', loaded by classloader : 'null'", mockMaker.describeClass(Class.class));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler mockHandler = null;
        MockCreationSettings<Object> mockCreationSettings = new GlobalConfiguration<Object>().getDefaultMockingSettings();
        try {
            mockMaker.resetMock(mock, mockHandler, mockCreationSettings);
            fail("Expected ClassCastException was not thrown");
        } catch (ClassCastException e) {
            assertEquals("'java.lang.Object' cannot be cast to class 'org.mockito.internal.InternalMockHandler'", e.getMessage());
        }
    }
}