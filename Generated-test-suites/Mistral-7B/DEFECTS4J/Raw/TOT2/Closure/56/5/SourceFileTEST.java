import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedLine = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 String code = expectedLine;
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 1;
 String expectedLineContent = "function testFunction() {\n";
 String actualLine = sourceFile.getLine(lineNumber);
 assertEquals(expectedLineContent, actualLine);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineNegativeNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String code = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = -1;
 sourceFile.getLine(lineNumber);
 }

 @Test
 public void testGetLineNonExistentLine() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String code = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 10;
 String expectedNull = null;
 String actualLine = sourceFile.getLine(lineNumber);
 assertNull(actualLine);
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String code = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  var y = 2;\n" +
 "  return x + y;\n}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 2;
 int regionLength = 3;
 String expectedRegionContent = "  var y = 2;\n";
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertEquals(expectedRegionContent, actualRegion.getCode());
 }

 @Test
 public void testGetRegionNonExistentLine() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String code = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 10;
 Region expectedNull = null;
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertNull(actualRegion);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionNegativeNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String code = "function testFunction() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(code);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = -1;
 sourceFile.getRegion(lineNumber);
 }
}