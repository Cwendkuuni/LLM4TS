package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

  @Test
  public void testElements() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    Elements elements = form.elements();

    assertTrue(elements.isEmpty());

    Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
    Element element2 = new Element(Tag.valueOf("select"), "", new Attributes());
    Element element3 = new Element(Tag.valueOf("textarea"), "", new Attributes());

    form.addElement(element1);
    form.addElement(element2);
    form.addElement(element3);

    elements = form.elements();

    assertEquals(3, elements.size());
    assertTrue(elements.contains(element1));
    assertTrue(elements.contains(element2));
    assertTrue(elements.contains(element3));
  }

  @Test
  public void testAddElement() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    assertTrue(form.elements().isEmpty());

    Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
    Element element2 = new Element(Tag.valueOf("select"), "", new Attributes());

    form.addElement(element1);
    form.addElement(element2);

    Elements elements = form.elements();

    assertEquals(2, elements.size());
    assertTrue(elements.contains(element1));
    assertTrue(elements.contains(element2));
  }

  @Test
  public void testRemoveChild() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    Element element = new Element(Tag.valueOf("input"), "", new Attributes());

    form.addElement(element);

    Elements elements = form.elements();

    assertEquals(1, elements.size());

    form.removeChild(element);

    elements = form.elements();

    assertTrue(elements.isEmpty());
  }

  @Test
  public void testSubmitNoAction() {
    FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());

    form.attr("method", "POST");

    assertNull(form.submit());
  }

  @Test
  public void testSubmitGetMethod() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    form.attr("action", "");

    Connection connection = form.submit();

    assertEquals(Connection.Method.GET, connection.request().method());
    assertEquals("http://example.com", connection.request().url().toString());
  }

  @Test
  public void testSubmitPostMethod() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    form.attr("action", "");
    form.attr("method", "POST");

    Connection connection = form.submit();

    assertEquals(Connection.Method.POST, connection.request().method());
    assertEquals("http://example.com", connection.request().url().toString());
  }

  @Test
  public void testSubmitNoBaseUri() {
    FormElement form = new FormElement(Tag.valueOf("form"), null, new Attributes());

    form.attr("action", "submit.php");

    assertNull(form.submit());
  }

  @Test
  public void testFormDataNoElements() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    List<Connection.KeyVal> formData = form.formData();

    assertTrue(formData.isEmpty());
  }

  @Test
  public void testFormData() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    Element input = new Element(Tag.valueOf("input"), "", new Attributes());
    Element select = new Element(Tag.valueOf("select"), "", new Attributes("name", "select"));
    Element option1 = new Element(Tag.valueOf("option"), "", new Attributes("value", "value1"));
    option1.attr("selected", "selected");
    Element option2 = new Element(Tag.valueOf("option"), "", new Attributes("value", "value2"));
    Element checkbox = new Element(Tag.valueOf("input"), "", new Attributes("name", "checkbox", "type", "checkbox", "checked", "checked"));
    Element radio = new Element(Tag.valueOf("input"), "", new Attributes("name", "radio", "type", "radio", "checked", "checked"));

    select.appendChild(option1);
    select.appendChild(option2);

    form.addElement(input);
    form.addElement(select);
    form.addElement(checkbox);
    form.addElement(radio);

    List<Connection.KeyVal> formData = form.formData();

    assertEquals(4, formData.size());

    Connection.KeyVal keyVal1 = HttpConnection.KeyVal.create("select", "value1");
    Connection.KeyVal keyVal2 = HttpConnection.KeyVal.create("checkbox", "on");
    Connection.KeyVal keyVal3 = HttpConnection.KeyVal.create("radio", "on");

    assertTrue(formData.contains(keyVal1));
    assertTrue(formData.contains(keyVal2));
    assertTrue(formData.contains(keyVal3));
  }
}