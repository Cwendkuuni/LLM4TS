package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.junit.Test;

import com.google.common.io.CharStreams;

public class SourceFileTest {

  @Test
  public void testSourceFile() {
    String fileName = "testfile.js";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertNull(sourceFile.getOriginalPath());
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
    assertNull(sourceFile.getCodeReader());
    assertNull(sourceFile.getRegion(1));
    assertEquals(0, sourceFile.getLineOffset(1));
  }

  @Test
  public void testSourceFileWithCode() throws IOException {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertTrue(sourceFile.hasSourceInMemory());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());
    assertSame(code, sourceFile.getCode());
    assertEquals(code, CharStreams.toString(sourceFile.getCodeReader()));
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());
    assertEquals("function foo() { return 42; }", region.getCode());
  }

  @Test
  public void testSourceFileWithCodeFromFile() throws IOException {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }";
    Files.write(new File(fileName).toPath(), code.getBytes());
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertFalse(sourceFile.hasSourceInMemory());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertEquals("function foo() { return 42; }", CharStreams.toString(sourceFile.getCodeReader()));
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());
    assertEquals("function foo() { return 42; }", region.getCode());
    Files.delete(new File(fileName).toPath());
  }

  @Test
  public void testSourceFileWithCodeFromInputStream() throws IOException {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }";
    try (InputStream inputStream = new ByteArrayInputStream(code.getBytes())) {
      SourceFile sourceFile = new SourceFile(fileName) {
        @Override
        public String getCode() throws IOException {
          return CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        }
      };
      assertEquals("function foo() { return 42; }", sourceFile.getCode());
      assertTrue(sourceFile.hasSourceInMemory());
      assertFalse(sourceFile.isExtern());
      assertEquals(1, sourceFile.getNumLines());
      assertEquals("function foo() { return 42; }", sourceFile.getCode());
      assertEquals("function foo() { return 42; }", CharStreams.toString(sourceFile.getCodeReader()));
      Region region = sourceFile.getRegion(1);
      assertEquals(1, region.getStartLine());
      assertEquals(1, region.getEndLine());
      assertEquals("function foo() { return 42; }", region.getCode());
    }
  }

  @Test
  public void testSourceFileWithCodeFromReader() throws IOException {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }";
    Reader reader = new StringReader(code);
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public Reader getCodeReader() throws IOException {
        return reader;
      }
    };
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertTrue(sourceFile.hasSourceInMemory());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertEquals("function foo() { return 42; }", CharStreams.toString(sourceFile.getCodeReader()));
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());
    assertEquals("function foo() { return 42; }", region.getCode());
  }

  @Test
  public void testSourceFileWithExtern() {
    String fileName = "testfile.js";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public boolean isExtern() {
        return true;
      }
    };
    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.isExtern());
    assertNull(sourceFile.getOriginalPath());
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
    assertNull(sourceFile.getCodeReader());
    assertNull(sourceFile.getRegion(1));
    assertEquals(0, sourceFile.getLineOffset(1));
  }

  @Test
  public void testSourceFileWithOriginalPath() {
    String fileName = "testfile.js";
    String originalPath = "/path/to/testfile.js";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getOriginalPath() {
        return originalPath;
      }
    };
    assertEquals(fileName, sourceFile.getName());
    assertNull(sourceFile.getOriginalPath());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
    assertNull(sourceFile.getCodeReader());
    assertNull(sourceFile.getRegion(1));
    assertEquals(0, sourceFile.getLineOffset(1));
  }

  @Test
  public void testGetLine() {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }\nbar()";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals("function foo() { return 42; }", sourceFile.getLine(1));
    assertEquals("bar()", sourceFile.getLine(2));
    assertEquals(null, sourceFile.getLine(3));
  }

  @Test(expected = IOException.class)
  public void testGetLineWithIOException() throws IOException {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }\nbar()";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }
    };
    sourceFile.getLine(1);
  }

  @Test
  public void testGetRegion() {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }\nbar()";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());
    assertEquals("function foo() { return 42; }", region.getCode());

    region = sourceFile.getRegion(3);
    assertEquals(1, region.getStartLine());
    assertEquals(2, region.getEndLine());
    assertEquals("bar()", region.getCode());

    region = sourceFile.getRegion(5);
    assertNull(region);
  }

  @Test
  public void testGetRegionWithOutOfBounds() {
    String fileName = "testfile.js";
    String code = "function foo() { return 42; }\nbar()";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    try {
      sourceFile.getRegion(0);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      sourceFile.getRegion(sourceFile.getNumLines() + 1);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}

class ByteArrayInputStream extends InputStream {
  private final byte[] bytes;

  ByteArrayInputStream(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public int read() throws IOException {
    if (bytes.length > 0) {
      return bytes[0];
    }
    return -1;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int i = Math.min(len, bytes.length);
    if (i > 0) {
      System.arraycopy(bytes, 0, b, off, i);
    }
    return i;
  }
}