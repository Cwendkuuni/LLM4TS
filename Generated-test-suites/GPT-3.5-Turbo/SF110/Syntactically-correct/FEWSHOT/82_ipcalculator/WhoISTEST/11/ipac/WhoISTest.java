package ipac;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQuery_noQueryDetected_displayError() {
        whois.queryTextField.setText("");
        whois.startQuery();
        assertEquals("No Query Detected", whois.errorMsg);
    }

    @Test
    public void testStartQuery_validQuery_queryMethodCalled() {
        whois.queryTextField.setText("example");
        whois.startQuery();
        assertTrue(whois.queryMethodCalled);
    }

    @Test
    public void testReset_resetCalled() {
        whois.resetButton.doClick();
        assertTrue(whois.resetMethodCalled);
    }

    @Test
    public void testGetQuery_defaultOptions_queryStringReturned() {
        whois.queryTextField.setText("example");
        String expected = "example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_inverseQueries_selectedItemsAppendedToQueryString() {
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedItem("admin-c");
        String expected = "-i admin-c example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_lowercaseLSelected_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        String expected = "-l example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_uppercaseLSelected_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.uppercaseL.setSelected(true);
        String expected = "-L example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_lowercaseMSelected_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.lowercaseM.setSelected(true);
        String expected = "-m example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_uppercaseMSelected_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.uppercaseM.setSelected(true);
        String expected = "-M example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_lowercaseXSelected_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.lowercaseX.setSelected(true);
        String expected = "-x example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_IPAddressLookups_booleanDTrue_queryStringPrefixAppended() {
        whois.queryTextField.setText("example");
        whois.booleanD = true;
        String expected = "-d example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_MiscellaneousQueries_selectedItemsAppendedToQueryString() {
        whois.queryTextField.setText("example");
        whois.miscList.setSelectedValue("as-block", true);
        whois.miscList.setSelectedValue("domain", true);
        String expected = "-T as-block,domain example";
        String actual = whois.getQuery();
        assertEquals(expected, actual);
    }
}