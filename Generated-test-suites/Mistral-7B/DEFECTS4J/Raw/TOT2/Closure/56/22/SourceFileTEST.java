import org.junit.Test;
import com.google.javascript.jscomp.SourceFile;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testSourceFileWithValidFileName() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    assertEquals("testFile.java", sourceFile.getName());
  }

  @Test
  public void testSourceFileWithExternFlagSet() {
    SourceFile sourceFile = new SourceFile("externFile.java");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.java");
    String code = "class TestClass {\n" +
        "  function test() {\n" +
        "    return 42;\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);
    assertEquals(code, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeThrowsIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.java");
    sourceFile.setCode(null);
    sourceFile.getCode();
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    String code = "class TestClass {\n" +
        "  function test() {\n" +
        "    return 42;\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);
    assertEquals("class TestClass {\n", sourceFile.getLine(1));
    assertEquals("    return 42;\n", sourceFile.getLine(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineThrowsIllegalArgumentException() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    sourceFile.getLine(0);
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    String code = "class TestClass {\n" +
        "  function test() {\n" +
        "    return 42;\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);
    Region region = sourceFile.getRegion(4);
    assertEquals("  function test() {\n    return 42;\n", region.getCode());
  }

  @Test(expected = NullPointerException.class)
  public void testGetRegionThrowsNullPointerException() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    sourceFile.getRegion(0);
  }
}