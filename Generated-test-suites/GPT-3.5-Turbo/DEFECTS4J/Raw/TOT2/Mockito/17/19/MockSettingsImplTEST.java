import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = mockSettings.serializable();
        assertNotNull(result);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        
        // Test with null interfaces
        reporter.extraInterfacesRequiresAtLeastOneInterface();
        
        // Test with null interface
        reporter.extraInterfacesDoesNotAcceptNullParameters();
        
        // Test with non-interface class
        reporter.extraInterfacesAcceptsOnlyInterfaces(String.class);

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
        String name = "TestName";
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
        Answer<Object> answer = (invocation) -> null;
        MockSettingsImpl result = mockSettings.defaultAnswer(answer);
        assertEquals(answer, result.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("TestName");
        mockSettings.initiateMockName(String.class);
        MockName mockName = mockSettings.getMockName();
        assertNotNull(mockName);
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
        
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

}