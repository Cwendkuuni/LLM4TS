
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.*;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettingsImpl;

    @Before
    public void setUp() {
        mockSettingsImpl = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings result = mockSettingsImpl.serializable();
        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettings result = mockSettingsImpl.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesWithNull() {
        Reporter reporter = mock(Reporter.class);
        Class<?>[] extraInterfaces = null;
        
        mockSettingsImpl.extraInterfaces(extraInterfaces);
        
        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
    }
    
    @Test
    public void testExtraInterfacesWithInvalidInterface() {
        Reporter reporter = mock(Reporter.class);
        Class<?>[] extraInterfaces = {String.class};
        
        mockSettingsImpl.extraInterfaces(extraInterfaces);
        
        verify(reporter).extraInterfacesAcceptsOnlyInterfaces(String.class);
    }
    
    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        mockSettingsImpl.initiateMockName(MockSettingsImpl.class);
        
        assertEquals(mockName, mockSettingsImpl.getMockName());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettingsImpl.extraInterfaces(extraInterfaces);
        
        assertEquals(extraInterfaces, mockSettingsImpl.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettingsImpl.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettingsImpl.getSpiedInstance());
    }
    
    @Test
    public void testSetName() {
        String name = "testName";
        MockSettings result = mockSettingsImpl.name(name);
        
        assertEquals(name, mockSettingsImpl.getMockName().toString());
    }
    
    @Test
    public void testSetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettingsImpl.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, mockSettingsImpl.getSpiedInstance());
    }
    
    @Test
    public void testSetDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettingsImpl.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, mockSettingsImpl.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettingsImpl.extraInterfaces(extraInterfaces);
        
        assertTrue(mockSettingsImpl.isSerializable());
    }
}
