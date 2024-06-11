import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.Writer;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Mock
    Writer mockWriter;

    @Test
    public void testTranslate() throws IOException {
        // Test with valid input, numeric entity
        CharSequence input = "&#65;";
        int index = 0;
        int result = numericEntityUnescaper.translate(input, index, mockWriter);
        assertEquals(4, result);
        Mockito.verify(mockWriter, Mockito.times(1)).write('A');

        // Test with valid input, hex numeric entity
        input = "&#x42;";
        index = 0;
        result = numericEntityUnescaper.translate(input, index, mockWriter);
        assertEquals(6, result);
        Mockito.verify(mockWriter, Mockito.times(1)).write('B');

        // Test with invalid input
        input = "&#invalid;";
        index = 0;
        result = numericEntityUnescaper.translate(input, index, mockWriter);
        assertEquals(0, result);
        Mockito.verify(mockWriter, Mockito.never()).write(Mockito.anyChar());

        // Test with invalid input, missing semicolon
        input = "&#65";
        index = 0;
        result = numericEntityUnescaper.translate(input, index, mockWriter);
        assertEquals(0, result);
        Mockito.verify(mockWriter, Mockito.never()).write(Mockito.anyChar());

        // Test with invalid input, missing numeric value
        input = "&;";
        index = 0;
        result = numericEntityUnescaper.translate(input, index, mockWriter);
        assertEquals(0, result);
        Mockito.verify(mockWriter, Mockito.never()).write(Mockito.anyChar());
    }
}