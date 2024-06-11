import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertEquals(mockSettings, mockSettings.serializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
        Reporter reporter = new Reporter();
        assertEquals(reporter, reporter.extraInterfacesRequiresAtLeastOneInterface());
    }

    @Test
    public void testGetMockName() {
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
        assertEquals(mockSettings, mockSettings.name("Mock Name"));
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        assertEquals(mockSettings, mockSettings.spiedInstance(spiedInstance));
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        assertEquals(mockSettings, mockSettings.defaultAnswer(defaultAnswer));
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
        mockSettings.initiateMockName(Object.class);
        MockName mockName = new MockName(null, Object.class);
        assertEquals(mockName, mockSettings.getMockName());
    }

}