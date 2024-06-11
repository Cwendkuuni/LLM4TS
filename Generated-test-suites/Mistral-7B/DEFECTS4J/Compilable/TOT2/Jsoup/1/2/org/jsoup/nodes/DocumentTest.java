package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    @Test
    public void testHeadMethod() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
    }
}