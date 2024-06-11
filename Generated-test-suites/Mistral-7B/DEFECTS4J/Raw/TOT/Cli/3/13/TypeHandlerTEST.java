package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Test the TypeHandler class.
 */
public class TypeHandlerTest
{
	@Test
	public void testCreateValueStringString()
	{
		// Test with String type
		String strValue = "Hello World";
		Object obj = TypeHandler.createValue(strValue, String.class);
		assertEquals("Hello World", obj);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateValueStringObjectInvalidType()
	{
		// Test with invalid type
		String strValue = "Hello World";
		Object obj = TypeHandler.createValue(strValue, Integer.class);
		fail("Expected IllegalArgumentException");
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderStringValue()
	{
		// Test with StringValue type
		String strValue = "Hello World";
		Class clazz = PatternOptionBuilder.STRING_VALUE;
		Object obj = TypeHandler.createValue(strValue, clazz);
		assertEquals("Hello World", obj);
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderObjectValue()
	{
		// Test with ObjectValue type
		String strValue = "com.example.TestClass";
		Class clazz = PatternOptionBuilder.OBJECT_VALUE;
		Object obj = TypeHandler.createValue(strValue, clazz);
		assertNotNull(obj);
		assertTrue(obj.getClass().isAssignableFrom(Class.forName(strValue)));
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderNumberValue()
	{
		// Test with NumberValue type
		String strValue = "42";
		Class clazz = PatternOptionBuilder.NUMBER_VALUE;
		Number obj = (Number)TypeHandler.createValue(strValue, clazz);
		assertEquals(42, obj.intValue());
	}

	@Test(expected = NumberFormatException.class)
	public void testCreateValueStringClassPatternOptionBuilderNumberValueInvalidFormat()
	{
		// Test with invalid format for NumberValue type
		String strValue = "abc";
		Class clazz = PatternOptionBuilder.NUMBER_VALUE;
		TypeHandler.createValue(strValue, clazz);
		fail("Expected NumberFormatException");
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderDateValue()
	{
		// Test with DateValue type
		String strValue = "2022-03-01";
		Class clazz = PatternOptionBuilder.DATE_VALUE;
		Date obj = (Date)TypeHandler.createValue(strValue, clazz);
		assertNotNull(obj);
		assertEquals(new Date(2022, 2, 1), obj);
	}

	@Test(expected = ParseException.class)
	public void testCreateValueStringClassPatternOptionBuilderDateValueInvalidFormat()
	{
		// Test with invalid format for DateValue type
		String strValue = "2022-3-1";
		Class clazz = PatternOptionBuilder.DATE_VALUE;
		try
		{
			TypeHandler.createValue(strValue, clazz);
			fail("Expected ParseException");
		}
		catch (ParseException pe)
		{
			// expected
		}
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderClassValue()
	{
		// Test with ClassValue type
		String strValue = "java.lang.String";
		Class clazz = PatternOptionBuilder.CLASS_VALUE;
		Class obj = (Class)TypeHandler.createValue(strValue, clazz);
		assertEquals(String.class, obj);
	}

	@Test(expected = ClassNotFoundException.class)
	public void testCreateValueStringClassPatternOptionBuilderClassValueInvalidClass()
	{
		// Test with invalid class for ClassValue type
		String strValue = "com.example.InvalidClass";
		Class clazz = PatternOptionBuilder.CLASS_VALUE;
		TypeHandler.createValue(strValue, clazz);
		fail("Expected ClassNotFoundException");
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderFileValue()
	{
		// Test with FileValue type
		String strValue = "path/to/file.txt";
		Class clazz = PatternOptionBuilder.FILE_VALUE;
		File obj = (File)TypeHandler.createValue(strValue, clazz);
		assertEquals(new File(strValue), obj);
	}

	@Test(expected = FileNotFoundException.class)
	public void testCreateValueStringClassPatternOptionBuilderFileValueNotFound()
	{
		// Test with non-existent file for FileValue type
		String strValue = "path/to/nonexistentfile.txt";
		Class clazz = PatternOptionBuilder.FILE_VALUE;
		TypeHandler.createValue(strValue, clazz);
		fail("Expected FileNotFoundException");
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderExistingFileValue()
	{
		// Test with ExistingFileValue type
		String strValue = "path/to/existingfile.txt";
		Class clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
		File obj = (File)TypeHandler.createValue(strValue, clazz);
		assertTrue(obj.exists());
		assertEquals(new File(strValue), obj);
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderFilesValue()
	{
		// Test with FilesValue type
		String strValue = "path/to/file1.txt,path/to/file2.txt";
		Class clazz = PatternOptionBuilder.FILES_VALUE;
		File[] obj = (File[])TypeHandler.createValue(strValue, clazz);
		assertEquals(2, obj.length);
		assertNotNull(obj[0]);
		assertNotNull(obj[1]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateValueStringClassPatternOptionBuilderURLValueInvalidProtocol()
	{
		// Test with invalid protocol for URLValue type
		String strValue = "file:path/to/file.txt";
		Class clazz = PatternOptionBuilder.URL_VALUE;
		TypeHandler.createValue(strValue, clazz);
		fail("Expected IllegalArgumentException");
	}

	@Test
	public void testCreateValueStringClassPatternOptionBuilderURLValue()
	{
		// Test with URLValue type
		String strValue = "http://example.com";
		Class clazz = PatternOptionBuilder.URL_VALUE;
		URL obj = (URL)TypeHandler.createValue(strValue, clazz);
		assertEquals(new URL("http://example.com"), obj);
	}
}