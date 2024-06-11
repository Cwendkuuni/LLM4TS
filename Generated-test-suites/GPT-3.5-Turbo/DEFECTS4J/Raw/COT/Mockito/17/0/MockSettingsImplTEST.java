
import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesInvalid() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
    }
    
    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }
    
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertSame(spiedInstance, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertSame(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testIsSerializableTrue() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
    }
    
}

