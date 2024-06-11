import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.mockito.*;

public class SAXDirParserTest {

    @Mock
    private SAXParserFactory saxFactory;

    @Mock
    private SAXParser myParser;

    @Mock
    private XMLReader myReader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String webData = parser.getWebData();
        assertEquals("", webData);
    }

    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        Attributes atts = Mockito.mock(Attributes.class);

        // Test qname "BrowserLoc"
        parser.startElement("", "", "BrowserLoc", atts);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        // Test qname "Editor"
        parser.startElement("", "", "Editor", atts);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        // Test qname "acceptsLineNumber"
        parser.startElement("", "", "acceptsLineNumber", atts);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        // Test qname "parameter"
        parser.startElement("", "", "parameter", atts);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        // Test qname "Group"
        parser.startElement("", "", "Group", atts);
        assertFalse(parser.check);
        assertEquals(2, parser.level);

        // Test qname "Web"
        parser.startElement("", "", "Web", atts);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        // Test qname "Local"
        parser.startElement("", "", "Local", atts);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        // Test qname "Names"
        parser.startElement("", "", "Names", atts);
        assertTrue(parser.check);

        // Test qname "pkg"
        parser.startElement("", "", "pkg", atts);
        assertEquals(4, parser.level);

    }

    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        char[] charArray = {'a', 'b', 'c'};
        parser.characters(charArray, 0, charArray.length);
        assertEquals("abc", parser.localBrowser);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 10;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("abc", SAXDirParser.file_editor);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 11;
        parser.characters(charArray, 0, charArray.length);
        assertFalse(SAXDirParser.lineNumAccepted);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 12;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("abc", SAXDirParser.lineNumParam);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 30;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("abc", parser.location);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 31;
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.local);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertTrue(parser.local);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 4;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("", SAXDirParser.directory.get("abc"));
        assertEquals("", parser.location);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 4;
        parser.local = true;
        parser.characters("abc".toCharArray(), 0, 3);
        assertEquals("abc", SAXDirParser.directory.get("abc"));
        assertEquals("", parser.location);
        assertTrue(SAXDirParser.localPackages.contains("abc"));
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }

    @Test
    public void testEndElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();

        // Test qname "Group"
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = Mockito.mock(HashMap.class);

        HashMap directory = SAXDirParser.getDirectory();
        assertNotNull(directory);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = Mockito.mock(StringList.class);

        StringList localPackages = SAXDirParser.getLocalPackages();
        assertNull(localPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "abc";
        String localBrowser = SAXDirParser.getLocalBrowser();
        assertEquals("abc", localBrowser);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "abc";
        String file_editor = SAXDirParser.getEditor();
        assertEquals("abc", file_editor);
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
        SAXDirParser.lineNumParam = "abc";
        String lineNumParam = SAXDirParser.lineNumberParameter();
        assertEquals("abc", lineNumParam);
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Mockito.when(saxFactory.newInstance()).thenReturn(saxFactory);
        Mockito.when(saxFactory.newSAXParser()).thenReturn(myParser);
        Mockito.when(myParser.getXMLReader()).thenReturn(myReader);

        SAXDirParser.main(new String[]{});

        Mockito.verify(myReader, Mockito.times(1)).setContentHandler(saxDirParser);
        Mockito.verify(myReader, Mockito.times(1)).parse("packagedirectory.xml");
    }

    @After
    public void tearDown() {
        SAXDirParser.directory = null;
        SAXDirParser.localPackages = null;
        SAXDirParser.localBrowser = "";
        SAXDirParser.file_editor = "";
        SAXDirParser.lineNumAccepted = false;
        SAXDirParser.lineNumParam = "";
    }
}