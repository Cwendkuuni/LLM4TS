package lotus.core.phases;

import lotus.core.*;
import lotus.core.effect.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PhaseTest {

    private Phase phase;
    private ChangePhase effect;

    @Before
    public void setup() {
        phase = Mockito.spy(Phase.class);
        effect = Mockito.mock(ChangePhase.class);
    }

    @Test
    public void testDoPhase() {
        // TODO: add test cases to test the doPhase method
    }

    @Test
    public void testNextPhase() {
        // TODO: add test cases to cover each condition in the nextPhase method
    }
}