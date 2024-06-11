import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor1() {
        Option option = new Option("opt1", "description1");
        assertEquals("opt1", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description1", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("opt2", true, "description2");
        assertEquals("opt2", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description2", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("opt3", "longOpt3", true, "description3");
        assertEquals("opt3", option.getOpt());
        assertEquals("longOpt3", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description3", option.getDescription());
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("opt4", "description4");
        assertEquals('o', option.getId());
    }
    
    @Test
    public void testGetKey() {
        Option option = new Option("opt5", "description5");
        assertEquals("opt5", option.getKey());
    }
    
    @Test
    public void testGetType() {
        Option option = new Option("opt6", "description6");
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        Option option = new Option("opt7", "description7");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }
    
    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt8", "description8");
        assertNull(option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt9", "description9");
        option.setLongOpt("longOpt9");
        assertEquals("longOpt9", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt10", "description10");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt11", "description11");
        assertFalse(option.hasLongOpt());
        option.setLongOpt("longOpt11");
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option1 = new Option("opt12", "description12");
        assertFalse(option1.hasArg());
        Option option2 = new Option("opt13", true, "description13");
        assertTrue(option2.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("opt14", "description14");
        assertEquals("description14", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        Option option = new Option("opt15", "description15");
        option.setDescription("newDescription15");
        assertEquals("newDescription15", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        Option option = new Option("opt16", "description16");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("opt17", "description17");
        option.setArgName("argName17");
        assertEquals("argName17", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        Option option1 = new Option("opt18", "description18");
        assertFalse(option1.hasArgName());
        Option option2 = new Option("opt19", "description19");
        option2.setArgName("argName19");
        assertTrue(option2.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option1 = new Option("opt20", "description20");
        assertFalse(option1.hasArgs());
        Option option2 = new Option("opt21", "description21");
        option2.setArgs(2);
        assertTrue(option2.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("opt22", "description22");
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt23", "description23");
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("opt24", "description24");
        assertFalse(option1.hasValueSeparator());
        Option option2 = new Option("opt25", "description25");
        option2.setValueSeparator(':');
        assertTrue(option2.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        Option option = new Option("opt26", "description26");
        assertEquals(-1, option.getArgs());
        option.setArgs(4);
        assertEquals(4, option.getArgs());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        Option option = new Option("opt27", "description27");
        option.setValueSeparator(',');
        option.setArgs(2);
        option.addValueForProcessing("value1,value2,value3");
    }
    
    @Test
    public void testGetValue() {
        Option option1 = new Option("opt28", "description28");
        assertNull(option1.getValue());
        Option option2 = new Option("opt29", "description29");
        option2.addValueForProcessing("value1");
        assertEquals("value1", option2.getValue());
    }
    
    @Test
    public void testGetValueIndexOutOfBounds() {
        Option option = new Option("opt30", "description30");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue(0));
        assertNull(option.getValue(1));
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        Option option1 = new Option("opt31", "description31");
        assertNull(option1.getValue("defaultValue1"));
        Option option2 = new Option("opt32", "description32");
        option2.addValueForProcessing("value1");
        assertEquals("value1", option2.getValue("defaultValue2"));
    }
    
    @Test
    public void testGetValues() {
        Option option1 = new Option("opt33", "description33");
        assertNull(option1.getValues());
        Option option2 = new Option("opt34", "description34");
        option2.addValueForProcessing("value1,value2,value3");
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, option2.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("opt35", "description35");
        assertTrue(option.getValuesList().isEmpty());
        option.addValueForProcessing("value1,value2,value3");
        assertEquals(3, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
        assertEquals("value3", option.getValuesList().get(2));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("opt36", "description36");
        assertEquals("[ option: opt36  :: description36 ]", option1.toString());
        Option option2 = new Option("opt37", "longOpt37", true, "description37");
        assertEquals("[ option: opt37 longOpt37 [ARG] :: description37 ]", option2.toString());
        Option option3 = new Option("opt38", true, "description38");
        assertEquals("[ option: opt38 [ARG...] :: description38 ]", option3.toString());
    }
    
    @Test
    public void testHasNoValues() {
        Option option1 = new Option("opt39", "description39");
        assertTrue(option1.hasNoValues());
        Option option2 = new Option("opt40", "description40");
        option2.addValueForProcessing("value1");
        assertFalse(option2.hasNoValues());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("opt41", "description41");
        Option option2 = new Option("opt42", "description42");
        assertFalse(option1.equals(option2));
        option1.setLongOpt("longOpt41");
        assertFalse(option1.equals(option2));
        option2.setLongOpt("longOpt42");
        assertFalse(option1.equals(option2));
        option2.setOpt("opt41");
        assertTrue(option1.equals(option2));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("opt43", "description43");
        Option option2 = new Option("opt43", "description43");
        assertEquals(option1.hashCode(), option2.hashCode());
        option2.setLongOpt("longOpt43");
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option1 = new Option("opt44", "description44");
        Option option2 = (Option) option1.clone();
        assertEquals(option1, option2);
        option2.setLongOpt("longOpt44");
        assertNotEquals(option1, option2);
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("opt45", "description45");
        option.addValueForProcessing("value1");
        option.clearValues();
        assertTrue(option.hasNoValues());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt46", "description46");
        option.addValue("value1");
    }
}