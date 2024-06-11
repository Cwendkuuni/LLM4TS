import org.junit.Assert.*; // for assertions
import org.junit.Test; // for test annotations
import org.mockito.ArgumentMatchers; // for argument matchers
import org.mockito.Mockito; // for mockito setup
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter; // for custom reporter

public class MockSettingsImplTest {

    @Test
    public void testSerializableMethod() {
        // Test when extraInterfaces is not null and contains Serializable
        MockSettingsImpl settings = new MockSettingsImpl().serializable();
        assertTrue(settings.isSerializable());

        // Test when extraInterfaces is null
        MockSettingsImpl settingsNull = new MockSettingsImpl();
        try {
            settingsNull.serializable();
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(Reporter.class.getName(), e.getCause().getClass().getName());
        }

        // Test when extraInterfaces contains a non-interface
        Class<?>[] extraInterfaces = new Class[] {String.class};
        try {
            MockSettingsImpl settingsNonInterface = new MockSettingsImpl().extraInterfaces(extraInterfaces);
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(Reporter.class.getName(), e.getCause().getClass().getName());
        }

        // Test when extraInterfaces is empty
        try {
            MockSettingsImpl settingsEmpty = new MockSettingsImpl().extraInterfaces(new Class[0]);
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(Reporter.class.getName(), e.getCause().getClass().getName());
        }
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class[] {String.class, Integer.class};
        MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
        assertArrayEquals(new Class[] {String.class, Integer.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testNameMethod() {
        String name = "MyMock";
        MockSettingsImpl settings = new MockSettingsImpl().name(name);
        assertEquals(name, settings.getName());
    }

    @Test
    public void testSpiedInstanceMethod() {
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswerMethod() {
        Answer<Object> answer = Mockito.mock(Answer.class);
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(answer);
        assertEquals(answer, settings.getDefaultAnswer());
    }

    // Other test methods for other methods in the MockSettingsImpl class

    @Test
    public void testInitiateMockNameMethod() {
        Class<?> classToMock = Mockito.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(classToMock);
        MockName mockName = settings.getMockName();
        assertNotNull(mockName);
        assertEquals("MyMock", mockName.getName());
        assertEquals(MockSettingsImpl.class, mockName.getSettingsClass());
        assertEquals(classToMock, mockName.getMockType());
    }

    @Test
    public void testIsSerializableMethod() {
        MockSettingsImpl settings = new MockSettingsImpl();
        assertFalse(settings.isSerializable());

        MockSettingsImpl serializableSettings = new MockSettingsImpl().serializable();
        assertTrue(serializableSettings.isSerializable());
    }
}