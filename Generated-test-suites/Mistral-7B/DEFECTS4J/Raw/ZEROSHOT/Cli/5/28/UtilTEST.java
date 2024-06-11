import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String strWithLeadingHyphens = "--option";
 String expected = "option";
 assertEquals(expected, Util.stripLeadingHyphens(strWithLeadingHyphens));

 String strWithoutLeadingHyphens = "option";
 assertEquals(strWithoutLeadingHyphens, Util.stripLeadingHyphens(strWithoutLeadingHyphens));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String strWithLeadingAndTrailingQuotes = "'one two'";
 String expected = "one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes));

 String strWithLeadingQuotes = "'one two" ;
 String expectedLeadingQuotes = "one two";
 assertEquals(expectedLeadingQuotes, Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotes));

 String strWithTrailingQuotes = "one two'";
 String expectedTrailingQuotes = "one two";
 assertEquals(expectedTrailingQuotes, Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotes));

 String strWithoutQuotes = "one two";
 String expectedWithoutQuotes = "one two";
 assertEquals(expectedWithoutQuotes, Util.stripLeadingAndTrailingQuotes(strWithoutQuotes));
 }
}