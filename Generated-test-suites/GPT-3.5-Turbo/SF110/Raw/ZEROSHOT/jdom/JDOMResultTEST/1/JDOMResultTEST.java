import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.transform.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("element"));
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("element"));
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

}