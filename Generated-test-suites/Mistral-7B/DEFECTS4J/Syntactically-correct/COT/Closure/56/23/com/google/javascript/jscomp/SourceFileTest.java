package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SourceFile.
 */
public class SourceFileTest {
  private static final String TEST_FILE_PATH = "testSourceFile.js";

  // Basic test cases for constructor, getName, and isExtern methods

  @Test
  public void testSourceFileConstructor() throws IOException {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    assertEquals("testSourceFile.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSourceFileConstructorWithExternFlag() throws IOException {
    SourceFile sourceFile = new SourceFile("testSourceFile.js") {
      @Override
      public boolean isExtern() {
        return true;
      }
    };
    assertEquals("testSourceFile.js", sourceFile.getName());
    assertTrue(sourceFile.isExtern());
  }

  // Test getLine method

  @Test
  public void testGetLineSuccess() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  // some code here\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", testCode);
    assertEquals("function testFunction() {\n" +
        "  // some code here\n" +
        "}", sourceFile.getLine(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("testSourceFile.js", "");
    sourceFile.getLine(10);
  }

  // Test getRegion method

  @Test
  public void testGetRegionSuccess() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  // some code here\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", testCode);
    Region region = sourceFile.getRegion(3);
    String expectedRegion = "  // some code here\n";
    assertEquals(expectedRegion, region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("testSourceFile.js", "");
    sourceFile.getRegion(10);
  }

  // Test fromFile method

  @Before
  public void setUp() throws IOException {
    Files.write(new File(TEST_FILE_PATH), "function testFunction() {\n" +
        "  // some code here\n" +
        "}", Charsets.UTF_8);
  }

  @Test
  public void testFromFileSuccess() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile(TEST_FILE_PATH, Charsets.UTF_8);
    assertEquals("testSourceFile.js", sourceFile.getName());
    String testCode = sourceFile.getCode();
    String expectedTestCode = "function testFunction() {\n" +
        "  // some code here\n" +
        "}";
    assertEquals(expectedTestCode, testCode);
  }

  @After
  public void tearDown() throws IOException {
    Files.delete(new File(TEST_FILE_PATH));
  }
}