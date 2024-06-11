package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testCreation() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
    assertFalse(sf.isExtern());
    assertEquals(0, sf.getLineOffset(0));
    assertEquals(1, sf.getNumLines());
    assertNull(sf.getCode());
    assertEquals("test.js", sf.toString());
  }

  @Test
  public void testCreationWithExternFlag() {
    SourceFile sf = new SourceFile("test.js");
    sf.setIsExtern(true);
    assertTrue(sf.isExtern());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(0, sf.getLineOffset(0));
    assertEquals(code.length() + 1, sf.getLineOffset(3));
  }

  @Test
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int invalidLineNumber = sf.getNumLines() + 1;

    try {
      sf.getLineOffset(invalidLineNumber);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(code, sf.getCode());
  }

  @Test
  public void testGetCodeWithIOException() throws IOException {
    SourceFile sf = new SourceFile("test.js");
    String code = "line1\nline2\nline3";
    sf.setCode(code);

    try {
      sf.getCode();
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Reader reader = sf.getCodeReader();
    assertEquals(new StringReader(code), reader);
  }

  @Test
  public void testGetCodeReaderWithIOException() throws IOException {
    SourceFile sf = new SourceFile("test.js");
    String code = "line1\nline2\nline3";
    sf.setCode(code);

    try {
      sf.getCodeReader();
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String line1 = sf.getLine(1);
    String line2 = sf.getLine(2);
    String line3 = sf.getLine(3);

    assertEquals("line1", line1);
    assertEquals("line2", line2);
    assertEquals("line3", line3);
  }

  @Test
  public void testGetLineWithIOException() throws IOException {
    SourceFile sf = new SourceFile("test.js");
    String code = "line1\nline2\nline3";
    sf.setCode(code);

    String line1 = sf.getLine(1);

    try {
      sf.getLine(4);
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testGetLineWithNullCode() throws IOException {
    SourceFile sf = new SourceFile("test.js");

    try {
      sf.getLine(1);
      fail();
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "line1\nline2\nline3\nline4";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = sf.getRegion(3);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(4, region.getEndLineNumber());
    assertEquals("line2\nline3", region.getCode());
  }

  @Test
  public void testGetRegionWithIOException() throws IOException {
    SourceFile sf = new SourceFile("test.js");
    String code = "line1\nline2\nline3\nline4";
    sf.setCode(code);

    Region region = sf.getRegion(3);

    try {
      sf.getRegion(5);
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testGetRegionWithNullCode() throws IOException {
    SourceFile sf = new SourceFile("test.js");

    try {
      sf.getRegion(1);
      fail();
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }

  @Test
  public void testFromFile() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    SourceFile sf = SourceFile.fromFile("test.js", new File("test.js"));
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    Charset charset = Charsets.UTF_8;
    SourceFile sf = SourceFile.fromFile("test.js", new File("test.js"), charset);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    InputStream inputStream = new FileInputStream(new File("test.js"));
    SourceFile sf = SourceFile.fromInputStream("test.js", inputStream);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromInputStreamWithCharset() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    InputStream inputStream = new FileInputStream(new File("test.js"));
    Charset charset = Charsets.UTF_8;
    SourceFile sf = SourceFile.fromInputStream("test.js", inputStream, charset);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    Reader reader = new FileReader("test.js");
    SourceFile sf = SourceFile.fromReader("test.js", reader);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromReaderWithCharset() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    Reader reader = new FileReader("test.js");
    Charset charset = Charsets.UTF_8;
    SourceFile sf = SourceFile.fromReader("test.js", reader, charset);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFromGenerator() throws IOException {
    String expectedCode = "function main() {\n  print('Hello World');\n}";
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return expectedCode;
      }
    };
    SourceFile sf = SourceFile.fromGenerator("test.js", generator);
    assertEquals("test.js", sf.getName());
    assertEquals(expectedCode, sf.getCode());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function main() {\n  print('Hello World');\n}";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int[] expectedLineOffsets = new int[4];
    expectedLineOffsets[0] = 0;
    expectedLineOffsets[1] = code.length();
    expectedLineOffsets[2] = code.length() + 1;
    expectedLineOffsets[3] = expectedLineOffsets[2] + "function main()".length();

    sf.findLineOffsets();
    assertArrayEquals(expectedLineOffsets, sf.lineOffsets);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function main() {\n  print('Hello World');\n}";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    sf.setCode(code);
    assertEquals(code, sf.getCode());

    sf.clearCachedSource();

    try {
      sf.getCode();
      fail();
    } catch (IOException e) {
      // expected
    }

    String exceptionMessage = e.getMessage();
    assertTrue(exceptionMessage.contains("IOException while getting code"));
  }
}