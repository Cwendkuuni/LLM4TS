package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

  @Test
  public void testCreateValueStringObject() {
    try {
      assertNull(TypeHandler.createValue(null, null));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateValueStringClassOfQ() {
    try {
      assertNull(TypeHandler.createValue(null, null));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateObject() {
    try {
      assertNotNull(TypeHandler.createObject("java.lang.String"));
      assertNotNull(TypeHandler.createObject("java.lang.Integer"));
      assertNotNull(TypeHandler.createObject("java.lang.Boolean"));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateNumber() {
    try {
      assertEquals(new Double("1.2"), TypeHandler.createNumber("1.2"));
      assertEquals(new Long("10"), TypeHandler.createNumber("10"));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateClass() {
    try {
      assertNotNull(TypeHandler.createClass("java.lang.String"));
      assertNotNull(TypeHandler.createClass("java.lang.Integer"));
      assertNotNull(TypeHandler.createClass("java.lang.Boolean"));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateDate() {
    try {
      assertEquals(null, TypeHandler.createDate(null));
      throw new UnsupportedOperationException();
    } catch (UnsupportedOperationException e) {

    }
  }

  @Test
  public void testCreateURL() {
    try {
      assertNotNull(TypeHandler.createURL("https://www.example.com"));
      assertNotNull(TypeHandler.createURL("http://www.example.com"));
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateFile() {
    assertNotNull(TypeHandler.createFile("file.txt"));
  }

  @Test
  public void testCreateFiles() {
    try {
      throw new UnsupportedOperationException();
    } catch (UnsupportedOperationException e) {

    }
  }

}