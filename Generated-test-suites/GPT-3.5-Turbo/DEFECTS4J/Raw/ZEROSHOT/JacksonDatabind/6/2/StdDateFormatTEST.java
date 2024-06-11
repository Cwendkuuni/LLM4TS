import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

   @Test
   public void testLooksLikeISO8601() {
       StdDateFormat dateFormat = new StdDateFormat();
       assertTrue(dateFormat.looksLikeISO8601("2021-09-15T12:00:00.000Z"));
       assertTrue(dateFormat.looksLikeISO8601("2021-09-15"));
       assertFalse(dateFormat.looksLikeISO8601("2021/09/15"));
       assertFalse(dateFormat.looksLikeISO8601("15-09-2021"));
   }

   @Test
   public void testParseAsRFC1123() throws Exception {
       StdDateFormat dateFormat = new StdDateFormat();
       assertNotNull(dateFormat.parseAsRFC1123("Wed, 15 Sep 2021 12:00:00 GMT", null));
   }

   @Test
   public void testParseAsISO8601() throws Exception {
       StdDateFormat dateFormat = new StdDateFormat();
       assertNotNull(dateFormat.parseAsISO8601("2021-09-15T12:00:00.000Z", null));
   }

   @Test
   public void testParse() throws Exception {
       StdDateFormat dateFormat = new StdDateFormat();
       assertNotNull(dateFormat.parse("2021-09-15T12:00:00.000Z"));
       assertNotNull(dateFormat.parse("2021-09-15"));
   }

   @Test
   public void testFormat() {
       StdDateFormat dateFormat = new StdDateFormat();
       assertEquals("2021-09-15T12:00:00.000Z", dateFormat.format(new Date()));
   }
}