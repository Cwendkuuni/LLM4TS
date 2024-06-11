import org.junit.Test;
import org.mockito.ArgumentCapture;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockName.MOCK_NAME_PREFIX;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();
        assertEquals(settings, settings.serializable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNulls() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfacesWithInterfaceClasses() {
        Class<?>[] extraInterfaces = new Class[] { Serializable.class, Runnable.class };
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        String name = "TestMock";
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.name(name);
        MockName mockName = settings.getMockName();
        assertEquals(name, mockName.name);
    }

    // Test cases for other methods with similar structure

    @Test
    public void testIsSerializableWithSerializableInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Serializable.class);
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutSerializableInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());
    }

    @Test(expected = NullPointerException.class)
    public void testNameAndSpiedInstanceNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.name(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSpiedInstanceNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.spiedInstance(null);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = mock(Answer.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.defaultAnswer(answer);
        assertEquals(answer, settings.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockName() {
        Class classToMock = Mockito.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(classToMock);
        MockName mockName = settings.getMockName();
        assertEquals(MOCK_NAME_PREFIX + classToMock.getName(), mockName.name);
    }
}