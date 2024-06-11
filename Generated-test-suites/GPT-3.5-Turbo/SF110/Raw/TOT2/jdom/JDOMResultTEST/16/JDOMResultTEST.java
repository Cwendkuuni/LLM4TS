import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collections;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("testElement");
        contentList.add(content);

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(contentList);

        assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(document);

        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);

        assertEquals(factory, jdomResult.getFactory());
    }

}