package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("testSourceFile.js");
  }

  // Test for getLineOffset() method

  @Test
  public void testGetLineOffset() {
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(2, sourceFile.getLineOffset(2));
    assertNull(sourceFile.getLineOffset(10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    sourceFile.getLineOffset(0);
  }

  // Test for getNumLines() method

  @Test
  public void testGetNumLines() {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(3, sourceFile.getNumLines());
  }

  // Test for getCode() method

  @Test(expected = IOException.class)
  public void testGetCodeWithIOException() {
    sourceFile.getCode();
  }

  @Test
  public void testGetCodeWithPreloadedCode() {
    String code = "function testFunction() {}";
    sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(code, sourceFile.getCode());
  }

  // Test for getRegion() method

  @Test
  public void testGetRegion() {
    String code = "function testFunction() {\n" +
                  "  // some code here\n" +
                  "}";
    sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region region = sourceFile.getRegion(5);
    assertEquals("  // some code here", region.getCode());
  }

  @Test
  public void testGetRegionWithEndOfFile() {
    String code = "function testFunction() {\n" +
                  "  // some code here\n" +
                  "}";
    sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region region = sourceFile.getRegion(50);
    assertNull(region);
  }

  // Test for getOriginalPath() method

  @Test
  public void testGetOriginalPath() {
    sourceFile.setOriginalPath("testOriginalPath");
    assertEquals("testOriginalPath", sourceFile.getOriginalPath());
  }

  // Test for isExtern() method

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testIsExternWithExternFile() {
    SourceFile extenFile = new SourceFile("testExternFile.js") {
      @Override
      public boolean isExtern() {
        return true;
      }
    };
    assertTrue(extenFile.isExtern());
  }

  // Test for clearCachedSource() method

  @Test
  public void testClearCachedSource() {
    String code = "function testFunction() {}";
    sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }
}