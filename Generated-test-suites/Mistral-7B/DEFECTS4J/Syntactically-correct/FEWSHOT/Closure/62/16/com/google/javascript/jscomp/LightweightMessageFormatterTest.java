package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.LINE_NUMBER_FORMATTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Region;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.RegionType;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Strings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test cases for LightweightMessageFormatter class
 */
@RunWith(MockitoJUnitRunner.class)
public class LightweightMessageFormatterTest {

  @Mock
  private SourceExcerptProvider sourceExcerptProvider;

  @Mock
  private Region region;

  @Mock
  private JSError error;

  @Mock
  private ExcerptFormatter lineNumberingFormatter;

  @InjectMocks
  private LightweightMessageFormatter lightweightMessageFormatter;

  @Test
  public void testFormatErrorWithoutSource() {
    String expectedErrorMessage = "ERROR - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithSource() {
    String expectedErrorMessage = "testSourceName:1: testDescription\n"
        + "testSourceName:1: some code here\n"
        + " 1| some code here\n";
    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("some code here");
    when(lineNumberingFormatter.formatRegion(region)).thenReturn(" 1| some code here\n");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    String expectedWarningMessage = "WARNING - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithSource() {
    String expectedWarningMessage = "testSourceName:1: testDescription\n"
        + "testSourceName:1: some code here\n"
        + " 1| some code here\n";
    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("some code here");
    when(lineNumberingFormatter.formatRegion(region)).thenReturn(" 1| some code here\n");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatErrorWithNullSource() {
    String expectedErrorMessage = "ERROR - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualErrorMessage = lightweightMessageFormatter.new LightweightMessageFormatter()
        .formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatWarningWithNullSource() {
    String expectedWarningMessage = "WARNING - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualWarningMessage = lightweightMessageFormatter.new LightweightMessageFormatter()
        .formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatErrorWithoutSourceExcerpt() {
    String expectedErrorMessage = "ERROR - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithEmptySourceExcerpt() {
    String expectedErrorMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithRegionNull() {
    String expectedErrorMessage = "ERROR - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithRegionEmptySourceExcerpt() {
    String expectedErrorMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.isEmpty()).thenReturn(true);

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithNullRegion() {
    String expectedErrorMessage = "ERROR - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithRegionSourceExcerptNull() {
    String expectedErrorMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn(null);

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithRegionSourceExcerptEmpty() {
    String expectedErrorMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatErrorWithRegionSourceExcerpt() {
    String expectedErrorMessage = "testSourceName:1: some code here\n"
        + " 1| some code here\n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("some code here");
    when(lineNumberingFormatter.formatRegion(region)).thenReturn(" 1| some code here\n");

    String actualErrorMessage = lightweightMessageFormatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatWarningWithRegionNull() {
    String expectedWarningMessage = "WARNING - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithRegionEmptySourceExcerpt() {
    String expectedWarningMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.isEmpty()).thenReturn(true);

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithNullRegion() {
    String expectedWarningMessage = "WARNING - testDescription";
    when(error.description).thenReturn("testDescription");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithRegionSourceExcerptNull() {
    String expectedWarningMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn(null);

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithRegionSourceExcerptEmpty() {
    String expectedWarningMessage = "testSourceName:1: \n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWarningWithRegionSourceExcerpt() {
    String expectedWarningMessage = "testSourceName:1: some code here\n"
        + " 1| some code here\n";

    when(error.sourceName).thenReturn("testSourceName");
    when(error.lineNumber).thenReturn(1);
    when(sourceExcerptProvider.getSource(RegionType.SOURCE_UNIT, "testSourceName"))
        .thenReturn(region);
    when(region.getSourceExcerpt()).thenReturn("some code here");
    when(lineNumberingFormatter.formatRegion(region)).thenReturn(" 1| some code here\n");

    String actualWarningMessage = lightweightMessageFormatter.formatWarning(error);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testNewLightweightMessageFormatterWithNullSourceProvider() {
    LightweightMessageFormatter lightweightMessageFormatter = new LightweightMessageFormatter(null);
    assertNull(lightweightMessageFormatter.getSource());
  }

  @Test
  public void testNewLightweightMessageFormatterWithSourceProvider() {
    LightweightMessageFormatter lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider);
    assertEquals(sourceExcerptProvider, lightweightMessageFormatter.getSource());
  }

  @Test
  public void testNewLightweightMessageFormatterWithNullExcerpt() {
    LightweightMessageFormatter lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, null);
    assertNull(lightweightMessageFormatter.getExcerpt());
  }

  @Test
  public void testNewLightweightMessageFormatterWithExcerpt() {
    LightweightMessageFormatter lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
    assertEquals(LINE, lightweightMessageFormatter.getExcerpt());
  }
}