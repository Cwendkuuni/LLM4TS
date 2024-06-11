package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test cases for the SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testNewSourceFile() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
    assertFalse(sf.isExtern());
    assertNull(sf.getOriginalPath());
    assertNull(sf.getCode());
  }

  @Test
  public void testGetLineOffset() {
    String code = "a\nb\nc\nd";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int[] expected = { 0, 1, 2, 3, 4 };
    for (int i = 1; i <= code.split("\n").length; i++) {
      int lineNumber = i;
      int offset = sf.getLineOffset(lineNumber);
      assertEquals(offset, expected[lineNumber - 1]);
    }
  }

  @Test
  public void testGetLine() {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String expected = "a";
    int lineNumber = 1;
    String actual = sf.getLine(lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetRegion() {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String expected = "a\nb";
    int lineNumber = 1;
    Region actual = sf.getRegion(lineNumber);
    assertEquals(expected, actual.getCode());
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String expected = "a\nb";
    String actual = sf.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    StringReader expected = new StringReader("a\nb");
    Reader actual = sf.getCodeReader();
    assertEquals(expected, actual);
  }

  @Test
  public void testHasSourceInMemory() {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertTrue(sf.hasSourceInMemory());
  }

  @Test
  public void testGetName() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
  }

  @Test
  public void testIsExtern() {
    SourceFile sf = new SourceFile("test.js");
    assertFalse(sf.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    SourceFile sf = new SourceFile("test.js");
    sf.setIsExtern(true);
    assertTrue(sf.isExtern());
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sf = new SourceFile("test.js");
    assertNull(sf.getOriginalPath());
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sf = new SourceFile("test.js");
    sf.setOriginalPath("/path/to/test.js");
    assertEquals("/path/to/test.js", sf.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String code = "a\nb";
    SourceFile sf = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String expected = null;
    String actual = sf.getCode();
    assertEquals(expected, actual);

    sf.clearCachedSource();

    expected = "a\nb";
    actual = sf.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    String code = "a\nb";

    SourceFile sf = SourceFile.fromFile(fileName);
    String expected = Files.toString(new File(fileName), Charsets.UTF_8);
    assertEquals(code, expected);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test.js";
    String code = "a\nb";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(code.getBytes());
    InputStream is = new ByteArrayInputStream(baos.toByteArray());

    SourceFile sf = SourceFile.fromInputStream(fileName, is);
    String expected = new String(is.readAllBytes(), 0, is.available());
    assertEquals(code, expected);
  }
}

class Region {
  private final int startLine, endLine, codeLength;
  private final String code;

  public Region(int startLine, int endLine, String code) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.code = code;
    this.codeLength = code.length();
  }

  public int getStartLine() {
    return startLine;
  }

  public int getEndLine() {
    return endLine;
  }

  public String getCode() {
    return code;
  }

  public int length() {
    return codeLength;
  }
}