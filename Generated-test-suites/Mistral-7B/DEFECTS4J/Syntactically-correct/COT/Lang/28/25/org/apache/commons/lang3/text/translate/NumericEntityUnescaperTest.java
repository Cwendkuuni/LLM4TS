import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleEntity() throws IOException {
 char[] input = {'&', '#', '3', ';'};
 String expectedOutput = " Charlese"; // Assuming the Writer output is appended to a String for testing
 String output = translateAndGetOutput(input);
 Assert.assertEquals("Unexpected output from translate method for simple entity", expectedOutput, output);
 }

 @Test
 public void testTranslateMultipleEntities() throws IOException {
 char[] input = {'&', '#', '3', ';', '&', '#', 'x', '2', '7', '1', '6', ';', '&', '#', '1', '2', '8', ';'};
 String expectedOutput = " Charlese😀";
 String output = translateAndGetOutput(input);
 Assert.assertEquals("Unexpected output from translate method for multiple entities", expectedOutput, output);
 }

 @Test
 public void testTranslateInvalidEntity() throws IOException {
 char[] input = {'&', '#', 'X', 'A', ';'};
 String output = translateAndGetOutput(input);
 Assert.assertEquals("Unexpected output from translate method for invalid entity", "", output);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumberFormat() throws IOException {
 char[] input = {'&', '#', 'x', 'a', 'b', ';'};
 translateAndGetOutput(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateIndexOutOfBounds() throws IOException {
 char[] input = {'&', '#', '3', ';', '&', ' ', ';'};
 translateAndGetOutput(input);
 }

 @Test(expected = IOException.class)
 public void testTranslateWriterException() throws IOException {
 Writer writer = new Writer() {
 @Override
 public void write(int c, java.io.Writer writer) throws IOException {
 throw new IOException("Writer exception");
 }
 };
 char[] input = {'&', '#', '3', ';'};
 unescaper.translate(new String(input), 0, writer);
 }

 private String translateAndGetOutput(char[] input) throws IOException {
 StringWriter stringWriter = new StringWriter();
 unescaper.translate(new String(input), 0, stringWriter);
 return stringWriter.toString();
}

}