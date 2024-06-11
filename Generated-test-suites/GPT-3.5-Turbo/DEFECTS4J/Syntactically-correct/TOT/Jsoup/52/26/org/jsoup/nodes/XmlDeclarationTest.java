import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class XmlDeclarationTest {

    @Mock
    XmlDeclaration xmlDeclaration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
    }

    @Test
    public void testNodeName() {
        String expected = "#declaration";
        String actual = xmlDeclaration.nodeName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testName() {
        String expected = "xml";
        String actual = xmlDeclaration.name();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclaration_rootNode() {
        String expected = "xml";
        String actual = xmlDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclaration_withAttributes() {
        xmlDeclaration.addAttribute("version", "1.0");
        xmlDeclaration.addAttribute("encoding", "UTF-8");

        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        String actual = xmlDeclaration.getWholeDeclaration();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        xmlDeclaration.outerHtmlHead(accum, depth, out);

        String expected = "<?xml>";
        String actual = accum.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        xmlDeclaration.outerHtmlTail(accum, depth, out);

        String expected = "";
        String actual = accum.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "<?xml>";
        String actual = xmlDeclaration.toString();
        Assert.assertEquals(expected, actual);
    }
}