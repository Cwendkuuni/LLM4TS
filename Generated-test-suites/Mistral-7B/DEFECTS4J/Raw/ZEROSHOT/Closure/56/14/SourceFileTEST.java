import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "testSourceFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    assertEquals("testSourceFile.js", sourceFile.getName());
    assertEquals(charset, sourceFile.getCharset());
    assertNull(sourceFile.getCode());

    String code = sourceFile.getCode();
    assertEquals(Files.toString(file, charset), code);
  }

  @Test
  public void testSourceFileFromInputStream() throws IOException {
    String fileName = "testSourceFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    InputStream inputStream = Files.newInputStream(file);
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, charset, inputStream);

    assertEquals("testSourceFile.js", sourceFile.getName());
    assertEquals(charset, sourceFile.getCharset());
    assertEquals(Files.toString(file, charset), sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromCode() {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n  return 'Hello World';\n}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("testSourceFile.js", sourceFile.getName());
    assertEquals(null, sourceFile.getCharset());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromGenerator() {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n  return 'Hello World';\n}";

    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    assertEquals("testSourceFile.js", sourceFile.getName());
    assertEquals(null, sourceFile.getCharset());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "testGetLine.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    String expectedLine1 = "function test() {\n  return 1;\n}";
    String expectedLine2 = "return 2;\n";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(3));

    fail("Expected an IOException when requesting line 100");
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "testGetRegion.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    String expectedRegion1 = "function test() {\n  return 1;\n}";
    String expectedRegion2 = "  return 2;\n";

    Region region1 = sourceFile.getRegion(1);
    assertEquals(1, region1.getStartLineNumber());
    assertEquals(1, region1.getEndLineNumber());
    assertEquals(expectedRegion1, region1.getSourceCode());

    Region region2 = sourceFile.getRegion(3);
    assertEquals(3, region2.getStartLineNumber());
    assertEquals(3, region2.getEndLineNumber());
    assertEquals(expectedRegion2, region2.getSourceCode());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String fileName = "testGetLineOffset.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    int expectedLineOffset1 = 0;
    int expectedLineOffset2 = 10;

    assertEquals(expectedLineOffset1, sourceFile.getLineOffset(1));
    assertEquals(expectedLineOffset2, sourceFile.getLineOffset(4));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String fileName = "testGetNumLines.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    int expectedNumLines = 5;
    assertEquals(expectedNumLines, sourceFile.getNumLines());
  }
}