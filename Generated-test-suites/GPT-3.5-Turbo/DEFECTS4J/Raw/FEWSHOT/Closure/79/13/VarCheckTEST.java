import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VarCheckTest {
    
    @Test
    public void testVisit() {
        // Test variable not declared
        VarCheck varCheck = new VarCheck(null);
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.NAME, "myVariable");
        Node parent = new Node(Token.VAR);
        varCheck.visit(t, n, parent);

        // Test variable declared in the same file
        VarCheck varCheck2 = new VarCheck(null);
        NodeTraversal t2 = new NodeTraversal(null, null, null);
        Node n2 = new Node(Token.NAME, "myVariable");
        Node parent2 = new Node(Token.VAR);
        Scope scope2 = new Scope(null);
        Scope.Var var2 = new Scope.Var("myVariable", parent2, scope2);
        scope2.declare("myVariable", parent2, null, null);
        t2.pushScope(scope2);
        varCheck2.visit(t2, n2, parent2);

        // Test variable declared in another file
        VarCheck varCheck3 = new VarCheck(null);
        NodeTraversal t3 = new NodeTraversal(null, null, null);
        Node n3 = new Node(Token.NAME, "myVariable");
        Node parent3 = new Node(Token.VAR);
        Scope scope3 = new Scope(null);
        Scope.Var var3 = new Scope.Var("myVariable", parent3, scope3);
        CompilerInput currInput = new CompilerInput(null);
        CompilerInput varInput = new CompilerInput(null);
        varInput.setModule(new JSModule("module1"));
        scope3.declare("myVariable", parent3, null, varInput);
        t3.pushScope(scope3);
        varCheck3.visit(t3, n3, parent3);
        
        // Test variable reference between modules with declared dependency
        VarCheck varCheck4 = new VarCheck(null);
        JSModule currModule = new JSModule("module2");
        CompilerInput currInput2 = new CompilerInput(null);
        currInput2.setModule(currModule);
        NodeTraversal t4 = new NodeTraversal(currInput2, null, null);
        Node n4 = new Node(Token.NAME, "myVariable");
        Node parent4 = new Node(Token.VAR);
        Scope scope4 = new Scope(null);
        Scope.Var var4 = new Scope.Var("myVariable", parent4, scope4);
        CompilerInput varInput2 = new CompilerInput(null);
        varInput2.setModule(new JSModule("module1"));
        scope4.declare("myVariable", parent4, null, varInput2);
        t4.pushScope(scope4);
        varCheck4.visit(t4, n4, parent4);

        // Test variable reference between modules with no declared dependency
        VarCheck varCheck5 = new VarCheck(null);
        JSModule currModule2 = new JSModule("module2");
        CompilerInput currInput3 = new CompilerInput(null);
        currInput3.setModule(currModule2);
        NodeTraversal t5 = new NodeTraversal(currInput3, null, null);
        Node n5 = new Node(Token.NAME, "myVariable");
        Node parent5 = new Node(Token.VAR);
        Scope scope5 = new Scope(null);
        Scope.Var var5 = new Scope.Var("myVariable", parent5, scope5);
        CompilerInput varInput3 = new CompilerInput(null);
        varInput3.setModule(new JSModule("module1"));
        scope5.declare("myVariable", parent5, null, varInput3);
        t5.pushScope(scope5);
        varCheck5.visit(t5, n5, parent5);
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        VarCheck varCheck = new VarCheck(null);
        varCheck.createSynthesizedExternVar("myVar");
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        VarCheck varCheck = new VarCheck(null);
        varCheck.getSynthesizedExternsInput();
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        VarCheck varCheck = new VarCheck(null);
        varCheck.getSynthesizedExternsRoot();
    }
}