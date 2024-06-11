package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("#doctype", "", "", "");
        String nodeName = documentType.nodeName();
        assert nodeName.equals("#doctype");
    }
}