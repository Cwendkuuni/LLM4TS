package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testutils.CommonUtils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Unit tests for {@link SourceFile}.
 */
public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    String fileContent = Files.toString(new File("test.js"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    String expectedLine = "function testFunction() {}";
    int lineNumber = 3;

    String actualLine = sourceFile.getLine(lineNumber);
    assertEquals("Expected the line number 3 to be: " + expectedLine,
        expectedLine, actualLine);
  }

  @Test
  public void testGetLineNotFound() throws IOException {
    String fileContent = Files.toString(new File("test.js"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int lineNumber = 10;
    String expectedNull = null;
    String actualLine = sourceFile.getLine(lineNumber);
    assertEquals("Expected null for line number 10", expectedNull, actualLine);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileContent =
        "function testFunction() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int lineNumber = 3;
    Region expectedRegion = new Region(1, 3, "var b = 2;");

    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals("Expected region with start line, end line and content",
        expectedRegion, actualRegion);
  }

  @Test
  public void testGetRegionNotFound() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int lineNumber = 10;
    Region expectedNull = null;

    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals("Expected null for line number 10", expectedNull, actualRegion);
  }

  @Test
  public void testGetLineAndRegionFromFile() throws IOException {
    String fileContent = Files.toString(new File("test.js"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new SourceFile("test.js");

    String expectedLine = "return a + b;";
    int lineNumber = 4;

    String actualLine = sourceFile.getLine(lineNumber);
    assertEquals("Expected the line number 4 to be: " + expectedLine,
        expectedLine, actualLine);

    Region expectedRegion = new Region(1, 4, "return a + b;");

    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals("Expected region with start line, end line and content",
        expectedRegion, actualRegion);
  }

  @Test
  public void testGetLineAndRegionFromCode() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    String expectedLine = "return a + b;";
    int lineNumber = 4;

    String actualLine = sourceFile.getLine(lineNumber);
    assertEquals("Expected the line number 4 to be: " + expectedLine,
        expectedLine, actualLine);

    Region expectedRegion = new Region(1, 4, "return a + b;");

    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals("Expected region with start line, end line and content",
        expectedRegion, actualRegion);
  }

  @Test
  public void testGetNumLines() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int expectedNumLines = 2;

    int actualNumLines = sourceFile.getNumLines();
    assertEquals("Expected the number of lines to be: " + expectedNumLines,
        expectedNumLines, actualNumLines);
  }

  @Test
  public void testGetOriginalPath() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    String expectedOriginalPath = "test.js";

    String actualOriginalPath = sourceFile.getOriginalPath();
    assertEquals("Expected the original path to be: " + expectedOriginalPath,
        expectedOriginalPath, actualOriginalPath);
  }

  @Test
  public void testToString() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    String expectedString = "test.js";

    String actualString = sourceFile.toString();
    assertEquals("Expected the string representation to be: " + expectedString,
        expectedString, actualString);
  }

  @Test
  public void testIsExtern() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    boolean expectedIsExtern = false;

    boolean actualIsExtern = sourceFile.isExtern();
    assertEquals("Expected the source file to not be extern: " + actualIsExtern,
        expectedIsExtern, actualIsExtern);
  }

  @Test
  public void testSetIsExtern() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    sourceFile.setIsExtern(true);

    boolean expectedIsExtern = true;

    boolean actualIsExtern = sourceFile.isExtern();
    assertEquals("Expected the source file to be extern: " + actualIsExtern,
        expectedIsExtern, actualIsExtern);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    String code = sourceFile.getCode();
    sourceFile.clearCachedSource();

    String expectedNull = null;

    String actualCode = sourceFile.getCode();
    assertEquals("Expected the code to be null after clearCachedSource: "
        + actualCode, expectedNull, actualCode);
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    boolean expectedHasSourceInMemory = true;

    boolean actualHasSourceInMemory = sourceFile.hasSourceInMemory();
    assertEquals("Expected the source file to have source in memory: "
        + actualHasSourceInMemory, expectedHasSourceInMemory, actualHasSourceInMemory);
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String fileContent = "function testFunction() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a + b;";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int lineNumber = 3;
    int expectedOffset = 23;

    int actualOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals("Expected the offset for line number 3 to be: " + expectedOffset,
        expectedOffset, actualOffset);
  }

  @Test
  public void testGetLineOffsetNotFound() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    int lineNumber = 10;
    int expectedOffset = -1;

    int actualOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals("Expected the offset for line number 10 to be: " + expectedOffset,
        expectedOffset, actualOffset);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return fileContent;
      }
    };

    Reader expectedReader = new StringReader(fileContent);

    Reader actualReader = sourceFile.getCodeReader();

    CommonUtils.assertReaderEquals(expectedReader, actualReader);
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = Files.toString(new File("test.js"), StandardCharsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromFile("test.js");

    String expectedCode = fileContent;

    String actualCode = sourceFile.getCode();
    assertEquals("Expected the code to be: " + expectedCode, expectedCode, actualCode);

    String expectedFileName = "test.js";

    String actualFileName = sourceFile.getName();
    assertEquals("Expected the file name to be: " + expectedFileName,
        expectedFileName, actualFileName);
  }

  @Test
  public void testFromCode() throws IOException {
    String fileContent = "function testFunction() {}";
    SourceFile sourceFile = SourceFile.fromCode("test.js", fileContent);

    String expectedCode = fileContent;

    String actualCode = sourceFile.getCode();
    assertEquals("Expected the code to be: " + expectedCode, expectedCode, actualCode);

    String expectedFileName = "test.js";

    String actualFileName = sourceFile.getName();
    assertEquals("Expected the file name to be: " + expectedFileName,
        expectedFileName, actualFileName);
  }
}