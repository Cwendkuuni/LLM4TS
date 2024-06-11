package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSListLogicalAtoms expectedState = new JSListLogicalAtoms();

        JSListLogicalAtoms e1 = new JSListLogicalAtoms();
        JSListLogicalAtoms e2 = new JSListLogicalAtoms();

        s.addElement(e1);
        state.addElementsToState(s);
        expectedState.addElement(e1);
        assertEquals(expectedState, state);

        s.addElement(e2);
        state.addElementsToState(s);
        expectedState.addElement(e2);
        assertEquals(expectedState, state);
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState ns = new JSState();
        JSState state = new JSState();
        JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
        assertNull(ns.apply(pl));
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSListLogicalAtoms add = new JSListLogicalAtoms();
        JSListLogicalAtoms del = new JSListLogicalAtoms();
        JSListLogicalAtoms opAddL = new JSListLogicalAtoms();
        JSListLogicalAtoms opDelL = new JSListLogicalAtoms();
        JSState ns = new JSState();
        JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
        JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedNAddL = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedNDelL = new JSListLogicalAtoms();

        op.setAddList(add);
        op.setDeleteList(del);
        add.applySubstitutionListLogicalAtoms(alpha);
        del.applySubstitutionListLogicalAtoms(alpha);

        ns.applyOp(op, alpha, addL, delL);
        assertEquals(expectedNAddL, nAddL);
        assertEquals(expectedNDelL, nDelL);
        assertEquals(ns, new JSTState(ns, nAddL, nDelL));
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expectedAnswer = new JSListSubstitution();

        JSListSubstitution satisfiers = axioms.TheoremProver(conds, this, alpha, false);

        JSSubstitution answer;
        if (satisfiers.fail()) {
            answer = new JSSubstitution();
            answer.assignFailure();
        } else {
            answer = (JSSubstitution) alpha.clone();
            answer.addElements(satisfiers.elementAt(0));
        }

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expectedAnswers = new JSListSubstitution();
        String first = conds.Label();
        boolean findfirst = !first.equalsIgnoreCase("first");

        JSListSubstitution satisfiers = axioms.TheoremProver(conds, this, alpha, findfirst);

        JSListSubstitution answers = new JSListSubstitution();
        for (int i = 0; i < satisfiers.size(); i++) {
            JSSubstitution beta = (JSSubstitution)satisfiers.elementAt(i).clone();
            beta.addElements((JSSubstitution)alpha.clone());
            answers.addElement(beta);
        }

        assertEquals(expectedAnswers, answers);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution expectedAnswers = new JSListSubstitution();
        JSListSubstitution answers = new JSListSubstitution();

        for (int i = 0; i < this.size(); i++) {
            JSPredicateForm el = this.elementAt(i);
            JSSubstitution subs = t.matches(el, alpha);
            if (!subs.fail()) {
                answers.addElement(subs);
            }
        }

        assertEquals(expectedAnswers, answers);
    }
}