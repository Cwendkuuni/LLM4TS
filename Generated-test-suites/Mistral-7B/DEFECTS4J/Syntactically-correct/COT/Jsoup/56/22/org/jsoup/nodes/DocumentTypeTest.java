import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import static org.jsoup.helper.StringUtil.isBlank;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        String expectedName = "html";
        String expectedPublicID = "publicID";
        String expectedSystemID = "systemID";

        Assert.assertEquals(expectedName, docType.name());
        Assert.assertEquals(expectedPublicID, docType.attr(DocumentType.PUBLIC_ID));
        Assert.assertEquals(expectedSystemID, docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Document document = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        String expectedHtml = "<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">";

        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, document.outputSettings());

        String actualHtml = accum.toString();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testOuterHtmlHeadWithHtml5() throws IOException {
        Document document = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        String expectedHtml = "<!doctype html>";

        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, document.outputSettings());

        String actualHtml = accum.toString();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() throws IOException {
        Document document = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        String expectedHtml = "<!DOCTYPE html>";

        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, document.outputSettings());

        String actualHtml = accum.toString();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadWithNullAppender() throws IOException {
        Document document = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);

        docType.outerHtmlHead(null, 0, document.outputSettings());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        boolean expectedHasPublicId = true;
        boolean expectedHasSystemId = true;
        boolean expectedHasName = true;
        boolean expectedHasNone = false;

        Assert.assertEquals(expectedHasPublicId, docType.has(DocumentType.PUBLIC_ID));
        Assert.assertEquals(expectedHasSystemId, docType.has(DocumentType.SYSTEM_ID));
        Assert.assertEquals(expectedHasName, docType.has(DocumentType.NAME));
        Assert.assertEquals(expectedHasNone, docType.has("nonexistentAttribute"));
    }
}