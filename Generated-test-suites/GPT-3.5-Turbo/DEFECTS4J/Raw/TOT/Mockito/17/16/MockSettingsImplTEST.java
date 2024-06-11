import org.junit.Assert; 
import org.junit.Test; 
import org.mockito.exceptions.base.MockitoException; 
import org.mockito.internal.matchers.NullsAreMatchers; 
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        Assert.assertEquals(mockSettings, result);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Comparable.class);
        Assert.assertEquals(mockSettings, result);
        Assert.assertArrayEquals(new Class<?>[]{Runnable.class, Comparable.class}, mockSettings.getExtraInterfaces());
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesWithNoInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesWithNullInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class, null, Integer.class);
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class, Integer.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName result = mockSettings.getMockName();
        Assert.assertNull(result);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.name("mockName");
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals("mockName", mockSettings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Comparable.class);
        Assert.assertEquals(mockSettings, result);
        Assert.assertArrayEquals(new Class<?>[]{Runnable.class, Comparable.class}, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = NullsAreMatchers.nullable();
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(mockSettings, result);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(Runnable.class, java.io.Serializable.class);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        mockSettings.initiateMockName(classToMock);
        Assert.assertEquals(classToMock, mockSettings.getMockName().getType());
    }
}