import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertNotNull(result);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertNotNull(result);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNoInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Comparable.class, null};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Comparable.class, String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.name("mockName");
        assertNotNull(result);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.spiedInstance(new Object());
        assertNotNull(result);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (inv) -> null;
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(String.class);
        assertNotNull(mockSettings.getMockName());
    }
}