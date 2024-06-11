package org.apache.commons.math.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private FractionFormat properFractionFormat;
 private FractionFormat improperFractionFormat;

 @Test
 public void testProperFractionFormat() {
 // Test constructors
 ProperFractionFormat pff1 = new ProperFractionFormat();
 ProperFractionFormat pff2 = new ProperFractionFormat(getDefaultNumberFormat());
 ProperFractionFormat pff3 = new ProperFractionFormat(getDefaultNumberFormat(),
 getDefaultNumberFormat(), getDefaultNumberFormat());

 // Test proper formatting
 Fraction fraction1 = new Fraction(1, 2);
 String expected1 = "1 1/2";
 String result1 = pff1.format(fraction1, new StringBuffer(), null).toString();
 assertEquals(expected1, result1);

 Fraction fraction2 = new Fraction(-3, 2);
 String expected2 = "-3 1/2";
 String result2 = pff1.format(fraction2, new StringBuffer(), null).toString();
 assertEquals(expected2, result2);

 Fraction fraction3 = new Fraction(0, 3);
 String expected3 = "0 1/3";
 String result3 = pff1.format(fraction3, new StringBuffer(), null).toString();
 assertEquals(expected3, result3);

 // Test custom formatting
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat pff4 = new ProperFractionFormat(customWholeFormat,
 customNumeratorFormat, customDenominatorFormat);
 String expected4 = "2 3/4";
 String result4 = pff4.format(new Fraction(2, 4), new StringBuffer(), null).toString();
 assertEquals(expected4, result4);

 // Test parse
 String input1 = "1 1/2";
 Fraction fractionParsed1 = pff1.parse(input1, new ParsePosition(0));
 assertEquals(fraction1, fractionParsed1);

 String input2 = "-3 1/2";
 Fraction fractionParsed2 = pff1.parse(input2, new ParsePosition(0));
 assertEquals(fraction2, fractionParsed2);

 String input3 = "0 1/3";
 Fraction fractionParsed3 = pff1.parse(input3, new ParsePosition(0));
 assertEquals(fraction3, fractionParsed3);

 String input4 = "2 3/4";
 Fraction fractionParsed4 = pff4.parse(input4, new ParsePosition(0));
 assertEquals(new Fraction(2, 4), fractionParsed4);

 // Test improper formatting and parsing
 FractionFormat improperFractionFormat = new ProperFractionFormat();
 improperFractionFormat.setDenominatorFormat(getDefaultNumberFormat());
 Fraction fraction5 = new Fraction(5, 2);
 String expected5 = "5";
 String result5 = improperFractionFormat.format(fraction5, new StringBuffer(), null).toString();
 assertEquals(expected5, result5);
 Fraction fractionParsed5 = improperFractionFormat.parse(expected5, new ParsePosition(0));
 assertEquals(fraction5, fractionParsed5);
 }

 private static NumberFormat getDefaultNumberFormat() {
 return NumberFormat.getNumberInstance();
 }
}