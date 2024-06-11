
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {

  @Test
  public void testConstructor1() {
    // Arrange
    String pattern = "Test {0}";

    // Act
    ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

    // Assert
    assertEquals(pattern, format.toPattern());
  }

  @Test
  public void testConstructor2() {
    // Arrange
    String pattern = "Test {0}";
    Locale locale = Locale.CANADA;

    // Act
    ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);

    // Assert
    assertEquals(pattern, format.toPattern());
    assertEquals(locale, format.getLocale());
  }

  @Test
  public void testConstructor3() {
    // Arrange
    String pattern = "Test {0}";
    Map<String, FormatFactory> registry = new HashMap<>();
    registry.put("date", new SimpleDateFormat.FormatFactory());

    // Act
    ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

    // Assert
    assertEquals(pattern, format.toPattern());
    assertEquals(registry, format.getRegistry());
  }

  @Test
  public void testConstructor4() {
    // Arrange
    String pattern = "Test {0}";
    Locale locale = Locale.CANADA;
    Map<String, FormatFactory> registry = new HashMap<>();
    registry.put("date", new SimpleDateFormat.FormatFactory());

    // Act
    ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);

    // Assert
    assertEquals(pattern, format.toPattern());
    assertEquals(locale, format.getLocale());
    assertEquals(registry, format.getRegistry());
  }

  @Test
  public void testApplyPattern() {
    // Arrange
    String pattern = "Test {0}";
    ExtendedMessageFormat format = new ExtendedMessageFormat("");
    String expectedPattern = "Test {0}";

    // Act
    format.applyPattern(pattern);

    // Assert
    assertEquals(expectedPattern, format.toPattern());
  }

  @Test
  public void testEquals() {
    // Arrange
    String pattern1 = "Test {0}";
    String pattern2 = "Test {1}";
    Locale locale = Locale.CANADA;
    Map<String, FormatFactory> registry = new HashMap<>();
    registry.put("date", new SimpleDateFormat.FormatFactory());

    ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern1, locale, registry);
    ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern1, locale, registry);
    ExtendedMessageFormat format3 = new ExtendedMessageFormat(pattern2, locale, registry);

    // Act & Assert
    assertTrue(format1.equals(format2));
    assertFalse(format1.equals(format3));
  }

  @Test
  public void testHashCode() {
    // Arrange
    String pattern = "Test {0}";
    Locale locale = Locale.CANADA;
    Map<String, FormatFactory> registry = new HashMap<>();
    registry.put("date", new SimpleDateFormat.FormatFactory());

    ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
    int expectedHashCode = pattern.hashCode() * 31 + locale.hashCode() * 31 + registry.hashCode();

    // Act
    int hashCode = format.hashCode();

    // Assert
    assertEquals(expectedHashCode, hashCode);
  }

  @Test
  public void testSetFormat() {
    // Arrange
    ExtendedMessageFormat format = new ExtendedMessageFormat("");

    // Act & Assert
    try {
      format.setFormat(0, null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testSetFormatByArgumentIndex() {
    // Arrange
    ExtendedMessageFormat format = new ExtendedMessageFormat("");

    // Act & Assert
    try {
      format.setFormatByArgumentIndex(0, null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testSetFormats() {
    // Arrange
    Format[] formats = new Format[]{new SimpleDateFormat()};
    ExtendedMessageFormat format = new ExtendedMessageFormat("");

    // Act & Assert
    try {
      format.setFormats(formats);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testSetFormatsByArgumentIndex() {
    // Arrange
    Format[] formats = new Format[]{new SimpleDateFormat()};
    ExtendedMessageFormat format = new ExtendedMessageFormat("");

    // Act & Assert
    try {
      format.setFormatsByArgumentIndex(formats);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }
}
