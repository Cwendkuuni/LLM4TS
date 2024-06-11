package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.fail;

public class NumericEntityUnescaperTest {

private static final String CHAR_SEQUENCE_TEST_DATA = "& #65; & #x41; & #127; % #65; & #65535; & #x65535; & #x123456; & #123456; & #12345; & #1234";

@Test
public void testMultipleEntities() {
CharSequence input = new String(CHAR_SEQUENCE_TEST_DATA);
Writer out = new StringWriter();
int actual = NumericEntityUnescaper.translate(input, 0, out);
assertEquals("Hello A world W", out.toString()); // verify the result
assertEquals(12, actual); // verify the length of the translated sequence
}

// Test cases for each expert
// ...
// Add more test cases if necessary

}