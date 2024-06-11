import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableMockSettings() {
        // Serializable mock settings
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());

        // Test with null extra interfaces
        mockSettings.extraInterfaces(null);
        new Reporter().extraInterfacesRequiresAtLeastOneInterface();

        // Test with non-interface extra interfaces
        mockSettings.extraInterfaces(Object.class);
        new Reporter().extraInterfacesAcceptsOnlyInterfaces(Object.class);

        // Test with empty extra interfaces
        mockSettings.extraInterfaces(new Class[0]);
        new Reporter().extraInterfacesRequiresAtLeastOneInterface();

        // Test with valid extra interfaces
        Class<?>[] extraInterfaces = {java.io.Serializable.class, String.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = Mockito.answer(new Object());
        mockSettings.defaultAnswer(defaultAnswer);
        Object answer = mockSettings.getDefaultAnswer();
        assertEquals(defaultAnswer, answer);

        // Test with null default answer
        mockSettings.defaultAnswer(null);
        assertNull(mockSettings.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Mockito.class;
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
    }
}