
import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.serializable();
        assertSame(settings, result);
        assertTrue(settings.isSerializable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_NullArray() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_NullElement() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_NonInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfaces_ValidInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] interfaces = {Comparable.class, Runnable.class};
        MockSettingsImpl result = settings.extraInterfaces(interfaces);
        assertSame(settings, result);
        assertArrayEquals(interfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertNull(settings.getMockName());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.name("mockName");
        assertEquals("mockName", settings.getMockName().toString());
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
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = settings.spiedInstance(spiedInstance);
        assertSame(settings, result);
        assertSame(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = null;
        MockSettingsImpl result = settings.defaultAnswer(defaultAnswer);
        assertSame(settings, result);
        assertSame(defaultAnswer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_NullExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testIsSerializable_SerializableExtraInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(java.io.Serializable.class);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class classToMock = Object.class;
        settings.initiateMockName(classToMock);
        MockName mockName = settings.getMockName();
        assertEquals(classToMock.getName(), mockName.toString());
    }

}
