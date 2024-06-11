
package org.junit.test; // Replace with your test package

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;
    private Answer<Object> answer;
    private Class<?>[] extraInterfaces;

    @Before
    public void setUp() {
        answer = Mockito.answer(invocation -> "Answer");
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        mockSettings = mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());

        extraInterfaces = new Class[]{java.io.Serializable.class};
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());

        Class<?>[] expectedExtraInterfaces = new Class[]{java.io.Serializable.class};
        assertArrayEquals(expectedExtraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testEmptyExtraInterfaces() {
        mockSettings = mockSettings.extraInterfaces((Class[]) null);
    }

    @Test(expected = Reporter.class)
    public void testNullExtraInterface() {
        Class<?> nullClass = null;
        mockSettings = mockSettings.extraInterfaces(new Class[]{nullClass});
    }

    @Test(expected = Reporter.class)
    public void testNonInterfaceExtraInterface() {
        Class<?> nonInterfaceClass = Mockito.class;
        mockSettings = mockSettings.extraInterfaces(new Class[]{nonInterfaceClass});
    }

    @Test
    public void testDefaultAnswer() {
        mockSettings = mockSettings.defaultAnswer(answer);
        Object defaultAnswer = mockSettings.getDefaultAnswer();
        assertSame(answer, defaultAnswer);
    }

    @Test
    public void testName() {
        String name = "MockName";
        mockSettings = mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testIsSerializable() {
        mockSettings = mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());

        extraInterfaces = new Class[]{java.io.Serializable.class};
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());

        extraInterfaces = new Class[0];
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());

        extraInterfaces = new Class[]{new Object()};
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> clazzToMock = Mockito.class;
        mockSettings = mockSettings; // Initialize mockSettings first
        mockSettings.initiateMockName(clazzToMock);
        assertNotNull(mockSettings.getMockName());
    }
}
