import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {
    
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl serializableSettings = mockSettings.serializable();
        assertTrue(serializableSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] interfaces = new Class[]{Comparable.class};
        MockSettingsImpl extraInterfaceSettings = mockSettings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, extraInterfaceSettings.getExtraInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNull() {
        mockSettings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesContainsNull() {
        Class<?>[] interfaces = new Class[]{Comparable.class, null};
        mockSettings.extraInterfaces(interfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNotInterface() {
        Class<?>[] interfaces = new Class[]{String.class};
        mockSettings.extraInterfaces(interfaces);
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        MockSettingsImpl nameSettings = mockSettings.name(name);
        assertEquals(name, nameSettings.getMockName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl spiedInstanceSettings = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, spiedInstanceSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = (invocation -> null);
        MockSettingsImpl defaultAnswerSettings = mockSettings.defaultAnswer(answer);
        assertEquals(answer, defaultAnswerSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableFalse() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableTrue() {
        Class<?>[] interfaces = new Class[]{Comparable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(interfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().toString());
    }
}