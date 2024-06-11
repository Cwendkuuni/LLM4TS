package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @Test
  public void testSourceFileFromFile() throws Exception {
    String fileName = "TestSourceFile.java";
    File file = new File(fileName);
    SourceFile sourceFile = new SourceFile(fileName);

    String code = Files.toString(file, Charsets.UTF_8);
    assertEquals(code, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCodeReader());
    assertEquals(1, sourceFile.getNumLines());
  }

  @Test
  public void testSourceFileFromCode() throws Exception {
    String fileName = "TestSourceFile.java";
    String code = "package test;\npublic class TestSourceFile {}";
    SourceFile sourceFile = new SourceFile(fileName, code);

    assertEquals(code, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCodeReader());
    assertEquals(2, sourceFile.getNumLines());
  }

  @Test
  public void testSourceFileFromGenerator() throws Exception {
    String fileName = "TestSourceFile.java";
    SourceFile.Generator generator = () -> "package test;\npublic class TestSourceFile {}";
    SourceFile sourceFile = new SourceFile(fileName, generator);

    assertEquals(generator.getCode(), sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCodeReader());
    assertEquals(2, sourceFile.getNumLines());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileNullName() throws Exception {
    new SourceFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileEmptyName() throws Exception {
    new SourceFile("");
  }

  @Test
  public void testSourceFileLineNumber() throws Exception {
    String fileName = "TestSourceFile.java";
    String code = "package test;\npublic class TestSourceFile {\n  int a = 1;\n  int b = 2;\n}";
    SourceFile sourceFile = new SourceFile(fileName, code);

    assertEquals(code, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCodeReader());
    assertEquals(3, sourceFile.getNumLines());
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(45, sourceFile.getLineOffset(4));
    String line1 = sourceFile.getLine(1);
    String line2 = sourceFile.getLine(2);
    String line3 = sourceFile.getLine(3);
    assertEquals("int a = 1;", line1);
    assertEquals("int b = 2;", line2);
    assertEquals("}", line3);
  }

  @Test
  public void testSourceFileRegion() throws Exception {
    String fileName = "TestSourceFile.java";
    String code = "package test;\npublic class TestSourceFile {\n  int a = 1;\n  int b = 2;\n  int c = 3;\n}";
    SourceFile sourceFile = new SourceFile(fileName, code);

    Region region = sourceFile.getRegion(3);
    assertNotNull(region);
    assertEquals(2, region.getLineNumberStart());
    assertEquals(4, region.getLineNumberEnd());
    assertEquals("int c = 3;\n", region.getCode());
  }

  @Test
  public void testSourceFileExtern() throws Exception {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new SourceFile(fileName);
    sourceFile.setIsExtern(true);

    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testSourceFileClearCachedSource() throws Exception {
    String fileName = "TestSourceFile.java";
    String code = "package test;\npublic class TestSourceFile {\n  int a = 1;\n  int b = 2;\n}";
    SourceFile sourceFile = new SourceFile(fileName, code);

    assertEquals(code, sourceFile.getCode());
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }
}