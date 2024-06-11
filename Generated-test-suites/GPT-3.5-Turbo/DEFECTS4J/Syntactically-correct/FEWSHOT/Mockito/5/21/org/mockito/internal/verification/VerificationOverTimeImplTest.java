import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VerificationOverTimeImplTest {
    private VerificationOverTimeImpl verificationMode;
    private VerificationData data;

    @Before
    public void setUp() {
        verificationMode = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true);
        data = mock(VerificationData.class);
    }

    @Test
    public void testVerify_ReturnOnSuccess() {
        // Verify that the delegate.verify() method is called
        doNothing().when(verificationMode.getDelegate()).verify(data);

        // Call the verify() method
        verificationMode.verify(data);

        // Verify that the delegate.verify() method is called only once
        verify(verificationMode.getDelegate(), times(1)).verify(data);
    }

    @Test
    public void testVerify_NotReturnOnSuccess() {
        // Verify that the delegate.verify() method is called twice
        doNothing().doThrow(MockitoAssertionError.class).when(verificationMode.getDelegate()).verify(data);

        // Call the verify() method
        verificationMode.verify(data);

        // Verify that the delegate.verify() method is called twice
        verify(verificationMode.getDelegate(), times(2)).verify(data);
    }
}