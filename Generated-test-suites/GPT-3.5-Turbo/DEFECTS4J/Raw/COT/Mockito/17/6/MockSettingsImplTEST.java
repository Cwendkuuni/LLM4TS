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
        Class<?>[] extraInterfaces = { java.io.Serializable.class, Comparable.class };
        MockSettings result = settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = settings.getMockName();
        assertNull(mockName);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mockObject";
        MockSettings result = settings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Comparable.class, Cloneable.class };
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
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        settings.initiateMockName(classToMock);
        assertEquals(classToMock, settings.getMockName().getMockedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesDoesNotAcceptNullParameters() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(null, Cloneable.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultAnswerNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.defaultAnswer(null);
    }
}