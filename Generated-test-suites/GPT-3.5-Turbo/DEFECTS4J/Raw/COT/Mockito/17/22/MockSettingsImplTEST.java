
import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockSettingsImplTest {

    @Test
    public void testMockSettingsSerializable() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        MockSettings result = mockSettings.serializable();
        
        // then
        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testMockSettingsExtraInterfacesNull() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        MockSettings result = mockSettings.extraInterfaces(null);
        
        // then
        assertNull(result.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsExtraInterfacesNoInterface() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        MockSettings result = mockSettings.extraInterfaces(String.class);
        
        // then
        assertNull(result.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsExtraInterfacesOneInterface() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        MockSettings result = mockSettings.extraInterfaces(Serializable.class);
        
        // then
        assertArrayEquals(new Class<?>[] {Serializable.class}, result.getExtraInterfaces());
    }

    @Test
    public void testMockSettingsExtraInterfacesMultipleInterfaces() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        MockSettings result = mockSettings.extraInterfaces(Serializable.class, Cloneable.class);
        
        // then
        assertArrayEquals(new Class<?>[] {Serializable.class, Cloneable.class}, result.getExtraInterfaces());
    }
    
    @Test
    public void testMockSettingsGetName() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("mock");
        
        // when
        MockName result = mockSettings.getMockName();
        
        // then
        assertEquals("mock", result.name());
    }
    
    @Test
    public void testMockSettingsGetSpiedInstance() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        
        // when
        Object result = mockSettings.getSpiedInstance();
        
        // then
        assertEquals(spiedInstance, result);
    }
    
    @Test
    public void testMockSettingsDefaultAnswer() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);
        
        // when
        Answer<Object> result = mockSettings.getDefaultAnswer();
        
        // then
        assertEquals(defaultAnswer, result);
    }
    
    @Test
    public void testMockSettingsIsSerializableFalse() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // when
        boolean result = mockSettings.isSerializable();
        
        // then
        assertFalse(result);
    }
    
    @Test
    public void testMockSettingsIsSerializableTrue() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(Serializable.class);
        
        // when
        boolean result = mockSettings.isSerializable();
        
        // then
        assertTrue(result);
    }
    
    @Test
    public void testMockSettingsInitiateMockName() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        
        // when
        mockSettings.initiateMockName(classToMock);
        
        // then
        MockName result = mockSettings.getMockName();
        assertNotNull(result);
        assertEquals(classToMock, result.getMockType());
    }
}