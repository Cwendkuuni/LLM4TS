package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    assertEquals("testSourceFile", sourceFile.getName());
    assertNull(sourceFile.getCode());
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(1, sourceFile.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "testCode";
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "testCode";
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public Reader getCodeReader() throws IOException {
        return new StringReader(code);
      }
    };
    assertEquals(code, sourceFile.getCode().trim());
  }

  @Test
  public void testGetCodeNoCache() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    String code = "testCode";
    sourceFile.setCode(code);
    assertEquals(code, sourceFile.getCodeNoCache());
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getCode() throws IOException {
        return "line1\nline2\nline3";
      }
    };
    assertEquals("line1", sourceFile.getLine(1));
    assertEquals("line2", sourceFile.getLine(2));
    assertEquals("line3", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineNegativeLineNumber() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    sourceFile.getLine(-1);
  }

  @Test
  public void testGetRegion() {
    String code = "line1: import java.io.*;\nline2: public class Test {\nline3:   Test() {\nline4:     File file = new File(\"test.txt\");\nline5:   }\nline6: }";
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region region = sourceFile.getRegion(3);
    assertEquals(1, region.getLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("   Test() {\n", region.getContent());
  }

  @Test
  public void testGetRegionNonExistentLine() {
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getCode() throws IOException {
        return "line1\nline2\nline3";
      }
    };
    Region region = sourceFile.getRegion(5);
    assertNull(region);
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    assertFalse(sourceFile.isExtern());

    sourceFile = new SourceFile("testSourceFile") {
      @Override
      public boolean isExtern() {
        return true;
      }
    };
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getOriginalPath() {
        return "testOriginalPath";
      }
    };
    assertEquals("testOriginalPath", sourceFile.getOriginalPath());
    assertSame(sourceFile, sourceFile.setOriginalPath("testOriginalPath2"));
  }

  @Test
  public void testHasSourceInMemory() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    assertFalse(sourceFile.hasSourceInMemory());

    sourceFile = new SourceFile("testSourceFile") {
      @Override
      public String getCode() throws IOException {
        return "testCode";
      }
    };
    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetName() {
    SourceFile sourceFile = new SourceFile("testSourceFile");
    assertEquals("testSourceFile", sourceFile.getName());
  }

  @Test
  public void testFromFile() throws IOException {
    String code = "testCode";
    File file = new File("testFile");
    SourceFile sourceFile = SourceFile.fromFile(file, Charsets.UTF_8);
    assertSame(file.getPath(), sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test(expected = FileNotFoundException.class)
  public void testFromFileNonExistentFile() throws IOException {
    File file = new File("nonExistentFile");
    SourceFile.fromFile(file);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String code = "testCode";
    InputStream inputStream = new MockInputStream(code);
    SourceFile sourceFile = SourceFile.fromInputStream("testSourceFile", inputStream);
    assertEquals("testSourceFile", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String code = "testCode";
    Reader reader = new StringReader(code);
    SourceFile sourceFile = SourceFile.fromReader("testSourceFile", reader);
    assertEquals("testSourceFile", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() {
    String code = "testCode";
    Generator generator = mock(Generator.class);
    when(generator.getCode()).thenReturn(code);
    SourceFile sourceFile = SourceFile.fromGenerator("testSourceFile", generator);
    assertEquals("testSourceFile", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  private static class MockInputStream implements InputStream {
    private String content;

    MockInputStream(String content) {
      this.content = content;
    }

    @Override
    public int read() throws IOException {
      return (content == null) ? -1 : content.charAt(0);
    }

    @Override
    public int available() throws IOException {
      return content == null ? 0 : content.length();
    }

    @Override
    public void close() throws IOException {}

    @Override
    public synchronized void mark(int readlimit) {}

    @Override
    public synchronized void reset() {}

    @Override
    public long skip(long n) throws IOException {
      if (content == null) {
        return 0;
      }
      long toSkip = Math.min(content.length(), n);
      String skippedContent = content.substring(0, (int) toSkip);
      content = content.substring(skippedContent.length());
      return toSkip;
    }
  }
}