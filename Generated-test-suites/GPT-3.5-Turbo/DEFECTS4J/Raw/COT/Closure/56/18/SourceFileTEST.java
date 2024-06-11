import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("fileName");
    assertEquals("fileName", sourceFile.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithEmptyFileName() {
    SourceFile sourceFile = new SourceFile("");
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("fileName");
    int offset = sourceFile.getLineOffset(1);
    assertEquals(0, offset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("fileName");
    sourceFile.getLineOffset(0);
  }

  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("fileName");
    assertEquals(1, sourceFile.getNumLines());
  }

  @Test
  public void testClearCachedSource() {
    SourceFile sourceFile = new SourceFile("fileName");
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCodeNoCache());
  }

  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("fileName");
    assertEquals("fileName", sourceFile.toString());
  }

  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("fileName", "code");
    assertEquals("fileName", sourceFile.getName());
    assertEquals("code", sourceFile.getCodeNoCache());
  }

  @Test
  public void testFromInputStream() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("code".getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream("fileName", inputStream);
    assertEquals("fileName", sourceFile.getName());
    assertEquals("code", sourceFile.getCodeNoCache());
  }

  @Test
  public void testFromReader() throws IOException {
    Reader reader = new StringReader("code");
    SourceFile sourceFile = SourceFile.fromReader("fileName", reader);
    assertEquals("fileName", sourceFile.getName());
    assertEquals("code", sourceFile.getCodeNoCache());
  }

  @Test
  public void testFromGenerator() {
    SourceFile.Generator generator = new SourceFile.Generator() {
      public String getCode() {
        return "code";
      }
    };
    SourceFile sourceFile = SourceFile.fromGenerator("fileName", generator);
    assertEquals("fileName", sourceFile.getName());
    assertEquals("code", sourceFile.getCodeNoCache());
  }

}