package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import java.text.*;
import org.junit.Test;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test format with positive whole number
        Fraction fraction1 = new Fraction(5, 3);
        StringBuffer buffer1 = new StringBuffer();
        FieldPosition pos1 = new FieldPosition(0);
        StringBuffer expected1 = new StringBuffer();
        format.getWholeFormat().format(1, expected1, pos1);
        expected1.append(" 2/3");
        assertEquals(expected1.toString(), format.format(fraction1, buffer1, pos1).toString());
        
        // Test format with negative whole number
        Fraction fraction2 = new Fraction(-5, 3);
        StringBuffer buffer2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0);
        StringBuffer expected2 = new StringBuffer();
        format.getWholeFormat().format(-1, expected2, pos2);
        expected2.append(" -2/3");
        assertEquals(expected2.toString(), format.format(fraction2, buffer2, pos2).toString());
        
        // Test format with zero whole number
        Fraction fraction3 = new Fraction(2, 3);
        StringBuffer buffer3 = new StringBuffer();
        FieldPosition pos3 = new FieldPosition(0);
        StringBuffer expected3 = new StringBuffer();
        expected3.append("2/3");
        assertEquals(expected3.toString(), format.format(fraction3, buffer3, pos3).toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test parse with positive whole number
        String source1 = "1 2/3";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction expected1 = new Fraction(5, 3);
        assertEquals(expected1, format.parse(source1, pos1));
        
        // Test parse with negative whole number
        String source2 = "-1 2/3";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction expected2 = new Fraction(-5, 3);
        assertEquals(expected2, format.parse(source2, pos2));
        
        // Test parse with zero whole number
        String source3 = "2/3";
        ParsePosition pos3 = new ParsePosition(0);
        Fraction expected3 = new Fraction(2, 3);
        assertEquals(expected3, format.parse(source3, pos3));
    }
}