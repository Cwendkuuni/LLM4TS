import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

  @Test
  public void testToPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.#}");
    String expected = "{0,number,#.#}";
    String actual = format.toPattern();
    assertEquals(expected, actual);
  }

  @Test
  public void testApplyPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.#}");
    format.applyPattern("{1,date,yyyy-MM-dd}");
    String expected = "{1,date,yyyy-MM-dd}";
    String actual = format.toPattern();
    assertEquals(expected, actual);
  }

  @Test
  public void testSetFormat() {
    try {
      ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.#}");
      format.setFormat(0, null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testSetFormatByArgumentIndex() {
    try {
      ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.#}");
      format.setFormatByArgumentIndex(0, null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testSetFormats() {
    try {
      ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
      format.setFormats(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testSetFormatsByArgumentIndex() {
    try {
      ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
      format.setFormatsByArgumentIndex(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
}