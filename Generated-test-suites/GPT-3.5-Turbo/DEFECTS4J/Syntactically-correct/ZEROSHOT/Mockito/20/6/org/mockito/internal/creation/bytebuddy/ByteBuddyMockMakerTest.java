import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings mockCreationSettings;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        mockCreationSettings = mock(MockCreationSettings.class);
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() {
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(mockCreationSettings, mockHandler);
        } catch (MockitoException e) {
            // Verify that the exception message is expected
            verify(mockCreationSettings).getSerializableMode();
            verifyNoMoreInteractions(mockCreationSettings, mockHandler);
            return;
        }

        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Class<?> typeToMock = Object.class;
        Object mock = new Object();

        Object result = mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);

        assertEquals(typeToMock, result.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithInvalidType() {
        Class<?> typeToMock = String.class;
        Object mock = new Object();

        Object result = mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
    }

    @Test
    public void testDescribeClassWithNotNullType() {
        Class<?> type = Object.class;

        String result = mockMaker.describeClass(type);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithNullType() {
        Class<?> type = null;

        String result = mockMaker.describeClass(type);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithNotNullInstance() {
        Object instance = new Object();

        String result = mockMaker.describeClass(instance);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithNullInstance() {
        Object instance = null;

        String result = mockMaker.describeClass(instance);

        assertEquals("null", result);
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        Object mock = new Object();

        MockHandler result = mockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandlerWithValidMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        when(mock.mockitoInterceptor().getMockHandler()).thenReturn(mockHandler);

        MockHandler result = mockMaker.getHandler(mock);

        assertEquals(mockHandler, result);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);

        mockMaker.resetMock(mock, mockHandler, mockCreationSettings);

        verify(mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithInvalidMockHandler() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);

        mockMaker.resetMock(mock, mock(MockHandler.class), mockCreationSettings);
    }

    @Test
    public void testInitializeClassInstantiator() {
        globalConfiguration.enableClassCache();

        mockMaker.initializeClassInstantiator();
    }
}