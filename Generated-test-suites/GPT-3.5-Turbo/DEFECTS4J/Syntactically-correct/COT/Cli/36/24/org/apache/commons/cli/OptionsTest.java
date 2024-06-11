import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", false, "description1");
        Option option2 = new Option("opt2", true, "description2");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        Assert.assertTrue(options.hasOption("opt1"));
        Assert.assertTrue(options.hasOption("opt2"));
        Assert.assertTrue(options.getOptionGroup(option1).equals(group));
        Assert.assertTrue(options.getOptionGroup(option2).equals(group));
    }
    
    @Test
    public void testAddOptionShort() {
        Options options = new Options();
        
        options.addOption("o", "description");
        
        Assert.assertTrue(options.hasOption("o"));
        Assert.assertFalse(options.hasOption("longopt"));
    }
    
    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        
        options.addOption("o", true, "description");
        
        Assert.assertTrue(options.hasOption("o"));
        Assert.assertTrue(options.getOption("o").hasArg());
        Assert.assertEquals(options.getOption("o").getDescription(), "description");
    }
    
    @Test
    public void testAddOptionWithLong() {
        Options options = new Options();
        
        options.addOption("o", "option", false, "description");
        
        Assert.assertTrue(options.hasOption("o"));
        Assert.assertTrue(options.hasOption("option"));
        Assert.assertEquals(options.getOption("o").getLongOpt(), "option");
        Assert.assertEquals(options.getOption("o").getDescription(), "description");
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertEquals(options.getOptions().size(), 2);
        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        options.addRequiredOption("o1");
        
        Assert.assertEquals(options.getRequiredOptions().size(), 1);
        Assert.assertTrue(options.getRequiredOptions().contains("o1"));
        Assert.assertFalse(options.getRequiredOptions().contains("o2"));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertEquals(options.getOption("o1"), option1);
        Assert.assertEquals(options.getOption("o2"), option2);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", false, "description1");
        Option option2 = new Option("option1", true, "description2");
        Option option3 = new Option("option2", false, "description3");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        Assert.assertEquals(options.getMatchingOptions("opt").size(), 1);
        Assert.assertTrue(options.getMatchingOptions("opt").contains("opt1"));
        Assert.assertEquals(options.getMatchingOptions("option").size(), 2);
        Assert.assertTrue(options.getMatchingOptions("option").contains("option1"));
        Assert.assertTrue(options.getMatchingOptions("option").contains("option2"));
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertTrue(options.hasOption("o1"));
        Assert.assertTrue(options.hasOption("o2"));
        Assert.assertFalse(options.hasOption("o3"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertFalse(options.hasLongOption("o1"));
        Assert.assertFalse(options.hasLongOption("o2"));
        Assert.assertFalse(options.hasLongOption("option1"));
        Assert.assertFalse(options.hasLongOption("option2"));
        
        options.addOption("o1", "option1", false, "description");
        
        Assert.assertTrue(options.hasLongOption("option1"));
        
        options.addOption("o2", "option2", false, "description");
        
        Assert.assertTrue(options.hasLongOption("option2"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertTrue(options.hasShortOption("o1"));
        Assert.assertTrue(options.hasShortOption("o2"));
        Assert.assertFalse(options.hasShortOption("option1"));
        Assert.assertFalse(options.hasShortOption("option2"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", false, "description1");
        Option option2 = new Option("option1", true, "description2");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        Assert.assertEquals(options.getOptionGroup(option1), group);
        Assert.assertEquals(options.getOptionGroup(option2), group);
        Assert.assertNull(options.getOptionGroup(new Option("opt2", false, "description")));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o1", false, "description1");
        Option option2 = new Option("o2", true, "description2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        Assert.assertEquals(options.toString(), "[ Options: [ short {o1: {@code <no argument>} description1, o2: {@code <args>}, description2} ] [ long {} ]");
    }
}