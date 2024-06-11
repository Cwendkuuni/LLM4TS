import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMFactory;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JDOMResultTest {

    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void setResult() {
        List<Content> result = mock(List.class);
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void setResult_multipleTimes() {
        List<Content> result1 = mock(List.class);
        List<Content> result2 = mock(List.class);
        jdomResult.setResult(result1);
        jdomResult.setResult(result2);
        assertEquals(result2, jdomResult.getResult());
    }

    @Test
    public void getResult_emptyResultList() {
        assertNull(jdomResult.getResult());
    }

    @Test
    public void getResult_nonEmptyResultList() {
        List<Content> result = mock(List.class);
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void setDocument() {
        Document document = mock(Document.class);
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void setDocument_clearsResultList() {
        List<Content> result = mock(List.class);
        jdomResult.setResult(result);
        Document document = mock(Document.class);
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void getDocument_emptyResultList() {
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_nonEmptyResultList() {
        List<Content> result = mock(List.class);
        jdomResult.setResult(result);
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void setFactory() {
        JDOMFactory factory = mock(JDOMFactory.class);
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void getFactory_defaultFactory() {
        JDOMFactory factory = jdomResult.getFactory();
        assertEquals(DefaultJDOMFactory.class, factory.getClass());
    }

    @Test
    public void retrieveResult_emptyResultListAndDocument() {
        jdomResult.retrieveResult();
        assertNull(jdomResult.getResult());
    }

    @Test
    public void retrieveResult_nonEmptyResultList() {
        List<Content> result = mock(List.class);
        jdomResult.setResult(result);
        jdomResult.retrieveResult();
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void retrieveResult_emptyResultList_nonEmptyDocument() {
        Document document = mock(Document.class);
        when(document.getContent()).thenReturn(Collections.emptyList());
        jdomResult.setDocument(document);
        jdomResult.retrieveResult();
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void retrieveResult_nonEmptyResultList_nonEmptyDocument() {
        Document document = mock(Document.class);
        List<Content> content = mock(List.class);
        List<Content> result = new ArrayList<>();
        Content content1 = mock(Content.class);
        Content content2 = mock(Content.class);

        result.add(content1);
        result.add(content2);

        when(document.getContent()).thenReturn(content);
        when(content.remove(0)).thenReturn(content1, content2);
        when(content.size()).thenReturn(2, 1, 0);

        jdomResult.setDocument(document);
        jdomResult.retrieveResult();

        assertEquals(result, jdomResult.getResult());
        assertNull(jdomResult.getDocument());
    }
}