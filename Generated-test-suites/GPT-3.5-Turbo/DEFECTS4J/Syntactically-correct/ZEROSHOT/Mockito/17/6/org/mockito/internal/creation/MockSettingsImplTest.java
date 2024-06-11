import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

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
        mockSettings.extraInterfaces(MockSettings.class);
        Class[] result = mockSettings.getExtraInterfaces();
        assertEquals(MockSettings.class, result[0]);
    }

    @Test
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporter = new Reporter();
        Throwable exception = assertThrows(Exception.class, () -> {
            mockSettings.extraInterfaces(null);
        });

        assertEquals("Extra interfaces requires at least one interface", exception.getMessage());
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Reporter reporter = new Reporter();
        Throwable exception = assertThrows(Exception.class, () -> {
            mockSettings.extraInterfaces(MockSettingsImpl.class);
        });

        assertEquals("Extra interfaces accepts only interfaces", exception.getMessage());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName result = mockSettings.getMockName();
        assertNull(result);
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object result = mockSettings.getSpiedInstance();
        assertNull(result);
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.name("mockName");
        assertEquals("mockName", mockSettings.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer defaultAnswer = (invocation) -> null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);
        boolean result = mockSettings.isSerializable();
        assertTrue(result);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(MockSettingsImpl.class);
        MockName result = mockSettings.getMockName();
        assertNotNull(result);
        assertEquals("MockSettingsImpl", result.getFullyQualifiedMockName());
    }
}