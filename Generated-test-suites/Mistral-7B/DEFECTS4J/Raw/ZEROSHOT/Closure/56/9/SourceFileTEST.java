import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceFile.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testBasicFunctionality() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);
    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";
    sourceFile.setCode(code);

    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("testFile.js", sourceFile.toString());
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(1, sourceFile.getNumLines());

    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n  return 1;\n}", line1);

    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals("function test() {\n  return 1;\n}", region.getSourceCode());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);

    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";
    sourceFile.setCode(code);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(2, lineOffsets.length);
    assertEquals(1, lineOffsets[0]);
    assertEquals(13, lineOffsets[1]);

    char[] testCodeChars = code.toCharArray();
    int pos = 0;
    for (int lineNumber = 1; lineNumber <= 2; lineNumber++) {
      int nextpos = findNextNewline(testCodeChars, pos);
      int lineLength = lineNumber == 1 ? testCodeChars.length : nextpos - pos;
      int expectedLineOffset = pos + lineLength;
      assertEquals(expectedLineOffset, lineOffsets[lineNumber - 1]);
      pos = nextpos;
    }
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);

    String code = "function test1() {\n" +
        "  return 1;\n" +
        "}\n" +
        "\n" +
        "function test2() {\n" +
        "  return 2;\n" +
        "}";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("function test2() {\n  return 2;\n}", region.getSourceCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);

    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";
    sourceFile.setCode(code);

    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLineNumberOutOfBounds() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);

    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";
    sourceFile.setCode(code);

    int lineOffset = sourceFile.getLineOffset(5);
  }

  private int findNextNewline(char[] chars, int pos) {
    int i = pos;
    while (i < chars.length) {
      if (chars[i] == '\n') {
        return i;
      }
      i++;
    }
    return chars.length;
  }
}