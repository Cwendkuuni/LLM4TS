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
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(String.class, Integer.class);

        assertTrue(result instanceof MockSettingsImpl);
        assertArrayEquals(new Class[]{String.class, Integer.class}, mockSettings.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = new MockName("testName", Object.class);
        mockSettings.initiateMockName(Object.class);

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "testName";
        mockSettings.name(name);

        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class[] extraInterfaces = {String.class, Integer.class};
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
        mockSettings.extraInterfaces(java.io.Serializable.class);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        
        assertNull(mockSettings.getMockName().toString());
    }
}