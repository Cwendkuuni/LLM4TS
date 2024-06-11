package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_String_Class_StringType() {
 String str = "test";
 Class<?> stringType = String.class;
 Object result = TypeHandler.createValue(str, stringType);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValue_String_Class_ObjectType() {
 String str = "test";
 Class<?> objectType = Object.class;
 assertNull(TypeHandler.createValue(str, objectType));
 }

 @Test(expected = ParseException.class)
 public void testCreateValue_String_Class_InvalidType() {
 String str = "test";
 Class<?> invalidType = int.class;
 TypeHandler.createValue(str, invalidType);
 }

 @Test
 public void testCreateObject_Classname() throws ParseException {
 String classname = "java.util.Date";
 Class<?> dateClass = TypeHandler.createClass(classname);
 assertInstanceOf(Date.class, dateClass);
 }

 @Test(expected = ParseException.class)
 public void testCreateObject_InvalidClassname() throws ParseException {
 String classname = "java.InvalidClass";
 TypeHandler.createObject(classname);
 }

 @Test
 public void testCreateNumber_LongString() throws ParseException {
 String str = "12345";
 Number result = TypeHandler.createNumber(str);
 assertEquals(12345, result.longValue());
 }

 @Test(expected = ParseException.class)
 public void testCreateNumber_InvalidString() throws ParseException {
 String str = "abc";
 TypeHandler.createNumber(str);
 }

 @Test
 public void testCreateNumber_DoubleString() throws ParseException {
 String str = "123.45";
 Number result = TypeHandler.createNumber(str);
 assertEquals(123.45, result.doubleValue(), 0.0);
 }

 @Test(expected = ParseException.class)
 public void testCreateDate_String() throws ParseException {
 String str = "2022-01-01";
 TypeHandler.createDate(str);
 }

 @Test
 public void testCreateURL_String() throws ParseException {
 String urlString = "http://www.example.com";
 URL result = TypeHandler.createURL(urlString);
 assertNotNull(result);
 assertEquals(urlString, result.toString());
 }

 @Test(expected = ParseException.class)
 public void testCreateURL_InvalidString() throws ParseException {
 String urlString = "http://www.example.com:80";
 TypeHandler.createURL(urlString);
 }

 @Test
 public void testCreateFile_String() {
 String filePath = "/path/to/file.txt";
 File result = TypeHandler.createFile(filePath);
 assertNotNull(result);
 assertEquals(filePath, result.getAbsolutePath());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFileInputStream_NonExistentFile() throws IOException, FileNotFoundException, ParseException {
 String filePath = "/path/to/non-existent-file.txt";
 File file = TypeHandler.createFile(filePath);
 FileInputStream result = TypeHandler.createFileInputStream(filePath);
 }

 @Test(expected = ParseException.class)
 public void testCreateFile_InvalidPath() throws ParseException {
 String filePath = "//path/to/file.txt";
 TypeHandler.createFile(filePath);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFileInputStream_InvalidPath() throws FileNotFoundException, ParseException {
 String filePath = "/path/to/non-existent-file.txt";
 File file = TypeHandler.createFile(filePath);
 FileInputStream result = TypeHandler.createFileInputStream(filePath);
 }

}