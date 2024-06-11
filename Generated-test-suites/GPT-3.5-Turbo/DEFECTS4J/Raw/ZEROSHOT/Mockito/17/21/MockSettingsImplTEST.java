import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertTrue(result instanceof MockSettingsImpl);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<Reporter>[] extraInterfaces = new Class[]{Reporter.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(result instanceof MockSettingsImpl);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_nullInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_emptyInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_nonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        MockSettings result = mockSettings.name(name);
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = mock(Object.class);
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertTrue(result instanceof MockSettingsImpl);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
        mockSettings.extraInterfaces(Serializable.class);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<String> classToMock = String.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        assertNotNull(mockName);
        assertEquals(classToMock, mockName.getMockType());
    }
}