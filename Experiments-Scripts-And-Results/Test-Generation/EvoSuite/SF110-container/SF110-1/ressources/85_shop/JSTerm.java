package umd.cs.shop;

import java.io.*;

public class JSTerm extends JSPredicateForm
{
    private boolean isVariable;
    private boolean isConstant;
    private boolean isFunction;
    private boolean isEval;
    
    JSTerm() {
    }
    
    JSTerm(final StreamTokenizer tokenizer) {
        this.isEval = false;
        if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
            throw new JSParserError();
        }
        if (tokenizer.ttype != JSJshopVars.leftPar) {
            if (tokenizer.ttype != JSJshopVars.interrogation) {
                if (tokenizer.ttype == JSJshopVars.percent) {
                    if (!JSUtil.expectTokenType(-3, tokenizer, " Expecting word as term")) {
                        throw new JSParserError();
                    }
                    this.addElement("%" + tokenizer.sval);
                }
                else if (tokenizer.ttype == -2) {
                    this.addElement(new Double(tokenizer.nval).toString());
                }
                else {
                    if (tokenizer.ttype != -3) {
                        JSUtil.println("Line : " + tokenizer.lineno() + " Term expected");
                        throw new JSParserError();
                    }
                    tokenizer.pushBack();
                    final String varname = JSUtil.readWord(tokenizer, "Expecting constant symbol as term");
                    this.addElement(varname);
                }
                this.isVariable = false;
                this.isConstant = true;
                this.isFunction = false;
                return;
            }
            if (!JSUtil.readToken(tokenizer, "Expecting variable name in term")) {
                throw new JSParserError();
            }
            tokenizer.pushBack();
            final String varname = JSUtil.readWord(tokenizer, "Expecting variable name in term");
            this.addElement("?" + varname);
            this.isVariable = true;
            this.isConstant = false;
            this.isFunction = false;
        }
        else {
            this.isVariable = false;
            this.isConstant = false;
            this.isFunction = true;
            if (tokenizer.ttype != JSJshopVars.leftPar) {
                JSUtil.println("Line : " + tokenizer.lineno() + " Expected (");
                throw new JSParserError();
            }
            if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
                throw new JSParserError();
            }
            if (tokenizer.ttype == -3 && tokenizer.sval.equalsIgnoreCase("call")) {
                this.isEval = true;
                if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
                    throw new JSParserError();
                }
            }
            if (tokenizer.ttype != -3 || !tokenizer.sval.equalsIgnoreCase("list")) {
                if (tokenizer.ttype == JSJshopVars.lessT) {
                    if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
                        throw new JSParserError();
                    }
                    if (tokenizer.ttype == JSJshopVars.equalT) {
                        this.addElement("<=");
                    }
                    else {
                        this.addElement("<");
                        tokenizer.pushBack();
                    }
                }
                else if (tokenizer.ttype == JSJshopVars.greaterT) {
                    if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
                        throw new JSParserError();
                    }
                    if (tokenizer.ttype == JSJshopVars.equalT) {
                        this.addElement(">=");
                    }
                    else {
                        this.addElement(">");
                        tokenizer.pushBack();
                    }
                }
                else if (tokenizer.ttype == JSJshopVars.plus || tokenizer.ttype == JSJshopVars.minus || tokenizer.ttype == JSJshopVars.slash || tokenizer.ttype == JSJshopVars.astherisk) {
                    this.addElement(JSUtil.stringTokenizer(tokenizer));
                }
                else {
                    if (tokenizer.ttype != -3) {
                        JSUtil.println("Line : " + tokenizer.lineno() + " Term expected");
                        throw new JSParserError();
                    }
                    tokenizer.pushBack();
                    final String w = JSUtil.readWord(tokenizer, "JSTerm");
                    if (w.equals("%%%")) {
                        JSUtil.println("Line : " + tokenizer.lineno() + " Term expected");
                        throw new JSParserError();
                    }
                    this.addElement(w);
                }
                try {
                    tokenizer.nextToken();
                    while (tokenizer.ttype != JSJshopVars.rightPar) {
                        tokenizer.pushBack();
                        this.addElement(new JSTerm(tokenizer));
                        tokenizer.nextToken();
                    }
                }
                catch (Exception e) {
                    JSUtil.println("JSTerm: Error reading control parameters: " + e);
                    System.exit(1);
                }
                return;
            }
            if (!JSUtil.readToken(tokenizer, "Expecting Term")) {
                throw new JSParserError();
            }
            if (tokenizer.ttype == JSJshopVars.rightPar) {
                this.addElement("nil");
                this.makeConstant();
                return;
            }
            tokenizer.pushBack();
            this.addElement(".");
            this.addElement(new JSTerm(tokenizer));
            this.addElement(this.parseList(tokenizer));
        }
    }
    
    public JSTerm parseList(final StreamTokenizer tokenizer) {
        final JSTerm t = new JSTerm();
        if (!JSUtil.readToken(tokenizer, "Expecting elements of the list")) {
            throw new JSParserError();
        }
        if (tokenizer.ttype == JSJshopVars.rightPar) {
            t.addElement("nil");
            t.makeConstant();
            return t;
        }
        if (tokenizer.ttype != JSJshopVars.dot) {
            tokenizer.pushBack();
            t.makeFunction();
            t.addElement(".");
            t.addElement(new JSTerm(tokenizer));
            t.addElement(this.parseList(tokenizer));
            return t;
        }
        final JSTerm cdr = new JSTerm(tokenizer);
        if (!JSUtil.expectTokenType(JSJshopVars.rightPar, tokenizer, "Expecting ')' for term.")) {
            throw new JSParserError();
        }
        if (cdr.isVariable || cdr.elementAt(0).equalsIgnoreCase("nil") || cdr.elementAt(0).equals(".")) {
            return cdr;
        }
        t.addElement(".");
        t.addElement(cdr);
        final JSTerm r = new JSTerm();
        r.addElement("nil");
        r.makeConstant();
        t.addElement(r);
        return t;
    }
    
    private void printList(final JSTerm t) {
        final String str = "nil";
        if (str.equalsIgnoreCase(t.elementAt(0))) {
            JSUtil.print(") ");
            return;
        }
        if (t.isVariable) {
            JSUtil.print(" . ");
            t.print();
            JSUtil.print(") ");
            return;
        }
        JSTerm tt = t.elementAt(1);
        tt.print();
        JSUtil.print(" ");
        tt = t.elementAt(2);
        this.printList(tt);
    }
    
    @Override
    public void print() {
        if (this.isVariable || this.isConstant) {
            JSUtil.print(this.elementAt(0) + " ");
        }
        else {
            JSUtil.print("(");
            if (this.elementAt(0).equals(".")) {
                JSUtil.print("list ");
                this.printList(this);
                return;
            }
            if (this.isEval) {
                JSUtil.print("call ");
            }
            JSUtil.print(this.elementAt(0) + " ");
            for (short i = 1; i < this.size(); ++i) {
                final JSTerm el = this.elementAt(i);
                el.print();
            }
            JSUtil.print(")");
        }
    }
    
    public JSTerm cloneT() {
        final JSTerm nt = new JSTerm();
        nt.isEval = this.isEval;
        if (this.isConstant()) {
            nt.addElement(this.elementAt(0));
            nt.makeConstant();
            return nt;
        }
        if (this.isVariable()) {
            nt.addElement(this.elementAt(0));
            nt.makeVariable();
            return nt;
        }
        nt.addElement(this.elementAt(0));
        for (short i = 1; i < this.size(); ++i) {
            final JSTerm ti = this.elementAt(i);
            nt.addElement(ti.cloneT());
        }
        nt.makeFunction();
        return nt;
    }
    
    public JSTerm applySubstitutionT(final JSSubstitution alpha) {
        if (this.isConstant()) {
            return this.cloneT();
        }
        if (this.isVariable()) {
            final JSTerm nt = alpha.instance(this);
            return nt.cloneT();
        }
        final JSTerm nt = new JSTerm();
        nt.makeEval(this.isEval);
        nt.makeFunction();
        nt.addElement(this.elementAt(0));
        for (short i = 1; i < this.size(); ++i) {
            final JSTerm ti = this.elementAt(i);
            nt.addElement(ti.applySubstitutionT(alpha));
        }
        return nt;
    }
    
    public JSSubstitution matches(final JSTerm t) {
        return this.matches(t, new JSSubstitution());
    }
    
    public JSSubstitution matches(final JSTerm t, final JSSubstitution alpha) {
        final JSSubstitution beta = new JSSubstitution();
        if (this.isConstant()) {
            if (!t.isConstant()) {
                return t.matches(this, alpha);
            }
            if (!this.equals(t)) {
                beta.assignFailure();
            }
            return beta;
        }
        else if (this.isVariable()) {
            final JSTerm nt = alpha.instance(this);
            if (!nt.equals(this)) {
                return nt.matches(t, alpha);
            }
            beta.addElement(new JSPairVarTerm(this, t));
            return beta;
        }
        else {
            if (t.isVariable) {
                return t.matches(this);
            }
            if (!t.isFunction()) {
                beta.assignFailure();
                return beta;
            }
            if (this.size() != t.size()) {
                beta.assignFailure();
                return beta;
            }
            final String functor = this.elementAt(0);
            if (!functor.equalsIgnoreCase(t.elementAt(0))) {
                beta.assignFailure();
                return beta;
            }
            JSSubstitution gama = new JSSubstitution();
            final JSSubstitution newAlpha = (JSSubstitution)alpha.clone();
            for (short i = 1; i < this.size(); ++i) {
                final JSTerm ti = this.elementAt(i);
                if (!ti.equals(t.elementAt(i))) {
                    gama = ti.matches(t.elementAt(i), newAlpha);
                    if (gama.fail()) {
                        gama.assignFailure();
                        return gama;
                    }
                    beta.addElements(gama);
                    newAlpha.addElements(gama);
                }
            }
            return beta;
        }
    }
    
    public boolean equals(final JSTerm t) {
        if (this.isConstant()) {
            if (!t.isConstant()) {
                return false;
            }
            final String name = this.elementAt(0);
            final String name2 = t.elementAt(0);
            return name.equalsIgnoreCase(name2);
        }
        else if (this.isVariable()) {
            if (!t.isVariable()) {
                return false;
            }
            final String name = this.elementAt(0);
            final String name2 = t.elementAt(0);
            return name.equalsIgnoreCase(name2);
        }
        else {
            if (!t.isFunction()) {
                return false;
            }
            if (this.size() != t.size()) {
                return false;
            }
            final String functor = this.elementAt(0);
            final String functor2 = t.elementAt(0);
            if (!functor.equalsIgnoreCase(functor2)) {
                return false;
            }
            for (short i = 1; i < this.size(); ++i) {
                final JSTerm ti = this.elementAt(i);
                final JSTerm oti = t.elementAt(i);
                if (!ti.equals(oti)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public boolean isVariable() {
        return this.isVariable;
    }
    
    public boolean isConstant() {
        return this.isConstant;
    }
    
    public boolean isFunction() {
        return this.isFunction;
    }
    
    public boolean isEval() {
        return this.isEval;
    }
    
    public void makeFunction() {
        this.isVariable = false;
        this.isConstant = false;
        this.isFunction = true;
    }
    
    public void makeVariable() {
        this.isVariable = true;
        this.isConstant = false;
        this.isFunction = false;
    }
    
    public void makeConstant() {
        this.isVariable = false;
        this.isConstant = true;
        this.isFunction = false;
    }
    
    public void makeEval(final boolean evaluable) {
        this.isEval = evaluable;
    }
    
    @Override
    public StringBuffer toStr() {
        final StringBuffer res = new StringBuffer();
        if (this.isVariable || this.isConstant) {
            res.append(this.elementAt(0) + " ");
        }
        else {
            res.append("(");
            res.append(this.elementAt(0) + " ");
            for (short i = 1; i < this.size(); ++i) {
                final JSTerm el = this.elementAt(i);
                res.append(el.toStr());
            }
            res.append(")");
        }
        return res;
    }
    
    public boolean isGround() {
        if (this.isConstant()) {
            return true;
        }
        if (this.isVariable()) {
            return false;
        }
        for (short i = 1; i < this.size(); ++i) {
            final JSTerm ti = this.elementAt(i);
            if (!ti.isGround()) {
                return false;
            }
        }
        return true;
    }
    
    public JSTerm standardizerTerm() {
        final JSTerm nt = new JSTerm();
        nt.makeEval(this.isEval);
        if (this.isConstant()) {
            nt.addElement(this.elementAt(0));
            nt.makeConstant();
            return nt;
        }
        if (this.isVariable()) {
            nt.addElement(this.elementAt(0) + String.valueOf(JSJshopVars.VarCounter));
            nt.makeVariable();
            return nt;
        }
        nt.addElement(this.elementAt(0));
        for (short i = 1; i < this.size(); ++i) {
            final JSTerm ti = this.elementAt(i);
            nt.addElement(ti.standardizerTerm());
        }
        nt.makeFunction();
        return nt;
    }
    
    public JSTerm call() {
        if (this.isConstant()) {
            return (JSTerm)this.clone();
        }
        if (!this.isEval() && this.isVariable()) {
            return (JSTerm)this.clone();
        }
        if (this.isVariable()) {
            return new JSTerm();
        }
        final JSTerm f = new JSTerm();
        f.makeFunction();
        f.addElement(this.elementAt(0));
        for (int i = 1; i < this.size(); ++i) {
            f.addElement(this.elementAt(i).call());
        }
        if (!this.isEval()) {
            return f;
        }
        final String op = new String(f.elementAt(0));
        final JSTerm operant1 = f.elementAt(1).call();
        if (operant1.size() == 0 && !op.equalsIgnoreCase("not")) {
            return new JSTerm();
        }
        JSTerm t;
        if (JSEvaluate.OperantNum(op) > 1) {
            final JSTerm operant2 = f.elementAt(2).call();
            if (operant2.size() == 0) {
                return new JSTerm();
            }
            t = JSEvaluate.applyOperator(op, operant1, operant2);
        }
        else {
            t = JSEvaluate.applyOperatorUnary(op, operant1);
        }
        return t;
    }
}
