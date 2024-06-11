import static org.junit.Assert.*;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

import java.io.File;
import java.io.IOException;

public class SourceFileTest {

  @org.junit.Test
  public void testBasicFunctionality() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("test", sourceFile.getLine(1).trim());
    assertEquals(42, Integer.parseInt(sourceFile.getLine(2).trim()));

    String expectedCode = Files.toString(new File("testFile.js"), Charsets.UTF_8);
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @org.junit.Test
  public void testReadingFromFile() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";

    Files.write(new File(fileName), expectedCode.getBytes(), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile(fileName);

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("test", sourceFile.getLine(1).trim());
    assertEquals(42, Integer.parseInt(sourceFile.getLine(2).trim()));

    Files.delete(new File(fileName));
  }

  @org.junit.Test
  public void testReadingFromInputStream() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromInputStream(fileName,
        new FileInputStream(fileName));

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("test", sourceFile.getLine(1).trim());
    assertEquals(42, Integer.parseInt(sourceFile.getLine(2).trim()));

    Files.delete(new File(fileName));
  }

  @org.junit.Test
  public void testReadingFromGenerator() throws IOException {
    String fileName = "testFile.js";

    SourceFileGenerator generator = new SourceFileGenerator() {
      @Override
      public String getCode() {
        return "function test() {\n" +
            "  return 42;\n" +
            "}";
      }
    };

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("test", sourceFile.getLine(1).trim());
    assertEquals(42, Integer.parseInt(sourceFile.getLine(2).trim()));
  }

  @org.junit.Test
  public void testGettingLinesAndRegions() throws IOException {
    String fileName = "testFile.js";
    String code = "function test1() {\n" +
        "  function test2() {\n" +
        "    function test3() {\n" +
        "      return 1;\n" +
        "    }\n" +
        "    return test3();\n" +
        "  }\n" +
        "  return test2();\n" +
        "}\n" +
        "\n" +
        "function test4() {\n" +
        "  return 2;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals(5, sourceFile.getNumLines());

    Region region1 = sourceFile.getRegion(1);
    assertEquals(1, region1.getStartLineNumber());
    assertEquals(1, region1.getEndLineNumber());
    assertEquals("function test1() {\n", region1.getSourceCode());

    Region region2 = sourceFile.getRegion(2);
    assertEquals(1, region2.getStartLineNumber());
    assertEquals(2, region2.getEndLineNumber());
    assertEquals("  function test2() {\n", region2.getSourceCode());

    Region region3 = sourceFile.getRegion(3);
    assertEquals(2, region3.getStartLineNumber());
    assertEquals(3, region3.getEndLineNumber());
    assertEquals("    function test3() {\n", region3.getSourceCode());

    Region region4 = sourceFile.getRegion(4);
    assertEquals(3, region4.getStartLineNumber());
    assertEquals(4, region4.getEndLineNumber());
    assertEquals("      return 1;\n", region4.getSourceCode());

    Region region5 = sourceFile.getRegion(5);
    assertEquals(4, region5.getStartLineNumber());
    assertEquals(5, region5.getEndLineNumber());
    assertEquals("  return test2();\n}", region5.getSourceCode());
  }

  @org.junit.Test
  public void testReadingFromFileWithCharset() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";

    Files.write(new File(fileName), code.getBytes(), Charsets.UTF_16);

    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_16);

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("test", sourceFile.getLine(1).trim());
    assertEquals(42, Integer.parseInt(sourceFile.getLine(2).trim()));

    Files.delete(new File(fileName));
  }

  @org.junit.Test
  public void testGettingLinesAndRegionsWithCharset() throws IOException {
    String fileName = "testFile.js";
    String code = "function test1() {\n" +
        "  function test2() {\n" +
        "    function test3() {\n" +
        "      return 1;\n" +
        "    }\n" +
        "    return test3();\n" +
        "  }\n" +
        "  return test2();\n" +
        "}\n" +
        "\n" +
        "function test4() {\n" +
        "  return 2;\n" +
        "}";

    Files.write(new File(fileName), code.getBytes(), Charsets.UTF_16);

    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_16);

    assertEquals(5, sourceFile.getNumLines());

    Region region1 = sourceFile.getRegion(1);
    assertEquals(1, region1.getStartLineNumber());
    assertEquals(1, region1.getEndLineNumber());
    assertEquals("function test1() {\n", region1.getSourceCode());

    Region region2 = sourceFile.getRegion(2);
    assertEquals(1, region2.getStartLineNumber());
    assertEquals(2, region2.getEndLineNumber());
    assertEquals("  function test2() {\n", region2.getSourceCode());

    Region region3 = sourceFile.getRegion(3);
    assertEquals(2, region3.getStartLineNumber());
    assertEquals(3, region3.getEndLineNumber());
    assertEquals("    function test3() {\n", region3.getSourceCode());

    Region region4 = sourceFile.getRegion(4);
    assertEquals(3, region4.getStartLineNumber());
    assertEquals(4, region4.getEndLineNumber());
    assertEquals("      return 1;\n", region4.getSourceCode());

    Region region5 = sourceFile.getRegion(5);
    assertEquals(4, region5.getStartLineNumber());
    assertEquals(5, region5.getEndLineNumber());
    assertEquals("  return test2();\n}", region5.getSourceCode());

    Files.delete(new File(fileName));
  }

  @org.junit.Test
  public void testGettingOriginalPath() throws IOException {
    String fileName = "testFile.js";
    String originalPath = "/path/to/testFile.js";

    SourceFile sourceFile = SourceFile.fromFile(fileName, originalPath);

    assertEquals(originalPath, sourceFile.getOriginalPath());
  }

  @org.junit.Test
  public void testClearCachedSource() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, expectedCode);

    String cachedCode = sourceFile.getCode();
    assertEquals(expectedCode, cachedCode);

    sourceFile.clearCachedSource();

    cachedCode = sourceFile.getCode();
    assertNull(cachedCode);

    expectedCode = "function test() {\n" +
        "  return 43;\n" +
        "}";
    sourceFile.setCode(expectedCode);

    cachedCode = sourceFile.getCode();
    assertEquals(expectedCode, cachedCode);
  }

  interface SourceFileGenerator {
    String getCode();
  }
}