package ipac;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanel() {
        JPanel queryPanel = whois.queryPanel();

        assertNotNull(queryPanel);

        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        JButton queryButton = (JButton) queryPanel.getComponent(2);
        JButton resetButton = (JButton) queryPanel.getComponent(3);

        assertNotNull(queryTextField);
        assertNotNull(queryButton);
        assertNotNull(resetButton);
        assertEquals(40, queryTextField.getColumns());
        assertTrue(queryTextField.isRequestFocusEnabled());
        assertEquals("Enter Whois database object you would like to query", queryTextField.getToolTipText());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        assertNotNull(additionalQueriesPanel);

        JPanel ipAddressLookupsPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        JPanel miscellaneousQueriesPanel = (JPanel) additionalQueriesPanel.getComponent(1);

        assertNotNull(ipAddressLookupsPanel);
        assertNotNull(miscellaneousQueriesPanel);
    }

    @Test
    public void testIPAddressLookups() {
        JPanel ipAddressLookupsPanel = whois.IPAddressLookups();

        assertNotNull(ipAddressLookupsPanel);
        assertFalse(whois.booleanD);

        JRadioButton lowercaseL = (JRadioButton) ipAddressLookupsPanel.getComponent(0);
        JRadioButton uppercaseL = (JRadioButton) ipAddressLookupsPanel.getComponent(1);
        JRadioButton lowercaseM = (JRadioButton) ipAddressLookupsPanel.getComponent(2);
        JRadioButton uppercaseM = (JRadioButton) ipAddressLookupsPanel.getComponent(3);
        JRadioButton lowercaseX = (JRadioButton) ipAddressLookupsPanel.getComponent(4);
        JCheckBox lowercaseD = (JCheckBox) ipAddressLookupsPanel.getComponent(5);

        assertNotNull(lowercaseL);
        assertNotNull(uppercaseL);
        assertNotNull(lowercaseM);
        assertNotNull(uppercaseM);
        assertNotNull(lowercaseX);
        assertNotNull(lowercaseD);
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel miscellaneousQueriesPanel = whois.MiscellaneousQueries();

        assertNotNull(miscellaneousQueriesPanel);

        JComboBox inverseQueries = (JComboBox) miscellaneousQueriesPanel.getComponent(1);
        JList miscList = (JList) miscellaneousQueriesPanel.getComponent(3);

        assertNotNull(inverseQueries);
        assertNotNull(miscList);
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel queryHintsPanel = whois.queryHintsPanel();

        assertNotNull(queryHintsPanel);
    }

    @Test
    public void testStartQuery() {
        whois.queryTextField.setText("exampleQuery");
        whois.startQuery();
    }

    @Test
    public void testReset() {
        whois.reset();

        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testGetQuery() {
        whois.queryTextField.setText("exampleQuery");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedIndex(2);

        String query = whois.getQuery();

        assertEquals("-i admin-c exampleQuery", query);
    }
}