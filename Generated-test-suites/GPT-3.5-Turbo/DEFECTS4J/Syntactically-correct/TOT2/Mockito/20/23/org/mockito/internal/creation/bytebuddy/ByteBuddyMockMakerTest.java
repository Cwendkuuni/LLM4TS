import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // Test serialization mode across classloaders exception
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Test ensureMockIsAssignableToMockedType method
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = new Object();
        try {
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("java.lang.Object cannot be cast to class MockCreationSettings", e.getMessage());
        }
    }

    @Test
    public void testDescribeClass() {
        // Test describeClass method with null input
        String description = ByteBuddyMockMaker.describeClass(null);
        assertEquals("null", description);
        
        // Test describeClass method with non-null input
        Class<?> clazz = String.class;
        description = ByteBuddyMockMaker.describeClass(clazz);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testGetHandler() {
        // Test getHandler method for non-MockMethodInterceptor.MockAccess object
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        assertNull(mockMaker.getHandler(mock));
        
        // Test getHandler method for MockMethodInterceptor.MockAccess object
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        mock = mockAccess;
        MockHandler handler = mock(MockHandler.class);
        when(mockAccess.mockitoInterceptor.getMockHandler()).thenReturn(handler);
        assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        // Test resetMock method
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        Object mock = mockAccess;
        mockMaker.resetMock(mock, newHandler, settings);
        verify(mockAccess).setMockitoInterceptor(any());
    }
}