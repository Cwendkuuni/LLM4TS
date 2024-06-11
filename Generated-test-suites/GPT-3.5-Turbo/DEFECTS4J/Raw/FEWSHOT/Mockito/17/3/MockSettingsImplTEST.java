import static org.junit.Assert.*;
import org.junit.Test;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Test with null extraInterfaces
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        
        // Test with empty extraInterfaces
        extraInterfaces = new Class<?>[]{};
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        
        // Test with valid extraInterfaces
        extraInterfaces = new Class<?>[]{Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        
        // Test with null interface in extraInterfaces
        extraInterfaces = new Class<?>[]{Comparable.class, null};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
        
        // Test with non-interface class in extraInterfaces
        extraInterfaces = new Class<?>[]{Comparable.class, String.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
        
        String name = "mockName";
        mockSettings.name(name);
        MockName mockName = mockSettings.getMockName();
        assertEquals(name, mockName.getName());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
        
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertSame(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getDefaultAnswer());
        
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        
        mockSettings.defaultAnswer(defaultAnswer);
        assertSame(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
        
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
        
        String name = "mockName";
        Class<?> classToMock = Object.class;
        
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        
        MockName mockName = mockSettings.getMockName();
        assertEquals(name, mockName.getName());
        assertEquals(classToMock, mockName.getType());
    }
}