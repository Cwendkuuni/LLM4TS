package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.lang.Class;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringObject() {
 String str = "test";
 Object obj = new Object() {
 };
 Object result = TypeHandler.createValue(str, obj.getClass());
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClass() {
 String str = "test";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClassInvalid() {
 String str = "test";
 Class clazz = Integer.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderStringValue() {
 String str = "test";
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderObjectValue() {
 String str = "test";
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderNumberValue() {
 String str = "123";
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
 assertEquals(123, result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderDateValue() {
 String str = "2023-03-23";
 Date expected = new Date(2023, 2, 23);
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderDateValueInvalid() {
 String str = "invalid_date";
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderClassValue() {
 String str = "java.lang.String";
 Class<?> clazz = String.class;
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
 assertEquals(clazz, result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderClassValueInvalid() {
 String str = "java.lang.Integer";
 Class<?> clazz = String.class;
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderFileValue() {
 String str = "/path/to/file";
 File expected = new File(str);
 File result = (File) TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
 assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderFileValueNull() {
 String str = null;
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
 assertNull(result);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderURLValue() {
 String str = "http://example.com";
 URL expected = new URL("http://example.com");
 URL result = (URL) TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderURLValueNull() {
 String str = null;
 Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
 assertNull(result);
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number number = TypeHandler.createNumber(str);
 assertEquals(123, number.intValue());
 }

 @Test
 public void testCreateNumberInvalid() {
 String str = "invalid";
 Number number = TypeHandler.createNumber(str);
 assertNull(number);
 }

 @Test
 public void testCreateClass() {
 String str = "java.lang.String";
 Class<?> clazz = String.class;
 Class<?> result = (Class<?>) TypeHandler.createClass(str);
 assertEquals(clazz, result);
 }

 @Test
 public void testCreateClassInvalid() {
 String str = "java.lang.Integer";
 Class<?> clazz = String.class;
 Class<?> result = (Class<?>) TypeHandler.createClass(str);
 assertNull(result);
 }

 @Test
 public void testCreateDate() {
 String str = "2023-03-23";
 Date expected = new Date(2023, 2, 23);
 Date result = TypeHandler.createDate(str);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateDateInvalid() {
 String str = "invalid_date";
 Date result = TypeHandler.createDate(str);
 assertNull(result);
 }

 @Test
 public void testCreateURL() {
 String str = "http://example.com";
 URL expected = new URL("http://example.com");
 URL result = TypeHandler.createURL(str);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testCreateURLInvalid() {
 String str = "invalid_url";
 URL result = TypeHandler.createURL(str);
 assertNull(result);
 }

 @Test
 public void testCreateFile() {
 String str = "/path/to/file";
 File expected = new File(str);
 File result = TypeHandler.createFile(str);
 assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testCreateFileNull() {
 String str = null;
 File result = TypeHandler.createFile(str);
 assertNull(result);
 }

 @Test
 public void testCreateFiles() {
 String str = "/path/to/file1,/path/to/file2";
 File[] expectedFiles = new File[] { new File("/path/to/file1"), new File("/path/to/file2") };
 File[] resultFiles = TypeHandler.createFiles(str);
 assertArrayEquals(expectedFiles, resultFiles);
 }

 @Test
 public void testCreateFilesNull() {
 String str = null;
 File[] resultFiles = TypeHandler.createFiles(str);
 assertNull(resultFiles);
 }
}