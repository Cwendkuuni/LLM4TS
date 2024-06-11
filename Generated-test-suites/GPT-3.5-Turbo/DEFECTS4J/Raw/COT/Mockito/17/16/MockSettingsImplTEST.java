
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = mockSettings.serializable();
        
        assertTrue(settings.isSerializable());
        assertArrayEquals(new Class<?>[]{java.io.Serializable.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = mockSettings.extraInterfaces(
                Runnable.class,
                Comparable.class
        );
        
        assertArrayEquals(new Class<?>[]{Runnable.class, Comparable.class}, settings.getExtraInterfaces());
    }

    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesWithNull() {
        mockSettings.extraInterfaces(
                Runnable.class,
                null,
                Comparable.class
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterfaces() {
        mockSettings.extraInterfaces(
                Runnable.class,
                String.class,
                Comparable.class
        );
    }

    @Test
    public void testMockName() {
        MockSettingsImpl settings = mockSettings.name("mockName");
        MockName mockName = settings.getMockName();
        
        assertEquals("mockName", mockName.toString());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl settings = mockSettings.spiedInstance(spiedInstance);
        Object result = settings.getSpiedInstance();
        
        assertEquals(spiedInstance, result);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        MockSettingsImpl settings = mockSettings.defaultAnswer(defaultAnswer);
        Answer<Object> result = settings.getDefaultAnswer();
        
        assertEquals(defaultAnswer, result);
    }
}
