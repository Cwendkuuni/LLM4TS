import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockSettingsImplTest {
    
    private MockSettingsImpl mockSettingsImpl;
    
    @Before
    public void setUp() {
        mockSettingsImpl = new MockSettingsImpl();
    }
    
    @Test
    public void testSerializable() {
        assertEquals(mockSettingsImpl, mockSettingsImpl.serializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {TestClass.class};
        assertEquals(mockSettingsImpl, mockSettingsImpl.extraInterfaces(extraInterfaces));
        assertArrayEquals(extraInterfaces, mockSettingsImpl.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesNull() {
        try {
            mockSettingsImpl.extraInterfaces(null);
            fail("Expected exception to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Extra interfaces require at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void testExtraInterfacesEmpty() {
        try {
            Class<?>[] extraInterfaces = {};
            mockSettingsImpl.extraInterfaces(extraInterfaces);
            fail("Expected exception to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Extra interfaces require at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void testExtraInterfacesNotInterface() {
        try {
            Class<?>[] extraInterfaces = {String.class};
            mockSettingsImpl.extraInterfaces(extraInterfaces);
            fail("Expected exception to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Extra interfaces only accept interfaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetMockName() {
        assertNull(mockSettingsImpl.getMockName());
    }
    
    @Test
    public void testGetName() {
        String name = "mockName";
        assertEquals(mockSettingsImpl, mockSettingsImpl.name(name));
        assertEquals(name, mockSettingsImpl.getMockName().toString());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        assertNull(mockSettingsImpl.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettingsImpl.getSpiedInstance());
    }
    
    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        assertEquals(mockSettingsImpl, mockSettingsImpl.spiedInstance(spiedInstance));
        assertEquals(spiedInstance, mockSettingsImpl.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        assertEquals(mockSettingsImpl, mockSettingsImpl.defaultAnswer(defaultAnswer));
        assertEquals(defaultAnswer, mockSettingsImpl.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        assertFalse(mockSettingsImpl.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        // TODO: test implementation
    }
}