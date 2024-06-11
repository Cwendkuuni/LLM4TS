package org.apache.commons.lang.text;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
  
  private ExtendedMessageFormat format;
  
  @Before
  public void setUp() {
    format = new ExtendedMessageFormat("");
  }
  
  @Test
  public void testToPattern() {
    format = new ExtendedMessageFormat("Hello {0}!");
    String result = format.toPattern();
    assertEquals("Hello {0}!", result);
  }
  
  @Test
  public void testApplyPattern() {
    format.applyPattern("Hello {0}!");
    String result = format.toPattern();
    assertEquals("Hello {0}!", result);
  }
  
  @Test
  public void testSetFormat() {
    try {
      format.setFormat(0, null);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testSetFormatByArgumentIndex() {
    try {
      format.setFormatByArgumentIndex(0, null);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }    
  }
  
  @Test
  public void testSetFormats() {
    try {
      format.setFormats(null);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testSetFormatsByArgumentIndex() {
    try {
      format.setFormatsByArgumentIndex(null);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }    
  }
  
  @Test
  public void testApplyPatternWithRegistry() {
    Map<String, FormatFactory> registry = new HashMap<>();
    registry.put("custom", new CustomFormatFactory());
    format = new ExtendedMessageFormat("", Locale.getDefault(), registry);
    format.applyPattern("Hello {0, custom}!");
    String result = format.toPattern();
    assertEquals("Hello {0, custom}!", result);
  }
  
  private static class CustomFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String name, String arguments, Locale locale) {
      return null;
    }
  }

}