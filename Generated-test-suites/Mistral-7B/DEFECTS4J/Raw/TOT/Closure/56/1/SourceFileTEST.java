package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;

  @Before
  public void setUp() {
    Generator generatorMock = mock(Generator.class);
    when(generatorMock.getCode()).thenReturn("mockCode");
    sourceFile = new SourceFile("testFile.js") {
      @Override
      public SourceFile.Generator getGenerator() {
        return generatorMock;
      }
    };
  }

  @Test
  public void testGetLine() {
    String expectedLine = "expectedLine";
    String code = sourceFile.getCode() + "\n" + expectedLine;
    String result = sourceFile.getLine(1);
    assertThat(result).isEqualTo(expectedLine);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineNegative() {
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineBeyondLast() {
    sourceFile.getLine(Integer.MAX_VALUE);
  }

  @Test
  public void testGetLineAfterChange() {
    String newCode = "newCode";
    sourceFile.setCode(newCode);
    String expectedLine = "expectedLine";
    String result = sourceFile.getLine(1);
    assertThat(result).isEqualTo(expectedLine);
  }

  @Test
  public void testGetLineWithEmptyCode() throws IOException {
    sourceFile.setCode("");
    String result = sourceFile.getLine(1);
    assertThat(result).isNull();
  }

  @Test
  public void testGetLineWithNoCode() throws IOException {
    sourceFile = new SourceFile("testFile.js");
    String result = sourceFile.getLine(1);
    assertThat(result).isNull();
  }

  @Test
  public void testGetRegion() {
    String expectedRegion = "expectedRegion";
    String code = sourceFile.getCode() + "\n" + expectedRegion;
    Region result = sourceFile.getRegion(5);
    assertThat(result.toString()).isEqualTo(expectedRegion);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionNegative() {
    sourceFile.getRegion(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionBeyondLast() {
    sourceFile.getRegion(Integer.MAX_VALUE);
  }

  @Test
  public void testGetRegionAfterChange() {
    String newCode = "newCode";
    sourceFile.setCode(newCode);
    String expectedRegion = "expectedRegion";
    Region result = sourceFile.getRegion(5);
    assertThat(result.toString()).isEqualTo(expectedRegion);
  }

  @Test
  public void testGetRegionWithEmptyCode() throws IOException {
    sourceFile.setCode("");
    Region result = sourceFile.getRegion(5);
    assertThat(result).isNull();
  }

  @Test
  public void testGetRegionWithNoCode() throws IOException {
    sourceFile = new SourceFile("testFile.js");
    Region result = sourceFile.getRegion(5);
    assertThat(result).isNull();
  }

  @Test
  public void testGetCode() throws IOException {
    String expectedCode = "expectedCode";
    sourceFile.setCode(expectedCode);
    String result = sourceFile.getCode();
    assertThat(result).isEqualTo(expectedCode);
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithNoCode() throws IOException {
    sourceFile = new SourceFile("testFile.js");
    sourceFile.getCode();
  }
}