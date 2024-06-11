
import org.jdom2.*;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> expectedResultList = Arrays.asList(
                new Element("element1"),
                new Element("element2"),
                new Element("element3")
        );

        jdomResult.setResult(expectedResultList);
        List<Content> actualResultList = jdomResult.getResult();

        assertEquals(expectedResultList, actualResultList);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetResult_NullResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> expectedResultList = new ArrayList<>();
        jdomResult.setDocument(new Document());

        List<Content> actualResultList = jdomResult.getResult();

        assertEquals(expectedResultList, actualResultList);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetResult_NullResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> expectedResultList = Arrays.asList(
                new Element("element1"),
                new Element("element2"),
                new Element("element3")
        );
        jdomResult.setResult(expectedResultList);

        List<Content> actualResultList = jdomResult.getResult();

        assertEquals(expectedResultList, actualResultList);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document expectedDocument = new Document();
        
        jdomResult.setDocument(expectedDocument);
        Document actualDocument = jdomResult.getDocument();
        
        assertEquals(expectedDocument, actualDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_NullResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document expectedDocument = new Document();
        jdomResult.setResult(Arrays.asList(
                new Element("element1"),
                new Element("element2"),
                new Element("element3")
        ));

        Document actualDocument = jdomResult.getDocument();

        assertNotEquals(expectedDocument, actualDocument);
        assertNull(actualDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_NullResultList() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);

        Document actualDocument = jdomResult.getDocument();

        assertNotNull(actualDocument);
        assertTrue(jdomResult.isQueried());
        assertEquals(factory, actualDocument.getFactory());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory expectedFactory = new DefaultJDOMFactory();
        
        jdomResult.setFactory(expectedFactory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        
        assertEquals(expectedFactory, actualFactory);
    }
    
    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> expectedResultList = new ArrayList<>();
        expectedResultList.add(new Element("element1"));
        expectedResultList.add(new Element("element2"));
        expectedResultList.add(new Element("element3"));
        
        jdomResult.setDocument(new Document());
        jdomResult.retrieveResult();
        List<Content> actualResultList = jdomResult.getResult();
        
        assertEquals(expectedResultList, actualResultList);
    }
    
    @Test
    public void testSetHandler() {
        JDOMResult jdomResult = new JDOMResult();
        ContentHandler expectedHandler = new DefaultHandler();
        
        jdomResult.setHandler(expectedHandler);
    }
    
    @Test
    public void testSetLexicalHandler() {
        JDOMResult jdomResult = new JDOMResult();
        LexicalHandler expectedHandler = new DefaultHandler();
        
        jdomResult.setLexicalHandler(expectedHandler);
    }
    
    @Test
    public void testFragmentHandler_GetResult() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(factory);
        List<Content> expectedResultList = Arrays.asList(
                new Element("element1"),
                new Element("element2"),
                new Element("element3")
        );
        fragmentHandler.startElement(null, null, null, null);
        fragmentHandler.characters("text".toCharArray(), 0, 4);
        fragmentHandler.startElement(null, null, "element1", null);
        fragmentHandler.endElement(null, null, "element1");
        fragmentHandler.startElement(null, null, "element2", null);
        fragmentHandler.endElement(null, null, "element2");
        fragmentHandler.startElement(null, null, "element3", null);
        fragmentHandler.endElement(null, null, "element3");

        List<Content> actualResultList = fragmentHandler.getResult();

        assertEquals(expectedResultList, actualResultList);
    }
    
    @Test
    public void testDocumentBuilder_GetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> expectedResultList = Arrays.asList(
                new Element("element1"),
                new Element("element2"),
                new Element("element3")
        );
        jdomResult.setHandler(jdomResult.new DocumentBuilder());
        jdomResult.setResult(expectedResultList);

        List<Content> actualResultList = jdomResult.getResult();

        assertEquals(expectedResultList, actualResultList);
        assertTrue(jdomResult.isQueried());
    }
}
