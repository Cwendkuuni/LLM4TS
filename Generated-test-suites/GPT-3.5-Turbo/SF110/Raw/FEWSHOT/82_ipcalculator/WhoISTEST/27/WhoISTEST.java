package ipac;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        // Testcase 1: when query is empty
        String query = "";
        whois.query(query);
        // no assertions needed, as it will show a popup error message

        // Testcase 2: query for a valid domain
        query = "example.com";
        whois.query(query);
        // assert that the query is executed successfully
        // verify the output using assert statements

        // Testcase 3: query for an invalid domain
        query = "invalid";
        whois.query(query);
        // assert that the query is executed successfully
        // verify the output using assert statements
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();

        // Testcase 1: when no additional options are selected
        assertEquals("example.com", whois.getQuery());

        // Testcase 2: when -i option is selected
        whois.inverseQueries.setSelectedIndex(1);
        assertEquals("-i admin-c example.com", whois.getQuery());

        // Testcase 3: when -l option is selected
        whois.lowercaseL.setSelected(true);
        assertEquals("-l example.com", whois.getQuery());

        // Testcase 4: when -L option is selected
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(true);
        assertEquals("-L example.com", whois.getQuery());

        // Testcase 5: when -m option is selected
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(true);
        assertEquals("-m example.com", whois.getQuery());

        // Testcase 6: when -M option is selected
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(true);
        assertEquals("-M example.com", whois.getQuery());

        // Testcase 7: when -x option is selected
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(true);
        assertEquals("-x example.com", whois.getQuery());

        // Testcase 8: when -d option is selected
        whois.lowercaseX.setSelected(false);
        whois.lowercaseD.setSelected(true);
        assertEquals("-d example.com", whois.getQuery());

        // Testcase 9: when miscList has one selected value
        whois.lowercaseD.setSelected(false);
        whois.miscList.setSelectedValue("as-block", true);
        assertEquals("-T as-block example.com", whois.getQuery());

        // Testcase 10: when miscList has multiple selected values
        whois.miscList.setSelectedValue("as-block", true);
        whois.miscList.setSelectedValue("aut-num", true);
        assertEquals("-T as-block,aut-num example.com", whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();

        // set some values
        whois.queryTextField.setText("example.com");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedValue("as-block", true);

        // reset the values
        whois.reset();

        // assert that all values are reset to default
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}