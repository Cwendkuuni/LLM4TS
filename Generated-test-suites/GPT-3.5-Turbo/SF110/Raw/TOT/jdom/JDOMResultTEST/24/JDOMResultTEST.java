package org.jdom2.transform;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;

public class JDOMResultTest {

   @Test
   public void testSetResult_typicalCase() {
      JDOMResult result = new JDOMResult();
      List<Content> resultList = new ArrayList<>();
      resultList.add(new Element("element"));
      resultList.add(new Comment("comment"));
      
      result.setResult(resultList);
      
      List<Content> retrievedResult = result.getResult();
      
      assertEquals(resultList, retrievedResult);
   }
   
   @Test
   public void testGetResult_resultListIsNull() {
      JDOMResult result = new JDOMResult();
      result.setResult(null);
      
      List<Content> retrievedResult = result.getResult();
      
      assertNotNull(retrievedResult);
      assertEquals(Collections.emptyList(), retrievedResult);
   }
   
   @Test
   public void testGetResult_resultListIsNotNullButQueried() {
      JDOMResult result = new JDOMResult();
      List<Content> resultList = new ArrayList<>();
      resultList.add(new Element("element"));
      
      result.setResult(resultList);
      
      List<Content> retrievedResult1 = result.getResult();
      List<Content> retrievedResult2 = result.getResult();
      
      assertEquals(resultList, retrievedResult1);
      assertEquals(resultList, retrievedResult2);
   }
   
   @Test
   public void testGetResult_resultDocNotNullButQueried() {
      JDOMResult result = new JDOMResult();
      Document resultDoc = new Document();
      
      result.setDocument(resultDoc);
      
      List<Content> retrievedResult1 = result.getResult();
      List<Content> retrievedResult2 = result.getResult();
      
      assertNotNull(retrievedResult1);
      assertNotNull(retrievedResult2);
   }
   
   @Test
   public void testGetResult_resultDocAndResultListNotNullButQueried() {
      JDOMResult result = new JDOMResult();
      Document resultDoc = new Document();
      List<Content> resultList = new ArrayList<>();
      resultList.add(new Element("element"));
      
      result.setDocument(resultDoc);
      result.setResult(resultList);
      
      List<Content> retrievedResult1 = result.getResult();
      List<Content> retrievedResult2 = result.getResult();
      
      assertNotNull(retrievedResult1);
      assertNotNull(retrievedResult2);
   }
   
   @Test
   public void testSetDocument_typicalCase() {
      JDOMResult result = new JDOMResult();
      Document resultDoc = new Document();
      
      result.setDocument(resultDoc);
      
      Document retrievedDoc = result.getDocument();
      
      assertEquals(resultDoc, retrievedDoc);
   }
   
   @Test
   public void testGetDocument_resultDocIsNull() {
      JDOMResult result = new JDOMResult();
      result.setDocument(null);
      
      Document retrievedDoc = result.getDocument();
      
      assertNull(retrievedDoc);
   }
   
   @Test
   public void testGetDocument_resultListIsNotNullButQueried() {
      JDOMResult result = new JDOMResult();
      List<Content> resultList = new ArrayList<>();
      resultList.add(new Element("element"));
      
      result.setResult(resultList);
      
      Document retrievedDoc1 = result.getDocument();
      Document retrievedDoc2 = result.getDocument();
      
      assertNull(retrievedDoc1);
      assertNull(retrievedDoc2);
   }
   
   @Test
   public void testGetDocument_resultDocIsNotNullButQueried() {
      JDOMResult result = new JDOMResult();
      Document resultDoc = new Document();
      
      result.setDocument(resultDoc);
      
      Document retrievedDoc1 = result.getDocument();
      Document retrievedDoc2 = result.getDocument();
      
      assertEquals(resultDoc, retrievedDoc1);
      assertEquals(resultDoc, retrievedDoc2);
   }
   
   @Test
   public void testSetFactory_typicalCase() {
      JDOMResult result = new JDOMResult();
      JDOMFactory factory = new DefaultJDOMFactory();
      
      result.setFactory(factory);
      
      JDOMFactory retrievedFactory = result.getFactory();
      
      assertEquals(factory, retrievedFactory);
   }
   
   @Test
   public void testGetFactory_typicalCase() {
      JDOMResult result = new JDOMResult();
      JDOMFactory factory = new DefaultJDOMFactory();
      
      result.setFactory(factory);
      
      JDOMFactory retrievedFactory = result.getFactory();
      
      assertEquals(factory, retrievedFactory);
   }
   
   @Test
   public void testRetrieveResult_resultListAndResultDocNull() {
      JDOMResult result = new JDOMResult();
      
      result.retrieveResult();
      
      assertNull(result.getResult());
   }
   
   @Test
   public void testSetHandler() {
      JDOMResult result = new JDOMResult();
      ContentHandler handler = null;
      
      result.setHandler(handler);
      
      // No assertions, as it is a void method
   }
   
   @Test
   public void testSetLexicalHandler() {
      JDOMResult result = new JDOMResult();
      LexicalHandler handler = null;
      
      result.setLexicalHandler(handler);
      
      // No assertions, as it is a void method
   }
   
   @Test
   public void testFragmentHandler_getResult_typicalCase() throws SAXException {
      JDOMFactory factory = new DefaultJDOMFactory();
      JDOMResult.FragmentHandler handler = new JDOMResult.FragmentHandler(factory);
      Element element = new Element("element");
      Comment comment = new Comment("comment");
      List<Content> expectedResult = new ArrayList<>();
      expectedResult.add(element);
      expectedResult.add(comment);
      handler.startElement("", "element", "", new AttributesImpl());
      handler.startCDATA();
      handler.characters("content".toCharArray(), 0, 7);
      handler.endCDATA();
      handler.comment("comment".toCharArray(), 0, 7);
      handler.endElement("", "element", "");
      
      List<Content> result = handler.getResult();
      
      assertEquals(expectedResult, result);
   }
   
   @Test
   public void testDocumentBuilder_getResult_typicalCase() throws SAXException {
      JDOMFactory factory = new DefaultJDOMFactory();
      JDOMResult.DocumentBuilder docBuilder = new JDOMResult.DocumentBuilder();
      Element element = new Element("element");
      Comment comment = new Comment("comment");
      List<Content> expectedResult = new ArrayList<>();
      expectedResult.add(element);
      expectedResult.add(comment);
      docBuilder.startElement("", "element", "", new AttributesImpl());
      docBuilder.startCDATA();
      docBuilder.characters("content".toCharArray(), 0, 7);
      docBuilder.endCDATA();
      docBuilder.comment("comment".toCharArray(), 0, 7);
      docBuilder.endElement("", "element", "");
      
      List<Content> result = docBuilder.getResult();
      
      assertEquals(expectedResult, result);
   }
}