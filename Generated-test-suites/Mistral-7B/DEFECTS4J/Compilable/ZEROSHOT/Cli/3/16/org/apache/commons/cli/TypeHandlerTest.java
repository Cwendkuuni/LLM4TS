package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringObject() {
 String str = "test";
 Object obj = new Object();
 Object result = TypeHandler.createValue(str, obj.getClass());
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClass() {
 String str = "testString";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClassNotFoundException() {
 String str = "org.apache.commons.cli.TypeHandlerTest";
 Class clazz = null;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringInstantiationException() {
 String str = "java.util.Date";
 Class clazz = Date.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringIllegalAccessException() {
 String str = "java.util.Date";
 Class clazz = Date.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number result = TypeHandler.createNumber(str);
 assertEquals(123, result.intValue());
 }

 @Test
 public void testCreateNumberInvalidNumber() {
 String str = "abc";
 Number result = TypeHandler.createNumber(str);
 assertNull(result);
 }

 @Test
 public void testCreateDate() {
 String str = "2019-12-31";
 Date result = TypeHandler.createDate(str);
 assertNotNull(result);
 }

 @Test
 public void testCreateDateInvalidDate() {
 String str = "2019-12-32";
 Date result = TypeHandler.createDate(str);
 assertNull(result);
 }

 @Test
 public void testCreateURL() {
 String str = "http://www.example.com";
 URL result = TypeHandler.createURL(str);
 assertNotNull(result);
 }

 @Test
 public void testCreateURLInvalidURL() {
 String str = "http://www.example.coma";
 URL result = TypeHandler.createURL(str);
 assertNull(result);
 }

 @Test
 public void testCreateFile() {
 String str = "/tmp/test.txt";
 File result = TypeHandler.createFile(str);
 assertNotNull(result);
 }

 @Test
 public void testCreateFileInvalidPath() {
 String str = "/tmp/test.txta";
 File result = TypeHandler.createFile(str);
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateFilesNull() {
 String str = null;
 File[] result = TypeHandler.createFiles(str);
 fail("Expected NullPointerException, but got null instead.");
 }

 @Test
 public void testCreateFilesInvalidPath() {
 String str = "/tmp/test.txt,/tmp/test2.txt,/tmp/test3.txta";
 File[] result = TypeHandler.createFiles(str);
 assertEquals(2, result.length);
 }

}