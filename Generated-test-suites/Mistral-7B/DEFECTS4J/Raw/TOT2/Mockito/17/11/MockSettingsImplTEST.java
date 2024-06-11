import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.MockSettings;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings mockSettings = new MockSettingsImpl().serializable();
        Class<?>[] expectedExtraInterfaces = {java.io.Serializable.class};
        verify(mockSettings).extraInterfaces(expectedExtraInterfaces);
    }

    // Edge case: testing with an existing array
    @Test
    public void testSerializable_existingArray() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        verify(mockSettings).extraInterfaces(extraInterfaces);
    }

    // Exception handling scenario: testing with null array
    @Test(expected = IllegalArgumentException.class)
    public void testSerializable_nullArray() {
        MockSettings mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test
    public void testExtraInterfaces_validInterface() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?> testInterface = Mockito.class;
        mockSettings.extraInterfaces(testInterface);
        Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
        assertArrayEquals(new Class<?>[]{testInterface}, actualExtraInterfaces);
    }

    // Edge case: testing with an empty array
    @Test
    public void testExtraInterfaces_emptyArray() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?>[] expectedEmptyArray = new Class[0];
        mockSettings.extraInterfaces(expectedEmptyArray);
        Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
        assertArrayEquals(expectedEmptyArray, actualExtraInterfaces);
    }

    // Exception handling scenario: testing with a non-interface
    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfaces_nonInterface() {
        MockSettings mockSettings = new MockSettingsImpl();
        Class<?> nonInterface = String.class;
        mockSettings.extraInterfaces(nonInterface);
    }

    // Edge case: testing with a null class
    @Test(expected = NullPointerException.class)
    public void testExtraInterfaces_nullClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test
    public void testGetMockName() {
        MockSettings mockSettings = mock(MockSettings.class);
        String testName = "testName";
        mockSettings.name(testName);
        MockName mockName = mockSettings.getMockName();
        verify(mockName).getMockName();
        assertEquals(testName, mockName.getName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?> testInterface = Mockito.class;
        Class<?>[] testExtraInterfaces = {testInterface};
        mockSettings.extraInterfaces(testExtraInterfaces);
        Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
        assertArrayEquals(testExtraInterfaces, actualExtraInterfaces);
    }

    @Test
    public void testGetExtraInterfaces_emptyList() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
        assertArrayEquals(new Class[0], actualExtraInterfaces);
    }

    @Test
    public void testGetExtraInterfaces_serializable() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?>[] testExtraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(testExtraInterfaces);
        Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
        assertTrue(Arrays.asList(actualExtraInterfaces).contains(java.io.Serializable.class));
    }

    @Test
    public void testIsSerializable_serializable() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?>[] serializableExtraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(serializableExtraInterfaces);
        verify(mockSettings).isSerializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_nonSerializable() {
        MockSettings mockSettings = mock(MockSettings.class);
        Class<?>[] nonSerializableExtraInterfaces = {String.class};
        mockSettings.extraInterfaces(nonSerializableExtraInterfaces);
        verify(mockSettings).isSerializable();
        assertFalse(mockSettings.isSerializable());
    }

    // Edge case: testing with empty extra interfaces
    @Test
    public void testIsSerializable_emptyList() {
        MockSettings mockSettings = mock(MockSettings.class);
        mockSettings.extraInterfaces(new Class[0]);
        verify(mockSettings).isSerializable();
        assertFalse(mockSettings.isSerializable());
    }
}