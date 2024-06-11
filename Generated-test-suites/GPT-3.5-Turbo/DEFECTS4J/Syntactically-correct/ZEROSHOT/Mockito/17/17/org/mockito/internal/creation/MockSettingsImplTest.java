import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        
        assertTrue(result instanceof MockSettingsImpl);
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        assertTrue(result instanceof MockSettingsImpl);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNullCheck() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = null;
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        fail("Should throw RuntimeException");
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesEmptyCheck() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        fail("Should throw RuntimeException");
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNullParameterCheck() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {null};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        fail("Should throw RuntimeException");
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNonInterfaceCheck() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        
        fail("Should throw RuntimeException");
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = new MockName("mockName", String.class);
        mockSettings.initiateMockName(String.class);
        
        assertEquals(mockName, mockSettings.getMockName());
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings result = mockSettings.name(name);
        
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(name, mockSettings.getMockName().getMockName());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
}