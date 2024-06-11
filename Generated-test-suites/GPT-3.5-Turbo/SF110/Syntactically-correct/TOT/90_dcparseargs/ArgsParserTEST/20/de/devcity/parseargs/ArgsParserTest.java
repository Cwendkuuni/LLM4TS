
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgsParserTest {

   ArgsParser parser;
   String[] args;

   @Before
   public void setUp() {
      parser = new ArgsParser();
      args = new String[] {"-a", "--long", "-b", "param1", "--long2", "param2"};
      parser.setArgs(args);
   }

   // Test getArgsCount method
   @Test
   public void testGetArgsCount() {
      assertEquals(args.length, parser.getArgsCount());
   }

   // Test getArgs method
   @Test
   public void testGetArgs() {
      assertArrayEquals(args, parser.getArgs());
   }

   // Test setArgs method with null input
   @Test(expected = NullPointerException.class)
   public void testSetArgsNullInput() {
      parser.setArgs(null);
   }

   // Test getArgsLeftCount method
   @Test
   public void testGetArgsLeftCount() {
      int expectedCount = args.length;
      assertEquals(expectedCount, parser.getArgsLeftCount());
      
      // Remove one argument and check if count decreased by 1
      parser.parseSwitchArgument("a");
      expectedCount--;
      assertEquals(expectedCount, parser.getArgsLeftCount());
   }

   // Test parseSwitchArgument method
   @Test
   public void testParseSwitchArgument() {
      // Test case for a valid switch argument
      SwitchArgument switchArg = parser.parseSwitchArgument("a");
      assertNotNull(switchArg);
      assertEquals(0, switchArg.getIndex());
      assertEquals("a", switchArg.getKey());
      assertEquals(true, switchArg.getValue());

      // Test case for a non-existent switch argument
      switchArg = parser.parseSwitchArgument("c");
      assertNotNull(switchArg);
      assertEquals(-1, switchArg.getIndex());
      assertEquals("c", switchArg.getKey());
      assertEquals(false, switchArg.getValue());
   }

   // Test parseParameterArgument method
   @Test
   public void testParseParameterArgument() {
      // Test case for a valid parameter argument
      ParameterArgument parameterArg = parser.parseParameterArgument("b");
      assertNotNull(parameterArg);
      assertEquals(2, parameterArg.getIndex());
      assertEquals("b", parameterArg.getKey());
      assertEquals("param1", parameterArg.getValue());

      // Test case for a non-existent parameter argument
      parameterArg = parser.parseParameterArgument("c");
      assertNull(parameterArg);
   }

   // Test parseStringArgument method
   @Test
   public void testParseStringArgument() {
      List<StringArgument> stringArgs = parser.parseStringArgument();
      assertEquals(args.length, stringArgs.size());

      // Test each parsed string argument
      for (int i = 0; i < stringArgs.size(); i++) {
         StringArgument arg = stringArgs.get(i);
         assertEquals(i, arg.getIndex());
         assertEquals(args[i], arg.getValue());
      }
   }

}

