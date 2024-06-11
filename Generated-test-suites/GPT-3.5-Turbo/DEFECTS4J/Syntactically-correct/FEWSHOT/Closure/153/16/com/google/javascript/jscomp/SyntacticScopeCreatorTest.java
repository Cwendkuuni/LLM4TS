//test variable declaration
package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testDeclareVar() {
        SyntacticScopeCreator.RedeclarationHandler redeclarationHandler = new SyntacticScopeCreator.DefaultRedeclarationHandler();
        SyntacticScopeCreator sc = new SyntacticScopeCreator(null);
        Scope scope = new Scope(null, null);
        Node n = new Node();
        Node parent = new Node();
        Node gramps = new Node();
        JSType declaredType = new JSType();

        sc.declareVar("var1", n, parent, gramps, declaredType, n);
        assertTrue(scope.isDeclared("var1", false));

        sc.declareVar("var1", n, parent, gramps, declaredType, n);
        assertFalse(scope.isDeclared("var1", false));

        scope.declare("var2", n, declaredType, null);
        assertFalse(scope.isDeclared("var2", false));

        redeclarationHandler.onRedeclaration(scope, "var1", n, parent, gramps, n);
        redeclarationHandler.onRedeclaration(scope, "var3", n, parent, gramps, n);
    }

    @Test
    public void testCreateScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
        assertNull(sc.createScope(null, null));
    }

    @Test
    public void testScanVars() {
        SyntacticScopeCreator sc = new SyntacticScopeCreator(null);
        Node n = new Node();
        Node parent = new Node();

        sc.scanVars(n, parent);
        sc.scanVars(null, parent);
        sc.scanVars(n, null);
    }

    @Test
    public void testScanRoot() {
        SyntacticScopeCreator sc = new SyntacticScopeCreator(null);
        Node n = new Node();
        Node parent = new Node();

        sc.scanRoot(n, parent);
    }
}