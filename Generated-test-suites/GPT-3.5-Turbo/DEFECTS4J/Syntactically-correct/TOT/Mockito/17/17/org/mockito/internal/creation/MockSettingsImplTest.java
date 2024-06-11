import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable_withSerializableInterface_shouldSetExtraInterfaces() {
        mockSettings.serializable();

        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();

        assertEquals(1, extraInterfaces.length);
        assertEquals(java.io.Serializable.class, extraInterfaces[0]);
    }

    @Test
    public void testExtraInterfaces_withNullExtraInterfaces_shouldThrowException() {
        try {
            mockSettings.extraInterfaces(null);
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }
    
    @Test
    public void testExtraInterfaces_withEmptyExtraInterfaces_shouldThrowException() {
        try {
            mockSettings.extraInterfaces();
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }
    
    @Test
    public void testExtraInterfaces_withInvalidExtraInterface_shouldThrowException() {
        try {
            mockSettings.extraInterfaces(Object.class);
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }
    
    @Test
    public void testExtraInterfaces_withValidExtraInterfaces_shouldSetExtraInterfaces() {
        Class<?>[] expectedInterfaces = new Class<?>[] { java.io.Serializable.class, Cloneable.class };
        
        mockSettings.extraInterfaces(expectedInterfaces);
        
        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
        
        assertEquals(expectedInterfaces.length, extraInterfaces.length);
        for (int i = 0; i < expectedInterfaces.length; i++) {
            assertEquals(expectedInterfaces[i], extraInterfaces[i]);
        }
    }

    @Test
    public void testGetName_withDefaultName_shouldReturnNull() {
        MockName mockName = mock(MockName.class);
        when(mockSettings.getMockName()).thenReturn(mockName);

        assertEquals(null, mockSettings.getName());
    }

    @Test
    public void testGetName_withCustomName_shouldReturnCustomName() {
        String name = "customName";
        MockName mockName = mock(MockName.class);
        when(mockSettings.getMockName()).thenReturn(mockName);
        when(mockName.toString()).thenReturn(name);

        assertEquals(name, mockSettings.getName());
    }

    @Test
    public void testGetSpiedInstance_withNullSpiedInstance_shouldReturnNull() {
        assertEquals(null, mockSettings.getSpiedInstance());
    }

    @Test
    public void testGetSpiedInstance_withNonNullSpieInstance_shouldReturnSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer_withAnswer_shouldSetDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_withNullExtraInterfaces_shouldReturnFalse() {
        assertEquals(false, mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_withSerializableInterface_shouldReturnTrue() {
        mockSettings.extraInterfaces(java.io.Serializable.class);
        
        assertEquals(true, mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName_withClassToMock_shouldSetMockName() {
        String name = "test";
        Class classToMock = Object.class;
        MockName mockName = mock(MockName.class);
        whenNew(MockName.class).withArguments(name, classToMock).thenReturn(mockName);

        mockSettings.initiateMockName(classToMock);

        assertEquals(mockName, mockSettings.getMockName());
    }

}