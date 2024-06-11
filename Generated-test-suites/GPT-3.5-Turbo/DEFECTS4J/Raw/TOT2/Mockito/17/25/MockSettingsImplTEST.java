
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.serializable();
        assertNotNull(result);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] interfaces = { Comparable.class, Cloneable.class };
        MockSettings result = settings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, settings.getExtraInterfaces());
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
        String testName = "testName";
        MockSettings result = settings.name(testName);
        assertEquals(testName, settings.getMockName().get());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object instance = new Object();
        MockSettings result = settings.spiedInstance(instance);
        assertEquals(instance, settings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> answer = (invocation) -> null;
        MockSettings result = settings.defaultAnswer(answer);
        assertEquals(answer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String testName = "testName";
        settings.name(testName);
        settings.initiateMockName(String.class);
        assertEquals(testName, settings.getMockName().get());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesWithNoInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] interfaces = { Object.class };
        settings.extraInterfaces(interfaces);
    }
}
