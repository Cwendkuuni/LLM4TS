import org.junit.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {
  
  @Test
  public void testGetWebData() {
    SAXDirParser parser = new SAXDirParser();
    assertEquals("", parser.getWebData());
  }
  
  @Test
  public void testStartElement_BrowserLoc() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "BrowserLoc", new Attributes());
    assertTrue(parser.check);
    assertEquals(0, parser.level);
  }
  
  @Test
  public void testStartElement_Editor() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "Editor", new Attributes());
    assertTrue(parser.check);
    assertEquals(10, parser.level);
  }
  
  @Test
  public void testStartElement_AcceptsLineNumber() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "acceptsLineNumber", new Attributes());
    assertTrue(parser.check);
    assertEquals(11, parser.level);
  }
  
  @Test
  public void testStartElement_Parameter() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "parameter", new Attributes());
    assertTrue(parser.check);
    assertEquals(12, parser.level);
  }
  
  @Test
  public void testStartElement_Group() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "Group", new Attributes());
    assertFalse(parser.check);
    assertEquals(2, parser.level);
  }
  
  @Test
  public void testStartElement_Web() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "Web", new Attributes());
    assertTrue(parser.check);
    assertEquals(30, parser.level);
  }
  
  @Test
  public void testStartElement_Local() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "Local", new Attributes());
    assertTrue(parser.check);
    assertEquals(31, parser.level);
  }
  
  @Test
  public void testStartElement_Names() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "Names", new Attributes());
    assertTrue(parser.check);
  }
  
  @Test
  public void testStartElement_pkg() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("namespace", "local", "pkg", new Attributes());
    assertEquals(4, parser.level);
  }
  
  @Test
  public void testCharacters_BrowserLoc() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.level = 0;
    parser.characters(new char[]{'t','e','s','t'}, 0, 4);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertEquals("test", SAXDirParser.localBrowser);
  }
  
  @Test
  public void testCharacters_Editor() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.level = 10;
    parser.characters(new char[]{'t','e','s','t'}, 0, 4);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertEquals("test", SAXDirParser.file_editor);
  }
  
  @Test
  public void testCharacters_AcceptsLineNumber_Yes() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.level = 11;
    parser.characters(new char[]{'Y','e','s'}, 0, 3);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertTrue(SAXDirParser.lineNumAccepted);
  }
  
  @Test
  public void testCharacters_AcceptsLineNumber_No() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.level = 11;
    parser.characters(new char[]{'N','o'}, 0, 2);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertFalse(SAXDirParser.lineNumAccepted);
  }
  
  @Test
  public void testCharacters_Parameter() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.level = 12;
    parser.characters(new char[]{'t','e','s','t'}, 0, 4);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertEquals("test", SAXDirParser.lineNumParam);
  }
  
  @Test
  public void testCharacters_Web() {
    SAXDirParser parser = new SAXDirParser();
    parser.level = 30;
    parser.characters(new char[]{'t','e','s','t'}, 0, 4);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertEquals("test", parser.location);
  }
  
  @Test
  public void testCharacters_Local_Yes() {
    SAXDirParser parser = new SAXDirParser();
    parser.level = 31;
    parser.characters(new char[]{'T','e','s','t'}, 0, 4);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertTrue(parser.local);
  }
  
  @Test
  public void testCharacters_Local_No() {
    SAXDirParser parser = new SAXDirParser();
    parser.level = 31;
    parser.characters(new char[]{'N','o'}, 0, 2);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertFalse(parser.local);
  }
  
  @Test
  public void testCharacters_PackageEndsWithDot() {
    SAXDirParser parser = new SAXDirParser();
    parser.level = 4;
    parser.location = "test/location";
    parser.local = true;
    parser.characters(new char[]{'t','e','s','t','.'}, 0, 5);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertEquals("test/location", SAXDirParser.directory.get("test"));
    assertNotNull(SAXDirParser.localPackages);
    assertTrue(SAXDirParser.localPackages.contains("test"));
  }
  
  @Test
  public void testCharacters_PackageWithEmptyString() {
    SAXDirParser parser = new SAXDirParser();
    parser.level = 4;
    parser.location = "test/location";
    parser.local = true;
    parser.characters(new char[]{' ', ' '}, 0, 2);
    assertFalse(parser.check);
    assertEquals(-1, parser.level);
    assertNull(SAXDirParser.directory.get(""));
  }
  
  @Test
  public void testEndElement_Group() {
    SAXDirParser parser = new SAXDirParser();
    parser.check = true;
    parser.endElement("namespace", "local", "Group");
    assertFalse(parser.check);
  }
  
  @Test
  public void testGetDirectory() {
    // Set up
    SAXDirParser.directory.put("key", "value");
    
    // Execute
    HashMap<String, String> result = SAXDirParser.getDirectory();
    
    // Verify
    assertEquals(SAXDirParser.directory, result);
  }
  
  @Test
  public void testGetLocalPackages_Empty() {
    // Set up
    SAXDirParser.localPackages.clear();
    
    // Execute
    StringList result = SAXDirParser.getLocalPackages();
    
    // Verify
    assertNull(result);
  }
  
  @Test
  public void testGetLocalPackages() {
    // Set up
    SAXDirParser.localPackages.add("package1");
    SAXDirParser.localPackages.add("package2");
    
    // Execute
    StringList result = SAXDirParser.getLocalPackages();
    
    // Verify
    assertEquals(SAXDirParser.localPackages, result);
  }
  
  @Test
  public void testGetLocalBrowser() {
    SAXDirParser.localBrowser = "browser";
    assertEquals("browser", SAXDirParser.getLocalBrowser());
  }
  
  @Test
  public void testGetEditor() {
    SAXDirParser.file_editor = "editor";
    assertEquals("editor", SAXDirParser.getEditor());
  }
  
  @Test
  public void testAcceptsLineNumber() {
    SAXDirParser.lineNumAccepted = true;
    assertTrue(SAXDirParser.acceptsLineNumber());
    
    SAXDirParser.lineNumAccepted = false;
    assertFalse(SAXDirParser.acceptsLineNumber());
  }
  
  @Test
  public void testLineNumberParameter() {
    SAXDirParser.lineNumParam = "param";
    assertEquals("param", SAXDirParser.lineNumberParameter());
  }
  
}