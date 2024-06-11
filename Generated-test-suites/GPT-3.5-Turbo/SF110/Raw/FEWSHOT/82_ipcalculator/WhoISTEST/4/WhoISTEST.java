package ipac;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WhoISTest {
    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @After
    public void tearDown() {
        whoIS = null;
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        String expected = "No Query Detected";
        whoIS.startQuery();
        assertEquals(expected, whoIS.displayError);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        assertNull(whoIS.displayError);
    }

    @Test
    public void testReset() {
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}