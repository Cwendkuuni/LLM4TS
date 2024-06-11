import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.serializable();
        
        assertTrue(settings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces(MyInterface.class);
        
        Class<?>[] extraInterfaces = settings.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertEquals(1, extraInterfaces.length);
        assertEquals(MyInterface.class, extraInterfaces[0]);
    }
    
    @Test(expected = ReporterException.class)
    public void testExtraInterfacesWithNullParameter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces((Class<?>[]) null);
    }
    
    @Test
    public void testExtraInterfacesWithNoInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces(MyClass.class);
        
        Class<?>[] extraInterfaces = settings.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertEquals(0, extraInterfaces.length);
    }
    
    @Test(expected = ReporterException.class)
    public void testExtraInterfacesWithNonInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces(MyClass.class);
    }
    
    @Test
    public void testMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.name("mockName");
        
        MockName mockName = settings.getMockName();
        assertNotNull(mockName);
        assertEquals("mockName", mockName.toString());
    }
    
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        Object spiedInstance = new MyClass();
        settings.spiedInstance(spiedInstance);
        
        Object actualInstance = settings.getSpiedInstance();
        assertNotNull(actualInstance);
        assertEquals(spiedInstance, actualInstance);
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        Answer<Object> defaultAnswer = mock(Answer.class);
        settings.defaultAnswer(defaultAnswer);
        
        Answer<Object> actualAnswer = settings.getDefaultAnswer();
        assertNotNull(actualAnswer);
        assertEquals(defaultAnswer, actualAnswer);
    }
    
    @Test
    public void testIsSerializableWithExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(MyInterface.class);
        
        assertTrue(settings.isSerializable());
    }
    
    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        assertFalse(settings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.initiateMockName(MyClass.class);
        
        MockName mockName = settings.getMockName();
        assertNotNull(mockName);
        assertEquals(MyClass.class, mockName.getClassToMock());
    }
}