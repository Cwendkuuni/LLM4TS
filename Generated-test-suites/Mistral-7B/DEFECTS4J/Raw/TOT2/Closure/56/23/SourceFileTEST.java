import com.google.javascript.jscomp.SourceFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    try {
      String line1 = sourceFile.getLine(1);
      Assert.assertEquals("Expected line 1 to contain the first line of source code.", "expectedLine1", line1);

      String line2 = sourceFile.getLine(2);
      Assert.assertEquals("Expected line 2 to contain the second line of source code.", "expectedLine2", line2);

      String lineNull = sourceFile.getLine(10);
      Assert.assertNull("Expected null for non-existent line number.", lineNull);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException when trying to get lines from source file.");
    }
  }

  @Test
  public void testGetLineException() {
    SourceFile sourceFile = new SourceFile("testSourceFileWithException.js");
    try {
      String line1 = sourceFile.getLine(1);
      Assert.assertEquals("Expected line 1 to contain the first line of source code.", "expectedLine1", line1);

      String lineException = sourceFile.getLine(100);
      Assert.assertNull("Expected null for non-existent line number.", lineException);

      // Test exception handling by attempting to read a line beyond EOF
      String lineBeyondEof = sourceFile.getLine(Integer.MAX_VALUE);
      Assert.assertNull("Expected null for line beyond EOF.", lineBeyondEof);
    } catch (IOException e) {
      Assert.assertNotNull("Expected IOException when source file contains an error.", e);
    }
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    try {
      Region region1 = sourceFile.getRegion(1);
      Assert.assertEquals("Expected region 1 to contain the first line of source code.", "expectedRegion1", region1.toString());

      Region region2 = sourceFile.getRegion(2);
      Assert.assertEquals("Expected region 2 to contain the second line of source code.", "expectedRegion2", region2.toString());

      Region regionNull = sourceFile.getRegion(10);
      Assert.assertNull("Expected null for non-existent region.", regionNull);

      // Test with a region that extends past EOF
      Region regionBeyondEof = sourceFile.getRegion(Integer.MAX_VALUE);
      Assert.assertNull("Expected null for region beyond EOF.", regionBeyondEof);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException when trying to get regions from source file.");
    }
  }

  @Test
  public void testGetRegionException() {
    SourceFile sourceFile = new SourceFile("testSourceFileWithException.js");
    try {
      Region region1 = sourceFile.getRegion(1);
      Assert.assertEquals("Expected region 1 to contain the first line of source code.", "expectedRegion1", region1.toString());

      Region regionNull = sourceFile.getRegion(10);
      Assert.assertNull("Expected null for non-existent region.", regionNull);

      // Test exception handling by attempting to read a region beyond EOF
      Region regionBeyondEof = sourceFile.getRegion(Integer.MAX_VALUE);
      Assert.assertNull("Expected null for region beyond EOF.", regionBeyondEof);
    } catch (IOException e) {
      Assert.assertNotNull("Expected IOException when source file contains an error.", e);
    }
  }

  @Test
  public void testGetCode() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    try {
      String code = sourceFile.getCode();
      Assert.assertEquals("Expected the entire source code to be retrieved.", "expectedCode", code);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException when trying to retrieve source code.");
    }
  }

  @Test
  public void testGetCodeException() {
    SourceFile sourceFile = new SourceFile("testSourceFileWithException.js");
    try {
      String code = sourceFile.getCode();
      Assert.assertNotNull("Expected non-null code when source file contains an error.", code);
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testSetCode() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    String newCode = "newCode";
    sourceFile.setCode(newCode);
    try {
      String code = sourceFile.getCode();
      Assert.assertEquals("Expected the new code to be set.", newCode, code);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException when trying to set and retrieve new code.");
    }
  }

  @Test
  public void testSetCodeException() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    String newCode = "newCode";
    try {
      sourceFile.setCode(newCode);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException when trying to set new code.");
    }
  }
}