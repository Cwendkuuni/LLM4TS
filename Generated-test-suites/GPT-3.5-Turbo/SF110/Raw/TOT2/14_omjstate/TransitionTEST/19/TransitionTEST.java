package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TransitionTest {

    @Test
    public void testAddGuardCondition1() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new Predicate());
        assertFalse(transition.getGuardConditions().isEmpty());
    }

    @Test
    public void testAddGuardCondition2() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new GuardCondition());
        assertFalse(transition.getGuardConditions().isEmpty());
    }

    // Add more test cases for other methods

}