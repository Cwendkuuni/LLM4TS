import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), false);
    }

    @Test
    public void verify_ReturnOnSuccessIsTrue_DelegateIsSatisfied_ReturnImmediately() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        doNothing().when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        verificationOverTime.verify(verificationDataMock);
        verify(verificationOverTime.getDelegate(), times(1)).verify(verificationDataMock);
    }

    @Test
    public void verify_ReturnOnSuccessIsTrue_DelegateIsNotSatisfied_ThrowMockitoAssertionError() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        MockitoAssertionError assertionError = new MockitoAssertionError("Assertion error");
        doThrow(assertionError).when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        try {
            verificationOverTime.verify(verificationDataMock);
        } catch (MockitoAssertionError e) {
            assertSame(assertionError, e);
        }
        verify(verificationOverTime.getDelegate(), times(10)).verify(verificationDataMock);
    }

    @Test
    public void verify_ReturnOnSuccessIsFalse_DelegateIsNotSatisfiedBeforeDuration_ThrowMockitoAssertionError() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        MockitoAssertionError assertionError = new MockitoAssertionError("Assertion error");
        doThrow(assertionError).when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        try {
            verificationOverTime.verify(verificationDataMock);
        } catch (MockitoAssertionError e) {
            assertSame(assertionError, e);
        }
        verify(verificationOverTime.getDelegate(), times(10)).verify(verificationDataMock);
    }

    @Test
    public void verify_ReturnOnSuccessIsFalse_DelegateIsNotSatisfiedAfterDuration_ThrowMockitoAssertionError() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        MockitoAssertionError assertionError = new MockitoAssertionError("Assertion error");
        doThrow(assertionError).when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        Timer timerMock = mock(Timer.class);
        when(timerMock.isCounting()).thenReturn(false);
        verificationOverTime.verify(verificationDataMock);
        verify(verificationOverTime.getDelegate(), times(10)).verify(verificationDataMock);
    }

    @Test
    public void verify_DelegateThrowsMockitoAssertionError_CanRecoverFromFailure_ReturnOnError() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        MockitoAssertionError assertionError = new MockitoAssertionError("Assertion error");
        doThrow(assertionError).when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        verificationOverTime.verify(verificationDataMock);
        verify(verificationOverTime.getDelegate(), times(1)).verify(verificationDataMock);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateThrowsMockitoAssertionError_CanNotRecoverFromFailure_ThrowOnError() {
        VerificationData verificationDataMock = mock(VerificationData.class);
        MockitoAssertionError assertionError = new MockitoAssertionError("Assertion error");
        doThrow(assertionError).when(verificationOverTime.getDelegate()).verify(verificationDataMock);
        when(verificationOverTime.canRecoverFromFailure(verificationOverTime.getDelegate())).thenReturn(false);
        verificationOverTime.verify(verificationDataMock);
    }

    @Test
    public void getPollingPeriod_ReturnsCorrectValue() {
        long pollingPeriod = verificationOverTime.getPollingPeriod();
        assertEquals(100, pollingPeriod);
    }

    @Test
    public void getDuration_ReturnsCorrectValue() {
        long duration = verificationOverTime.getDuration();
        assertEquals(1000, duration);
    }

    @Test
    public void getDelegate_ReturnsCorrectValue() {
        VerificationMode delegate = verificationOverTime.getDelegate();
        assertNotNull(delegate);
    }

}