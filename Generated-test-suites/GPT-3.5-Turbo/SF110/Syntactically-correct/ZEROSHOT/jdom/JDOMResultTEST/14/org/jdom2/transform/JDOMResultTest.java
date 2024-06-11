package org.jdom2.transform;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

  @Test
  public void testSetResult() {
    JDOMResult result = new JDOMResult();

    List<Content> contentList = new ArrayList<>();

    result.setResult(contentList);

    Assert.assertEquals(contentList, result.getResult());
  }

  @Test
  public void testGetResult() {
    JDOMResult result = new JDOMResult();

    List<Content> contentList = new ArrayList<>();

    result.setResult(contentList);

    Assert.assertEquals(contentList, result.getResult());
  }

  @Test
  public void testSetDocument() {
    JDOMResult result = new JDOMResult();

    Document document = new Document();

    result.setDocument(document);

    Assert.assertEquals(document, result.getDocument());
  }

  @Test
  public void testGetDocument() {
    JDOMResult result = new JDOMResult();

    Document document = new Document();

    result.setDocument(document);

    Assert.assertEquals(document, result.getDocument());
  }

  @Test
  public void testSetFactory() {
    JDOMResult result = new JDOMResult();

    JDOMFactory factory = new JDOMFactory();

    result.setFactory(factory);

    Assert.assertEquals(factory, result.getFactory());
  }

  @Test
  public void testGetFactory() {
    JDOMResult result = new JDOMResult();

    JDOMFactory factory = new JDOMFactory();

    result.setFactory(factory);

    Assert.assertEquals(factory, result.getFactory());
  }
}