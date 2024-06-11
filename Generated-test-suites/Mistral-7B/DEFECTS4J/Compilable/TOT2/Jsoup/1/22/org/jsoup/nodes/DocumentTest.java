package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testHeadMethod() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertEquals(Element.class, head.getClass());
    }
}