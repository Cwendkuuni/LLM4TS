import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_successful() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.createMock(new MockCreationSettings<String>(String.class), new MockHandler()));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_serializableModeAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(new MockCreationSettings<String>(String.class, SerializableMode.ACROSS_CLASSLOADERS), new MockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_classCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(new MockCreationSettings<String>(String.class), new MockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_instantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(new MockCreationSettings<String>(ByteBuddyMockMaker.class), new MockHandler());
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.ensureMockIsAssignableToMockedType(new MockCreationSettings<String>(String.class), "mockedString"));
    }

    @Test
    public void testDescribeClass_typeNotNull() {
        assertEquals("'java.lang.String', loaded by classloader : 'null'", ByteBuddyMockMaker.describeClass(String.class));
    }

    @Test
    public void testDescribeClass_typeNull() {
        assertEquals("null", ByteBuddyMockMaker.describeClass((Class) null));
    }

    @Test
    public void testDescribeClass_instanceNotNull() {
        assertEquals("'java.lang.String', loaded by classloader : 'null'", ByteBuddyMockMaker.describeClass("mockedString"));
    }

    @Test
    public void testDescribeClass_instanceNull() {
        assertEquals("null", ByteBuddyMockMaker.describeClass((Object) null));
    }

    @Test(expected = MockitoException.class)
    public void testGetHandler_nonMockMethodInterceptor() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.getHandler("nonMockMethodInterceptor");
    }

    @Test
    public void testGetHandler_mockMethodInterceptor() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        assertNotNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockCreationSettings settings = new MockCreationSettings(ByteBuddyMockMaker.class);
        MockHandler newHandler = new MockHandler();
        mockMaker.resetMock(mock, newHandler, settings);
    }
}