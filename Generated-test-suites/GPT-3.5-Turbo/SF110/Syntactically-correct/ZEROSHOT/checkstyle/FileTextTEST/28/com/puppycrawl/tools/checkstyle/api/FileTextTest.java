import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import com.google.common.io.*;

public class FileTextTest {

  @Test
  public void testConstructorWithFileAndCharsetName() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    FileText fileText = new FileText(file, charsetName);
    
    assertNotNull(fileText);
    assertEquals(file, fileText.getFile());
    assertEquals(Charset.forName(charsetName), fileText.getCharset());
    assertNotNull(fileText.getFullText());
    assertNotNull(fileText.toLinesArray());
    assertNotNull(fileText.findLineBreaks());
    assertEquals(fileText.size() + 1, fileText.findLineBreaks().length);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testConstructorWithUnsupportedCharsetName() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UnsupportedCharset";
    FileText fileText = new FileText(file, charsetName);
  }
  
  @Test
  public void testConstructorWithFileText() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    FileText original = new FileText(file, charsetName);
    FileText copy = new FileText(original);
    
    assertNotNull(copy);
    assertEquals(original.getFile(), copy.getFile());
    assertEquals(original.getCharset(), copy.getCharset());
    assertEquals(original.getFullText(), copy.getFullText());
    assertArrayEquals(original.toLinesArray(), copy.toLinesArray());
    assertArrayEquals(original.findLineBreaks(), copy.findLineBreaks());
  }
  
  @Test
  public void testConstructorWithFileAndLines() {
    File file = new File("test.txt");
    List<String> lines = Arrays.asList("Hello", "World");
    
    FileText fileText = FileText.fromLines(file, lines);
    
    assertNotNull(fileText);
    assertEquals(file, fileText.getFile());
    assertNull(fileText.getCharset());
    assertNotNull(fileText.getFullText());
    assertNotNull(fileText.toLinesArray());
    assertNotNull(fileText.findLineBreaks());
    assertEquals(fileText.size() + 1, fileText.findLineBreaks().length);
  }
  
  @Test
  public void testReadFile() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    String testContent = "Hello\nWorld\n";
    Files.write(testContent, file, Charset.forName(charsetName));
    
    CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
    decoder.onMalformedInput(CodingErrorAction.REPLACE);
    decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
    String readFileContent = FileText.readFile(file, decoder);
    
    assertNotNull(readFileContent);
    assertEquals(testContent, readFileContent);
  }
  
  @Test(expected = FileNotFoundException.class)
  public void testReadFileWithNonExistentFile() throws IOException {
    File file = new File("nonexistent.txt");
    String charsetName = "UTF-8";
    CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
    decoder.onMalformedInput(CodingErrorAction.REPLACE);
    decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
    String readFileContent = FileText.readFile(file, decoder);
  }
  
  @Test
  public void testLineColumn() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    String testContent = "Hello\nWorld\n";
    FileText fileText = new FileText(file, charsetName);
    Files.write(testContent, file, Charset.forName(charsetName));
    
    int pos = 7;
    FileText.LineColumn lineColumn = fileText.lineColumn(pos);
    
    assertNotNull(lineColumn);
    assertEquals(2, lineColumn.getLine());
    assertEquals(1, lineColumn.getColumn());
  }
  
  @Test
  public void testGet() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    String testContent = "Hello\nWorld\n";
    FileText fileText = new FileText(file, charsetName);
    Files.write(testContent, file, Charset.forName(charsetName));
    
    assertEquals("Hello", fileText.get(0));
    assertEquals("World", fileText.get(1));
  }
  
  @Test
  public void testSize() throws IOException {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    String testContent = "Hello\nWorld\n";
    FileText fileText = new FileText(file, charsetName);
    Files.write(testContent, file, Charset.forName(charsetName));
    
    assertEquals(2, fileText.size());
  }

  @After
  public void cleanup() {
    File file = new File("test.txt");
    if (file.exists()) {
      file.delete();
    }
  }
  
  // Add more test methods here
  
}

