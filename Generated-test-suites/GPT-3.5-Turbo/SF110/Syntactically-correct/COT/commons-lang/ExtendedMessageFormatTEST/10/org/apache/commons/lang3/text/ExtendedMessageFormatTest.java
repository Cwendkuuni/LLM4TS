
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
  @Test
  public void testConstructor1() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    assertNotNull(format);
    assertEquals("", format.toPattern());
  }

  @Test
  public void testConstructor2() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.getDefault());
    assertNotNull(format);
    assertEquals("", format.toPattern());
  }

  @Test
  public void testConstructor3() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
    assertNotNull(format);
    assertEquals("", format.toPattern());
  }

  @Test
  public void testConstructor4() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.getDefault(), null);
    assertNotNull(format);
    assertEquals("", format.toPattern());
  }

  @Test
  public void testToPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    assertEquals("", format.toPattern());
  }

  @Test
  public void testApplyPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    format.applyPattern("");
    assertEquals("", format.toPattern());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormat() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    format.setFormat(0, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormatByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    format.setFormatByArgumentIndex(0, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormats() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    format.setFormats(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormatsByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    format.setFormatsByArgumentIndex(null);
  }

  @Test
  public void testEquals() {
    ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
    ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
    assertTrue(format1.equals(format2));
  }

  @Test
  public void testHashCode() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    assertNotNull(format.hashCode());
  }
}
