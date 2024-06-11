package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

  @Test
  public void testToPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    assertEquals("Pattern", format.toPattern());
  }

  @Test
  public void testApplyPattern() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    format.applyPattern("NewPattern");
    assertEquals("NewPattern", format.toPattern());
  }

  @Test
  public void testEquals() {
    ExtendedMessageFormat format1 = new ExtendedMessageFormat("Pattern");
    ExtendedMessageFormat format2 = new ExtendedMessageFormat("Pattern");
    assertEquals(format1, format2);
  }

  @Test
  public void testHashCode() {
    ExtendedMessageFormat format1 = new ExtendedMessageFormat("Pattern");
    ExtendedMessageFormat format2 = new ExtendedMessageFormat("Pattern");
    assertEquals(format1.hashCode(), format2.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormat() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    format.setFormat(0, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormatByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    format.setFormatByArgumentIndex(0, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormats() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    format.setFormats(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFormatsByArgumentIndex() {
    ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
    format.setFormatsByArgumentIndex(null);
  }
}