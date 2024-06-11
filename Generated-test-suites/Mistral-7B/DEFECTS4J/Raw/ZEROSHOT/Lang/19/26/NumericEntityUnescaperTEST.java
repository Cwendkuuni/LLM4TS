import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test decimal entities
 String input1 = "&#128;";
 String expected1 = "\u0080";
 String output1 = "";
 try {
 output1 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input1), 0, new StringWriter(new StringBuffer(10), output1) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected1, output1);

 String input2 = "&#160;";
 String expected2 = " ";
 String output2 = "";
 try {
 output2 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input2), 0, new StringWriter(new StringBuffer(10), output2) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected2, output2);

 String input3 = "&#[128];";
 String expected3 = "\u0080";
 String output3 = "";
 try {
 output3 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input3), 0, new StringWriter(new StringBuffer(10), output3) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected3, output3);

 // Test hex entities
 String input4 = "&#x61;";
 String expected4 = "a";
 String output4 = "";
 try {
 output4 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input4), 0, new StringWriter(new StringBuffer(10), output4) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected4, output4);

 String input5 = "&#x416;";
 String expected5 = "ā";
 String output5 = "";
 try {
 output5 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input5), 0, new StringWriter(new StringBuffer(10), output5) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected5, output5);

 String input6 = "&#[x416];";
 String expected6 = "ā";
 String output6 = "";
 try {
 output6 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input6), 0, new StringWriter(new StringBuffer(10), output6) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected6, output6);

 // Test entities with invalid format
 String input7 = "&##123;";
 String expected7 = "";
 String output7 = "";
 try {
 output7 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input7), 0, new StringWriter(new StringBuffer(10), output7) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected7, output7);

 String input8 = "&&#123;";
 String expected8 = "";
 String output8 = "";
 try {
 output8 = new StringWriter(new StringBuffer(10)).toString();
 unescaper.translate( new StringReader(input8), 0, new StringWriter(new StringBuffer(10), output8) );
 } catch (IOException e) {
 e.printlnstream("TEST FAILED: ");
 e.printStackTrace();
 }
 assertEquals(expected8, output8);

 String input9 = "&&#1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 String expected9 = "";
 String output9 = "";
 try {
 output9 = new StringWriter(new StringBuffer(100)).toString();
 unescaper.translate( new StringReader(input9), 0, new StringWriter(new StringBuffer(100), output9) );
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected9, output9);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testTranslateNumericEntityWithNegativeIndex() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "&#[128];";
 unescaper.translate(new StringReader(""), -1, new StringWriter(new StringBuffer(10)));
 }

 @Test (expected = IOException.class)
 public void testTranslateNumericEntityWithNullInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(null, 0, new StringWriter(new StringBuffer(10)));
 }

 @Test (expected = IOException.class)
 public void testTranslateNumericEntityWithNullWriter() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader("&#[128];"), 0, null);
 }
}