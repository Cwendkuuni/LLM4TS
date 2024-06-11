package lotus.core.phases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.effect.UntapPhase;
import lotus.core.effect.UpkeepPhase;
import lotus.core.effect.DrawPhase;
import lotus.core.effect.Main1Phase;
import lotus.core.effect.CombatBeginningPhase;
import lotus.core.effect.DeclareAttackersPhase;
import lotus.core.effect.DeclareBlockersPhase;
import lotus.core.effect.CombatDamagePhase;
import lotus.core.effect.CombatEndPhase;
import lotus.core.effect.Main2Phase;
import lotus.core.effect.EndOfTurnPhase;
import lotus.core.effect.CleanupPhase;
import lotus.core.effect.PlayerChangePhase;

public class PhaseTest {

    @Before
    public void setup() {
        Game.currentPhase = Mockito.mock(Phase.class);
        Game.playingPlayer = 1;
    }

    @Test
    public void testDoPhase() {
        Phase phase = new UntapPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new UpkeepPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new DrawPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new Main1Phase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new CombatBeginningPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new DeclareAttackersPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new DeclareBlockersPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new CombatDamagePhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new CombatEndPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new Main2Phase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new EndOfTurnPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new CleanupPhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        phase = new PlayerChangePhase();
        phase.doPhase();
        Mockito.verify(Game.currentPhase).doPhase();
    }

    @Test
    public void testNextPhase() {
        Phase.phaseChange(new UntapPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new UpkeepPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new DrawPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new Main1Phase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new CombatBeginningPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new DeclareAttackersPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new DeclareBlockersPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new CombatDamagePhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new CombatEndPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new Main2Phase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new EndOfTurnPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new CleanupPhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();

        Phase.phaseChange(new PlayerChangePhase());
        Phase.nextPhase();
        Mockito.verify(Game.currentPhase).doPhase();
    }

    private void phaseChange(Phase phase) {
        Mockito.when(Game.currentPhase instanceof Phase).thenReturn(true);
        Mockito.when(Game.currentPhase).thenReturn(phase);
    }
}