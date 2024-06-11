
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (IOException e) {
            Assert.fail("Unexpected exception");
        }

        // Verify the output
        String expectedOutput = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        Assert.assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Call the method
        documentType.outerHtmlTail(accum, 0, outputSettings);

        // Verify the output
        Assert.assertEquals("", accum.toString());
    }
}
