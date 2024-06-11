import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettings result = settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, null};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, String.class};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertNotNull(settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings result = settings.name(name);
        assertEquals(name, settings.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = settings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class, java.io.Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        settings.initiateMockName(classToMock);
        assertNotNull(settings.getMockName());
    }
}