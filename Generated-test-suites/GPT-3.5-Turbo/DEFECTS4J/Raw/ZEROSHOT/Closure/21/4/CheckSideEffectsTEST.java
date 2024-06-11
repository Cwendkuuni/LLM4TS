import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, false);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null);
        Node node = new Node(Token.ADD);
        NodeTraversal.NodeHandler handler = new NodeTraversal.NodeHandler();
        Node parent = new Node(Token.EXPR_RESULT);
        checkSideEffects.visit(t, node, parent);
      
        // Assertions
        assertEquals(handler, t.getCompiler().report(t.makeError(node, CheckSideEffects.USELESS_CODE_ERROR, "The result of the 'add' operator is not being used.")));
        assertEquals(handler, t.getCompiler().report(t.makeError(node, CheckSideEffects.USELESS_CODE_ERROR, "This code lacks side-effects. Is there a bug?")));
    }
    
    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
        checkSideEffects.problemNodes.add(new Node(Token.ADD));

        // Adding extern
        Node name = new Node(Token.NAME, CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node var = new Node(Token.VAR);
        var.addChildToFront(name);
        var.setJSDocInfo(JSDocInfoBuilder.recordNoAlias().build(var));
        checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler).addChildrenToBack(var);
        checkSideEffects.compiler.reportCodeChange();

        // Protecting side-effects
        Node problemNode = checkSideEffects.problemNodes.get(0);
        Node name = new Node(Token.NAME, CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node replacement = new Node(Token.CALL, name);
        replacement.putBooleanProp(Node.FREE_CALL, true);
        problemNode.getParent().replaceChild(problemNode, replacement);
        replacement.addChildToBack(problemNode);
        checkSideEffects.compiler.reportCodeChange();

        // Assertions
        assertEquals(1, checkSideEffects.problemNodes.size());
    }

    @Test
    public void testAddExtern() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);

        // Adding extern
        Node name = new Node(Token.NAME, CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node var = new Node(Token.VAR);
        var.addChildToFront(name);
        var.setJSDocInfo(JSDocInfoBuilder.recordNoAlias().build(var));
        checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler).addChildrenToBack(var);
        checkSideEffects.compiler.reportCodeChange();

        // Assertions
        assertEquals(1, checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler).getChildCount());
        assertEquals(var, checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler).getChildAtIndex(0));
    }

    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(new AbstractCompiler());

        // Stripping protection
        Node root = new Node(Token.ROOT);
        Node externs = new Node(Token.BLOCK);
        Node script = new Node(Token.BLOCK);
        FunctionNode functionNode = new FunctionNode(new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));

        Node name = new Node(Token.NAME, CheckSideEffects.PROTECTOR_FN);
        Node expr = new Node(Token.EXPR_RESULT, name);
        functionNode.getBody().addChildToFront(expr);
        script.addChildToFront(functionNode);
        root.addChildToFront(externs);
        root.addChildToBack(script);

        stripProtection.process(externs, root);

        // Assertions
        assertEquals(0, externs.getChildCount());
    }

}