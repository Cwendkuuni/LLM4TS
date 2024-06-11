import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        
        Assert.assertEquals(mockSettings, result);
        // Verify that java.io.Serializable interface is added to extraInterfaces
        Class[] extraInterfaces = mockSettings.getExtraInterfaces();
        Assert.assertTrue(extraInterfaces.length == 1);
        Assert.assertEquals(java.io.Serializable.class, extraInterfaces[0]);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class[] interfaces = {MyInterface.class};
        MockSettings result = mockSettings.extraInterfaces(interfaces);
        
        Assert.assertEquals(mockSettings, result);
        // Verify that extraInterfaces are correctly set
        Class[] extraInterfaces = mockSettings.getExtraInterfaces();
        Assert.assertArrayEquals(interfaces, extraInterfaces);
    }

    @Test(expected = Exception.class)
    public void testExtraInterfacesRequiresAtLeastOneInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }

    @Test(expected = Exception.class)
    public void testExtraInterfacesDoesNotAcceptNullParameters() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class[] interfaces = {null};
        mockSettings.extraInterfaces(interfaces);
    }

    @Test(expected = Exception.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class[] interfaces = {MyClass.class};
        mockSettings.extraInterfaces(interfaces);
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings result = mockSettings.name(name);
        
        Assert.assertEquals(mockSettings, result);
        // Verify that name is correctly set
        Assert.assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        
        Assert.assertEquals(mockSettings, result);
        // Verify that spiedInstance is correctly set
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        
        Assert.assertEquals(mockSettings, result);
        // Verify that defaultAnswer is correctly set
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Verify that isSerializable returns false when extraInterfaces is null
        Assert.assertFalse(mockSettings.isSerializable());
        
        // Verify that isSerializable returns false when extraInterfaces does not contain java.io.Serializable
        Class[] interfaces = {MyInterface.class};
        mockSettings.extraInterfaces(interfaces);
        Assert.assertFalse(mockSettings.isSerializable());
        
        // Verify that isSerializable returns true when extraInterfaces contains java.io.Serializable
        Class[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class classToMock = MyClass.class;
        
        // Verify that mockName is null before initiateMockName is called
        Assert.assertNull(mockSettings.getMockName());
        
        // Verify that mockName is correctly initiated after initiateMockName is called
        mockSettings.initiateMockName(classToMock);
        Assert.assertNotNull(mockSettings.getMockName());
    }

    interface MyInterface {}
    
    class MyClass {}
}