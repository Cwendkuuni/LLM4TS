package org.apache.commons.math.fraction;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.FieldPosition;

public class ProperFractionFormatTest {

  @Test
  public void testFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer buffer = new StringBuffer();
    
    // Test case 1: Fraction with whole number, numerator, and denominator
    format.format(new Fraction(3, 2), buffer, pos);
    assert buffer.toString().equals("1 1 / 2");
    
    // Test case 2: Fraction without whole number
    format.format(new Fraction(1, 2), buffer, pos);
    assert buffer.toString().equals("1 / 2");
    
    // Test case 3: Fraction with negative whole number
    format.format(new Fraction(-3, 2), buffer, pos);
    assert buffer.toString().equals("-1 1 / 2");
  }

  @Test
  public void testParse() {
    ProperFractionFormat format = new ProperFractionFormat();
    ParsePosition pos = new ParsePosition(0);
    
    // Test case 1: Proper formatted fraction
    Fraction result = format.parse("1 1/2", pos);
    assert result.getNumerator() == 3;
    assert result.getDenominator() == 2;
    assert pos.getIndex() == 5;
    
    // Test case 2: Fraction without whole number
    pos = new ParsePosition(0);
    result = format.parse("1/2", pos);
    assert result.getNumerator() == 1;
    assert result.getDenominator() == 2;
    assert pos.getIndex() == 3;
    
    // Test case 3: Fraction with negative whole number
    pos = new ParsePosition(0);
    result = format.parse("-1 1/2", pos);
    assert result.getNumerator() == -3;
    assert result.getDenominator() == 2;
    assert pos.getIndex() == 6;
  }

  @Test
  public void testSetWholeFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    NumberFormat newFormat = NumberFormat.getCurrencyInstance();
    format.setWholeFormat(newFormat);
    assert format.getWholeFormat() == newFormat;
  }
}