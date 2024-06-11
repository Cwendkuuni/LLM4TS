import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.OnDisk;

import java.io.File;
import java.io.IOException;

public class SourceFileTest {

  @org.junit.Test
  public void testGetLine() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    // Test from code
    SourceFile srcFileFromCode = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals("var x = 1;\n", srcFileFromCode.getLine(1));
    assertEquals("return x + y;\n", srcFileFromCode.getLine(4));
    assertEquals(null, srcFileFromCode.getLine(5));

    // Test from file
    File testFile = new File("src/test/java/com/google/javascript/jscomp/testFile.js");
    Files.write(testFile, code.getBytes(), Charsets.UTF_8);
    SourceFile srcFileFromFile = new OnDisk(testFile);
    assertEquals("var x = 1;\n", srcFileFromFile.getLine(1));
    assertEquals("return x + y;\n", srcFileFromFile.getLine(4));
    assertEquals(null, srcFileFromFile.getLine(5));
    Files.delete(testFile);
  }

  @org.junit.Test
  public void testGetRegion() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    // Test from code
    SourceFile srcFileFromCode = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region regionFromCode = srcFileFromCode.getRegion(3);
    assertEquals(2, regionFromCode.getStartLine());
    assertEquals(3, regionFromCode.getEndLine());
    assertEquals("  var y = 2;\n", regionFromCode.getSource());

    // Test from file
    File testFile = new File("src/test/java/com/google/javascript/jscomp/testFile.js");
    Files.write(testFile, code.getBytes(), Charsets.UTF_8);
    SourceFile srcFileFromFile = new OnDisk(testFile);
    Region regionFromFile = srcFileFromFile.getRegion(3);
    assertEquals(2, regionFromFile.getStartLine());
    assertEquals(3, regionFromFile.getEndLine());
    assertEquals("  var y = 2;\n", regionFromFile.getSource());
    Files.delete(testFile);
  }

  @org.junit.Test
  public void testGetLineOffset() {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}\n";

    // Test from code
    SourceFile srcFileFromCode = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(0, srcFileFromCode.getLineOffset(1));
    assertEquals(3, srcFileFromCode.getLineOffset(3));
    assertEquals(5, srcFileFromCode.getLineOffset(4));

    // Test from file
    File testFile = new File("src/test/java/com/google/javascript/jscomp/testFile.js");
    Files.write(testFile, code.getBytes(), Charsets.UTF_8);
    SourceFile srcFileFromFile = new OnDisk(testFile);
    assertEquals(0, srcFileFromFile.getLineOffset(1));
    assertEquals(3, srcFileFromFile.getLineOffset(3));
    assertEquals(5, srcFileFromFile.getLineOffset(4));
    Files.delete(testFile);
  }

  @org.junit.Test
  public void testNumLines() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    // Test from code
    SourceFile srcFileFromCode = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(4, srcFileFromCode.getNumLines());

    // Test from file
    File testFile = new File("src/test/java/com/google/javascript/jscomp/testFile.js");
    Files.write(testFile, code.getBytes(), Charsets.UTF_8);
    SourceFile srcFileFromFile = new OnDisk(testFile);
    assertEquals(4, srcFileFromFile.getNumLines());
    Files.delete(testFile);
  }
}