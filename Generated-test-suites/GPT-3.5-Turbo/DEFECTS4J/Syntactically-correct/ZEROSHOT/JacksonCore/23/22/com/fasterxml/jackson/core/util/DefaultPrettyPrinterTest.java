package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

	@Test
	public void testWriteRootValueSeparator() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeRootValueSeparator(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals("", result);
	}

	@Test
	public void testWriteStartObject() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeStartObject(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals("{", result);
	}

	@Test
	public void testBeforeObjectEntries() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.beforeObjectEntries(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals("", result);
	}

	@Test
	public void testWriteObjectFieldValueSeparator() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeObjectFieldValueSeparator(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals(": ", result);
	}

	@Test
	public void testWriteObjectEntrySeparator() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeObjectEntrySeparator(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals(", ", result);
	}

	@Test
	public void testWriteEndObject() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		int nrOfEntries = 0;
		try {
			prettyPrinter.writeEndObject(g, nrOfEntries);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals(" }", result);
	}

	@Test
	public void testWriteStartArray() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeStartArray(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals("[", result);
	}

	@Test
	public void testBeforeArrayValues() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.beforeArrayValues(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals("", result);
	}

	@Test
	public void testWriteArrayValueSeparator() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		try {
			prettyPrinter.writeArrayValueSeparator(g);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals(", ", result);
	}

	@Test
	public void testWriteEndArray() {
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		JsonGenerator g = new JsonGenerator();
		int nrOfValues = 0;
		try {
			prettyPrinter.writeEndArray(g, nrOfValues);
		} catch (IOException e) {
			Assert.fail();
		}
		String result = g.getOutput();
		Assert.assertEquals(" ]", result);
	}

}