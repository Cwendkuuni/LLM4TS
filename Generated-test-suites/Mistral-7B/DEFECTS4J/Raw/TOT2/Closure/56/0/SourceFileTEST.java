import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @Test
  public void testGetCode() throws IOException {
    String fileName = "test.js";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    String expectedCode = Files.toString(new File(fileName), StandardCharsets.UTF_8);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(expectedCode, actualCode);
  }

  @Test(expected = IOException.class)
  public void testGetCodeIOException() throws IOException {
    String fileName = "nonexistentfile.js";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    sourceFile.getCode();
  }

  @Test
  public void testGetLine() {
    String jsCode = "function test() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    String expectedLine = "  var y = 2;";
    String actualLine = sourceFile.getLine(3);
    Assert.assertEquals(expectedLine, actualLine);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidLineNumber() {
    String jsCode = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    sourceFile.getLine(10);
  }

  @Test
  public void testGetRegion() {
    String jsCode = "function test() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    Region expectedRegion = new SimpleRegion(1, 2, "var y = 2;");
    Region actualRegion = sourceFile.getRegion(2);
    Assert.assertEquals(expectedRegion, actualRegion);
  }

  @Test(expected = IOException.class)
  public void testGetRegionIOException() throws IOException {
    String fileName = "nonexistentfile.js";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    sourceFile.getRegion(1);
  }
}