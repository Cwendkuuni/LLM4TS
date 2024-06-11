package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  private static final String FILE_NAME = "testFile.js";
  private static final String FILE_CONTENTS = "function test() {}";
  private SourceFile sourceFile;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() throws IOException {
    File testFile = new File(FILE_NAME);
    Files.write(testFile, FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
    sourceFile = new OnDisk(FILE_NAME);
  }

  @Test
  public void testGetCode() throws IOException {
    String code = sourceFile.getCode();
    assertThat(code).isEqualTo(FILE_CONTENTS);
  }

  @Test
  public void testGetLine() {
    String line1 = sourceFile.getLine(1);
    String line2 = sourceFile.getLine(2);
    String expectedLine1 = "function test() {";
    String expectedLine2 = "}";
    assertThat(line1).isEqualTo(expectedLine1);
    assertThat(line2).isEqualTo(expectedLine2);
  }

  @Test
  public void testGetRegion() {
    Region region = sourceFile.getRegion(2);
    String expectedRegion = "}";
    String regionString = region.getCode();
    assertThat(regionString).isEqualTo(expectedRegion);
  }

  @Test
  public void testInvalidLineNumber() {
    assertThrows(IllegalArgumentException.class, () -> sourceFile.getLine(0));
  }

  @Test
  public void testNonExistentFile() {
    File nonExistentFile = new File("nonExistentFile.js");
    assertThrows(IOException.class, () -> new OnDisk(nonExistentFile));
  }

  @Test
  public void testNegativeLineNumber() {
    assertThrows(IllegalArgumentException.class, () -> sourceFile.getLine(-1));
  }

  @Test
  public void testGetLineWithOffset() throws IOException {
    String line1 = sourceFile.getLine(1, 5);
    String expectedLine1 = "test";
    assertThat(line1).isEqualTo(expectedLine1);
  }

  @Test
  public void testGetRegionWithOffset() throws IOException {
    Region region = sourceFile.getRegion(2, 1, 2);
    String expectedRegion = "}";
    String regionCode = region.getCode();
    assertThat(regionCode).isEqualTo(expectedRegion);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String initialCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    String newCode = sourceFile.getCode();
    assertThat(initialCode).isNotEqualTo(newCode);
  }

  @After
  public void tearDown() throws IOException {
    Files.delete(new File(FILE_NAME).toPath());
  }
}