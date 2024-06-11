
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
        Assert.assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings(Syntax.html));
        Assert.assertEquals("", accum.toString());
    }
}
