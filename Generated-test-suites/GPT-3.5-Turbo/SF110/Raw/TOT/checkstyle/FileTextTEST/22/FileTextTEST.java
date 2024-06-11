import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
  
  @Test
  public void testFileTextConstructor() throws IOException {
    // Typical use case
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    assertNotNull(fileText);
    
    // Edge case: Empty file
    File emptyFile = new File("emptyfile.txt");
    
    FileText emptyFileText = new FileText(emptyFile, charsetName);
    assertNotNull(emptyFileText);
    
    // Edge case: Unsupported charset
    file = new File("unsupportedCharset.txt");
    String invalidCharsetName = "INVALID";
    
    try {
      FileText unsupportedCharset = new FileText(file, invalidCharsetName);
      fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("Unsupported charset: " + invalidCharsetName, e.getMessage());
    }
    
    // Error scenario: File not found
    File nonExistentFile = new File("nonexistentfile.txt");
    
    try {
      FileText nonExistentFileText = new FileText(nonExistentFile, charsetName);
      fail("Expected a FileNotFoundException to be thrown");
    } catch (FileNotFoundException e) {
      assertEquals(nonExistentFile.getPath() + " (No such file or directory)", e.getMessage());
    }
  }
  
  @Test
  public void testFileTextCopyConstructor() throws IOException {
    // Typical use case
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText1 = new FileText(file, charsetName);
    FileText fileText2 = new FileText(fileText1);
    
    assertNotNull(fileText2);
    assertEquals(fileText1.getFile(), fileText2.getFile());
    assertEquals(fileText1.getCharset(), fileText2.getCharset());
    assertEquals(fileText1.getFullText(), fileText2.getFullText());
    assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
    
    // Edge case: Null lineBreaks
    FileText fileText3 = new FileText(file, charsetName);
    fileText3.getLineBreaks = null;
    
    FileText fileText4 = new FileText(fileText3);
    
    assertNotNull(fileText4);
    assertEquals(fileText3.getFile(), fileText4.getFile());
    assertEquals(fileText3.getCharset(), fileText4.getCharset());
    assertEquals(fileText3.getFullText(), fileText4.getFullText());
    assertArrayEquals(fileText3.toLinesArray(), fileText4.toLinesArray());
    assertNull(fileText4.getLineBreaks());
  }
  
  @Test
  public void testFromFileTextFactoryMethod() {
    // Typical use case
    File file = new File("testfile.txt");
    List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
    
    FileText fileText1 = FileText.fromLines(file, lines);
    
    assertNotNull(fileText1);
    assertEquals(file, fileText1.getFile());
    assertNull(fileText1.getCharset());
    assertEquals("Line 1\nLine 2\nLine 3\n", fileText1.getFullText().toString());
    assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText1.toLinesArray());
    
    // Edge case: Input list is already a FileText instance
    FileText fileText2 = FileText.fromLines(file, fileText1);
    
    assertNotNull(fileText2);
    assertEquals(file, fileText2.getFile());
    assertNull(fileText2.getCharset());
    assertEquals("Line 1\nLine 2\nLine 3\n", fileText2.getFullText().toString());
    assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText2.toLinesArray());
    assertEquals(fileText1, fileText2);
  }
  
  @Test
  public void testGetFile() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    assertEquals(file, fileText.getFile());
  }
  
  @Test
  public void testGetCharset() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    Charset charset = Charset.forName(charsetName);
    assertEquals(charset, fileText.getCharset());
  }
  
  @Test
  public void testGetFullText() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    assertEquals(fileText.getFullText().toString(), fileText.getFullText());
    assertEquals(fileText.getFullText().length(), fileText.getFullText().toString().length());
  }
  
  @Test
  public void testToLinesArray() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    String[] linesArray = fileText.toLinesArray();
    assertArrayEquals(fileText.getLines(), linesArray);
  }
  
  @Test
  public void testFindLineBreaks() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    int[] lineBreaks = fileText.findLineBreaks();
    assertNotNull(lineBreaks);
    
    // Edge case: No line breaks
    FileText emptyFileText = new FileText(new File("emptyfile.txt"), charsetName);
    
    int[] emptyLineBreaks = emptyFileText.findLineBreaks();
    assertNotNull(emptyLineBreaks);
    assertEquals(0, emptyLineBreaks.length);
  }
  
  @Test
  public void testLineColumn() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    LineColumn lineColumn = fileText.lineColumn(10);
    assertNotNull(lineColumn);
    
    // Edge case: Position before first line
    LineColumn lineColumn1 = fileText.lineColumn(-1);
    assertNotNull(lineColumn1);
    
    // Edge case: Position after last line
    LineColumn lineColumn2 = fileText.lineColumn(100000);
    assertNotNull(lineColumn2);
  }
  
  @Test
  public void testGet() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    String line = fileText.get(0);
    assertNotNull(line);
    
    // Error scenario: Invalid line number
    try {
      String invalidLine = fileText.get(-1);
      fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
    } catch (ArrayIndexOutOfBoundsException e) {
      assertEquals("Index -1 out of bounds for length " + fileText.size(), e.getMessage());
    }
  }
  
  @Test
  public void testSize() {
    File file = new File("testfile.txt");
    String charsetName = "UTF-8";
    
    FileText fileText = new FileText(file, charsetName);
    
    assertEquals(fileText.toLinesArray().length, fileText.size());
  }
  
}