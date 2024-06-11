
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        // Test case: Input starting with "&#" followed by a decimal digit
        CharSequence input1 = "&#123;";
        Writer mockWriter1 = mock(Writer.class);
        int result1 = translator.translate(input1, 0, mockWriter1);
        verify(mockWriter1, times(1)).write((char) 123);
        assertEquals(5, result1);

        // Test case: Input starting with "&#" followed by "x" or "X" and a hexadecimal digit
        CharSequence input2 = "&#x12A;";
        Writer mockWriter2 = mock(Writer.class);
        int result2 = translator.translate(input2, 0, mockWriter2);
        verify(mockWriter2, times(1)).write((char) 298);
        assertEquals(7, result2);

        // Test case: Input starting with "&#" followed by non-digit character
        CharSequence input3 = "&#abc;";
        Writer mockWriter3 = mock(Writer.class);
        int result3 = translator.translate(input3, 0, mockWriter3);
        assertEquals(0, result3);
    }
}
