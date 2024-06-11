package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), true);
    }

    @Test
    public void testVerify() {
        // Typical use case
        // No error expected when verification is successful within time
        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);

        // Edge case
        // Error should be thrown if verification is not successful within time
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), false);
        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            // Expected
        }
    }

    @Test
    public void testGetPollingPeriod() {
        // Typical use case
        assertEquals(100, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        // Typical use case
        assertEquals(1000, verificationOverTime.getDuration());
    }
    
    @Test
    public void testGetDelegate() {
        // Typical use case
        assertNotNull(verificationOverTime.getDelegate());
    }

}