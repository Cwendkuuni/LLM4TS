import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setup() {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        // Typical use case
        parser.webData = "Test web data";
        Assert.assertEquals("Test web data", parser.getWebData());

        // Edge case: empty web data
        parser.webData = "";
        Assert.assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() {
        // Typical use case: qname equals "BrowserLoc"
        parser.startElement("", "", "BrowserLoc", null);
        Assert.assertTrue(parser.check);
        Assert.assertEquals(0, parser.level);

        // Edge case: qname equals "Editor"
        parser.startElement("", "", "Editor", null);
        Assert.assertTrue(parser.check);
        Assert.assertEquals(10, parser.level);

        // Error scenario: qname not recognized
        parser.startElement("", "", "Unknown", null);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(0, parser.level);
    }

    @Test
    public void testCharacters() {
        // Typical use case: level equals 0
        parser.level = 0;
        parser.check = true;
        parser.characters("TestBrowserLoc".toCharArray(), 0, 14);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertEquals("TestBrowserLoc", SAXDirParser.localBrowser);

        // Typical use case: level equals 10
        parser.level = 10;
        parser.check = true;
        parser.characters("TestEditor".toCharArray(), 0, 9);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertEquals("TestEditor", SAXDirParser.file_editor);

        // Typical use case: level equals 11
        parser.level = 11;
        parser.check = true;
        parser.characters("Yes".toCharArray(), 0, 3);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertTrue(SAXDirParser.lineNumAccepted);

        // Typical use case: level equals 12
        parser.level = 12;
        parser.check = true;
        parser.characters("TestParameter".toCharArray(), 0, 13);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertEquals("TestParameter", SAXDirParser.lineNumParam);

        // Typical use case: level equals 30
        parser.level = 30;
        parser.check = true;
        parser.characters("TestLocation".toCharArray(), 0, 12);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertEquals("TestLocation", parser.location);

        // Typical use case: level equals 31
        parser.level = 31;
        parser.check = true;
        parser.characters("No".toCharArray(), 0, 2);
        Assert.assertFalse(parser.check);
        Assert.assertEquals(-1, parser.level);
        Assert.assertFalse(parser.local);

        // Typical use case: level equals 4
        parser.level = 4;
        parser.check = true;
        parser.local = true;
        parser.characters("TestPackage".toCharArray(), 0, 11);
        Assert.assertEquals("TestPackage", SAXDirParser.directory.get("TestPackage"));
        Assert.assertTrue(SAXDirParser.localPackages.contains("TestPackage"));
    }

    @Test
    public void testEndElement() {
        // Typical use case: qname equals "Group"
        parser.endElement("", "", "Group");
        Assert.assertFalse(parser.check);

        // Error scenario: qname not recognized
        parser.endElement("", "", "Unknown");
        Assert.assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        // Typical use case
        SAXDirParser.directory.put("key", "value");
        HashMap<String, String> clone = SAXDirParser.getDirectory();
        Assert.assertEquals("value", clone.get("key"));

        // Edge case: empty directory
        SAXDirParser.directory.clear();
        clone = SAXDirParser.getDirectory();
        Assert.assertTrue(clone.isEmpty());
    }

    @Test
    public void testGetLocalPackages() {
        // Typical use case: localPackages contains values
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        StringList clone = SAXDirParser.getLocalPackages();
        Assert.assertEquals(2, clone.size());
        Assert.assertEquals("package1", clone.get(0));
        Assert.assertEquals("package2", clone.get(1));

        // Edge case: localPackages is empty
        SAXDirParser.localPackages.clear();
        clone = SAXDirParser.getLocalPackages();
        Assert.assertNull(clone);
    }

    @Test
    public void testGetLocalBrowser() {
        // Typical use case
        SAXDirParser.localBrowser = "TestLocalBrowser";
        Assert.assertEquals("TestLocalBrowser", SAXDirParser.getLocalBrowser());

        // Edge case: empty localBrowser
        SAXDirParser.localBrowser = "";
        Assert.assertEquals("", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        // Typical use case
        SAXDirParser.file_editor = "TestEditor";
        Assert.assertEquals("TestEditor", SAXDirParser.getEditor());

        // Edge case: empty file_editor
        SAXDirParser.file_editor = "";
        Assert.assertEquals("", SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        // Typical use case
        SAXDirParser.lineNumAccepted = true;
        Assert.assertTrue(SAXDirParser.acceptsLineNumber());

        // Typical use case
        SAXDirParser.lineNumAccepted = false;
        Assert.assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        // Typical use case
        SAXDirParser.lineNumParam = "TestParameter";
        Assert.assertEquals("TestParameter", SAXDirParser.lineNumberParameter());

        // Edge case: empty lineNumParam
        SAXDirParser.lineNumParam = "";
        Assert.assertEquals("", SAXDirParser.lineNumberParameter());
    }
}