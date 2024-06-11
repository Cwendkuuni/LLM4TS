import static org.junit.Assert.*;
import org.junit.Test;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertSame(mockSettings, mockSettings.serializable());
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Test with null extraInterfaces
        assertSame(mockSettings, mockSettings.extraInterfaces(null));
        assertNull(mockSettings.getExtraInterfaces());
        
        // Test with empty extraInterfaces
        assertSame(mockSettings, mockSettings.extraInterfaces());
        assertNull(mockSettings.getExtraInterfaces());
        
        // Test with single extraInterface
        Class<?> extraInterface = MyInterface.class;
        assertSame(mockSettings, mockSettings.extraInterfaces(extraInterface));
        assertArrayEquals(new Class<?>[] {extraInterface}, mockSettings.getExtraInterfaces());
        
        // Test with multiple extraInterfaces
        Class<?>[] extraInterfaces = {MyInterface1.class, MyInterface2.class};
        assertSame(mockSettings, mockSettings.extraInterfaces(extraInterfaces));
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
        
        // Test with null extraInterface
        assertThrows(ReporterException.class, () -> {
            mockSettings.extraInterfaces(nullExtraInterface);
        });
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());

        String name = "mockName";
        mockSettings.name(name);
        assertNotNull(mockSettings.getMockName());
        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());

        Object spiedInstance = new MySpiedInstance();
        assertSame(mockSettings, mockSettings.spiedInstance(spiedInstance));
        assertSame(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getDefaultAnswer());

        Answer<Object> defaultAnswer = new MyDefaultAnswer();
        assertSame(mockSettings, mockSettings.defaultAnswer(defaultAnswer));
        assertSame(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());

        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());

        Class<?> classToMock = MyClass.class;
        String name = "mockName";
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
        assertEquals(name, mockSettings.getMockName().toString());
    }
    
    // Helper classes/interfaces for testing
    private interface MyInterface {}

    private interface MyInterface1 {}

    private interface MyInterface2 {}

    private class MySpiedInstance {}

    private class MyDefaultAnswer implements Answer<Object> {
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    }

    private class MyClass {}
}