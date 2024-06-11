import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {

	@Test
	public void testConstructor1() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		assertEquals("Hello World!", emf.format(new Object[] { "World" }));
	}

	@Test
	public void testConstructor2() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0} {1}!");
		assertEquals("Hello Alice Bob!", emf.format(new Object[] { "Alice", "Bob" }));
	}

	@Test
	public void testConstructor3() {
		Map<String, CustomFormatFactory> registry = new HashMap<>();
		registry.put("uppercase", new CustomFormatFactory());
		registry.put("lowercase", new CustomFormatFactory());
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0, uppercase}, {1, lowercase}!");
		emf.setRegistry(registry);
		assertEquals("Hello WORLD, bob!", emf.format(new Object[] { "world", "BOB" }));
	}

	@Test
	public void testConstructor4() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,date,yyyy-MM-dd}");
		assertEquals("Hello 2020-01-01", emf.format(new Object[] { "2020-01-01" }));
	}

	@Test
	public void testConstructorWithLocale() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,date,short}", Locale.FRANCE);
		assertEquals("Hello 01/01/20", emf.format(new Object[] { "2020-01-01" }));
	}

	@Test
	public void testConstructorWithRegistryAndLocale() {
		Map<String, CustomFormatFactory> registry = new HashMap<>();
		registry.put("myCustomFormat", new CustomFormatFactory());
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,myCustomFormat}", Locale.US, registry);
		assertEquals("Hello CustomFormat", emf.format(new Object[] { "test" }));
	}

	@Test
	public void testToPattern() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		assertEquals("Hello {0}!", emf.toPattern());
	}

	@Test
	public void testApplyPattern() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		emf.applyPattern("Goodbye {0}!");
		assertEquals("Goodbye World!", emf.format(new Object[] { "World" }));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetFormat() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		emf.setFormat(0, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetFormatByArgumentIndex() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		emf.setFormatByArgumentIndex(0, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetFormats() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		emf.setFormats(new java.text.Format[] { null });
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetFormatsByArgumentIndex() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}!");
		emf.setFormatsByArgumentIndex(new java.text.Format[] { null });
	}

	@Test
	public void testEquals() {
		ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}!");
		ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}!");
		assertTrue(emf1.equals(emf2));
	}

	@Test
	public void testHashCode() {
		ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}!");
		ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}!");
		assertEquals(emf1.hashCode(), emf2.hashCode());
	}

	@Test
	public void testGetQuotedString() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello '{0}'!");
		assertEquals("'World'", emf.format(new Object[] { "World" }));
	}

	@Test
	public void testGetQuotedStringWithEscaping() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello ''{0}''!");
		assertEquals("'World'!", emf.format(new Object[] { "World" }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFormatDescriptionUnmatchedBraces() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,uppercase");
		emf.toPattern();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFormatDescriptionUnterminatedElement() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0t");
		emf.toPattern();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFormatDescriptionInvalidArgumentIndex() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {text}");
		emf.toPattern();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFormatDescriptionInvalidFormatElement() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,nonexistent}");
		emf.toPattern();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyPatternUnterminatedQuotedString() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello '{0}");
		emf.toPattern();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyPatternInvalidFormatElement() {
		ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0,invalid");
		emf.toPattern();
	}

	class CustomFormatFactory implements java.text.FormatFactory {
		@Override
		public java.text.Format getFormat(String name, String arguments, Locale locale) {
			return new CustomFormat();
		}
	}

	class CustomFormat extends java.text.Format {
		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {
			toAppendTo.append(obj.toString().toUpperCase());
			return toAppendTo;
		}

		@Override
		public Object parseObject(String source, java.text.ParsePosition pos) {
			return null;
		}
	}
}