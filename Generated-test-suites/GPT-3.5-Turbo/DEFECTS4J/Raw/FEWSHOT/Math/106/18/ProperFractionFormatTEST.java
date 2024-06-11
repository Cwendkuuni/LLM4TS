package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
	
	private ProperFractionFormat fractionFormat;
	private Fraction fraction;
	private StringBuffer stringBuffer;
	private FieldPosition fieldPosition;
	private NumberFormat wholeFormat;
	private NumberFormat numeratorFormat;
	private NumberFormat denominatorFormat;
	
	@Before
	public void setUp() {
		fractionFormat = new ProperFractionFormat();
		fraction = mock(Fraction.class);
		stringBuffer = mock(StringBuffer.class);
		fieldPosition = mock(FieldPosition.class);
		wholeFormat = mock(NumberFormat.class);
		numeratorFormat = mock(NumberFormat.class);
		denominatorFormat = mock(NumberFormat.class);
	}
	
	@Test
	public void testFormat() {
		when(fraction.getNumerator()).thenReturn(2);
		when(fraction.getDenominator()).thenReturn(3);
		fractionFormat.setWholeFormat(wholeFormat);
		fractionFormat.setNumeratorFormat(numeratorFormat);
		fractionFormat.setDenominatorFormat(denominatorFormat);
		
		String expected = "2 / 3";
		
		assertEquals(expected, fractionFormat.format(fraction, stringBuffer, fieldPosition).toString());
		verify(wholeFormat).format(0, stringBuffer, fieldPosition);
		verify(numeratorFormat).format(2, stringBuffer, fieldPosition);
		verify(denominatorFormat).format(3, stringBuffer, fieldPosition);
	}
	
	@Test
	public void testParseWhenInvalidFractionFormat() {
		String source = "2 3 5";
		ParsePosition pos = new ParsePosition(0);
		
		assertNull(fractionFormat.parse(source, pos));
		assertEquals(0, pos.getErrorIndex());
	}
	
	@Test
	public void testParseWhenInvalidWholeFormat() {
		String source = "a 2 / 3";
		doReturn(null).when(wholeFormat).parse(anyString(), any(ParsePosition.class));
		when(numeratorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(2);
		when(denominatorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(3);
		fractionFormat.setWholeFormat(wholeFormat);
		fractionFormat.setNumeratorFormat(numeratorFormat);
		fractionFormat.setDenominatorFormat(denominatorFormat);
		ParsePosition pos = new ParsePosition(0);
		
		assertNull(fractionFormat.parse(source, pos));
		verify(wholeFormat).parse("a ", pos);
		verify(numeratorFormat).parse("2 ", pos);
		verify(denominatorFormat).parse("3", pos);
		assertEquals(0, pos.getErrorIndex());
	}
	
	@Test
	public void testParseWhenInvalidNumeratorFormat() {
		String source = "2 a / 3";
		when(wholeFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(2);
		doReturn(null).when(numeratorFormat).parse(anyString(), any(ParsePosition.class));
		when(denominatorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(3);
		fractionFormat.setWholeFormat(wholeFormat);
		fractionFormat.setNumeratorFormat(numeratorFormat);
		fractionFormat.setDenominatorFormat(denominatorFormat);
		ParsePosition pos = new ParsePosition(0);
		
		assertNull(fractionFormat.parse(source, pos));
		verify(wholeFormat).parse("2 ", pos);
		verify(numeratorFormat).parse("a ", pos);
		verify(denominatorFormat).parse("3", pos);
		assertEquals(0, pos.getErrorIndex());
	}
	
	@Test
	public void testParseWhenInvalidDenominatorFormat() {
		String source = "2 / a";
		when(wholeFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(2);
		when(numeratorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(3);
		doReturn(null).when(denominatorFormat).parse(anyString(), any(ParsePosition.class));
		fractionFormat.setWholeFormat(wholeFormat);
		fractionFormat.setNumeratorFormat(numeratorFormat);
		fractionFormat.setDenominatorFormat(denominatorFormat);
		ParsePosition pos = new ParsePosition(0);
		
		assertNull(fractionFormat.parse(source, pos));
		verify(wholeFormat).parse("2 ", pos);
		verify(numeratorFormat).parse("/ ", pos);
		verify(denominatorFormat).parse("a", pos);
		assertEquals(0, pos.getErrorIndex());
	}
	
	@Test
	public void testParseWhenInvalidCharacter() {
		String source = "2 a / 3";
		when(wholeFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(2);
		doReturn(null).when(numeratorFormat).parse(anyString(), any(ParsePosition.class));
		when(denominatorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(3);
		ParsePosition pos = new ParsePosition(0);
		
		assertNull(fractionFormat.parse(source, pos));
		verify(wholeFormat).parse("2 ", pos);
		verify(numeratorFormat).parse("a ", pos);
		verify(denominatorFormat, never()).parse(anyString(), any(ParsePosition.class));
		assertEquals(4, pos.getErrorIndex());
	}
	
	@Test
	public void testParse() {
		String source = "2 / 3";
		when(wholeFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(2);
		when(numeratorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(3);
		when(denominatorFormat.parse(anyString(), any(ParsePosition.class))).thenReturn(1);
		fractionFormat.setWholeFormat(wholeFormat);
		fractionFormat.setNumeratorFormat(numeratorFormat);
		fractionFormat.setDenominatorFormat(denominatorFormat);
		ParsePosition pos = new ParsePosition(0);
		
		Fraction expected = new Fraction(7, 2);
		
		assertEquals(expected, fractionFormat.parse(source, pos));
		verify(wholeFormat).parse("2 ", pos);
		verify(numeratorFormat).parse("3", pos);
		verify(denominatorFormat, never()).parse(anyString(), any(ParsePosition.class));
		assertEquals(0, pos.getErrorIndex());
	}
}