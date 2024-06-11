import org.junit.Test;
import java.util.List;
import org.jdom2.*;
import org.jdom2.transform.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;
import java.util.Collections;
import java.util.ArrayList;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();

        jdomResult.setResult(result);

        assertSame(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));

        jdomResult.setResult(result);

        assertSame(result, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();

        jdomResult.setDocument(document);

        assertSame(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);

        Document document = jdomResult.getDocument();

        assertNotNull(document);
        assertEquals(result, document.getContent());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();

        jdomResult.setFactory(factory);

        assertSame(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();

        JDOMFactory factory = jdomResult.getFactory();

        assertNull(factory);
    }
}