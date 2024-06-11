package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

 @Test
 public void testCreateValueWithStringObj() {
 String str = "test";
 Object obj = TypeHandler.createValue(str, String.class);
 assertEquals(str, obj);
 }

 @Test
 public void testCreateValueWithPatternOptionStringValueClass() {
 String str = "test";
 Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
 assertEquals(str, obj);
 }

 @Test
 public void testCreateValueWithObject() {
 String className = "java.util.Date";
 Object obj = TypeHandler.createValue(className, Date.class);
 assertEquals(new Date(), obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateValueWithInvalidClass() {
 Object obj = TypeHandler.createValue("test", Integer.TYPE);
 assertNull(obj);
 }

 @Test
 public void testCreateNumber() {
 String numberStr = "123";
 Number number = TypeHandler.createNumber(numberStr);
 assertEquals(123, number.intValue());

 String decimalStr = "123.45";
 number = TypeHandler.createNumber(decimalStr);
 assertEquals(123.45, number.doubleValue(), 0.001);
 }

 @Test
 public void testCreateClass() {
 String className = "java.util.Date";
 Class clazz = TypeHandler.createClass(className);
 assertEquals(Date.class, clazz);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateClassWithInvalidClassName() {
 Class clazz = TypeHandler.createClass("java.util.InvalidClass");
 assertNull(clazz);
 }

 @Test
 public void testCreateDate() {
 String dateStr = "2022-03-25";
 Date date = TypeHandler.createDate(dateStr);
 assertEquals(new Date(2022, 3, 25), date);
 }

 @Test
 public void testCreateURL() {
 String urlString = "https://www.example.com";
 URL url = TypeHandler.createURL(urlString);
 assertEquals(new URL("https://www.example.com"), url);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateURLWithInvalidURL() {
 URL url = TypeHandler.createURL("invalidURL");
 assertNull(url);
 }

 @Test
 public void testCreateFile() {
 String filePath = "test.txt";
 File file = TypeHandler.createFile(filePath);
 assertEquals(new File(filePath), file);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFileWithInvalidFilePath() {
 File file = TypeHandler.createFile("invalidFilePath");
 assertNull(file);
 }

 @Test
 public void testCreateFiles() {
 String filesPath = "test1.txt, test2.txt";
 File[] files = TypeHandler.createFiles(filesPath);
 assertEquals(2, files.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFilesWithInvalidFilePaths() {
 File[] files = TypeHandler.createFiles("test1.txt, invalidFilePath");
 assertNull(files);
 }

}