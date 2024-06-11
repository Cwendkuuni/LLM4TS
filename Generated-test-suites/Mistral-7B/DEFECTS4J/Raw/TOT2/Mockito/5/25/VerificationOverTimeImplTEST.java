import org.junit.jupit.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;

public class VerificationOverTimeImplTest {

  @Test
  public void testVerifySuccess() {
    VerificationMode delegate = Mockito.mock(VerificationMode.class);
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
    VerificationData data = new VerificationData(Mockito.mock(Object.class), ArgumentMatchers.eq("testMethod"));

    verificationOverTime.verify(data);
    Mockito.verify(delegate).verify(data);
  }
}