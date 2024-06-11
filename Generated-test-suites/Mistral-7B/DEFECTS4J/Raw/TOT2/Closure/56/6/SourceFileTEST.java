import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 private SourceFile sourceFile;

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedLine = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedLine);
 String actualLine = sourceFile.getLine(1);
 assertEquals(expectedLine.substring(0, expectedLine.lastIndexOf('\n')), actualLine);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during getLine test");
 }
 }

 @Test
 public void testGetLineWithInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during setCode test");
 }
 int invalidLineNumber = 10;
 String actualLine = sourceFile.getLine(invalidLineNumber);
 assertNull(actualLine);
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during setCode test");
 }
 Region actualRegion = sourceFile.getRegion(5);
 String expectedRegion = "function testFunction() {\n// Some code here\n}";
 assertEquals(expectedRegion, actualRegion.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyFileName() {
 new SourceFile("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullFileName() {
 new SourceFile(null);
 }

 @Test
 public void testSetOriginalPath() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 sourceFile.setOriginalPath("path/to/originalFile.js");
 assertEquals("path/to/originalFile.js", sourceFile.getOriginalPath());
 }

 @Test
 public void testFindLineOffsets() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during setCode test");
 }
 int[] actualLineOffsets = sourceFile.lineOffsets;
 int expectedLineOffsetsLength = expectedCode.split("\n").length;
 assertEquals(expectedLineOffsetsLength, actualLineOffsets.length);
 for (int i = 0; i < expectedLineOffsetsLength; i++) {
 int expectedLineOffset = i * (expectedCode.split("\n")[i].length() + 1) + 1;
 assertEquals(expectedLineOffset, actualLineOffsets[i]);
 }
 }

 @Test
 public void testGetNumLines() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during setCode test");
 }
 int expectedNumLines = expectedCode.split("\n").length;
 int actualNumLines = sourceFile.getNumLines();
 assertEquals(expectedNumLines, actualNumLines);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineWithNegativeLineNumber() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 String expectedCode = "function testFunction() {\n" +
 "// Some code here\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown during setCode test");
 }
 int negativeLineNumber = -1;
 String actualLine = sourceFile.getLine(negativeLineNumber);
 fail("IllegalArgumentException should be thrown with negative line number");
 }

 @Test
 public void testIsExtern() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 assertFalse(sourceFile.isExtern());
 }

 @Test
 public void testSetIsExtern() {
 SourceFile sourceFile = new SourceFile("testSourceFile.js");
 sourceFile.setIsExtern(true);
 assertTrue(sourceFile.isExtern());
 }

}