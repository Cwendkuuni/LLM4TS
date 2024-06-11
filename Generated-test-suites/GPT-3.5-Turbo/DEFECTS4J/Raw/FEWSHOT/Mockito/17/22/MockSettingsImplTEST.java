import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.serializable();
        assertNotNull(result);
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Runnable.class };
        MockSettingsImpl result = settings.extraInterfaces(extraInterfaces);
        assertNotNull(result);
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertNull(settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertNull(settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertNull(settings.getSpiedInstance());
    }

    @Test
    public void testSetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mock";
        MockSettingsImpl result = settings.name(name);
        assertNotNull(result);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testSetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = settings.spiedInstance(spiedInstance);
        assertNotNull(result);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testSetDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.defaultAnswer(null);
        assertNotNull(result);
        assertEquals(null, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(java.io.Serializable.class);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(Object.class);
        assertNotNull(settings.getMockName());
    }
}