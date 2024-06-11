import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    @Mock
    private VerificationMode verificationModeMock;

    @Captor
    private ArgumentCaptor<VerificationData> verificationDataCaptor;

    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Before
    public void setUp() {
        verificationOverTimeImpl = new VerificationOverTimeImpl(100L, 1000L, verificationModeMock, false, new Timer(1000L));
    }

    @Test
    public void testDelegateVerificationIsSatisfiedBeforeTimeout() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willReturn(null);

        // When
        verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));

        // Then
        VerificationData verificationData = verificationDataCaptor.getValue();
        then(verificationModeMock).verify(verificationData);

        // Verify that the delegate verification was successful and the test passed
        verify(verificationModeMock, Mockito.times(1)).verify(verificationData);
    }

    @Test
    public void testDelegateVerificationIsNotSatisfiedBeforeTimeout() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willThrow(new MockitoAssertionError("Expected invocation on mock not found."));

        // When
        try {
            verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));
        } catch (AssertionError e) {
            // Then
            AssertionError expectedError = new MockitoAssertionError("Expected invocation on mock not found.");
            AssertionError actualError = e;
            assertEquals(expectedError.getMessage(), actualError.getMessage());
            verify(verificationModeMock, Mockito.never()).verify(verificationDataCaptor.capture().getActual());
            return;
        }

        fail("Expected AssertionError was not thrown.");
    }

    @Test
    public void testDelegateVerificationIsSatisfiedAfterTimeout() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willReturn(null);

        // When
        InOrder inOrder = Mockito.inOrder(verificationModeMock);

        verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));

        // Then
        VerificationData verificationData = verificationDataCaptor.getValue();
        then(verificationModeMock).verify(verificationData);

        // Verify that the delegate verification was successful after the timeout
        inOrder.verify(verificationModeMock).verify(verificationData);

        // Wait for some time to make sure the verification has completed
        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            // Do nothing
        }

        // Verify that the test passed
        verifyNoMoreInteractions(verificationModeMock);
    }

    @Test
    public void testDelegateVerificationIsNotSatisfiedAfterTimeout() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willThrow(new MockitoAssertionError("Expected invocation on mock not found."));

        // When
        InOrder inOrder = Mockito.inOrder(verificationModeMock);

        verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));

        // Then
        VerificationData verificationData = verificationDataCaptor.getValue();
        then(verificationModeMock).verify(verificationData);

        // Verify that the test fails with an AssertionError after the timeout
        inOrder.verify(verificationModeMock).verify(verificationData);

        // Wait for some time to make sure the verification has completed
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // Do nothing
        }

        try {
            verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));
            fail("Expected AssertionError was not thrown.");
        } catch (AssertionError e) {
            // Verify that the error message is the same as the delegate's
            assertEquals("Expected invocation on mock not found.", e.getMessage());
        }
    }

    @Test
    public void testDelegateVerificationRecoversFromFailure() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willThrow(new MockitoAssertionError("Expected invocation on mock not found."))
                .willReturn(null);

        // When
        VerificationData verificationData = verificationDataCaptor.getValue();
        ArgumentCaptor<VerificationData> verificationDataRecaptor = ArgumentCaptor.forClass(VerificationData.class);

        verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));

        // Then
        // Verify that the delegate verification recovers from failure and eventually succeeds
        InOrder inOrder = Mockito.inOrder(verificationModeMock);
        inOrder.verify(verificationModeMock).verify(verificationData);
        inOrder.verify(verificationModeMock).verify(verificationDataRecaptor.capture());

        // Verify that the test passes
        verifyNoMoreInteractions(verificationModeMock);
    }

    @Test
    public void testDelegateVerificationDoesNotRecoverFromFailure() {
        // Given
        given(verificationModeMock.verify(verificationDataCaptor.capture()))
                .willThrow(new NoMoreInteractions())
                .willThrow(new NoMoreInteractions());

        // When
        try {
            verificationOverTimeImpl.verify(Mockito.mock(VerificationData.class));
        } catch (AssertionError e) {
            // Then
            assertEquals("VerificationError: No more interactions allowed on mock.\n" +
                    "Mockito suggested following alternatives:\n" +
                    "   1. Use 'verifyNoInteractions()' instead of 'verify()' if you want to verify no interaction on a mock.\n" +
                    "   2. Use 'doThrow(...)' or 'doAnswer(...)' to stub the side effect of the interaction.\n" +
                    "   3. Use 'doCallRealMethod()' to call the real method and make sure it returns the desired result.\n" +
                    "   4. If you are testing a method that has multiple possible outcomes, use 'thenReturn()', 'thenThrow()' or 'when()' as appropriate.\n" +
                    "   5. For a more flexible and powerful mocking strategy, use Mockito's Mockito JUnit Runner instead of the static API.\n" +
                    "   6. If you have a complex mocking scenario, consider using Mockito's spies, partial mocks or annotations to selectively mock only certain parts of the class.\n", e.getMessage());
        }
    }
}