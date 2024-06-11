package org.apache.commons.cli.test;

 import org.apache.commons.cli.*;
 import org.junit.Assert;
 import org.junit.Test;

 public class OptionBuilderTest
 {
     @Test
     public void testWithLongOpt()
     {
         OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
         Option option = optionBuilder.create();
         Assert.assertEquals("longOpt", option.getLongOpt());
     }

     @Test
     public void testHasArg()
     {
         OptionBuilder optionBuilder = OptionBuilder.hasArg();
         Option option = optionBuilder.create();
         Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
         Assert.assertTrue(option.isRequired());

         String[] args = new String[]{"arg"};
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.assertEquals("arg", line.getOptionValue("longOpt"));
         }
         catch (ParseException e)
         {
             Assert.fail(e.getMessage());
         }
     }

     @Test
     public void testHasArgWithArgName()
     {
         OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
         Option option = optionBuilder.create();
         Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
         Assert.assertTrue(option.isRequired());

         String[] args = new String[]{"argName", "arg"};
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.assertEquals("arg", line.getOptionValue("longOpt", "argName"));
         }
         catch (ParseException e)
         {
             Assert.fail(e.getMessage());
         }
     }

     @Test
     public void testHasArgWithValueSeparator()
     {
         OptionBuilder optionBuilder = OptionBuilder.hasArg().withValueSeparator(':');
         Option option = optionBuilder.create();
         Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
         Assert.assertTrue(option.isRequired());

         String[] args = new String[]{"longOpt", "arg1:arg2"};
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.assertEquals("arg1", line.getOptionValue("longOpt", null));
             Assert.assertEquals("arg2", line.getOptionValue("longOpt", null));
         }
         catch (ParseException e)
         {
             Assert.fail(e.getMessage());
         }
     }

     @Test
     public void testIsRequired()
     {
         OptionBuilder optionBuilder = OptionBuilder.isRequired();
         Option option = optionBuilder.create();
         Assert.assertTrue(option.isRequired());

         String[] args = new String[0];
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.fail("Expected ParseException not reached");
         }
         catch (ParseException e)
         {
             // expected
         }
     }

     @Test
     public void testHasArgs()
     {
         OptionBuilder optionBuilder = OptionBuilder.hasArgs();
         Option option = optionBuilder.create();
         Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
         Assert.assertTrue(option.isRequired());

         String[] args = new String[]{"longOpt", "arg1", "arg2", "arg3"};
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.assertEquals(3, line.getOptions().length);
             for (int i = 0; i < line.getOptions().length; i++)
             {
                 Option opt = line.getOption(i);
                 Assert.assertEquals("longOpt", opt.getLongOpt());
                 Assert.assertArrayEquals(new String[]{"arg" + (i + 1)}, opt.getArgs());
             }
         }
         catch (ParseException e)
         {
             Assert.fail(e.getMessage());
         }
     }

     @Test
     public void testHasOptionalArg()
     {
         OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
         Option option = optionBuilder.create();
         Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
         Assert.assertFalse(option.isRequired());

         String[] args = new String[]{"longOpt", "value"};
         CommandLine parser = new DefaultParser();
         try
         {
             CommandLine line = parser.parse(args);
             Assert.assertEquals("value", line.getOptionValue("longOpt"));
         }
         catch (ParseException e)
         {
             Assert.assertEquals("Unrecognized option: value", e.getMessage());
         }

         args = new String[]{"longOpt"};
         try
         {
             CommandLine line = parser.parse(args);
             // expected no value
         }
         catch (ParseException e)
         {
             Assert.assertEquals("Required option 'longOpt' is missing", e.getMessage());
         }
     }

     @Test(expected = IllegalArgumentException.class)
     public void testCreateWithoutLongOpt()
     {
         OptionBuilder optionBuilder = OptionBuilder.create();
     }
 }