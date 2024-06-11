
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.transform.*;
import org.xml.sax.*;
import javax.xml.transform.sax.*;
import org.junit.Before;

public class JDOMResultTest {

    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void setResult_ValidList_UpdatesResultlist() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        List<Content> actual = jdomResult.getResult();
        assertEquals(result, actual);
    }

    @Test
    public void getResult_EmptyResult_ReturnsEmptyList() {
        List<Content> expected = Collections.emptyList();
        List<Content> actual = jdomResult.getResult();
        assertEquals(expected, actual);
    }

    @Test
    public void getDocument_ValidResultList_CreatesDocument() {
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        Document actual = jdomResult.getDocument();
        assertNotNull(actual);
        assertEquals(result, actual.getContent());
    }

    @Test
    public void setFactory_ValidFactory_SetsFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actual = jdomResult.getFactory();
        assertEquals(factory, actual);
    }

    // Exception Handling Tests

    @Test
    public void setDocument_ValidDocument_SetsResultdoc() {
        Document document = new Document();
        jdomResult.setDocument(document);
        Document actual = jdomResult.getDocument();
        assertEquals(document, actual);
    }

    @Test
    public void setDocument_NullDocument_SetsResultdocNull() {
        jdomResult.setDocument(null);
        Document actual = jdomResult.getDocument();
        assertNull(actual);
    }

    @Test
    public void getDocument_NullResultlist_CreatesDocumentUsingDefaultJDOMFactory() {
        Document expected = new DefaultJDOMFactory().document(null);
        jdomResult.setResult(null);
        Document actual = jdomResult.getDocument();
        assertEquals(expected, actual);
    }

    @Test
    public void getDocument_NullResultlistAndNullFactory_ReturnsNull() {
        jdomResult.setResult(null);
        jdomResult.setFactory(null);
        Document actual = jdomResult.getDocument();
        assertNull(actual);
    }

    @Test
    public void getDocument_NullResultlistAndRuntimeException_ReturnsNull() {
        jdomResult.setResult(null);
        jdomResult.setFactory(null);
        Document actual = jdomResult.getDocument();
        assertNull(actual);
    }

}
