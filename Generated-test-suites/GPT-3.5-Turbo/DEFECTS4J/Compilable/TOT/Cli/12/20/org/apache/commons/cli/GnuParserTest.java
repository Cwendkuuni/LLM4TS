package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

   @Test
   public void testFlatten_FirstOptionExists() {
      Options options = new Options();
      options.addOption("a", "add", false, "Add file");
      options.addOption("d", "delete", true, "Delete file");

      GnuParser parser = new GnuParser();
      String[] arguments = {"-a", "-d", "file.txt"};

      String[] flattened = parser.flatten(options, arguments, true);

      assertEquals(3, flattened.length);
      assertEquals("-a", flattened[0]);
      assertEquals("-d", flattened[1]);
      assertEquals("file.txt", flattened[2]);
   }

   @Test
   public void testFlatten_FirstOptionDoesNotExist() {
      Options options = new Options();
      options.addOption("a", "add", false, "Add file");
      options.addOption("d", "delete", true, "Delete file");

      GnuParser parser = new GnuParser();
      String[] arguments = {"-z", "-d", "file.txt"};

      String[] flattened = parser.flatten(options, arguments, true);

      assertEquals(2, flattened.length);
      assertEquals("-z", flattened[0]);
      assertEquals("file.txt", flattened[1]);
   }

   @Test
   public void testFlatten_DoubleDash() {
      Options options = new Options();
      options.addOption("a", "add", false, "Add file");
      options.addOption("d", "delete", true, "Delete file");

      GnuParser parser = new GnuParser();
      String[] arguments = {"--", "-d", "file.txt"};

      String[] flattened = parser.flatten(options, arguments, false);

      assertEquals(3, flattened.length);
      assertEquals("--", flattened[0]);
      assertEquals("-d", flattened[1]);
      assertEquals("file.txt", flattened[2]);
   }

   @Test
   public void testFlatten_ShortOptionValue() {
      Options options = new Options();
      options.addOption("a", "add", false, "Add file");
      options.addOption("d", "delete", true, "Delete file");

      GnuParser parser = new GnuParser();
      String[] arguments = {"-dfile.txt"};

      String[] flattened = parser.flatten(options, arguments, true);

      assertEquals(1, flattened.length);
      assertEquals("-dfile.txt", flattened[0]);
   }

   @Test
   public void testFlatten_NonOptionEncountered() {
      Options options = new Options();
      options.addOption("a", "add", false, "Add file");
      options.addOption("d", "delete", true, "Delete file");

      GnuParser parser = new GnuParser();
      String[] arguments = {"-a", "file.txt", "-z"};

      String[] flattened = parser.flatten(options, arguments, true);

      assertEquals(3, flattened.length);
      assertEquals("-a", flattened[0]);
      assertEquals("file.txt", flattened[1]);
      assertEquals("-z", flattened[2]);
   }

   @Test
   public void testFlatten_NoOptions() {
      Options options = new Options();

      GnuParser parser = new GnuParser();
      String[] arguments = {"-a", "-b", "-c"};

      String[] flattened = parser.flatten(options, arguments, true);

      assertEquals(3, flattened.length);
      assertEquals("-a", flattened[0]);
      assertEquals("-b", flattened[1]);
      assertEquals("-c", flattened[2]);
   }

}