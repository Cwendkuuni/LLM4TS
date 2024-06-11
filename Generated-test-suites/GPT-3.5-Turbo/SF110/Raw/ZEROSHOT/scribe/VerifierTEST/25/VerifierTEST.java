import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

  @Test
  public void testGetValue() {
    Verifier verifier = new Verifier("testValue");
    assertEquals("testValue", verifier.getValue());
  }
  
  @Test(expected = NullPointerException.class)
  public void testNullValue() {
    new Verifier(null);
  }
}