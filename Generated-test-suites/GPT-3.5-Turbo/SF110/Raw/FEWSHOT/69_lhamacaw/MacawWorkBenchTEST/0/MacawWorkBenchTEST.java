package macaw.presentationLayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {
    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;
    private JDialog dialog;
    private JButton editConstants;
    private JButton editVariables;
    private JButton exportVariableData;
    private JButton exit;
    
    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }
    
    @Test
    public void testMain() {
        // Test the main() method
    }
    
    @Test
    public void testMacawWorkBench() {
        assertNotNull(macawWorkBench);
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }
    
    @Test
    public void testCreateButtonPanel() {
        JPanel panel = macawWorkBench.createButtonPanel(sessionProperties.getUserInterfaceFactory());
        assertNotNull(panel);
        assertNotNull(panel.getComponent(0));
        assertNotNull(panel.getComponent(1));
        assertNotNull(panel.getComponent(2));
        assertNotNull(panel.getComponent(3));
    }
    
    @Test
    public void testShow() {
        macawWorkBench.show();
        assertTrue(macawWorkBench.dialog.isVisible());
    }
    
    @Test
    public void testEditConstants() {
        macawWorkBench.editConstants();
        // Test the editConstants method
    }
    
    @Test
    public void testEditVariables() {
        macawWorkBench.editVariables();
        // Test the editVariables method
    }
    
    @Test
    public void testExportVariableData() {
        macawWorkBench.exportVariableData();
        // Test the exportVariableData method
    }
    
    @Test
    public void testExit() {
        macawWorkBench.exit();
        // Test the exit method
    }
    
    @Test
    public void testActionPerformed() {
        // Test the actionPerformed method
    }
}