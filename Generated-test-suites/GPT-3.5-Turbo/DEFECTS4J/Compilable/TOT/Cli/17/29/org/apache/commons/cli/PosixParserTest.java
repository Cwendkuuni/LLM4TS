package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

   private PosixParser parser;
   private Options options;

   @Before
   public void setUp(){
      parser = new PosixParser();
      options = new Options();
      options.addOption(new Option("a", "option a"));
      options.addOption(new Option("b", true, "option b with argument"));
      options.addOption(new Option("c", false, "option c without argument"));
   }

   @Test
   public void testFlatten_typicalUseCase(){
      String[] arguments = {"-a", "-b", "argument1", "-c"};
      String[] expected = {"-a", "-b", "argument1", "-c"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_edgeCase(){
      String[] arguments = {"--", "-a", "-b", "argument1"};
      String[] expected = {"--", "-a", "-b", "argument1"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_errorScenario(){
      String[] arguments = {"-d", "-e"};
      String[] expected = {"-d", "-e"};

      String[] result = parser.flatten(options, arguments, true);

      assertArrayEquals(expected, result);
   }

}