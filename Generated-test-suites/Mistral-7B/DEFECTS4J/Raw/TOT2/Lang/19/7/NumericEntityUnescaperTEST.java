import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateValidNumericEntities() {
        // Method 1
    }

    @Test(expected = IOException.class)
    public void testTranslateInvalidInput() {
        // Method 2
    }

    @Test
    public void testTranslateEmptyInput() {
        // Method 3
    }

    @Test
    public void testTranslateNumericEntitiesOnly() {
        // Method 4
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidCharacterCodes() {
        // Method 5
    }

    @Test
    public void testTranslateNumericEntitiesOutsideOfMethodRange() {
        // Method 6
    }

    @Test
    public void testTranslateMixedValidAndInvalidNumericEntities() {
        // Method 7
    }

    @Test
    public void testTranslateNonNumericEntity() {
        // Method 8
    }
}