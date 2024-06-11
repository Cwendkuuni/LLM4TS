package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    // Add test cases for other methods

    // ...
}