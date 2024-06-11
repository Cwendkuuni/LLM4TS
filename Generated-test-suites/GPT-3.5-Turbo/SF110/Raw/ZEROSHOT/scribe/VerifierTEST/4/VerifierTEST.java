import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
  
  @Test(expected = NullPointerException.class)
  public void testConstructor_NullValue() {
    Verifier verifier = new Verifier(null);
  }
  
  @Test
  public void testConstructor_ValidValue() {
    String value = "ValidValue";
    Verifier verifier = new Verifier(value);
    assertEquals(value, verifier.getValue());
  }
  
  @Test
  public void testGetValue() {
    String value = "ValidValue";
    Verifier verifier = new Verifier(value);
    assertEquals(value, verifier.getValue());
  }
  
}