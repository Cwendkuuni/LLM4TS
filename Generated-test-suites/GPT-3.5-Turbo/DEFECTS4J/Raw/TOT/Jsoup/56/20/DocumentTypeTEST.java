import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";

        documentType = new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicAndSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(false);

        StringBuilder expected = new StringBuilder("<!doctype");
        expected.append(" html");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(false);

        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD HTML 4.01//EN");

        StringBuilder expected = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(false);

        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/strict.dtd");

        StringBuilder expected = new StringBuilder("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\"");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicAndSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(false);

        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD XHTML 1.0 Transitional//EN");
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");

        StringBuilder expected = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPrivateDoctype() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(false);

        documentType.attr(DocumentType.PUBLIC_ID, "-//W3O//DTD HTML 3.2//EN");
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/REC-html32.dtd");

        StringBuilder expected = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3O//DTD HTML 3.2//EN\" \"http://www.w3.org/TR/REC-html32.dtd\"");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoSyntax() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);

        StringBuilder expected = new StringBuilder("<!DOCTYPE html");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdAndIndentation() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        outputSettings.prettyPrint(true);
        outputSettings.indentAmount(4);

        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/strict.dtd");

        StringBuilder expected = new StringBuilder("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\"");
        expected.append(System.lineSeparator()).append("    ");

        StringBuilder output = new StringBuilder();
        try {
            documentType.outerHtmlHead(output, 1, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testHasWithValidAttribute() {
        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD HTML 4.01//EN");

        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testHasWithNullAttribute() {
        assertFalse(documentType.has(null));
    }

    @Test
    public void testHasWithEmptyAttribute() {
        assertFalse(documentType.has(""));
    }

    @Test
    public void testHasWithNonExistentAttribute() {
        assertFalse(documentType.has("nonexistent"));
    }
}