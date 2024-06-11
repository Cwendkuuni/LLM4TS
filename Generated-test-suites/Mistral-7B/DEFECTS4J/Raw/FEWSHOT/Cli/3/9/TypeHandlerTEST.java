package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringObject() {
 String str = "testString";
 Object obj = new String("testString");
 Object value = TypeHandler.createValue(str, obj.getClass());
 assertEquals(obj, value);
 }

 @Test
 public void testCreateValueStringClass() {
 String str = "testString";
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertEquals(str, value);
 }

 @Test
 public void testCreateValueEmptyStringPatternOptionString() {
 String str = "";
 Class clazz = PatternOptionBuilder.STRING_VALUE;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueEmptyStringPatternOptionObject() {
 String str = "";
 Object obj = new String("testString");
 Class clazz = PatternOptionBuilder.STRING_VALUE;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueEmptyStringClass() {
 String str = "";
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueNullStringPatternOptionString() {
 String str = null;
 Class clazz = PatternOptionBuilder.STRING_VALUE;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueNullStringPatternOptionObject() {
 String str = null;
 Object obj = new String("testString");
 Class clazz = PatternOptionBuilder.STRING_VALUE;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueNullStringClass() {
 String str = null;
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertNull(value);
 }

 @Test
 public void testCreateValueObject() {
 Object obj = new String("testString");
 Object value = TypeHandler.createValue("testString", obj.getClass());
 assertEquals(obj, value);
 }

 @Test
 public void testCreateValueClass() {
 String className = "java.lang.String";
 Class clazz = null;
 try {
 clazz = Class.forName(className);
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 }
 Object value = TypeHandler.createValue("testString", clazz);
 assertEquals("testString", value.toString());
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number number = TypeHandler.createNumber(str);
 Long longNumber = Long.parseLong(str);
 assertEquals(longNumber, number);
 }

 @Test
 public void testCreateNumberFail() {
 String str = "testString";
 Number number = TypeHandler.createNumber(str);
 assertNull(number);
 }

 @Test
 public void testCreateDate() {
 String dateStr = "2023-03-21";
 Date date = new Date(0);
 try {
 date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
 } catch (ParseException e) {
 e.printStackTrace();
 }
 Object dateValue = TypeHandler.createValue(dateStr, Date.class);
 assertEquals(date, dateValue);
 }

 @Test
 public void testCreateDateFail() {
 String dateStr = "testString";
 Object dateValue = TypeHandler.createValue(dateStr, Date.class);
 assertNull(dateValue);
 }

 @Test
 public void testCreateURL() {
 String urlStr = "http://www.example.com";
 URL url = new URL(urlStr);
 Object urlValue = TypeHandler.createValue(urlStr, URL.class);
 assertEquals(url, urlValue);
 }

 @Test
 public void testCreateURLFail() {
 String urlStr = "testString";
 Object urlValue = TypeHandler.createValue(urlStr, URL.class);
 assertNull(urlValue);
 }

 @Test
 public void testCreateFile() {
 String filePath = "test.txt";
 File file = new File(filePath);
 Object fileValue = TypeHandler.createValue(filePath, File.class);
 assertEquals(file, fileValue);
 }

 @Test
 public void testCreateFileFail() {
 String filePath = "test.txt";
 Object fileValue = TypeHandler.createValue("test.txt", File[].class);
 assertNull(fileValue);
 }

 @Test
 public void testCreateFiles() {
 String[] filePaths = new String[] {"test1.txt", "test2.txt"};
 File[] files = new File[filePaths.length];
 for (int i = 0; i < files.length; i++) {
 files[i] = new File(filePaths[i]);
 }
 Object filesValue = TypeHandler.createValue(String.join(" ", filePaths), File[].class);
 assertArrayEquals(files, filesValue);
 }

 @Test
 public void testCreateFilesFail() {
 String filePath = "test.txt";
 Object filesValue = TypeHandler.createValue(filePath, File[].class);
 assertNull(filesValue);
 }

 @Test
 public void testCreateObjectFail() {
 String className = "java.lang.String";
 Object obj = null;
 try {
 obj = Class.forName(className).newInstance();
 } catch (Exception e) {
 e.printStackTrace();
 }
 String str = "testString";
 Object value = TypeHandler.createValue(str, obj.getClass());
 assertNull(value);
 }

}