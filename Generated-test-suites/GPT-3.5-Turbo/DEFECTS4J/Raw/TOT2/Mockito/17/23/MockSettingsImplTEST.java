import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings settings = mockSettings.serializable();
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] interfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NullInput() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NullInterface() {
        Class<?>[] interfaces = {Comparable.class, null};
        mockSettings.extraInterfaces(interfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NonInterfaceInput() {
        Class<?>[] interfaces = {Comparable.class, String.class};
        mockSettings.extraInterfaces(interfaces);
    }

    @Test
    public void testGetMockName() {
        mockSettings.name("mockName");
        assertEquals("mockName", mockSettings.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = (invocation) -> "Mockito";
        mockSettings.defaultAnswer(answer);
        assertEquals(answer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockName() {
        mockSettings.initiateMockName(String.class);
        assertEquals("String", mockSettings.getMockName().toString());
    }
}