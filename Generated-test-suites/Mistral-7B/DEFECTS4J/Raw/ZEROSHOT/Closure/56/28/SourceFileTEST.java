import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;
  private Generator generatorMock;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("testFileName");
    generatorMock = mock(Generator.class);
    when(generatorMock.getCode()).thenReturn("testCode");
  }

  @After
  public void tearDown() {
    if (sourceFile != null) {
      sourceFile.clearCachedSource();
    }
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(lineOffsets(1, 13), sourceFile.getLineOffset(3));
  }

  @Test
  public void testGetCode() throws IOException {
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }

      @Override
      public Reader getCodeReader() throws IOException {
        return new StringReader("testCode");
      }
    };

    assertNull(sourceFile.getCode());
    assertEquals("testCode", sourceFile.getCode(StandardCharsets.UTF_8));
  }

  @Test
  public void testGetCodeReader() throws IOException {
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }

      @Override
      public Reader getCodeReader() throws IOException {
        return null;
      }
    };

    assertNull(sourceFile.getCodeReader());

    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }

      @Override
      public Reader getCodeReader() throws IOException {
        return new StringReader("testCode");
      }
    };

    assertEquals("testCode", sourceFile.getCode(StandardCharsets.UTF_8));
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals("line1", sourceFile.getLine(1));
    assertEquals("line3", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(4));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = sourceFile.getRegion(3);
    assertEquals(2, region.getEndLine());
    assertEquals("line2\nline3", region.getSource());
  }

  @Test
  public void testGetName() {
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    sourceFile = new SourceFile("testFileName");
    assertFalse(sourceFile.isExtern());

    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(code, sourceFile.getCode(StandardCharsets.UTF_8));

    sourceFile.clearCachedSource();

    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertTrue(sourceFile.hasSourceInMemory());

    sourceFile.clearCachedSource();

    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = "testFileContent";

    SourceFile sourceFile = SourceFile.fromFile("testFileName.js",
        StandardCharsets.UTF_8);
    File testFile = new File("testFileName.js");
    Files.write(testFile, fileContent.getBytes(StandardCharsets.UTF_8));

    sourceFile = SourceFile.fromFile("testFileName.js");

    assertEquals("testFileName.js", sourceFile.getName());
    assertEquals(fileContent, sourceFile.getCode(StandardCharsets.UTF_8));

    Files.delete(testFile);
  }

  @Test
  public void testFromCode() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("testFileName.js", "testCode");

    assertEquals("testFileName.js", sourceFile.getName());
    assertEquals("testCode", sourceFile.getCode(StandardCharsets.UTF_8));
  }

  @Test
  public void testFromInputStream() throws IOException {
    String inputCode = "testInputCode";

    SourceFile sourceFile = SourceFile.fromInputStream("testFileName.js",
        new ByteArrayInputStream(inputCode.getBytes(StandardCharsets.UTF_8)));

    assertEquals("testFileName.js", sourceFile.getName());
    assertEquals(inputCode, sourceFile.getCode(StandardCharsets.UTF_8));
  }

  @Test
  public void testFromReader() throws IOException {
    String readerCode = "testReaderCode";

    SourceFile sourceFile = SourceFile.fromReader("testFileName.js",
        new StringReader(readerCode));

    assertEquals("testFileName.js", sourceFile.getName());
    assertEquals(readerCode, sourceFile.getCode(StandardCharsets.UTF_8));
  }

  @Test
  public void testFromGenerator() throws IOException {
    sourceFile = new SourceFile("testFileName") {
      @Override
      public String getCode() throws IOException {
        throw new IOException("Cannot read from file.");
      }
    };

    sourceFile = SourceFile.fromGenerator("testFileName.js", generatorMock);

    assertEquals("testFileName.js", sourceFile.getName());
    assertEquals("testCode", sourceFile.getCode(StandardCharsets.UTF_8));
  }

  private int[] lineOffsets(int lines, int length) {
    int[] lineOffsets = new int[lines];
    for (int i = 0; i < lines; i++) {
      lineOffsets[i] = i == 0 ? 0 : lineOffsets[i - 1] + length;
    }
    return lineOffsets;
  }
}

class ByteArrayInputStream extends InputStream {

  private final byte[] bytes;
  private int index;

  public ByteArrayInputStream(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public int read() throws IOException {
    if (index < bytes.length) {
      return bytes[index++];
    }
    return -1;
  }

  @Override
  public int available() throws IOException {
    return bytes.length - index;
  }
}