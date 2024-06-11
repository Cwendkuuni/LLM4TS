import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expectedName = "#declaration";
        String actualName = xmlDeclaration.nodeName();
        assertEquals(expectedName, actualName);

        xmlDeclaration = new XmlDeclaration("xml version=\"1.0\"", null, false);
        Attributes attributes = xmlDeclaration.attributes();
        String expectedVersion = "1.0";
        String actualVersion = attributes.get("version");
        assertEquals(expectedVersion, actualVersion);
    }

    @Test
    public void testNameMethod() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expectedName = "xml";
        String actualName = xmlDeclaration.name();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetWholeDeclarationMethodNormalCase() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expectedDeclaration = "<xml>";
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test
    public void testGetWholeDeclarationMethodWithVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml version=\"1.0\"", null, false);
        String expectedDeclaration = "<xml version=\"1.0\">";
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test
    public void testGetWholeDeclarationMethodWithEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml encoding=\"UTF-8\"", null, false);
        String expectedDeclaration = "<xml encoding=\"UTF-8\">";
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test
    public void testGetWholeDeclarationMethodWithVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml version=\"1.0\" encoding=\"UTF-8\"", null, false);
        String expectedDeclaration = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclarationMethodWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
        xmlDeclaration.getWholeDeclaration();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWholeDeclarationMethodWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml processing=\"foo\"", null, true);
        xmlDeclaration.getWholeDeclaration();
    }

    @Test
    public void testOuterHtmlHeadMethod() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String expectedOutput = "<xml>";
        String actualOutput = accum.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOuterHtmlHeadMethodWithProcessingInstruction() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml processing=\"foo\"", null, true);
        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        String expectedOutput = "<?xml processing=\"foo\">";
        String actualOutput = accum.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOuterHtmlTailMethod() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        String expectedOutput = ">";
        String actualOutput = accum.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToStringMethod() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expectedOutput = "<xml>";
        String actualOutput = xmlDeclaration.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToStringMethodWithVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml version=\"1.0\"", null, false);
        String expectedOutput = "<xml version=\"1.0\">";
        String actualOutput = xmlDeclaration.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToStringMethodWithEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml encoding=\"UTF-8\"", null, false);
        String expectedOutput = "<xml encoding=\"UTF-8\">";
        String actualOutput = xmlDeclaration.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToStringMethodWithVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml version=\"1.0\" encoding=\"UTF-8\"", null, false);
        String expectedOutput = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        String actualOutput = xmlDeclaration.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}