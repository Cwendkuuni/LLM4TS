
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> mockSettings.extraInterfaces(null));
    }
    
    @Test
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> mockSettings.extraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertThrows(Reporter.class, () -> mockSettings.extraInterfaces(String.class));
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testIsSerializableTrue() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
    }
}
