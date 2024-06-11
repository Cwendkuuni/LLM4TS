import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whois;
    
    @Before
    public void setUp() {
        whois = new WhoIS();
    }
    
    // Test cases for queryTextField
    @Test
    public void testQueryTextField_typicalCase() {
        whois.queryTextField.setText("example");
        Assert.assertEquals("example", whois.getQuery());
    }
    
    @Test
    public void testQueryTextField_emptyCase() {
        whois.queryTextField.setText("");
        Assert.assertEquals("", whois.getQuery());
    }
    
    // Test cases for inverseQueries
    @Test
    public void testInverseQueries_typicalCase() {
        whois.inverseQueries.setSelectedItem("admin-c");
        Assert.assertEquals("-i admin-c example", whois.getQuery());
    }
    
    @Test
    public void testInverseQueries_noneCase() {
        whois.inverseQueries.setSelectedItem("None");
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for lowercaseL
    @Test
    public void testLowercaseL_selected() {
        whois.lowercaseL.setSelected(true);
        Assert.assertEquals("-l example", whois.getQuery());
    }
    
    @Test
    public void testLowercaseL_notSelected() {
        whois.lowercaseL.setSelected(false);
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for uppercaseL
    @Test
    public void testUppercaseL_selected() {
        whois.uppercaseL.setSelected(true);
        Assert.assertEquals("-L example", whois.getQuery());
    }
    
    @Test
    public void testUppercaseL_notSelected() {
        whois.uppercaseL.setSelected(false);
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for lowercaseM
    @Test
    public void testLowercaseM_selected() {
        whois.lowercaseM.setSelected(true);
        Assert.assertEquals("-m example", whois.getQuery());
    }
    
    @Test
    public void testLowercaseM_notSelected() {
        whois.lowercaseM.setSelected(false);
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for uppercaseM
    @Test
    public void testUppercaseM_selected() {
        whois.uppercaseM.setSelected(true);
        Assert.assertEquals("-M example", whois.getQuery());
    }
    
    @Test
    public void testUppercaseM_notSelected() {
        whois.uppercaseM.setSelected(false);
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for lowercaseX
    @Test
    public void testLowercaseX_selected() {
        whois.lowercaseX.setSelected(true);
        Assert.assertEquals("-x example", whois.getQuery());
    }
    
    @Test
    public void testLowercaseX_notSelected() {
        whois.lowercaseX.setSelected(false);
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for lowercaseD
    @Test
    public void testLowercaseD_selected() {
        whois.booleanD = true;
        Assert.assertEquals("-d example", whois.getQuery());
    }
    
    @Test
    public void testLowercaseD_notSelected() {
        whois.booleanD = false;
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for miscList
    @Test
    public void testMiscList_typicalCase() {
        whois.miscList.setSelectedIndices(new int[] {1, 2, 3});
        Assert.assertEquals("-T as-set,aut-num,domain example", whois.getQuery());
    }
    
    @Test
    public void testMiscList_emptyCase() {
        whois.miscList.setSelectedIndices(new int[] {});
        Assert.assertEquals("example", whois.getQuery());
    }
    
    // Test cases for reset()
    @Test
    public void testReset() {
        whois.queryTextField.setText("example");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndex(1);
        
        whois.reset();
        
        Assert.assertEquals("", whois.queryTextField.getText());
        Assert.assertEquals(false, whois.lowercaseD.isSelected());
        Assert.assertEquals(0, whois.inverseQueries.getSelectedIndex());
        Assert.assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
}