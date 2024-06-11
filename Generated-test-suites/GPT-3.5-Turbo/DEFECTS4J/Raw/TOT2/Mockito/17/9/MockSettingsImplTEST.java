import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertTrue(result instanceof MockSettings);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Runnable.class, Comparable.class};
        MockSettings result = mockSettings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, result.getExtraInterfaces());
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
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().getMockName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnwer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> answer = invocation -> null;
        MockSettings result = mockSettings.defaultAnswer(answer);
        assertEquals(answer, result.getDefaultAnswer());
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
        mockSettings.initiateMockName(MockSettingsImpl.class);
        assertNotNull(mockSettings.getMockName());
    }

    @Test
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(null);
            fail("Test should fail when passing null extra interfaces");
        } catch (Reporter reporter) {
            // expected exception
        }
    }

    @Test
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces();
            fail("Test should fail when passing empty extra interfaces");
        } catch (Reporter reporter) {
            // expected exception
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(String.class);
            fail("Test should fail when passing a non-interface extra interface");
        } catch (Reporter reporter) {
            // expected exception
        }
    }
}