import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.ParsePosition;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProperFractionFormatTest {

 private NumberFormat defaultWholeFormat;
 private NumberFormat customWholeFormat;
 private NumberFormat defaultNumeratorFormat;
 private NumberFormat defaultDenominatorFormat;
 private NumberFormat customNumeratorFormat;
 private NumberFormat customDenominatorFormat;
 private FractionFormat defaultProperFractionFormat;
 private FractionFormat customProperFractionFormat;

 @BeforeAll
 public void setup() {
 defaultWholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 customWholeFormat = new DecimalFormat("#,##0");
 defaultNumeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 defaultDenominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 customNumeratorFormat = new DecimalFormat("#,##0");
 customDenominatorFormat = new DecimalFormat("#,##0");
 defaultProperFractionFormat = new ProperFractionFormat();
 customProperFractionFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 }

 @Test
 public void testProperFormatDefault() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "2 1/3";
 String actual = defaultProperFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testProperFormatCustom() {
 Fraction fraction = new Fraction(11, 4);
 String expected = "11,000 2,750/4,000";
 String actual = customProperFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testParseDefault() {
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction expectedDefault = new Fraction(2, 3);
 Fraction actualDefault = defaultProperFractionFormat.parse("2 1/3", parsePosition);
 assertEquals(expectedDefault, actualDefault);
 }

 @Test
 public void testParseCustom() {
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction expectedCustom = new Fraction(11, 4);
 Fraction actualCustom = customProperFractionFormat.parse("11,000 2,750/4,000", parsePosition);
 assertEquals(expectedCustom, actualCustom);
 }

 @Test
 public void testParseInvalidFormat1() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("-3 -1/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat2() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("-3 -1/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat3() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/-2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat4() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/-2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat5() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 / 1", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat6() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 / 1", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat7() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat8() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat9() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 /3", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat10() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 /3", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat11() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/3/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat12() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/3/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat13() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1 / 3", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat14() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1 / 3", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat15() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/3 / 2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat16() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/3 / 2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat17() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/3 / 2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat18() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/3 / 2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat19() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat20() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat21() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("2 1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat22() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("2 1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat23() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat24() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat25() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(defaultProperFractionFormat.parse("1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testParseInvalidFormat26() {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull(customProperFractionFormat.parse("1/3/2/2", parsePosition));
 assertEquals(-1, parsePosition.getIndex());
 parsePosition.setIndex(0);
 }

 @Test
 public void testValidParseDefault() {
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction expectedDefault = new Fraction(4, 3);
 Fraction actualDefault = defaultProperFractionFormat.parse("-3 1/2", parsePosition);
 assertEquals(expectedDefault.getNumerator(), actualDefault.getNumerator());
 assertEquals(expectedDefault.getDenominator(), actualDefault.getDenominator());
 assertEquals(-1, parsePosition.getIndex());
 }

 @Test
 public void testValidParseCustom() {
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction expectedCustom = new Fraction(11, 4);
 Fraction actualCustom = customProperFractionFormat.parse("11,000 2,750/4,000", parsePosition);
 assertEquals(expectedCustom.getNumerator(), actualCustom.getNumerator());
 assertEquals(expectedCustom.getDenominator(), actualCustom.getDenominator());
 assertEquals(-1, parsePosition.getIndex());
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat wholeFormat = new DecimalFormat("##0.###");
 customProperFractionFormat.setWholeFormat(wholeFormat);
 Fraction fraction = new Fraction(17, 9);
 String expected = "17,000.000 1,000.000/9,000.000";
 String actual = customProperFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testParseWithBigDecimal() {
 ParsePosition parsePosition = new ParsePosition(0);
 BigDecimal whole = new BigDecimal("12345678901234567890");
 BigDecimal numerator = new BigDecimal("12345678901234567890");
 BigDecimal denominator = new BigDecimal("1234567890");
 Fraction expected = new Fraction(whole.toBigIntegerExact(), denominator.toBigIntegerExact());
 Fraction actual = (Fraction) customProperFractionFormat.parse("123,456,789,012,345,678,901,234,567,890 1,234,567,890/1,234,567", parsePosition);
 assertEquals(expected.getNumerator().toBigIntegerExact(), actual.getNumerator().toBigIntegerExact());
 assertEquals(expected.getDenominator().toBigIntegerExact(), actual.getDenominator().toBigIntegerExact());
 assertEquals(-1, parsePosition.getIndex());
 }

 @Test
 public void testParseWithBigInteger() {
 ParsePosition parsePosition = new ParsePosition(0);
 BigInteger whole = new BigInteger("12345678901234567890");
 BigInteger numerator = new BigInteger("12345678901234567890");
 BigInteger denominator = new BigInteger("1234567890");
 Fraction expected = new Fraction(whole, denominator);
 Fraction actual = (Fraction) customProperFractionFormat.parse("123,456,789,012,345,678,901,234,567,890 1,234,567,890/1,234,567", parsePosition);
 assertEquals(expected.getNumerator(), actual.getNumerator());
 assertEquals(expected.getDenominator(), actual.getDenominator());
 assertEquals(-1, parsePosition.getIndex());
 }

}