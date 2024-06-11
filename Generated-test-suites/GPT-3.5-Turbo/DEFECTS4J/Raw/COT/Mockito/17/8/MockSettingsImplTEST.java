
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesRequiresAtLeastOneInterface() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesDoesNotAcceptNullParameters() {
        mockSettings.extraInterfaces((Class<?>) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        mockSettings.extraInterfaces(MockSettingsImpl.class);
    }

    @Test
    public void testMockName() {
        MockName mockName = new MockName("test", MockSettingsImpl.class);
        mockSettings.name("test");

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockName mockName = new MockName(null, MockSettingsImpl.class);
        mockSettings.initiateMockName(MockSettingsImpl.class);

        assertEquals(mockName, mockSettings.getMockName());
    }
}
