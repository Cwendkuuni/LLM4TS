package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "foo";
 Object obj = new Object() {
 };
 Object value = TypeHandler.createValue(str, obj.getClass());
 assertEquals(str, value);
 }

 @Test
 public void testCreateValue_StringClass_StringValue() {
 String str = "foo";
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertEquals(str, value);
 }

 @Test
 public void testCreateValue_StringClass_ObjectValue() {
 String str = "java.lang.String";
 Object obj = new Object() {
 };
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 assertEquals("java.lang.String@" + Integer.toHexString(System.identityHashCode(value)), value.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateValue_NullString() {
 TypeHandler.createValue(null, String.class);
 }

 @Test
 public void testCreateValue_StringClass_NumberValue() {
 String str = "123";
 Class clazz = Integer.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertEquals(123, value);
 }

 @Test
 public void testCreateValue_StringClass_DateValue() {
 String str = "2022-12-31";
 Class clazz = Date.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 }

 @Test
 public void testCreateValue_StringClass_ClassValue() {
 String str = "java.util.Date";
 Class clazz = Date.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 assertEquals("java.util.Date@" + Integer.toHexString(System.identityHashCode(value)), value.toString());
 }

 @Test
 public void testCreateValue_StringClass_FileValue() {
 String str = "/path/to/file.txt";
 Class clazz = File.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 assertEquals("/path/to/file.txt", value.toString());
 }

 @Test
 public void testCreateValue_StringClass_URLValue() {
 String str = "http://example.com";
 Class clazz = URL.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 assertEquals(new URL("http://example.com"), value);
 }

 @Test
 public void testCreateValue_StringClass_FileArrayValue() {
 String str = "/path/to/file1.txt, /path/to/file2.txt";
 Class clazz = File[].class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 File[] files = (File[]) value;
 assertEquals(2, files.length);
 assertEquals("/path/to/file1.txt", files[0].toString());
 assertEquals("/path/to/file2.txt", files[1].toString());
 }

 @Test
 public void testCreateValue_StringClass_URLArrayValue() {
 String str = "http://example.com, http://example.org";
 Class clazz = URL[].class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull(value);
 URL[] urls = (URL[]) value;
 assertEquals(2, urls.length);
 assertEquals(new URL("http://example.com"), urls[0]);
 assertEquals(new URL("http://example.org"), urls[1]);
 }

 @Test
 public void testCreateObject() {
 String className = "java.util.Date";
 Object obj = TypeHandler.createObject(className);
 assertNotNull(obj);
 assertInstanceof(Date.class, obj);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_InvalidClassName() {
 String className = "java.util.InvalidClassName";
 TypeHandler.createObject(className);
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number number = TypeHandler.createNumber(str);
 assertEquals(123, number.intValue());
 }

 @Test
 public void testCreateNumber_Decimal() {
 String str = "123.45";
 Number number = TypeHandler.createNumber(str);
 assertEquals(123.45d, number.doubleValue());
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumber_InvalidString() {
 String str = "foo";
 TypeHandler.createNumber(str);
 }

 @Test
 public void testCreateDate() {
 String str = "2022-12-31";
 Date date = TypeHandler.createDate(str);
 assertNotNull(date);
 }

 @Test
 public void testCreateDate_InvalidString() {
 String str = "2022-12-32";
 Date date = TypeHandler.createDate(str);
 assertNull(date);
 }

 @Test
 public void testCreateURL() {
 String str = "http://example.com";
 URL url = TypeHandler.createURL(str);
 assertNotNull(url);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidString() {
 String str = "http://example.com%";
 TypeHandler.createURL(str);
 }

 @Test
 public void testCreateFile() {
 String str = "/path/to/file.txt";
 File file = TypeHandler.createFile(str);
 assertNotNull(file);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_NonExistentFile() {
 String str = "/path/to/non-existent-file.txt";
 TypeHandler.createFile(str);
 }
}