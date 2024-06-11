import org.junit.Test;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.serializable();
        
        assertNotNull(result);
        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces_withValidInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.extraInterfaces(Runnable.class, Comparable.class);
        
        assertNotNull(result);
        assertArrayEquals(new Class<?>[]{Runnable.class, Comparable.class}, result.getExtraInterfaces());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_withNoInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_withNullInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces((Class<?>[]) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_withNonInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.name("mockName");
        MockName result = settings.getMockName();
        
        assertNotNull(result);
        assertEquals("mockName", result.toString());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Cloneable.class, Serializable.class);
        Class<?>[] result = settings.getExtraInterfaces();
        
        assertNotNull(result);
        assertArrayEquals(new Class<?>[]{Cloneable.class, Serializable.class}, result);
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object obj = new Object();
        settings.spiedInstance(obj);
        Object result = settings.getSpiedInstance();
        
        assertNotNull(result);
        assertSame(obj, result);
    }
    
    @Test
    public void testName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.name("mockName");
        
        assertNotNull(result);
        assertEquals("mockName", result.getMockName().toString());
    }
    
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object obj = new Object();
        MockSettingsImpl result = settings.spiedInstance(obj);
        
        assertNotNull(result);
        assertSame(obj, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> answer = mock(Answer.class);
        MockSettingsImpl result = settings.defaultAnswer(answer);
        
        assertNotNull(result);
        assertSame(answer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_withSerializableExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Serializable.class);
        
        assertTrue(settings.isSerializable());
    }
    
    @Test
    public void testIsSerializable_withNoExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        assertFalse(settings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(String.class);
        
        assertNotNull(settings.getMockName());
        assertEquals("String", settings.getMockName().toString());
    }
}