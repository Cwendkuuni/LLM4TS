import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    // Test method for serializable()
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettings result = settings.serializable();
        
        assertTrue(result.isSerializable());
    }

    // Test method for extraInterfaces(Class<?>... extraInterfaces)
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Runnable.class, Comparable.class };
        MockSettings result = settings.extraInterfaces(extraInterfaces);
        
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    // Test method for extraInterfaces(Class<?>... extraInterfaces) with no interfaces specified
    @Test(expected = ReporterException.class)
    public void testExtraInterfacesNoInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces();
    }

    // Test method for extraInterfaces(Class<?>... extraInterfaces) with null interface
    @Test(expected = ReporterException.class)
    public void testExtraInterfacesNullInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Runnable.class, null };
        settings.extraInterfaces(extraInterfaces);
    }

    // Test method for extraInterfaces(Class<?>... extraInterfaces) with non-interface class
    @Test(expected = ReporterException.class)
    public void testExtraInterfacesNonInterfaceClass() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Runnable.class, String.class };
        settings.extraInterfaces(extraInterfaces);
    }

    // Test method for getMockName()
    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = new MockName("mock", Object.class);
        settings.initiateMockName(Object.class);
        MockName result = settings.getMockName();
        
        assertEquals(mockName, result);
    }

    // Test method for getExtraInterfaces()
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = { Runnable.class, Comparable.class };
        settings.extraInterfaces(extraInterfaces);
        Class<?>[] result = settings.getExtraInterfaces();
        
        assertArrayEquals(extraInterfaces, result);
    }

    // Test method for getSpiedInstance()
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        Object result = settings.getSpiedInstance();
        
        assertEquals(spiedInstance, result);
    }

    // Test method for name(String name)
    @Test
    public void testName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mock";
        MockSettings result = settings.name(name);
        
        assertEquals(name, result.getMockName().toString());
    }

    // Test method for spiedInstance(Object spiedInstance)
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = settings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    // Test method for defaultAnswer(Answer defaultAnswer)
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    // Test method for isSerializable()
    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(java.io.Serializable.class);
        
        assertTrue(settings.isSerializable());
    }

    // Test method for initiateMockName(Class classToMock)
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class classToMock = Object.class;
        settings.initiateMockName(classToMock);
        
        assertEquals(classToMock, settings.getMockName().getMockedType());
    }
}