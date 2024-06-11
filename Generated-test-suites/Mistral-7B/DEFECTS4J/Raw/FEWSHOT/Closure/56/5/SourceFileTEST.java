package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.commons.cli.SourceFile;
import org.apache.commons.cli.SourceFile.Generator;
import org.apache.commons.cli.SourceFile.OnDisk;
import org.apache.commons.cli.SourceFile.Preloaded;
import org.apache.commons.cli.SourceFile.PreloadedWithOriginalPath;
import org.apache.commons.cli.SourceFile.GeneratedWithGenerator;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testPreloaded() {
    String code = "function foo() {}";
    Preloaded preloaded = new Preloaded("Test.js", code);

    assertEquals("Test.js", preloaded.getName());
    assertEquals(code, preloaded.getCode());
    assertNull(preloaded.getOriginalPath());

    assertEquals(code, preloaded.getCode());
    assertEquals(code.getBytes(), preloaded.getCodeBytes());
    assertEquals(code, preloaded.getCodeNoCache());
  }

  @Test
  public void testPreloadedWithOriginalPath() {
    String code = "function foo() {}";
    PreloadedWithOriginalPath preloadedWithOriginalPath = new PreloadedWithOriginalPath(
        "Test.js", code, "path/to/original/file.js");

    assertEquals("Test.js", preloadedWithOriginalPath.getName());
    assertEquals(code, preloadedWithOriginalPath.getCode());
    assertEquals("path/to/original/file.js", preloadedWithOriginalPath.getOriginalPath());

    assertEquals(code, preloadedWithOriginalPath.getCode());
    assertEquals(code.getBytes(), preloadedWithOriginalPath.getCodeBytes());
    assertEquals(code, preloadedWithOriginalPath.getCodeNoCache());
  }

  @Test
  public void testOnDisk() throws IOException {
    String fileName = "Test.js";
    String code = "function foo() {}";

    try {
      Files.deleteIfExists(new File(fileName).toPath());
    } catch (Exception e) {
      // Ignore
    }

    OnDisk onDisk = new OnDisk(new File(fileName));
    assertEquals(fileName, onDisk.getName());
    assertNull(onDisk.getCode());
    assertNull(onDisk.getOriginalPath());

    Files.write(new File(fileName).toPath(), code.getBytes());

    assertEquals(code, onDisk.getCode());
    assertEquals(code.getBytes(), onDisk.getCodeBytes());
    assertEquals(code, onDisk.getCodeNoCache());

    Files.deleteIfExists(new File(fileName).toPath());
  }

  @Test
  public void testOnDiskWithCharset() throws IOException {
    String fileName = "Test.js";
    String code = "function foo() {}";
    Charset charset = Charset.forName("UTF-8");

    try {
      Files.deleteIfExists(new File(fileName).toPath());
    } catch (Exception e) {
      // Ignore
    }

    OnDisk onDisk = new OnDisk(new File(fileName), charset);
    assertEquals(fileName, onDisk.getName());
    assertNull(onDisk.getCode());
    assertNull(onDisk.getOriginalPath());

    Files.write(new File(fileName).toPath(), code.getBytes());

    assertEquals(code, onDisk.getCode());
    assertEquals(code.getBytes(), onDisk.getCodeBytes());
    assertEquals(code, onDisk.getCodeNoCache());

    assertEquals(charset, onDisk.getCharset());

    Files.deleteIfExists(new File(fileName).toPath());
  }

  @Test
  public void testGenerated() {
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return "function foo() {}";
      }
    };
    Generated generated = new Generated("Test.js", generator);

    assertEquals("Test.js", generated.getName());
    assertNull(generated.getCode());
    assertNull(generated.getOriginalPath());

    assertEquals("function foo() {}", generated.getCode());
    assertEquals("function foo() {}".getBytes(), generated.getCodeBytes());
    assertEquals("function foo() {}", generated.getCodeNoCache());
  }

  @Test
  public void testGeneratedString() {
    String code = "function foo() {}";
    GeneratedWithGenerator generated = new GeneratedWithGenerator("Test.js", code);

    assertEquals("Test.js", generated.getName());
    assertEquals(code, generated.getCode());
    assertNull(generated.getOriginalPath());

    assertEquals(code, generated.getCode());
    assertEquals(code.getBytes(), generated.getCodeBytes());
    assertEquals(code, generated.getCodeNoCache());
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("Test.js");
    assertFalse(sourceFile.isExtern());

    SourceFile sourceFileExtern = new SourceFile("Test.js");
    sourceFileExtern.setIsExtern(true);
    assertTrue(sourceFileExtern.isExtern());
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sourceFile = new SourceFile("Test.js");
    sourceFile.setOriginalPath("/path/to/original/file.js");

    assertEquals("/path/to/original/file.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String fileName = "Test.js";
    String code = "function foo() {}";

    try {
      Files.deleteIfExists(new File(fileName).toPath());
    } catch (Exception e) {
      // Ignore
    }

    OnDisk onDisk = new OnDisk(new File(fileName));

    Files.write(new File(fileName).toPath(), code.getBytes());

    assertEquals(code, onDisk.getCode());
    assertEquals(code.getBytes(), onDisk.getCodeBytes());
    assertEquals(code, onDisk.getCodeNoCache());

    onDisk.clearCachedSource();

    assertEquals(null, onDisk.getCode());
    assertEquals(null, onDisk.getCodeBytes());
    assertEquals(null, onDisk.getCodeNoCache());

    Files.deleteIfExists(new File(fileName).toPath());
  }

  @Test
  public void testGetLine() {
    String fileName = "Test.js";
    String code = "function foo() {\n  bar();\n}";

    try {
      Files.deleteIfExists(new File(fileName).toPath());
    } catch (Exception e) {
      // Ignore
    }

    OnDisk onDisk = new OnDisk(new File(fileName));

    Files.write(new File(fileName).toPath(), code.getBytes());

    assertEquals("function foo() {\n  bar();\n}", onDisk.getCode());

    assertEquals("function foo() {\n", onDisk.getLine(1));
    assertEquals("  bar();\n}", onDisk.getLine(2));
    assertEquals("}", onDisk.getLine(3));
    assertNull(onDisk.getLine(4));

    Files.deleteIfExists(new File(fileName).toPath());
  }

  @Test
  public void testGetRegion() {
    String fileName = "Test.js";
    String code = "function foo() {\n  bar();\n  baz();\n}";

    try {
      Files.deleteIfExists(new File(fileName).toPath());
    } catch (Exception e) {
      // Ignore
    }

    OnDisk onDisk = new OnDisk(new File(fileName));

    Files.write(new File(fileName).toPath(), code.getBytes());

    Region region = onDisk.getRegion(3);

    assertEquals(2, region.getStartLineNumber());
    assertEquals(4, region.getEndLineNumber());
    assertEquals("  baz();\n", region.getSource());

    Files.deleteIfExists(new File(fileName).toPath());
  }
}