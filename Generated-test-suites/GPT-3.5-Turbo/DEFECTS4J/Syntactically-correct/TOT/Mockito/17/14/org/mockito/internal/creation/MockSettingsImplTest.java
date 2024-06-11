import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        
        assertTrue(result.isSerializable());
    }
    

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfaces_withNoInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        assertNull(result.getExtraInterfaces());
    }
    
    @Test(expected = NullPointerException.class)
    public void testExtraInterfaces_withNullInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_withNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String expectedName = "mockObject";
        mockSettings.name(expectedName);
        
        assertEquals(expectedName, mockSettings.getMockName().getMockName());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                // custom logic
                return null;
            }
        };
        mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);
        
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String expectedName = "mockObject";
        mockSettings.name(expectedName);
        Class classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        
        assertEquals(expectedName, mockSettings.getMockName().getMockName());
        assertEquals(classToMock, mockSettings.getMockName().getMockedClass());
    }
    
}