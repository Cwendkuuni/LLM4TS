import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.OnDisk;
import com.google.javascript.rhino.FileUtil;

public class SourceFileTest {

  @Test
  public void testOnDiskSourceFile() throws Exception {
    String filePath = "test/SourceFileTest.java";
    File testFile = FileUtil.toTempFile(FileUtil.newJavaSourceFile(""), "SourceFileTest");
    OnDisk sourceFile = new OnDisk(testFile);
    sourceFile.setCharset(Charsets.UTF_8);

    String code = "class Test {\n"
        + "  static void main(String[] args) {\n"
        + "    System.out.println(\"Hello World!\");\n"
        + "  }\n"
        + "}";
    Files.write(testFile.toPath(), code.getBytes(), Charsets.UTF_8);

    Assertions.assertEquals("class Test {\n" + code + "}\n", sourceFile.getCode());
    Assertions.assertEquals("Test", sourceFile.getName());
    Assertions.assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testPreloadedSourceFile() {
    SourceFile preloadedSourceFile = new SourceFile("test/PreloadedSourceFile.java") {
      @Override
      public String getCode() {
        return "class Test {\n"
            + "  static void main(String[] args) {\n"
            + "    System.out.println(\"Hello World!\");\n"
            + "  }\n"
            + "}";
      }
    };

    Assertions.assertEquals("class Test {\n" + preloadedSourceFile.getCode() + "}\n", preloadedSourceFile.getCode());
    Assertions.assertEquals("test/PreloadedSourceFile.java", preloadedSourceFile.getName());
    Assertions.assertFalse(preloadedSourceFile.isExtern());
  }

  @Test
  public void testGeneratorSourceFile() {
    SourceFile generatorSourceFile = new SourceFile("test/GeneratorSourceFile.java") {
      private static final String code = "class Test {\n"
          + "  static void main(String[] args) {\n"
          + "    System.out.println(\"Hello World!\");\n"
          + "  }\n"
          + "}";

      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Assertions.assertEquals("class Test {\n" + generatorSourceFile.getCode() + "}\n", generatorSourceFile.getCode());
    Assertions.assertEquals("test/GeneratorSourceFile.java", generatorSourceFile.getName());
    Assertions.assertFalse(generatorSourceFile.isExtern());
  }

  @Test
  public void testGetLine() throws Exception {
    String code = "class Test {\n"
        + "  static void main(String[] args) {\n"
        + "    System.out.println(\"Hello World!\\nLine 3\");\n"
        + "  }\n"
        + "}";
    SourceFile sourceFile = new OnDisk(new File("test/TestGetLine.java"), Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String line1 = sourceFile.getLine(1);
    Assertions.assertEquals("class Test {\n", line1);

    String line3 = sourceFile.getLine(3);
    Assertions.assertEquals("    System.out.println(\"Hello World!\\nLine 3\");\n", line3);
  }

  @Test
  public void testGetRegion() throws Exception {
    String code = "class Test {\n"
        + "  static void main(String[] args) {\n"
        + "    System.out.println(\"Hello World!\");\n"
        + "    System.out.println(\"Line 2\");\n"
        + "  }\n"
        + "}";
    SourceFile sourceFile = new OnDisk(new File("test/TestGetRegion.java"), Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String region1 = sourceFile.getRegion(1).getCode();
    Assertions.assertEquals("class Test {\n", region1);

    String region2 = sourceFile.getRegion(2).getCode();
    Assertions.assertEquals("    System.out.println(\"Hello World!\");\n", region2);
  }

  @Test
  public void testFindLineOffsets() throws Exception {
    String code = "class Test {\n"
        + "  static void main(String[] args) {\n"
        + "    System.out.println(\"Hello World!\");\n"
        + "  }\n"
        + "}";
    SourceFile sourceFile = new OnDisk(new File("test/TestFindLineOffsets.java"), Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int line1 = sourceFile.getLineOffset(1);
    Assertions.assertEquals(1, line1);

    int line3 = sourceFile.getLineOffset(3);
    Assertions.assertEquals(-1, line3);

    int line10 = sourceFile.getLineOffset(10);
    Assertions.assertEquals(-1, line10);
  }
}