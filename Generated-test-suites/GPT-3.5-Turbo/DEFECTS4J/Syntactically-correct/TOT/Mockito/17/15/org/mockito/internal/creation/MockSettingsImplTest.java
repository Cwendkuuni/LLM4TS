import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
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
        // Test if serializable() method returns the instance itself
        assertSame(mockSettings, mockSettings.serializable());
    }

    @Test
    public void testExtraInterfaces() {
        // Test if extraInterfaces() method sets the extraInterfaces correctly
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());

        // Test if extraInterfaces() method throws exception when null or empty array is passed
        try {
            mockSettings.extraInterfaces();
            fail("Expected Reporter exception");
        } catch (Reporter reporter) {
            // Exception expected, do nothing
        }

        try {
            Class<?>[] emptyArray = {};
            mockSettings.extraInterfaces(emptyArray);
            fail("Expected Reporter exception");
        } catch (Reporter reporter) {
            // Exception expected, do nothing
        }

        // Test if extraInterfaces() method throws exception when null is present in the array
        try {
            Class<?>[] invalidArray = {Comparable.class, null};
            mockSettings.extraInterfaces(invalidArray);
            fail("Expected Reporter exception");
        } catch (Reporter reporter) {
            // Exception expected, do nothing
        }

        // Test if extraInterfaces() method throws exception when a non-interface class is passed
        try {
            Class<?>[] invalidArray = {String.class};
            mockSettings.extraInterfaces(invalidArray);
            fail("Expected Reporter exception");
        } catch (Reporter reporter) {
            // Exception expected, do nothing
        }
    }

    @Test
    public void testGetMockName() {
        // Test if getMockName() method returns null when name is not set
        assertNull(mockSettings.getMockName());

        // Test if getMockName() method returns the correct MockName when name is set
        String name = "mockName";
        mockSettings.name(name);
        assertEquals(new MockName(name, null), mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        // Test if getExtraInterfaces() method returns null when extraInterfaces is not set
        assertNull(mockSettings.getExtraInterfaces());

        // Test if getExtraInterfaces() method returns the correct extraInterfaces when set
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        // Test if getSpiedInstance() method returns null when spiedInstance is not set
        assertNull(mockSettings.getSpiedInstance());

        // Test if getSpiedInstance() method returns the correct spiedInstance when set
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        // Test if name() method sets the name correctly
        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getMockName());

        // Test if name() method returns the instance itself
        assertSame(mockSettings, mockSettings.name(name));
    }

    @Test
    public void testSpiedInstance() {
        // Test if spiedInstance() method sets the spiedInstance correctly
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());

        // Test if spiedInstance() method returns the instance itself
        assertSame(mockSettings, mockSettings.spiedInstance(spiedInstance));
    }

    @Test
    public void testDefaultAnswer() {
        // Test if defaultAnswer() method sets the defaultAnswer correctly
        Answer<Object> defaultAnswer = (invocation) -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());

        // Test if defaultAnswer() method returns the instance itself
        assertSame(mockSettings, mockSettings.defaultAnswer(defaultAnswer));
    }

    @Test
    public void testIsSerializable() {
        // Test if isSerializable() method returns false when extraInterfaces is not set
        assertFalse(mockSettings.isSerializable());

        // Test if isSerializable() method returns true when extraInterfaces contains Serializable
        Class<?>[] extraInterfaces = {Comparable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());

        // Test if isSerializable() method returns false when extraInterfaces does not contain Serializable
        extraInterfaces = new Class<?>[]{Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        // Test if initiateMockName() method sets the correct MockName when name is not set
        Class classToMock = String.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(new MockName(null, classToMock), mockSettings.getMockName());

        // Test if initiateMockName() method sets the correct MockName when name is set
        String name = "mockName";
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(new MockName(name, classToMock), mockSettings.getMockName());
    }
}