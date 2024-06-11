package org.joda.time.format;

import org.joda.time.*;
import org.junit.Test;
import java.util.Locale;

public class DateTimeFormatterTest {
  
  @Test
  public void testIsPrinter() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    assert !formatter.isPrinter();
  }
  
  @Test
  public void testGetPrinter() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    assert formatter.getPrinter() == null;
  }
  
  @Test
  public void testIsParser() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    assert !formatter.isParser();
  }
  
  @Test
  public void testGetParser() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    assert formatter.getParser() == null;
  }
  
  @Test
  public void testWithLocale() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    Locale locale = Locale.ENGLISH;
    DateTimeFormatter newFormatter = formatter.withLocale(locale);
    assert newFormatter.getLocale().equals(locale);
  }
  
  @Test
  public void testGetLocale() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    assert formatter.getLocale() == null;
  }
  
  @Test
  public void testWithOffsetParsed() {
    DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    DateTimeFormatter newFormatter = formatter.withOffsetParsed();
    assert newFormatter.isOffsetParsed();
  }
  
  // Additional test methods for other functionality in DateTimeFormatter class
  
}