import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertNotNull;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        String mock = mockitoCore.mock(stringClass, mockSettings, shouldResetOngoingStubbing);
        assertNotNull(mock);
    }
}