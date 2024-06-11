import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.Content;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        contentList.add(element);
        result.setResult(contentList);

        assertEquals(contentList, result.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();

        assertNull(result.getResult());

        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        contentList.add(element);
        result.setResult(contentList);

        assertEquals(contentList, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();

        result.setDocument(document);

        assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();

        assertNull(result.getDocument());

        Document document = new Document();
        result.setDocument(document);

        assertEquals(document, result.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();

        result.setFactory(factory);

        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();

        assertNull(result.getFactory());

        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);

        assertEquals(factory, result.getFactory());
    }
}