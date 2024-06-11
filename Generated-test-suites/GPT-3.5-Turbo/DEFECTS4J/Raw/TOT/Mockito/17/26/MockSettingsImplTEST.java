import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        // Typical use case: One extra interface
        MockSettingsImpl mockSettings1 = new MockSettingsImpl();
        Class<?>[] extraInterfaces1 = {Comparable.class};
        mockSettings1.extraInterfaces(extraInterfaces1);

        assertArrayEquals(extraInterfaces1, mockSettings1.getExtraInterfaces());

        // Typical use case: Multiple extra interfaces
        MockSettingsImpl mockSettings2 = new MockSettingsImpl();
        Class<?>[] extraInterfaces2 = {Comparable.class, Cloneable.class};
        mockSettings2.extraInterfaces(extraInterfaces2);

        assertArrayEquals(extraInterfaces2, mockSettings2.getExtraInterfaces());

        // Error scenario: No extra interfaces provided
        MockSettingsImpl mockSettings3 = new MockSettingsImpl();
        try {
            mockSettings3.extraInterfaces();
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be thrown");
        } catch (Reporter reporter) {
            // Exception expected
        }

        // Error scenario: Null extra interface parameter
        MockSettingsImpl mockSettings4 = new MockSettingsImpl();
        Class<?>[] extraInterfaces4 = {Comparable.class, null};
        try {
            mockSettings4.extraInterfaces(extraInterfaces4);
            fail("Expected Reporter.extraInterfacesDoesNotAcceptNullParameters() to be thrown");
        } catch (Reporter reporter) {
            // Exception expected
        }

        // Error scenario: Non-interface class as extra interface
        MockSettingsImpl mockSettings5 = new MockSettingsImpl();
        Class<?>[] extraInterfaces5 = {Comparable.class, String.class};
        try {
            mockSettings5.extraInterfaces(extraInterfaces5);
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces() to be thrown");
        } catch (Reporter reporter) {
            // Exception expected
        }
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = mockSettings.getMockName();

        assertNull(mockName);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        mockSettings.name(name);

        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings1 = new MockSettingsImpl();
        Class<?>[] extraInterfaces1 = {Comparable.class, Cloneable.class};
        mockSettings1.extraInterfaces(extraInterfaces1);
        assertFalse(mockSettings1.isSerializable());

        MockSettingsImpl mockSettings2 = new MockSettingsImpl();
        Class<?>[] extraInterfaces2 = {java.io.Serializable.class};
        mockSettings2.extraInterfaces(extraInterfaces2);
        assertTrue(mockSettings2.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);

        assertEquals(classToMock, mockSettings.getMockName().getTypeToMock());
    }

}