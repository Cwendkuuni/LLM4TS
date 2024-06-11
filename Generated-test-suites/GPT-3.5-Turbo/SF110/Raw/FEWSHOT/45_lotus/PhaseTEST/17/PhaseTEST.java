package lotus.core.phases;

import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhaseTest {

    @Mock
    private Game game;

    @Mock
    private Phase phase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPhase() {
        phase.doPhase();
        // TODO: Add assertions for the behavior of the doPhase() method
    }

    @Test
    public void testChangePhase() {
        Phase p = mock(Phase.class);
        ChangePhase effect = new ChangePhase(p);

        phase.changePhase(p);

        assertEquals(p, Game.currentPhase);
        verify(effect).resolve();
    }

    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        phase.nextPhase();
        // TODO: Add assertions for the behavior of the nextPhase() method
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsUntapPhase() {
        Game.currentPhase = new UntapPhase();

        phase.nextPhase();

        assertEquals(new UpkeepPhase(), Game.currentPhase);
        verify(phase).changePhase(any(UpkeepPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsUpkeepPhase() {
        Game.currentPhase = new UpkeepPhase();

        phase.nextPhase();

        assertEquals(new DrawPhase(), Game.currentPhase);
        verify(phase).changePhase(any(DrawPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsDrawPhase() {
        Game.currentPhase = new DrawPhase();

        phase.nextPhase();

        assertEquals(new Main1Phase(), Game.currentPhase);
        verify(phase).changePhase(any(Main1Phase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain1Phase() {
        Game.currentPhase = new Main1Phase();

        phase.nextPhase();

        assertEquals(new CombatBeginningPhase(), Game.currentPhase);
        verify(phase).changePhase(any(CombatBeginningPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatBeginningPhase() {
        Game.currentPhase = new CombatBeginningPhase();

        phase.nextPhase();

        assertEquals(new DeclareAttackersPhase(), Game.currentPhase);
        verify(phase).changePhase(any(DeclareAttackersPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareAttackersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();

        phase.nextPhase();

        assertEquals(new DeclareBlockersPhase(), Game.currentPhase);
        verify(phase).changePhase(any(DeclareBlockersPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsDeclareBlockersPhase() {
        Game.currentPhase = new DeclareBlockersPhase();

        phase.nextPhase();

        assertEquals(new CombatDamagePhase(), Game.currentPhase);
        verify(phase).changePhase(any(CombatDamagePhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatDamagePhase() {
        Game.currentPhase = new CombatDamagePhase();

        phase.nextPhase();

        assertEquals(new CombatEndPhase(), Game.currentPhase);
        verify(phase).changePhase(any(CombatEndPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsCombatEndPhase() {
        Game.currentPhase = new CombatEndPhase();

        phase.nextPhase();

        assertEquals(new Main2Phase(), Game.currentPhase);
        verify(phase).changePhase(any(Main2Phase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsMain2Phase() {
        Game.currentPhase = new Main2Phase();

        phase.nextPhase();

        assertEquals(new EndOfTurnPhase(), Game.currentPhase);
        verify(phase).changePhase(any(EndOfTurnPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsEndOfTurnPhase() {
        Game.currentPhase = new EndOfTurnPhase();

        phase.nextPhase();

        assertEquals(new CleanupPhase(), Game.currentPhase);
        verify(phase).changePhase(any(CleanupPhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsCleanupPhase() {
        Game.currentPhase = new CleanupPhase();

        phase.nextPhase();

        assertEquals(new PlayerChangePhase(), Game.currentPhase);
        verify(phase).changePhase(any(PlayerChangePhase.class));
    }

    @Test
    public void testNextPhase_WhenCurrentPhaseIsPlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();

        phase.nextPhase();

        assertEquals(new UntapPhase(), Game.currentPhase);
        verify(phase).changePhase(any(UntapPhase.class));
    }
}