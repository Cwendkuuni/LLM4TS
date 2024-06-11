package org.jdom2.transform;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;


public class JDOMResultTest {

    @Test
    public void setResult_NullList_QueryResult_ReturnsEmptyList() {
        JDOMResult result = new JDOMResult();
        result.setResult(null);
        List<Content> expectedResult = new ArrayList<>();
        List<Content> actualResult = result.getResult();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setResult_EmptyList_QueryResult_ReturnsEmptyList() {
        JDOMResult result = new JDOMResult();
        result.setResult(new ArrayList<>());
        List<Content> expectedResult = new ArrayList<>();
        List<Content> actualResult = result.getResult();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setResult_NonEmptyList_QueryResult_ReturnsSameList() {
        JDOMResult result = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element"));
        result.setResult(resultList);
        List<Content> expectedResult = resultList;
        List<Content> actualResult = result.getResult();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setDocument_NullDocument_QueryDocument_ReturnsNull() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);
        Document expectedResult = null;
        Document actualResult = result.getDocument();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setDocument_ValidDocument_QueryDocument_ReturnsSameDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);
        Document expectedResult = doc;
        Document actualResult = result.getDocument();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setFactory_NullFactory_GetFactory_ReturnsNull() {
        JDOMResult result = new JDOMResult();
        result.setFactory(null);
        JDOMFactory expectedResult = null;
        JDOMFactory actualResult = result.getFactory();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setFactory_ValidFactory_GetFactory_ReturnsSameFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        JDOMFactory expectedResult = factory;
        JDOMFactory actualResult = result.getFactory();
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void setHandler_ContentHandler_NoAction() {
        JDOMResult result = new JDOMResult();
        result.setHandler(null);
        // No assertion, as the method does not alter the state of the object
    }
    
    @Test
    public void setLexicalHandler_LexicalHandler_NoAction() {
        JDOMResult result = new JDOMResult();
        result.setLexicalHandler(null);
        // No assertion, as the method does not alter the state of the object
    }

}