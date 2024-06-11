package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.io.Charsets;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

  @Test
  public void testSourceFileConstruction() {
    SourceFile src = new SourceFile("test.js");
    assertEquals("test.js", src.getName());
    assertFalse(src.isExtern());
  }

  @Test
  public void testSourceFileGetLineOffset() throws IOException {
    SourceFile src = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return "line1\nline2\nline3";
      }
    };

    assertEquals(0, src.getLineOffset(1));
    assertEquals(4, src.getLineOffset(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileGetLineOffsetInvalidLineNumber() throws IOException {
    SourceFile src = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return "line1\nline2\nline3";
      }
    };

    src.getLineOffset(5);
  }

  @Test
  public void testSourceFileGetRegion() throws IOException {
    SourceFile src = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return "line1\nline2\nline3";
      }
    };

    String region = src.getRegion(2).getText();
    assertEquals("line2", region);
  }

  @Test(expected = NullPointerException.class)
  public void testSourceFileGetRegionNullCode() {
    SourceFile src = new SourceFile("test.js");
    src.setCode(null);
    src.getRegion(1);
  }

  @Test
  public void testSourceFileFromFile() throws IOException {
    String testContent = "function test() {}";
    File testFile = new File("test.js");
    Files.write(testFile, Charsets.UTF_8, testContent);

    SourceFile src = SourceFile.fromFile("test.js", Charsets.UTF_8);
    assertEquals("test.js", src.getName());
    assertTrue(src.hasSourceInMemory());
    assertEquals("function test() {}\n", src.getLine(1).trim());
    Files.delete(testFile);
  }

  @Test(expected = FileNotFoundException.class)
  public void testSourceFileFromFileNotFound() {
    SourceFile src = SourceFile.fromFile("nonexistentfile.js", Charsets.UTF_8);
  }

  @Test
  public void testSourceFileFromCode() {
    String testContent = "function test() {}";
    SourceFile src = SourceFile.fromCode("test.js", testContent);
    assertEquals("test.js", src.getName());
    assertTrue(src.hasSourceInMemory());
    assertEquals("function test() {}\n", src.getLine(1).trim());
  }

  @Test
  public void testSourceFileFromInputStream() throws IOException {
    String testContent = "function test() {}";
    File testFile = new File("test.js");
    Files.write(testFile, Charsets.UTF_8, testContent);

    SourceFile src = SourceFile.fromInputStream("test.js", testFile.getInputStream(), Charsets.UTF_8);
    assertEquals("test.js", src.getName());
    assertTrue(src.hasSourceInMemory());
    assertEquals("function test() {}\n", src.getLine(1).trim());
    Files.delete(testFile);
  }

  @Test
  public void testSourceFileFromReader() throws IOException {
    String testContent = "function test() {}";
    File testFile = new File("test.js");
    Files.write(testFile, Charsets.UTF_8, testContent);

    Reader testReader = new FileReader(testFile);
    SourceFile src = SourceFile.fromReader("test.js", testReader, Charsets.UTF_8);
    assertEquals("test.js", src.getName());
    assertTrue(src.hasSourceInMemory());
    assertEquals("function test() {}\n", src.getLine(1).trim());
    Files.delete(testFile);
  }

  @Test
  public void testSourceFileFromGenerator() {
    String testContent = "function test() {}";
    SourceFile src = SourceFile.fromGenerator("test.js", () -> testContent);
    assertEquals("test.js", src.getName());
    assertTrue(src.hasSourceInMemory());
    assertEquals("function test() {}\n", src.getLine(1).trim());
  }
}