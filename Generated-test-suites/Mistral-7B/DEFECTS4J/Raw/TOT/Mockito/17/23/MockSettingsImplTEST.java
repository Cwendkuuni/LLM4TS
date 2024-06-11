import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Serializable.class};
        MockSettings serializableMockSettings = mockSettings.serializable().extraInterfaces(extraInterfaces);

        // when
        boolean isSerializable = serializableMockSettings.isSerializable();

        // then
        assertTrue(isSerializable);
    }

    @Test
    public void testExtraInterfacesWithNull() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(null);

        // then
        assertThrows(IllegalArgumentException.class, () -> extraInterfacesMockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?> nonInterfaceClass = String.class;
        MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(nonInterfaceClass);

        // then
        assertThrows(IllegalArgumentException.class, () -> extraInterfacesMockSettings.isSerializable());
    }

    @Test
    public void testGetExtraInterfaces() {
        // given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{Serializable.class, OtherInterface.class};
        MockSettings mockSettingsWithExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);

        // when
        Class<?>[] actualExtraInterfaces = mockSettingsWithExtraInterfaces.getExtraInterfaces();

        // then
        assertArrayEquals(new Class[]{Serializable.class, OtherInterface.class}, actualExtraInterfaces);
    }
}