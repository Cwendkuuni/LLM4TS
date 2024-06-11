package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringString() {
 String str = "foo";
 Object obj = TypeHandler.createValue(str, String.class);
 assertEquals("foo", obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringNumber() {
 String str = "foo";
 Object obj = TypeHandler.createValue(str, Number.class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringClass() throws ClassNotFoundException {
 String classname = "java.lang.String";
 Object obj = TypeHandler.createValue("foo", Class.forName(classname));
 assertEquals("foo", obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringClassNotFound() throws ClassNotFoundException {
 String classname = "java.lang.NotFound";
 Object obj = TypeHandler.createValue("foo", Class.forName(classname));
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringObject() throws ParseException, ClassNotFoundException {
 String classname = "java.lang.String";
 Object obj = TypeHandler.createValue("foo", Class.forName(classname));
 Object expected = TypeHandler.createObject("foo");
 assertEquals(expected.getClass(), obj.getClass());
 assertEquals(expected, obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringURL() {
 String str = "http://example.com";
 Object obj = TypeHandler.createValue(str, URL.class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringURLSuccess() {
 String str = "http://example.com";
 Object obj = TypeHandler.createValue(str, URL.class);
 URL expected = new URL(str);
 assertEquals(expected.toString(), obj.toString());
 }

 @Test
 public void testCreateValueStringFile() {
 String str = "path/to/file.txt";
 Object obj = TypeHandler.createValue(str, File.class);
 File expected = new File(str);
 assertEquals(expected.getAbsolutePath(), obj.getAbsolutePath());
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringFileNotFound() {
 String str = "non-existent-file.txt";
 Object obj = TypeHandler.createValue(str, File.class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringFileArray() {
 String str = "path/to/file1.txt,path/to/file2.txt";
 Object obj = TypeHandler.createValue(str, File[].class);
 File[] expected = {new File("path/to/file1.txt"), new File("path/to/file2.txt")};
 assertArrayEq(expected, obj);
 }

 @Test
 public void testCreateValueStringArrayOfFiles() {
 String[] strs = {"path/to/file1.txt", "path/to/file2.txt"};
 Object obj = TypeHandler.createValue(String.join(",", strs), File[].class);
 File[] expected = {new File("path/to/file1.txt"), new File("path/to/file2.txt")};
 assertArrayEq(expected, obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringArrayOfFilesMissingFiles() {
 String[] strs = {"path/to/file1.txt", "non-existent-file.txt"};
 Object obj = TypeHandler.createValue(String.join(",", strs), File[].class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringDate() {
 String str = "2022-12-31";
 Object obj = TypeHandler.createValue(str, Date.class);
 Date expected = new Date(Long.parseLong("1671142399000"));
 assertEquals(expected, obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringDateInvalidFormat() {
 String str = "2022-12-31-31";
 Object obj = TypeHandler.createValue(str, Date.class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringNumberLong() {
 String str = "42";
 Object obj = TypeHandler.createValue(str, Long.class);
 Long expected = 42L;
 assertEquals(expected, obj);
 }

 @Test
 public void testCreateValueStringNumberDouble() {
 String str = "3.14";
 Object obj = TypeHandler.createValue(str, Double.class);
 Double expected = 3.14;
 assertEquals(expected, obj);
 }

 @Test(expected = ParseException.class)
 public void testCreateValueStringNumberInvalidFormat() {
 String str = "foo";
 Object obj = TypeHandler.createValue(str, Integer.class);
 assertNull(obj);
 }

 @Test
 public void testCreateValueStringNumberArrayLong() {
 String str = "1,2,3";
 Object obj = TypeHandler.createValue(str, long[].class);
 long[] expected = {1, 2, 3};
 assertArrayEq(expected, obj);
 }

 @Test
 public void testCreateValueStringNumberArrayDouble() {
 String str = "1.0,2.0,3.0";
 Object obj = TypeHandler.createValue(str, double[].class);
 double[] expected = {1.0, 2.0, 3.0};
 assertArrayEq(expected, obj);
 }
}