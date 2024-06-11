package com.google.javascript.jscomp; // Import necessary classes

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    String fileName = "testFile.js";
    SourceFile srcFile = new SourceFile(fileName);
    assertEquals("testFile.js", srcFile.getName());
    assertFalse(srcFile.isExtern());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileConstructorWithEmptyFileName() {
    String fileName = "";
    try {
      new SourceFile(fileName);
    } catch (IllegalArgumentException e) {
      assertEquals("a source must have a name", e.getMessage());
    }
  }

  @Test
  public void testGetLineOffset() {
    String fileName = "testFile.js";
    SourceFile srcFile = new SourceFile(fileName);
    assertEquals(0, srcFile.getLineOffset(1));

    String code = "line1\nline2\nline3\nline4";
    srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(16, srcFile.getLineOffset(4));
    assertEquals(11, srcFile.getLineOffset(3));
  }

  @Test
  public void testGetNumLines() {
    String fileName = "testFile.js";
    SourceFile srcFile = new SourceFile(fileName);
    assertEquals(0, srcFile.getNumLines());

    String code = "line1\nline2\nline3\nline4";
    srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(4, srcFile.getNumLines());
  }

  @Test
  public void testFindLineOffsets() {
    String fileName = "testFile.js";
    SourceFile srcFile = new SourceFile(fileName);

    String code = "line1\nline2\nline3\nline4";
    srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    srcFile.findLineOffsets();

    int[] lineOffsets = srcFile.lineOffsets;
    assertEquals(4, lineOffsets.length);
    assertEquals(1, lineOffsets[0]);
    assertEquals(11, lineOffsets[1]);
    assertEquals(21, lineOffsets[2]);
    assertEquals(32, lineOffsets[3]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    String fileName = "testFile.js";
    SourceFile srcFile = new SourceFile(fileName);
    try {
      srcFile.getLineOffset(100);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected line number between 1 and 4\n" +
          "Actual: 100", e.getMessage());
    }
  }

  @Test
  public void testGetCode() throws IOException {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(code, srcFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }

      @Override
      public Reader getCodeReader() throws IOException {
        return new StringReader(code);
      }
    };

    Reader codeReader = srcFile.getCodeReader();
    String line = null;
    char[] buffer = new char[100];
    int ch = 0;

    try {
      ch = codeReader.read(buffer, 0, buffer.length);
      line = new String(buffer, 0, ch);
    } finally {
      codeReader.close();
    }

    assertEquals("line1", line);
  }

  @Test
  public void testGetRegion() {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = srcFile.getRegion(3);
    assertEquals("line3", region.toString());
  }

  @Test
  public void testGetRegionWithInvalidLineNumber() {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile srcFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = srcFile.getRegion(10);
    assertNull(region);
  }

  @Test
  public void testFromFile() throws IOException {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));

    SourceFile srcFile = SourceFile.fromFile(fileName);
    assertEquals("testFile.js", srcFile.getName());
    assertEquals(code, srcFile.getCode());
    Files.delete(file);
  }

  @Test(expected = NullPointerException.class)
  public void testFromFileWithNullFileName() {
    try {
      SourceFile srcFile = SourceFile.fromFile(null);
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile srcFile = SourceFile.fromCode(fileName, code);
    assertEquals("testFile.js", srcFile.getName());
    assertEquals(code, srcFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromCodeWithNullFileName() {
    try {
      SourceFile srcFile = SourceFile.fromCode(null, "code");
    } catch (IllegalArgumentException e) {
      assertEquals("a source must have a name", e.getMessage());
    }
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));

    InputStream inputStream = new FileInputStream(file);
    SourceFile srcFile = SourceFile.fromInputStream(fileName, inputStream);
    assertEquals("testFile.js", srcFile.getName());
    assertEquals(code, srcFile.getCode());
    Files.delete(file);
  }

  @Test(expected = NullPointerException.class)
  public void testFromInputStreamWithNullFileName() {
    try (InputStream inputStream = new ByteArrayInputStream("code".getBytes())) {
      SourceFile srcFile = SourceFile.fromInputStream(null, inputStream);
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));

    Reader reader = new FileReader(file);
    SourceFile srcFile = SourceFile.fromReader(fileName, reader);
    assertEquals("testFile.js", srcFile.getName());
    assertEquals(code, srcFile.getCode());
    Files.delete(file);
  }

  @Test(expected = NullPointerException.class)
  public void testFromReaderWithNullFileName() {
    try (Reader reader = new StringReader("code")) {
      SourceFile srcFile = SourceFile.fromReader(null, reader);
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFromGenerator() {
    String fileName = "testFile.js";
    String code = "line1\nline2\nline3\nline4";
    SourceFile.Generator generator = new SourceFile.Generator() {
      @Override
      public String getCode() {
        return code;
      }
    };

    SourceFile srcFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals("testFile.js", srcFile.getName());
    assertEquals(code, srcFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromGeneratorWithNullFileName() {
    try {
      SourceFile.Generator generator = new SourceFile.Generator() {
        @Override
        public String getCode() {
          return "code";
        }
      };

      SourceFile srcFile = SourceFile.fromGenerator(null, generator);
    } catch (IllegalArgumentException e) {
      assertEquals("a source must have a name", e.getMessage());
    }
  }
}