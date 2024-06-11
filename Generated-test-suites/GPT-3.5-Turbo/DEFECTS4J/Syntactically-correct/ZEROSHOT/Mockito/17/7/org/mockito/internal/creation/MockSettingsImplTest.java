import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.junit.Assert.*;

public class MockSettingsImplTest {
    
    private MockSettingsImpl mockSettings;
    
    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }
    
    @Test
    public void testSerializable() {
        MockSettings result = mockSettings.serializable();
        assertTrue(result instanceof Serializable);
    }
    
    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNoInterfaces() {
        Class<?>[] extraInterfaces = {};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNullParameters() {
        Class<?>[] extraInterfaces = {null};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = {String.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
    }
    
    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }
    
    @Test
    public void testGetName() {
        String name = "test";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = AdditionalAnswers.returnsFirstArg();
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializableWithSerializableInterface() {
        Class<?>[] extraInterfaces = {Comparable.class, Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }
    
    @Test
    public void testIsSerializableWithoutSerializableInterface() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        String name = "test";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().toString());
    }
}