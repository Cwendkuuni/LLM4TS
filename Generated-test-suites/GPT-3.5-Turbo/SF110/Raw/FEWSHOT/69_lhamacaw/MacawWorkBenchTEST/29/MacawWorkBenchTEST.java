package macaw.presentationLayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.ProductionCurationService;
import macaw.businessLayer.ProductionRetrievalService;
import macaw.businessLayer.SessionProperties;
import macaw.businessLayer.StartupOptions;
import macaw.businessLayer.User;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.system.MacawMessages;
import macaw.system.UserInterfaceFactory;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;

    @Before
    public void setUp() {
        sessionProperties = mock(SessionProperties.class);
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testMacawWorkBench() {
        assertEquals(sessionProperties, macawWorkBench.sessionProperties);
        assertEquals(300, macawWorkBench.dialog.getWidth());
        assertEquals(300, macawWorkBench.dialog.getHeight());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Write additional tests as needed
    }

    @Test
    public void testShow() {
        // Use a GUI testing tool like Selenium or TestFX to test UI visibility
    }

    @Test
    public void testEditConstants() {
        // Use a mocking tool like Mockito to mock dependencies and verify interactions
    }

    @Test
    public void testEditVariables() {
        // Use a mocking tool like Mockito to mock dependencies and verify interactions
    }

    @Test
    public void testExportVariableData() {
        // Use a mocking tool like Mockito to mock dependencies and verify interactions
    }

    @Test
    public void testExit() {
        // Use a mocking tool like Mockito to mock dependencies and verify interactions
    }

    @Test
    public void testActionPerformed() {
        // Use a mocking tool like Mockito to mock dependencies and verify interactions
    }
}