
package macaw.presentationLayer;

import org.junit.*;
import static org.junit.Assert.*;
import macaw.system.*;

public class MacawWorkBenchTest {

    @Test
    public void testShow() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.show();
    }
    
    @Test
    public void testActionPerformed() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent event = new ActionEvent(macawWorkBench.editConstants, 0, "");
        macawWorkBench.actionPerformed(event);
        
        event = new ActionEvent(macawWorkBench.editVariables, 0, "");
        macawWorkBench.actionPerformed(event);
        
        event = new ActionEvent(macawWorkBench.exportVariableData, 0, "");
        macawWorkBench.actionPerformed(event);
        
        event = new ActionEvent(macawWorkBench.exit, 0, "");
        macawWorkBench.actionPerformed(event);
    }

}
