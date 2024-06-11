import org.apache.commons.io.StringReader;
import org.apache.commons.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateDecimalEntities() {
 // ...
 }

 @Test
 public void testTranslateHexEntities() {
 // ...
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidEntity() {
 // ...
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateArrayIndexOutOfBounds() {
 // ...
 }
}