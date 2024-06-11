import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

   // Test for constructor
   @Test
   public void testConstructor() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertEquals("TestFile", sourceFile.getName());
   }

   // Test for getLineOffset
   @Test
   public void testGetLineOffset() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertEquals(0, sourceFile.getLineOffset(1));
   }

   // Test for getNumLines
   @Test
   public void testGetNumLines() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertEquals(0, sourceFile.getNumLines());
   }

   // Test for getLine
   @Test
   public void testGetLine() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertNull(sourceFile.getLine(1));
   }

   // Test for getRegion
   @Test
   public void testGetRegion() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertNull(sourceFile.getRegion(1));
   }

   // Test for fromCode
   @Test
   public void testFromCode() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      assertEquals("TestFile", sourceFile.getName());
   }

   // Test for getCode
   @Test
   public void testGetCode() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      assertEquals("test code", sourceFile.getCode());
   }

   // Test for getCodeReader
   @Test
   public void testGetCodeReader() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      assertTrue(sourceFile.getCodeReader() instanceof StringReader);
   }

   // Test for clearCachedSource
   @Test
   public void testClearCachedSource() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      sourceFile.clearCachedSource();
      assertNull(sourceFile.getCode());
   }

   // Test for getOriginalPath and setOriginalPath
   @Test
   public void testOriginalPath() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertEquals("TestFile.jar", sourceFile.getOriginalPath());
      sourceFile.setOriginalPath("TestOriginalPath");
      assertEquals("TestOriginalPath", sourceFile.getOriginalPath());
   }

   // Test for isExtern and setIsExtern
   @Test
   public void testIsExtern() {
      SourceFile sourceFile = new SourceFile("TestFile");
      assertFalse(sourceFile.isExtern());
      sourceFile.setIsExtern(true);
      assertTrue(sourceFile.isExtern());
   }

   // Test for getLine when line does not exist
   @Test
   public void testGetNonExistingLine() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      assertNull(sourceFile.getLine(10));
   }

   // Test for getRegion when line does not exist
   @Test
   public void testGetNonExistingRegion() {
      SourceFile sourceFile = SourceFile.fromCode("TestFile", "test code");
      assertNull(sourceFile.getRegion(10));
   }

   // Test for fromFile
   @Test
   public void testFromFile() {
      SourceFile sourceFile = SourceFile.fromFile("test.txt");
      assertEquals("test.txt", sourceFile.getName());
   }

}