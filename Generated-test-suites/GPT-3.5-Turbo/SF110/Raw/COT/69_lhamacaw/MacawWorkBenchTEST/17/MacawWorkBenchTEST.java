
package macaw.presentationLayer;

import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {

    @Test
    public void testMain() {
        // basic functionality test
        MacawWorkBench.main(new String[]{"arg1", "arg2"});
    }

    @Test
    public void testMacawWorkBench() {
        // basic object creation test
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench);
    }

    @Test
    public void testShow() {
        // basic show test
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.show();
    }

    @Test
    public void testActionPerformed() {
        // mock an ActionEvent and test each button action
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        ActionEvent event = new ActionEvent(macawWorkBench.editConstants, 0, "");
        macawWorkBench.actionPerformed(event);
        event = new ActionEvent(macawWorkBench.editVariables, 0, "");
        macawWorkBench.actionPerformed(event);
        event = new ActionEvent(macawWorkBench.exportVariableData, 0, "");
        macawWorkBench.actionPerformed(event);
        event = new ActionEvent(macawWorkBench.exit, 0, "");
        macawWorkBench.actionPerformed(event);
    }

    @Test(expected = NullPointerException.class)
    public void testMainWithNullArguments() {
        // test main() with null arguments for dbUser and dbPassword
        MacawWorkBench.main(new String[]{null, null});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMacawWorkBenchWithNullSessionProperties() {
        // test MacawWorkBench() with null SessionProperties parameter
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformedWithNullEvent() {
        // test actionPerformed() with null ActionEvent parameter
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.actionPerformed(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActionPerformedWithUnknownButton() {
        // test actionPerformed() with ActionEvent from unknown button
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent event = new ActionEvent(new JButton(), 0, "");
        macawWorkBench.actionPerformed(event);
    }
}