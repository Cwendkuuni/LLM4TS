import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableSetting_ValidInput_ExpectSerializable() {
        mockSettings.serializable();

        assertTrue("Serializable setting not applied", mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfacesSetting_ValidInput_ExpectExtraInterfacesSet() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals("ExtraInterfaces setting not applied", extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesSetting_NullInput_ExpectException() {
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesSetting_NoInterfacesInput_ExpectException() {
        Class<?>[] extraInterfaces = {};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesSetting_NonInterfaceInput_ExpectException() {
        Class<?>[] extraInterfaces = {String.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testMockNameSetting_ValidInput_ExpectMockNameSet() {
        MockName mockName = new MockName("testMock");
        mockSettings.name("testMock");

        assertEquals("MockName setting not applied", mockName, mockSettings.getMockName());
    }

    @Test
    public void testSpiedInstanceSetting_ValidInput_ExpectSpiedInstanceSet() {
        Object spiedInstance = Mockito.spy(Object.class);
        mockSettings.spiedInstance(spiedInstance);

        assertEquals("SpiedInstance setting not applied", spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswerSetting_ValidInput_ExpectDefaultAnswerSet() {
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);

        assertEquals("DefaultAnswer setting not applied", defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_WithSerializableExtraInterface_ExpectTrue() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertTrue("isSerializable returned false for Serializable extra interface", mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_WithoutSerializableExtraInterface_ExpectFalse() {
        Class<?>[] extraInterfaces = {Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertFalse("isSerializable returned true for non-Serializable extra interface", mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName_ValidInput_ExpectMockNameInitialized() {
        String name = "testMock";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);

        mockSettings.initiateMockName(classToMock);

        MockName expectedMockName = new MockName(name, classToMock);
        assertEquals("MockName not initialized correctly", expectedMockName, mockSettings.getMockName());
    }
}