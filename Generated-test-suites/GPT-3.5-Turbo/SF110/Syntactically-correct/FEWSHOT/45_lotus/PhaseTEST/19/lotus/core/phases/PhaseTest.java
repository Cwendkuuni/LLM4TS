package lotus.core.phases;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PhaseTest {

    @Test
    public void testDoPhase() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // Implement test logic here
            }
        };
        
        phase.doPhase();
        // Add assertions here
    }
    
    @Test
    public void testNextPhase() {
        UntapPhase untapPhase = Mockito.mock(UntapPhase.class);
        UpkeepPhase upkeepPhase = Mockito.mock(UpkeepPhase.class);
        DrawPhase drawPhase = Mockito.mock(DrawPhase.class);
        Main1Phase main1Phase = Mockito.mock(Main1Phase.class);
        CombatBeginningPhase combatBeginningPhase = Mockito.mock(CombatBeginningPhase.class);
        DeclareAttackersPhase declareAttackersPhase = Mockito.mock(DeclareAttackersPhase.class);
        DeclareBlockersPhase declareBlockersPhase = Mockito.mock(DeclareBlockersPhase.class);
        CombatDamagePhase combatDamagePhase = Mockito.mock(CombatDamagePhase.class);
        CombatEndPhase combatEndPhase = Mockito.mock(CombatEndPhase.class);
        Main2Phase main2Phase = Mockito.mock(Main2Phase.class);
        EndOfTurnPhase endOfTurnPhase = Mockito.mock(EndOfTurnPhase.class);
        CleanupPhase cleanupPhase = Mockito.mock(CleanupPhase.class);
        PlayerChangePhase playerChangePhase = Mockito.mock(PlayerChangePhase.class);
        
        Game.currentPhase = untapPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(upkeepPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = upkeepPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(drawPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = drawPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(main1Phase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = main1Phase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(combatBeginningPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = combatBeginningPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(declareAttackersPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = declareAttackersPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(declareBlockersPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = declareBlockersPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(combatDamagePhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = combatDamagePhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(combatEndPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = combatEndPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(main2Phase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = main2Phase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(endOfTurnPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = endOfTurnPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(cleanupPhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = cleanupPhase;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(playerChangePhase.getClass(), Game.currentPhase.getClass());
        
        Game.currentPhase = playerChangePhase;
        Game.playingPlayer = 1;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(untapPhase.getClass(), Game.currentPhase.getClass());
        assertEquals(2, Game.playingPlayer);
        
        Game.currentPhase = playerChangePhase;
        Game.playingPlayer = 2;
        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(untapPhase.getClass(), Game.currentPhase.getClass());
        assertEquals(1, Game.playingPlayer);
    }
}