package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Test basic functionality
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Test error", "Test source", 5);
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - Test error\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    // Test basic functionality
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError("Test warning", "Test source", 10);
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning\n", formattedWarning);
  }

  @Test
  public void testFormatRegion() {
    // Test basic functionality
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("Test code", 1, 2);
    String formattedRegion = formatter.formatRegion(region);
    assertEquals("  1| Test code\n  2| ", formattedRegion);
  }

  @Test
  public void testFormatRegionEmptyCode() {
    // Test when the code is empty
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    Region region = new Region("", 1, 2);
    String formattedRegion = formatter.formatRegion(region);
    assertNull(formattedRegion);
  }

  @Test
  public void testFormatRegionNullRegion() {
    // Test when the region is null
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedRegion = formatter.formatRegion(null);
    assertNull(formattedRegion);
  }

  @Test
  public void testLineNumberingFormatterLine() {
    // Test formatting a single line
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedLine = formatter.formatLine("Test line", 1);
    assertEquals("Test line", formattedLine);
  }

  @Test
  public void testLineNumberingFormatterEmptyLine() {
    // Test formatting an empty line
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedLine = formatter.formatLine("", 1);
    assertEquals("", formattedLine);
  }

  @Test
  public void testWithoutSource() {
    // Test creating a formatter without source information
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

}