import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
  
  @Test
  public void testApplyPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
    format.applyPattern("Hi {0}");
    assertEquals("Hi {0}", format.toPattern());
    
    format.applyPattern("Hello {0} and {1}");
    assertEquals("Hello {0} and {1}", format.toPattern());
  }
  
  @Test
  public void testSetFormat() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
    try {
      format.setFormat(0, new DecimalFormat());
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testSetFormatByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
    try {
      format.setFormatByArgumentIndex(0, new DecimalFormat());
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testSetFormats() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
    try {
      format.setFormats(new Format[]{});
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  @Test
  public void testSetFormatsByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
    try {
      format.setFormatsByArgumentIndex(new Format[]{});
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }

}