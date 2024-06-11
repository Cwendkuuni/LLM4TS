import org.junit.Test;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option opt = new Option("t", "Test Option");
        assert opt.getOpt().equals("t");
        assert opt.getLongOpt() == null;
        assert !opt.hasArg();
        assert opt.getDescription().equals("Test Option");
    }

    @Test
    public void testConstructorWithHasArg() {
        Option opt = new Option("t", true, "Test Option");
        assert opt.getOpt().equals("t");
        assert opt.getLongOpt() == null;
        assert opt.hasArg();
        assert opt.getDescription().equals("Test Option");
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option opt = new Option("t", "test", true, "Test Option");
        assert opt.getOpt().equals("t");
        assert opt.getLongOpt().equals("test");
        assert opt.hasArg();
        assert opt.getDescription().equals("Test Option");
    }

    @Test
    public void testGetId() {
        Option opt = new Option("t", "Test Option");
        assert opt.getId() == 't';
    }

    @Test
    public void testGetKey() {
        Option opt = new Option("t", "Test Option");
        assert opt.getKey().equals("t");
    }

    @Test
    public void testGetType() {
        Option opt = new Option("t", "Test Option");
        assert opt.getType() == null;
    }

    @Test
    public void testSetType() {
        Option opt = new Option("t", "Test Option");
        opt.setType("type");
        assert opt.getType().equals("type");
    }

    @Test
    public void testGetLongOpt() {
        Option opt = new Option("t", "test", true, "Test Option");
        assert opt.getLongOpt().equals("test");
    }

    @Test
    public void testSetLongOpt() {
        Option opt = new Option("t", "Test Option");
        opt.setLongOpt("test");
        assert opt.getLongOpt().equals("test");
    }

    @Test
    public void testSetOptionalArg() {
        Option opt = new Option("t", "Test Option");
        opt.setOptionalArg(true);
        assert opt.hasOptionalArg();
    }

    @Test
    public void testHasLongOpt() {
        Option opt = new Option("t", "test", true, "Test Option");
        assert opt.hasLongOpt();
    }

    @Test
    public void testHasArg() {
        Option opt1 = new Option("t", "Test Option");
        assert !opt1.hasArg();

        Option opt2 = new Option("t", true, "Test Option");
        assert opt2.hasArg();
    }

    @Test
    public void testGetDescription() {
        Option opt = new Option("t", "Test Option");
        assert opt.getDescription().equals("Test Option");
    }

    @Test
    public void testSetDescription() {
        Option opt = new Option("t", "Test Option");
        opt.setDescription("New Description");
        assert opt.getDescription().equals("New Description");
    }

    @Test
    public void testIsRequired() {
        Option opt = new Option("t", "Test Option");
        assert !opt.isRequired();
    }

    @Test
    public void testSetRequired() {
        Option opt = new Option("t", "Test Option");
        opt.setRequired(true);
        assert opt.isRequired();
    }

    @Test
    public void testSetArgName() {
        Option opt = new Option("t", "Test Option");
        opt.setArgName("argument");
        assert opt.getArgName().equals("argument");
    }

    @Test
    public void testHasArgName() {
        Option opt = new Option("t", "Test Option");
        opt.setArgName("argument");
        assert opt.hasArgName();
    }

    @Test
    public void testHasArgs() {
        Option opt1 = new Option("t", "Test Option");
        assert !opt1.hasArgs();

        Option opt2 = new Option("t", true, "Test Option");
        assert !opt2.hasArgs();

        Option opt3 = new Option("t", "test", true, "Test Option");
        assert opt3.hasArgs();
    }

    @Test
    public void testSetArgs() {
        Option opt = new Option("t", "Test Option");
        opt.setArgs(2);
        assert opt.getArgs() == 2;
    }

    @Test
    public void testSetValueSeparator() {
        Option opt = new Option("t", "Test Option");
        opt.setValueSeparator(',');
        assert opt.getValueSeparator() == ',';
    }

    @Test
    public void testHasValueSeparator() {
        Option opt = new Option("t", "Test Option");
        opt.setValueSeparator(',');
        assert opt.hasValueSeparator();
    }

    @Test
    public void testGetArgs() {
        Option opt = new Option("t", "Test Option");
        opt.setArgs(2);
        assert opt.getArgs() == 2;
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option opt = new Option("t", "Test Option");
        opt.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option opt1 = new Option("t", "Test Option");
        opt1.setArgs(2);
        opt1.processValue("value1,value2");
        assert opt1.getValues().length == 2;

        Option opt2 = new Option("t", "Test Option");
        opt2.setValueSeparator(',');
        opt2.setArgs(2);
        opt2.processValue("value1,value2");
        assert opt2.getValues().length == 2;
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        Option opt = new Option("t", "Test Option");
        opt.setArgs(2);
        opt.addValue("value1");
        opt.addValue("value2");
        opt.addValue("value3");
    }

    @Test
    public void testGetValue() {
        Option opt = new Option("t", "Test Option");
        opt.addValue("value");
        assert opt.getValue().equals("value");
    }

    @Test
    public void testGetValueWithIndex() {
        Option opt = new Option("t", "Test Option");
        opt.addValue("value1");
        opt.addValue("value2");
        assert opt.getValue(0).equals("value1");
        assert opt.getValue(1).equals("value2");
    }

    @Test
    public void testGetValueWithDefault() {
        Option opt = new Option("t", "Test Option");
        assert opt.getValue("default").equals("default");

        opt.addValue("value");
        assert opt.getValue("default").equals("value");
    }

    @Test
    public void testGetValues() {
        Option opt = new Option("t", "Test Option");
        opt.addValue("value1");
        opt.addValue("value2");
        String[] values = opt.getValues();
        assert values.length == 2;
        assert values[0].equals("value1");
        assert values[1].equals("value2");
    }

    @Test
    public void testGetValuesList() {
        Option opt = new Option("t", "Test Option");
        opt.addValue("value1");
        opt.addValue("value2");
        List<String> valuesList = opt.getValuesList();
        assert valuesList.size() == 2;
        assert valuesList.get(0).equals("value1");
        assert valuesList.get(1).equals("value2");
    }

    @Test
    public void testToString() {
        Option opt = new Option("t", "Test Option");
        assert opt.toString().equals("[ option: t [ARG] :: Test Option ]");

        Option opt2 = new Option("t", "test", true, "Test Option");
        assert opt2.toString().equals("[ option: t test [ARG...] :: Test Option ]");
    }

    @Test
    public void testEquals() {
        Option opt1 = new Option("t", "Test Option");
        Option opt2 = new Option("t", "Test Option");
        assert opt1.equals(opt2);
    }

    @Test
    public void testHashCode() {
        Option opt1 = new Option("t", "Test Option");
        Option opt2 = new Option("t", "Test Option");
        assert opt1.hashCode() == opt2.hashCode();
    }

    @Test
    public void testClone() {
        Option opt1 = new Option("t", "Test Option");
        Option opt2 = (Option) opt1.clone();
        assert opt1.equals(opt2);

        opt1.addValue("value");
        assert !opt1.equals(opt2);
    }
}