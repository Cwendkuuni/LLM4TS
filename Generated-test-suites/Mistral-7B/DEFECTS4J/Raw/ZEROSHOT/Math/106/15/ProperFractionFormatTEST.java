import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simplify.FractionFormat;
import org.apache.commons.text.simplify.FractionFormat.ParsePosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProperFractionFormatTest {

 private static final NumberFormat WHOLE_FORMAT = new DecimalFormat("#");
 private static final NumberFormat NUMERATOR_FORMAT = new DecimalFormat("#");
 private static final NumberFormat DENOMINATOR_FORMAT = new DecimalFormat("#");

 @Test
 public void testProperFractionFormat() {
 // Test default constructor
 FractionFormat format = new FractionFormat();
 // Test format method
 Fraction fraction = new Fraction(1, 2);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 format.format(fraction, toAppendTo, pos);
 Assertions.assertEquals("1 / 2", toAppendTo.toString());
 // Test parse method
 String source = "1 / 2";
 ParsePosition posParse = new ParsePosition(0);
 Fraction parsedFraction = format.parse(source, posParse);
 Assertions.assertEquals(fraction, parsedFraction);
 // Test constructor with custom number format
 FractionFormat formatCustom = new FractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 // Test format method
 fraction = new Fraction(-3, 2);
 toAppendTo = new StringBuffer();
 pos = new FieldPosition(0, 0);
 formatCustom.format(fraction, toAppendTo, pos);
 Assertions.assertEquals("-3 / 2", toAppendTo.toString());
 // Test parse method
 source = "-3 1/2";
 posParse = new ParsePosition(0);
 parsedFraction = formatCustom.parse(source, posParse);
 Assertions.assertEquals(new Fraction(-7, 2), parsedFraction);
 // Test invalid input
 source = "-3 -1/2";
 posParse = new ParsePosition(0);
 parsedFraction = formatCustom.parse(source, posParse);
 Assertions.assertNull(parsedFraction);
 }
}