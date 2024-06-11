package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import java.util.List;
import java.util.ArrayList;

public class DocumentTest {

  private Document document;
  private final String BASE_URI = "http://example.com";

  @Before
  public void setUp() {
    document = new Document(BASE_URI);
  }

  @After
  public void tearDown() {
    document = null;
  }

  @Test
  public void testCreateShell() {
    Document shell = Document.createShell(BASE_URI);

    Assert.assertNotNull(shell);
    Assert.assertEquals(BASE_URI, shell.baseUri());
    Assert.assertEquals("html", shell.getElementsByTag("html").first().nodeName());
    Assert.assertEquals("head", shell.getElementsByTag("head").first().nodeName());
    Assert.assertEquals("body", shell.getElementsByTag("body").first().nodeName());
  }

  @Test
  public void testHead() {
    Element head = document.head();

    Assert.assertNull(head);
  }

  @Test
  public void testBody() {
    Element body = document.body();

    Assert.assertNull(body);
  }

  @Test
  public void testTitleWithTitleTagExists() {
    String expectedTitle = "Page Title";
    document.head().appendElement("title").text(expectedTitle);

    String actualTitle = document.title();

    Assert.assertEquals(expectedTitle, actualTitle);
  }

  @Test
  public void testTitleWithNoTitleTag() {
    String actualTitle = document.title();

    Assert.assertEquals("", actualTitle);
  }

  @Test
  public void testSetTitleWithTitleTagExists() {
    String expectedTitle = "Page Title";
    document.head().appendElement("title").text(expectedTitle);

    document.title(expectedTitle);

    String actualTitle = document.title();
    Assert.assertEquals(expectedTitle, actualTitle);
  }

  @Test
  public void testSetTitleWithNoTitleTag() {
    String expectedTitle = "Page Title";
    document.title(expectedTitle);

    String actualTitle = document.title();
    Assert.assertEquals(expectedTitle, actualTitle);
    Assert.assertEquals(expectedTitle, document.head().getElementsByTag("title").first().text());
  }

  @Test
  public void testCreateElement() {
    String tagName = "div";
    Element element = document.createElement(tagName);

    Assert.assertNotNull(element);
    Assert.assertEquals(tagName, element.tagName());
    Assert.assertEquals(BASE_URI, element.baseUri());
  }

  @Test
  public void testNormaliseWithEmptyDocument() {
    Document normalizedDocument = document.normalise();

    Assert.assertEquals("#root", normalizedDocument.nodeName());
    Assert.assertEquals("head", normalizedDocument.head().nodeName());
    Assert.assertEquals("body", normalizedDocument.body().nodeName());
  }

  @Test
  public void testNormaliseWithExistingHeadAndBody() {
    document.appendElement("html");
    document.select("html").first().prependElement("head");
    document.select("head").first().appendElement("title").text("Page Title");
    document.select("html").first().appendElement("body");

    Document normalizedDocument = document.normalise();

    Assert.assertEquals("#root", normalizedDocument.nodeName());
    Assert.assertEquals("head", normalizedDocument.head().nodeName());
    Assert.assertEquals("body", normalizedDocument.body().nodeName());
    Assert.assertEquals("Page Title", normalizedDocument.head().getElementsByTag("title").first().text());
  }

  @Test
  public void testOuterHtml() {
    String expectedOuterHtml = ""; // No outer wrapper tag, so outerHtml() should return empty string

    String actualOuterHtml = document.outerHtml();

    Assert.assertEquals(expectedOuterHtml, actualOuterHtml);
  }

  @Test
  public void testText() {
    String expectedText = "Document Body";
    document.appendElement("body");

    document.text(expectedText);

    String actualText = document.body().text();
    Assert.assertEquals(expectedText, actualText);
  }

  @Test
  public void testNodeName() {
    String expectedNodeName = "#document";

    String actualNodeName = document.nodeName();

    Assert.assertEquals(expectedNodeName, actualNodeName);
  }

}