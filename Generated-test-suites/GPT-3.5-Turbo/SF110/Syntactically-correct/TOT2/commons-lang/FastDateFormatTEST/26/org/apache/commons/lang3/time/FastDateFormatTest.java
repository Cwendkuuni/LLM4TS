package org.apache.commons.lang3.time;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        //additional test cases can be added here
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
        //additional test cases can be added here
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
        //additional test cases can be added here
    }

    // Similar test methods can be implemented for other getInstance variations

    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        Object obj = new Object();
        StringBuffer result = instance.format(obj, buffer, fieldPosition);
        assertNotNull(result);
        //additional test cases can be added here
    }

    // Similar test methods can be implemented for other format variations

    // Additional test cases for other public methods can be added here

}