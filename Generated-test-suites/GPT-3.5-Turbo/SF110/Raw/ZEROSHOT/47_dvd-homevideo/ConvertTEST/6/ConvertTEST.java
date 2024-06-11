import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class ConvertTest {

   @Test
   public void testInit() {
      Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
      boolean result = convert.init();
      assertEquals(true, result);
   }

   @Test
   public void testRun() {
      Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
      convert.run();
   }

   @Test
   public void testTranscode() {
      Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
      convert.Transcode();
   }

   @Test
   public void testMatrix() {
      Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
      convert.matrix();
   }
}