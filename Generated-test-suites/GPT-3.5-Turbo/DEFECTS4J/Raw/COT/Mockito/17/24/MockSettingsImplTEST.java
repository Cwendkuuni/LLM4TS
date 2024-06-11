import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettingsImpl result = mockSettings.serializable();

        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Cloneable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesWithNullParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNoInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = new MockName("mockName", String.class);
        mockSettings.initiateMockName(String.class);

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testSetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);

        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testSetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testSetDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = invocation -> null;
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Cloneable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(mockSettings.isSerializable());
    }

}