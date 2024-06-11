import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettingsImpl;

    @Before
    public void setUp() {
        mockSettingsImpl = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings result = mockSettingsImpl.serializable();
        assertNotNull(result);
        assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettings result = mockSettingsImpl.extraInterfaces(CharSequence.class, Comparable.class);
        assertNotNull(result);
        assertEquals(CharSequence.class, result.getExtraInterfaces()[0]);
        assertEquals(Comparable.class, result.getExtraInterfaces()[1]);
        
        // Test edge cases
        assertThrows(IllegalArgumentException.class, () -> {
            mockSettingsImpl.extraInterfaces(); // no interfaces provided
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            mockSettingsImpl.extraInterfaces(CharSequence.class, null); // null interface provided
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            mockSettingsImpl.extraInterfaces(String.class); // non-interface class provided
        });
    }

    @Test
    public void testGetMockName() {
        MockName result = mockSettingsImpl.getMockName();
        assertNull(result);
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] result = mockSettingsImpl.getExtraInterfaces();
        assertNull(result);
    }

    @Test
    public void testGetSpiedInstance() {
        Object result = mockSettingsImpl.getSpiedInstance();
        assertNull(result);
    }

    @Test
    public void testName() {
        MockSettings result = mockSettingsImpl.name("MockName");
        assertNotNull(result);
        assertEquals("MockName", result.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        Object instance = new Object();
        MockSettings result = mockSettingsImpl.spiedInstance(instance);
        assertNotNull(result);
        assertEquals(instance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = (inv) -> 42;
        MockSettings result = mockSettingsImpl.defaultAnswer(answer);
        assertNotNull(result);
        assertEquals(answer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        assertFalse(mockSettingsImpl.isSerializable());
        mockSettingsImpl.extraInterfaces(java.io.Serializable.class);
        assertTrue(mockSettingsImpl.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        mockSettingsImpl.name("MockName");
        Class classToMock = Object.class;
        mockSettingsImpl.initiateMockName(classToMock);
        assertEquals("MockName", mockSettingsImpl.getMockName().toString());
        assertEquals(classToMock, mockSettingsImpl.getMockName().getType());
    }
}