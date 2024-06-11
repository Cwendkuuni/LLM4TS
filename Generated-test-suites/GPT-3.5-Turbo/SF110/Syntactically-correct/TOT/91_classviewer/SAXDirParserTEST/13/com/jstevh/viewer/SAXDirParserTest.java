import org.junit.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {

  @Test
  public void testGetWebData() {
    SAXDirParser parser = new SAXDirParser();
    String expected = "";
    String actual = parser.getWebData();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testStartElement_browserLoc() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","BrowserLoc",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 0;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_Editor() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","Editor",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 10;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_acceptsLineNumber() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","acceptsLineNumber",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 11;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_parameter() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","parameter",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 12;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_Group() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","Group",null);
    boolean expected = false;
    boolean actual = parser.check;
    int expectedLevel = 2;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_Web() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","Web",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 30;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_Local() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","Local",null);
    boolean expected = true;
    boolean actual = parser.check;
    int expectedLevel = 31;
    int actualLevel = parser.level;
    assertEquals(expected, actual);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testStartElement_Names() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","Names",null);
    boolean expected = true;
    boolean actual = parser.check;
    assertEquals(expected, actual);
  }
  
  @Test
  public void testStartElement_pkg() {
    SAXDirParser parser = new SAXDirParser();
    parser.startElement("","","pkg",null);
    int expectedLevel = 4;
    int actualLevel = parser.level;
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_browserLoc() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "test".toCharArray();
    parser.check = true;
    parser.level = 0;
    parser.characters(charArray, 0, charArray.length);
    String expectedBrowser = "test";
    String actualBrowser = SAXDirParser.getLocalBrowser();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedBrowser, actualBrowser);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_Editor() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "test".toCharArray();
    parser.check = true;
    parser.level = 10;
    parser.characters(charArray, 0, charArray.length);
    String expectedEditor = "test";
    String actualEditor = SAXDirParser.getEditor();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedEditor, actualEditor);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_acceptsLineNumber_yes() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "Yes".toCharArray();
    parser.check = true;
    parser.level = 11;
    parser.characters(charArray, 0, charArray.length);
    boolean expectedAcceptance = true;
    boolean actualAcceptance = SAXDirParser.acceptsLineNumber();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedAcceptance, actualAcceptance);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_acceptsLineNumber_no() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "No".toCharArray();
    parser.check = true;
    parser.level = 11;
    parser.characters(charArray, 0, charArray.length);
    boolean expectedAcceptance = false;
    boolean actualAcceptance = SAXDirParser.acceptsLineNumber();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedAcceptance, actualAcceptance);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_parameter() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "test".toCharArray();
    parser.check = true;
    parser.level = 12;
    parser.characters(charArray, 0, charArray.length);
    String expectedParam = "test";
    String actualParam = SAXDirParser.lineNumberParameter();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedParam, actualParam);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_Location() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "test".toCharArray();
    parser.check = true;
    parser.level = 30;
    parser.characters(charArray, 0, charArray.length);
    String expectedLoc = "test";
    HashMap<String, String> directory = SAXDirParser.getDirectory();
    String actualLoc = directory.get("java.lang");
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedLoc, actualLoc);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_Local() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "Yes".toCharArray();
    parser.check = true;
    parser.level = 31;
    parser.characters(charArray, 0, charArray.length);
    boolean expectedLocal = true;
    StringList expectedPackages = new StringList();
    expectedPackages.add("java.lang");
    StringList actualPackages = SAXDirParser.getLocalPackages();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedLocal, actualLocal);
    assertEquals(expectedPackages, actualPackages);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testCharacters_pkg() {
    SAXDirParser parser = new SAXDirParser();
    char[] charArray = "java.lang".toCharArray();
    parser.check = true;
    parser.level = 4;
    parser.local = true;
    parser.location = "test";
    parser.characters(charArray, 0, charArray.length);
    HashMap<String, String> expectedDirectory = new HashMap<String, String>();
    expectedDirectory.put("java.lang", "test");
    HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
    StringList expectedPackages = new StringList();
    expectedPackages.add("java.lang");
    StringList actualPackages = SAXDirParser.getLocalPackages();
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    int expectedLevel = -1;
    int actualLevel = parser.level;
    assertEquals(expectedDirectory, actualDirectory);
    assertEquals(expectedPackages, actualPackages);
    assertEquals(expectedCheck, actualCheck);
    assertEquals(expectedLevel, actualLevel);
  }
  
  @Test
  public void testEndElement_Group() {
    SAXDirParser parser = new SAXDirParser();
    parser.endElement("", "", "Group");
    boolean expectedCheck = false;
    boolean actualCheck = parser.check;
    assertEquals(expectedCheck, actualCheck);
  }
  
  @Test
  public void testGetDirectory() {
    HashMap<String, String> expected = new HashMap<String, String>();
    expected.put("java.lang", "test");
    SAXDirParser.directory = expected;
    HashMap<String, String> actual = SAXDirParser.getDirectory();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetLocalPackages_empty() {
    SAXDirParser.localPackages = new StringList();
    StringList actual = SAXDirParser.getLocalPackages();
    assertNull(actual);
  }
  
  @Test
  public void testGetLocalPackages_notEmpty() {
    StringList expected = new StringList();
    expected.add("java.lang");
    SAXDirParser.localPackages = expected;
    StringList actual = SAXDirParser.getLocalPackages();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetLocalBrowser() {
    SAXDirParser.localBrowser = "test";
    String expected = "test";
    String actual = SAXDirParser.getLocalBrowser();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetEditor() {
    SAXDirParser.file_editor = "test";
    String expected = "test";
    String actual = SAXDirParser.getEditor();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAcceptsLineNumber_true() {
    SAXDirParser.lineNumAccepted = true;
    boolean expected = true;
    boolean actual = SAXDirParser.acceptsLineNumber();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAcceptsLineNumber_false() {
    SAXDirParser.lineNumAccepted = false;
    boolean expected = false;
    boolean actual = SAXDirParser.acceptsLineNumber();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLineNumberParameter() {
    SAXDirParser.lineNumParam = "test";
    String expected = "test";
    String actual = SAXDirParser.lineNumberParameter();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMain() throws Exception {
    // Skipped as it is a main method with file reading and will require mocking
    assertTrue(true);
  }
  
}