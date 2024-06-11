import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MockSettings result = settings.serializable();
        
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces_ValidInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MockSettings result = settings.extraInterfaces(Runnable.class, Comparable.class);
        
        assertThat(result.getExtraInterfaces(), containsInAnyOrder(Runnable.class, Comparable.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_NullInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces((Class<?>[]) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_EmptyInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_InvalidInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        settings.extraInterfaces(Integer.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MockName name = settings.getMockName();
        
        assertNull(name);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MockSettings result = settings.name("mockName");
        
        assertThat(result.getMockName().toString(), equalTo("mockName"));
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MockSettings result = settings.extraInterfaces(Runnable.class, Comparable.class);
        
        assertThat(result.getExtraInterfaces(), containsInAnyOrder(Runnable.class, Comparable.class));
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        
        MockSettings result = settings.spiedInstance(spiedInstance);
        
        assertThat(result.getSpiedInstance(), sameInstance(spiedInstance));
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        
        MockSettings result = settings.defaultAnswer(defaultAnswer);
        
        assertThat(result.getDefaultAnswer(), sameInstance(defaultAnswer));
    }

    @Test
    public void testIsSerializable_WithSerializableInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Runnable.class, Comparable.class, java.io.Serializable.class);
        
        boolean isSerializable = settings.isSerializable();
        
        assertTrue(isSerializable);
    }
    
    @Test
    public void testIsSerializable_WithoutSerializableInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Runnable.class, Comparable.class);
        
        boolean isSerializable = settings.isSerializable();
        
        assertFalse(isSerializable);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        
        settings.initiateMockName(classToMock);
        
        assertNotNull(settings.getMockName());
    }
}