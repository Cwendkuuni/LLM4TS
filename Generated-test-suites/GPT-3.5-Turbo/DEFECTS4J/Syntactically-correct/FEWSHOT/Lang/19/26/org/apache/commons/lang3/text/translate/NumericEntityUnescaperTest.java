package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Testing decimal entities
        StringBuffer sb1 = new StringBuffer("The decimal entity &#65; represents the letter A");
        unescaper.translate(sb1, 0, null);
        assertEquals("The decimal entity A represents the letter A", sb1.toString());

        StringBuffer sb2 = new StringBuffer("The decimal entity &#8364; represents the Euro sign €");
        unescaper.translate(sb2, 0, null);
        assertEquals("The decimal entity € represents the Euro sign €", sb2.toString());

        // Testing hexadecimal entities
        StringBuffer sb3 = new StringBuffer("The hexadecimal entity &#x3C; represents the less-than symbol <");
        unescaper.translate(sb3, 0, null);
        assertEquals("The hexadecimal entity < represents the less-than symbol <", sb3.toString());

        StringBuffer sb4 = new StringBuffer("The hexadecimal entity &#x20AC; represents the Euro sign €");
        unescaper.translate(sb4, 0, null);
        assertEquals("The hexadecimal entity € represents the Euro sign €", sb4.toString());

        // Testing entities without semicolon
        StringBuffer sb5 = new StringBuffer("The entity &amp represents the ampersand character &");
        unescaper.translate(sb5, 0, null);
        assertEquals("The entity & represents the ampersand character &", sb5.toString());

        StringBuffer sb6 = new StringBuffer("The entity &copy represents the copyright symbol ©");
        unescaper.translate(sb6, 0, null);
        assertEquals("The entity © represents the copyright symbol ©", sb6.toString());

        // Testing invalid entities
        StringBuffer sb7 = new StringBuffer("The entity &#invalid; is not a valid entity");
        unescaper.translate(sb7, 0, null);
        assertEquals("The entity &#invalid; is not a valid entity", sb7.toString());
    }
}