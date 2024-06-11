package umd.cs.shop;

import java.io.*;

public class JSState extends JSListLogicalAtoms
{
    private String label;
    
    public JSState() {
    }
    
    public JSState(final StreamTokenizer tokenizer) {
        super(tokenizer);
    }
    
    public void addElementsToState(final JSListLogicalAtoms s) {
        int j = 0;
        for (int i = 0; i < s.size(); ++i) {
            if (!this.contains(s.elementAt(i))) {
                this.insertElementAt(s.elementAt(i), j);
                ++j;
            }
        }
    }
    
    JSState apply(final JSPlan pl) {
        final JSState ns = new JSState();
        JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
        return ns;
    }
    
    JSTState applyOp(final JSOperator op, final JSSubstitution alpha, final JSListLogicalAtoms addL, final JSListLogicalAtoms delL) {
        final JSListLogicalAtoms add = op.addList();
        final JSListLogicalAtoms del = op.deleteList();
        final JSListLogicalAtoms opAddL = add.applySubstitutionListLogicalAtoms(alpha);
        final JSListLogicalAtoms opDelL = del.applySubstitutionListLogicalAtoms(alpha);
        final JSState ns = new JSState();
        final JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
        final JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
        if (JSJshopVars.flagLevel > 3) {
            JSUtil.print(" Matching Operator : (");
            JSUtil.print(":Operator ");
            op.head().applySubstitutionTA(alpha).print();
            JSUtil.print("-: ");
            opDelL.print();
            JSUtil.print("+: ");
            opAddL.print();
            JSUtil.println(")");
        }
        for (short i = 0; i < this.size(); ++i) {
            final JSPredicateForm el = this.elementAt(i);
            if (!opDelL.contains(el)) {
                ns.addElement(el);
            }
        }
        ns.addElementsToState(opAddL);
        for (short i = 0; i < addL.size(); ++i) {
            final JSPredicateForm el = addL.elementAt(i);
            if (!opDelL.contains(el)) {
                nAddL.addElement(el);
            }
        }
        nAddL.addElements(opAddL);
        for (short i = 0; i < delL.size(); ++i) {
            final JSPredicateForm el = delL.elementAt(i);
            if (!opAddL.contains(el)) {
                nDelL.addElement(el);
            }
        }
        nDelL.addElements(opDelL);
        if (JSJshopVars.flagLevel > 3) {
            JSUtil.println("The resulting state :");
            ns.print();
            JSUtil.println("-----------");
        }
        return new JSTState(ns, nAddL, nDelL);
    }
    
    public JSSubstitution satisfies(final JSListLogicalAtoms conds, final JSSubstitution alpha, final JSListAxioms axioms) {
        final JSListSubstitution satisfiers = axioms.TheoremProver(conds, this, alpha, false);
        JSSubstitution answer;
        if (satisfiers.fail()) {
            answer = new JSSubstitution();
            answer.assignFailure();
        }
        else {
            answer = (JSSubstitution)alpha.clone();
            answer.addElements(satisfiers.elementAt(0));
        }
        return answer;
    }
    
    public JSListSubstitution satisfiesAll(final JSListLogicalAtoms conds, final JSSubstitution alpha, final JSListAxioms axioms) {
        final String first = conds.Label();
        final boolean findfirst = !first.equalsIgnoreCase("first");
        final JSListSubstitution answers = new JSListSubstitution();
        final JSListSubstitution satisfiers = axioms.TheoremProver(conds, this, alpha, findfirst);
        for (int i = 0; i < satisfiers.size(); ++i) {
            final JSSubstitution beta = (JSSubstitution)satisfiers.elementAt(i).clone();
            beta.addElements((JSSubstitution)alpha.clone());
            answers.addElement(beta);
        }
        return answers;
    }
    
    public JSListSubstitution satisfiesTAm(final JSPredicateForm t, final JSSubstitution alpha) {
        final JSListSubstitution answers = new JSListSubstitution();
        if (JSJshopVars.flagLevel > 7) {
            System.out.println(" ");
        }
        for (int i = 0; i < this.size(); ++i) {
            final JSPredicateForm el = this.elementAt(i);
            final JSSubstitution subs = t.matches(el, alpha);
            if (!subs.fail()) {
                if (JSJshopVars.flagLevel > 7) {
                    JSUtil.println(" Goal matches atom: ");
                    el.print();
                }
                answers.addElement(subs);
            }
        }
        if (JSJshopVars.flagLevel > 7 && answers.isEmpty()) {
            JSUtil.println(" Goal does not match any atom.");
        }
        return answers;
    }
}
