import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {
    
    private MockSettingsImpl mockSettings;
    
    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }
    
    @Test
    public void testSerializable() {
        MockSettings result = mockSettings.serializable();
        assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }
    
    @Test
    public void testExtraInterfaces() {
        Class<?>[] interfaces = {Runnable.class};
        mockSettings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesNullCheck() {
        mockSettings.extraInterfaces(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        Class<?>[] interfaces = {String.class};
        mockSettings.extraInterfaces(interfaces);
    }
    
    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }
    
    @Test
    public void testGetName() {
        String name = "mock";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().toString());
    }
    
    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = (inv) -> null;
        mockSettings.defaultAnswer(answer);
        assertEquals(answer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        mockSettings.name("mock");
        mockSettings.initiateMockName(String.class);
        assertEquals("mock", mockSettings.getMockName().toString());
    }
}