import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        // Given
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        String mock = Mockito.mock(stringClass, mockSettings);

        // Then
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testObtainOngoingStubbing() {
        // Given
        IOngoingStubbing stubbing = MockitoCore.stub();

        // Then
        assertNotNull(stubbing);
    }

    @Test
    public void testVerifyWithValidMock() {
        // Given
        String mock = Mockito.mock(String.class);

        // When
        MockitoCore.verify(mock, Mockito.times(1));

        // Then (Verify that no interactions have occurred)
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testMockCreationWithNullClass() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When & Then
        Mockito.mock(null, mockSettings);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Given
        String mock1 = Mockito.mock(String.class);
        String mock2 = Mockito.mock(String.class);

        // When
        MockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Then
        Mockito.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        // Given
        Object mock = null;
        Object[] mocks = new Object[]{mock};

        // When & Then
        MockitoCore.verifyNoMoreInteractions(mocks);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyWithNullMock() {
        // Given
        VerificationMode mode = Mockito.times(1);

        // When & Then
        MockitoCore.verify(null, mode);
    }
}