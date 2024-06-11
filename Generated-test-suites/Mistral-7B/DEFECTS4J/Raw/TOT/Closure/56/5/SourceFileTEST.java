package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SourceFile} class.
 */
public class SourceFileTest {
  private File testFile;

  @Before
  public void setUp() throws Exception {
    // Prepare test file.
    testFile = new File("test.js");
    Files.write(testFile, "function test() {}", Charsets.UTF_8);
  }

  @After
  public void tearDown() throws IOException {
    Files.delete(testFile);
  }

  @Test
  public void testOnDisk() throws IOException {
    // Given source file on disk.
    SourceFile sourceFile = SourceFile.fromFile("test.js");

    // When reading source code.
    assertEquals("function test() {}", sourceFile.getCode());

    // When reading source code as InputStream.
    try (InputStream inputStream = Files.newInputStream(testFile.toPath())) {
      SourceFile sourceFileFromInputStream = SourceFile.fromInputStream("test.js", inputStream);
      assertEquals("function test() {}", sourceFileFromInputStream.getCode());
    }

    // When reading source code as Reader.
    SourceFile sourceFileFromReader = SourceFile.fromReader("test.js", Files.newReader(testFile, Charsets.UTF_8));
    assertEquals("function test() {}", sourceFileFromReader.getCode());

    // When checking line number.
    assertEquals(1, sourceFile.getLineOffset(1));

    // When checking line number out of range.
    assertNull(sourceFile.getLineOffset(100));

    // When checking line.
    assertEquals("function test() {}", sourceFile.getLine(1));

    // When checking region.
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());
    assertEquals("function test() {}", region.getCode());

    // When generating source code.
    SourceFile generatedSourceFile = SourceFile.fromGenerator("test.js", () -> "function test() {}", "test.js");
    assertEquals("function test() {}", generatedSourceFile.getCode());

    // When reading source code from Java code.
    SourceFile javaSourceFile = new JavaSource("TestJavaSource.java");
    assertEquals("public class TestJavaSource {}\n", javaSourceFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFileName() {
    SourceFile.fromFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyFileName() {
    SourceFile.fromFile("");
  }

  @Test(expected = IOException.class)
  public void testIOException() throws IOException {
    // Given source file that causes I/O exception.
    File corruptFile = new File("corrupt.js");
    Files.deleteOnExit(corruptFile);
    Files.write(corruptFile, new byte[1], Charsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromFile("corrupt.js");

    // When reading source code.
    sourceFile.getCode();
  }

  @Test
  public void testClearCachedSource() throws IOException {
    // Given source file on disk.
    SourceFile sourceFile = SourceFile.fromFile("test.js");

    // When reading source code.
    String code = sourceFile.getCode();

    // When clearing cached source.
    sourceFile.clearCachedSource();

    // When reading source code again.
    assertNull(sourceFile.getCodeNoCache());
  }

  /**
   * Test for {@link SourceFile} that is generated from Java code.
   */
  static class JavaSource extends SourceFile {
    JavaSource(String fileName) {
      super(fileName);
    }

    @Override
    public String getCode() throws IOException {
      return "public class TestJavaSource {}\n";
    }
  }
}