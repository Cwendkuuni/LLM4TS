import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 private SourceFile sourceFile;

 @Test
 public void testGetLine() {
 sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 String line1 = sourceFile.getLine(1);
 String line2 = sourceFile.getLine(2);
 assertEquals("int a = 1;", line1);
 assertEquals("int b = 2;", line2);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to get lines from source file.");
 }
 }

 @Test
 public void testGetLineInvalidLineNumber() {
 sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 String line = sourceFile.getLine(10);
 assertNull(line);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to get lines from source file.");
 }
 }

 @Test
 public void testGetRegion() {
 sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 Region region = sourceFile.getRegion(2);
 assertEquals("int b = 2;", region.getCode());
 assertEquals(2, region.getStartLineNumber());
 assertEquals(2, region.getEndLineNumber());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to get lines from source file.");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionInvalidLineNumber() {
 sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 Region region = sourceFile.getRegion(10);
 } catch (IOException e) {
 fail("Should not throw IO exception.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFindLineOffsets() {
 sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 int numLines = sourceFile.getNumLines();
 int[] lineOffsets = sourceFile.getLineOffsets();
 for (int lineNumber = 1; lineNumber <= numLines; lineNumber++) {
 int offset = lineOffsets[lineNumber - 1];
 int lineLength = sourceFile.getLine(lineNumber).length();
 assertEquals(lineLength + offset, sourceFile.getLineOffset(lineNumber));
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to get lines from source file.");
 }
 }

 @Test
 public void testHasSourceInMemory() {
 sourceFile = new SourceFile("TestFile.java");
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 assertTrue(sourceFile.hasSourceInMemory());
 }

 @Test
 public void testClearCachedSource() {
 sourceFile = new SourceFile("TestFile.java");
 sourceFile.setCode("public class TestClass {\n" +
 "  int a = 1;\n" +
 "  int b = 2;\n" +
 "}");
 assertTrue(sourceFile.hasSourceInMemory());
 sourceFile.clearCachedSource();
 assertFalse(sourceFile.hasSourceInMemory());
 }

 @Test
 public void testGetName() {
 sourceFile = new SourceFile("TestFile.java");
 assertEquals("TestFile.java", sourceFile.getName());
 }

 @Test
 public void testIsExtern() {
 sourceFile = new SourceFile("TestFile.java");
 assertFalse(sourceFile.isExtern());

 sourceFile = new SourceFile("TestFile.java", true);
 assertTrue(sourceFile.isExtern());
 }
}