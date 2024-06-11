import org.junit.*;
import static org.junit.Assert.*;

public class FileTextTest {

  @Test
  public void testConstructor_withFileAndCharsetName() {
    File file = new File("test.txt");
    String charsetName = "UTF-8";
    try {
      FileText fileText = new FileText(file, charsetName);
      assertNotNull(fileText);
      assertEquals(file, fileText.getFile());
      assertEquals(Charset.forName(charsetName), fileText.getCharset());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testConstructor_withFileText() {
    FileText fileText1 = new FileText(new File("test.txt"), "UTF-8");
    FileText fileText2 = new FileText(fileText1);
    assertNotNull(fileText2);
    assertEquals(fileText1.getFile(), fileText2.getFile());
    assertEquals(fileText1.getCharset(), fileText2.getCharset());
    assertEquals(fileText1.getFullText(), fileText2.getFullText());
    assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
  }

  @Test
  public void testStaticMethod_fromLines() {
    List<String> lines = new ArrayList<>();
    lines.add("line1");
    lines.add("line2");
    FileText fileText = FileText.fromLines(new File("test.txt"), lines);
    assertNotNull(fileText);
    assertEquals("line1\nline2\n", fileText.getFullText());
  }

  @Test
  public void testGet() {
    FileText fileText = new FileText(new File("test.txt"), "UTF-8");
    assertEquals("line1", fileText.get(0));
    assertEquals("line2", fileText.get(1));
  }

  @Test
  public void testSize() {
    FileText fileText = new FileText(new File("test.txt"), "UTF-8");
    assertEquals(2, fileText.size());
  }

  @Test
  public void testLineColumn() {
    FileText fileText = new FileText(new File("test.txt"), "UTF-8");
    LineColumn lc = fileText.lineColumn(7);
    assertEquals(1, lc.line);
    assertEquals(7, lc.column);
  }
}

class LineColumn {
  public final int line;
  public final int column;

  public LineColumn(int line, int column) {
    this.line = line;
    this.column = column;
  }
}