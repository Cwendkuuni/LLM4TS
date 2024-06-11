import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        String mock = mockitoCore.mock(stringClass, mockSettings, shouldResetOngoingStubbing);
        verify(mockitoCore).mock(stringClass, mockSettings);
    }
}