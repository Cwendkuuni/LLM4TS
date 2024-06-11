import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.tools.StringList;

import java.util.HashMap;

public class SAXDirParserTest {

  private SAXDirParser saxParser;

  @Before
  public void setUp() throws Exception {
    saxParser = new SAXDirParser();
  }

  // Test case for getWebData() method
  @Test
  public void testGetWebData() {
    saxParser.webData = "Test Web Data";
    assertEquals("Test Web Data", saxParser.getWebData());
  }

  // Test case for startElement() method - BrowserLoc
  @Test
  public void testStartElementWithBrowserLoc() throws SAXException {
    saxParser.startElement("", "", "BrowserLoc", null);
    assertTrue(saxParser.check);
    assertEquals(0, saxParser.level);
  }

  // Test case for startElement() method - Editor
  @Test
  public void testStartElementWithEditor() throws SAXException {
    saxParser.startElement("", "", "Editor", null);
    assertTrue(saxParser.check);
    assertEquals(10, saxParser.level);
  }

  // Test case for startElement() method - acceptsLineNumber
  @Test
  public void testStartElementWithAcceptsLineNumber() throws SAXException {
    saxParser.startElement("", "", "acceptsLineNumber", null);
    assertTrue(saxParser.check);
    assertEquals(11, saxParser.level);
  }

  // Test case for startElement() method - parameter
  @Test
  public void testStartElementWithParameter() throws SAXException {
    saxParser.startElement("", "", "parameter", null);
    assertTrue(saxParser.check);
    assertEquals(12, saxParser.level);
  }

  // Test case for startElement() method - Group
  @Test
  public void testStartElementWithGroup() throws SAXException {
    saxParser.startElement("", "", "Group", null);
    assertEquals(2, saxParser.level);
    assertFalse(saxParser.check);
  }

  // Test case for startElement() method - Web
  @Test
  public void testStartElementWithWeb() throws SAXException {
    saxParser.startElement("", "", "Web", null);
    assertTrue(saxParser.check);
    assertEquals(30, saxParser.level);
  }

  // Test case for startElement() method - Local
  @Test
  public void testStartElementWithLocal() throws SAXException {
    saxParser.startElement("", "", "Local", null);
    assertTrue(saxParser.check);
    assertEquals(31, saxParser.level);
    
    // Negative test case for local attribute value
    saxParser.characters("No".toCharArray(), 0, 2);
    assertFalse(saxParser.local);
    
    // Positive test case for local attribute value
    saxParser.characters("Yes".toCharArray(), 0, 3);
    assertTrue(saxParser.local);
  }

  // Test case for startElement() method - Names
  @Test
  public void testStartElementWithNames() throws SAXException {
    saxParser.startElement("", "", "Names", null);
    assertTrue(saxParser.check);
  }

  // Test case for startElement() method - pkg
  @Test
  public void testStartElementWithPackage() throws SAXException {
    saxParser.startElement("", "", "pkg", null);
    assertEquals(4, saxParser.level);
  }

  // Test case for characters() method - BrowserLoc
  @Test
  public void testCharactersWithBrowserLoc() {
    saxParser.check = true;
    saxParser.level = 0;
    saxParser.characters("Test BrowserLoc".toCharArray(), 0, 15);
    assertEquals("Test BrowserLoc", SAXDirParser.getLocalBrowser());
    assertFalse(saxParser.check);
    assertEquals(-1, saxParser.level);
  }

  // Test case for characters() method - Editor
  @Test
  public void testCharactersWithEditor() {
    saxParser.check = true;
    saxParser.level = 10;
    saxParser.characters("Test Editor".toCharArray(), 0, 11);
    assertEquals("Test Editor", SAXDirParser.getEditor());
    assertFalse(saxParser.check);
    assertEquals(-1, saxParser.level);
  }

  // Test case for characters() method - acceptsLineNumber
  @Test
  public void testCharactersWithAcceptsLineNumber() {
    saxParser.check = true;
    saxParser.level = 11;
    
    // Negative test case for acceptsLineNumber value
    saxParser.characters("No".toCharArray(), 0, 2);
    assertFalse(SAXDirParser.acceptsLineNumber());
    
    // Positive test case for acceptsLineNumber value
    saxParser.characters("Yes".toCharArray(), 0, 3);
    assertTrue(SAXDirParser.acceptsLineNumber());
    
    assertFalse(saxParser.check);
    assertEquals(-1, saxParser.level);
  }

  // Test case for characters() method - parameter
  @Test
  public void testCharactersWithParameter() {
    saxParser.check = true;
    saxParser.level = 12;
    saxParser.characters("Test Parameter".toCharArray(), 0, 14);
    assertEquals("Test Parameter", SAXDirParser.lineNumberParameter());
    assertFalse(saxParser.check);
    assertEquals(-1, saxParser.level);
  }

  // Test case for characters() method - Web
  @Test
  public void testCharactersWithWeb() {
    saxParser.level = 30;
    saxParser.characters("Test Web".toCharArray(), 0, 8);
    assertEquals("Test Web", saxParser.location);
    assertFalse(saxParser.check);
    assertEquals(-1, saxParser.level);
  }

  // Test case for endElement() method - Group
  @Test
  public void testEndElementWithGroup() throws SAXException {
    saxParser.check = true;
    saxParser.endElement("", "", "Group");
    assertFalse(saxParser.check);
  }

  // Test case for getDirectory() method
  @Test
  public void testGetDirectory() {
    SAXDirParser.directory = new HashMap<String, String>();
    SAXDirParser.directory.put("java.lang", "test/location");
    assertEquals("test/location", SAXDirParser.getDirectory().get("java.lang"));
  }

  // Test case for getLocalPackages() method
  @Test
  public void testGetLocalPackages() {
    SAXDirParser.localPackages = new StringList();
    SAXDirParser.localPackages.add("test.package");
    assertEquals("test.package", SAXDirParser.getLocalPackages().get(0));
  }

  // Test case for getLocalBrowser() method
  @Test
  public void testGetLocalBrowser() {
    SAXDirParser.localBrowser = "Test Local Browser";
    assertEquals("Test Local Browser", SAXDirParser.getLocalBrowser());
  }

  // Test case for getEditor() method
  @Test
  public void testGetEditor() {
    SAXDirParser.file_editor = "Test Editor";
    assertEquals("Test Editor", SAXDirParser.getEditor());
  }

  // Test case for acceptsLineNumber() method
  @Test
  public void testAcceptsLineNumber() {
    SAXDirParser.lineNumAccepted = true;
    assertTrue(SAXDirParser.acceptsLineNumber());
  }

  // Test case for lineNumberParameter() method
  @Test
  public void testLineNumberParameter() {
    SAXDirParser.lineNumParam = "Test Parameter";
    assertEquals("Test Parameter", SAXDirParser.lineNumberParameter());
  }

  // Test case for main() method
  @Test
  public void testMain() throws Exception {
    // Execution of this method can be complex and it heavily relies on external factors,
    // thus it is not recommended to write automated unit tests for this method.
    // Manual testing along with system-level testing is advised.
  }

}