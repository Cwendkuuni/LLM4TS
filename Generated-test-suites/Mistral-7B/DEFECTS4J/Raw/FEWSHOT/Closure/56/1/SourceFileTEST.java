package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.jscomp.SourceFile.OnDisk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    File testFile = new File(fileName);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    String code = sourceFile.getCode();
    assertEquals(Files.toString(testFile, charset), code);
  }

  @Test
  public void testFromGenerator() {
    String fileName = "test.js";
    String code = "function test() {}";
    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    String sourceCode = sourceFile.getCode();
    assertEquals(code, sourceCode);
  }

  @Test
  public void testOnDisk() throws IOException {
    String fileName = "test.js";
    File testFile = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = new OnDisk(testFile, charset) {
      @Override
      protected String inputCharset = "UTF-8";
    };

    String code = sourceFile.getCode();
    assertEquals(Files.toString(testFile, charset), code);
  }

  @Test
  public void testPreloaded() {
    String fileName = "test.js";
    String code = "function test() {}";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String sourceCode = sourceFile.getCode();
    assertEquals(code, sourceCode);
  }
}