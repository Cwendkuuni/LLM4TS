package lotus.core.phases;

import static org.junit.Assert.*;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void testChangePhase() {
        // Test case 1: currentPhase is an instance of UntapPhase
        // Expected result: Game.currentPhase becomes an instance of UpkeepPhase
        //                  and ChangePhase effect is resolved
        Phase.changePhase(new UntapPhase());
        assertTrue(Game.currentPhase instanceof UpkeepPhase);
        // Add assertions for the resolved effect

        // Test case 2: currentPhase is an instance of UpkeepPhase
        // Expected result: Game.currentPhase becomes an instance of DrawPhase
        //                  and ChangePhase effect is resolved

        // Test case 3: currentPhase is an instance of DrawPhase
        // Expected result: Game.currentPhase becomes an instance of Main1Phase
        //                  and ChangePhase effect is resolved

        // Test case 4: currentPhase is an instance of Main1Phase
        // Expected result: Game.currentPhase becomes an instance of CombatBeginningPhase
        //                  and ChangePhase effect is resolved
        
        // ... continue with test cases for the other conditions
        
    }

    @Test
    public void testNextPhase() {
        // Test case 1: currentPhase is an instance of UntapPhase
        // Expected result: Game.currentPhase becomes an instance of UpkeepPhase
        //                  and ChangePhase effect is resolved
        Phase.nextPhase();
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        // Test case 2: currentPhase is an instance of UpkeepPhase
        // Expected result: Game.currentPhase becomes an instance of DrawPhase
        //                  and ChangePhase effect is resolved
        
        // Test case 3: currentPhase is an instance of DrawPhase
        // Expected result: Game.currentPhase becomes an instance of Main1Phase
        //                  and ChangePhase effect is resolved

        // Test case 4: currentPhase is an instance of Main1Phase
        // Expected result: Game.currentPhase becomes an instance of CombatBeginningPhase
        //                  and ChangePhase effect is resolved
        
        // ... continue with test cases for the other conditions
        
    }
}