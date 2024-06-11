import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.stubbing.*;
import org.mockito.exceptions.*;
import org.mockito.internal.util.*;

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
        
        MockSettings result = mockSettings.extraInterfaces(Cloneable.class, Comparable.class);
        
        Class<?>[] interfaces = result.getExtraInterfaces();
        assertEquals(2, interfaces.length);
        assertEquals(Cloneable.class, interfaces[0]);
        assertEquals(Comparable.class, interfaces[1]);
    }
    
    @Test
    public void testExtraInterfaces_nullParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        try {
            MockSettings result = mockSettings.extraInterfaces((Class<?>) null);
            fail("Expected ReporterException");
        } catch (ReporterException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testExtraInterfaces_emptyParameter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        try {
            MockSettings result = mockSettings.extraInterfaces();
            fail("Expected ReporterException");
        } catch (ReporterException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testExtraInterfaces_nonInterfaceClass() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        try {
            MockSettings result = mockSettings.extraInterfaces(String.class);
            fail("Expected ReporterException");
        } catch (ReporterException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        MockName result = mockSettings.getMockName();
        
        assertNull(result);
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        
        MockSettings result = mockSettings.name(name);
        
        assertEquals(name, result.getName());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Cloneable.class, Comparable.class};
        
        mockSettings.extraInterfaces(interfaces);
        
        assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable_true() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Cloneable.class, Comparable.class, java.io.Serializable.class};
        
        mockSettings.extraInterfaces(interfaces);
        
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testIsSerializable_false() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Cloneable.class, Comparable.class};
        
        mockSettings.extraInterfaces(interfaces);
        
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mock";
        Class<?> classToMock = Object.class;
        
        mockSettings.name(name);
        
        mockSettings.initiateMockName(classToMock);
        
        MockName mockName = mockSettings.getMockName();
        assertNotNull(mockName);
        assertEquals(name, mockName.toString());
        assertEquals(classToMock, mockName.getTypeToMock());
    }
    
}