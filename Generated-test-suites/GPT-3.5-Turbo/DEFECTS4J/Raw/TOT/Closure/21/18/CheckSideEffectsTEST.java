import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Typical use case
    // Test the process() method with a simple JavaScript code that has a side effect
    // Expect the warning message to be generated for the suspicious code
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new AbstractCompiler(), CheckLevel.WARNING, false);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node code = new Node(Token.SCRIPT);
    code.addChildToFront(new Node(Token.VAR, new Node(Token.NAME, IR.string("s")), new Node(Token.STRING, IR.string("this string is "))));

    scriptRoot.addChildToBack(code);
    root.addChildToBack(scriptRoot);

    checkSideEffects.process(externs, root);

    Assert.assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    Assert.assertEquals("Variable declaration with string concatenation without '+'",
        problemNode.getJSDocInfo().getOriginalCommentString());
  }

  @Test
  public void testHotSwapScript() {
    // Typical use case
    // Test the hotSwapScript() method with a simple JavaScript code that has a side effect
    // Expect the warning message to be generated for the suspicious code
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new AbstractCompiler(), CheckLevel.WARNING, false);
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node code = new Node(Token.SCRIPT);
    code.addChildToFront(new Node(Token.VAR, new Node(Token.NAME, IR.string("s")), new Node(Token.STRING, IR.string("this string is "))));

    scriptRoot.addChildToBack(code);

    checkSideEffects.hotSwapScript(scriptRoot, scriptRoot);

    Assert.assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    Assert.assertEquals("Variable declaration with string concatenation without '+'",
        problemNode.getJSDocInfo().getOriginalCommentString());
  }

  @Test
  public void testVisit() {
    // Edge case
    // Test the visit() method with a non-side effect JavaScript code
    // Expect no warning message to be generated
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new AbstractCompiler(), CheckLevel.WARNING, false);
    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        // Non-side effect code
        Node code = new Node(Token.VAR, new Node(Token.LP));
        code.addChildToBack(new Node(Token.NAME, IR.string("x")));
        code.addChildToBack(new Node(Token.ENUM, IR.string("foo")));
        Node block = new Node(Token.BLOCK, code);
        node.getParent().replaceChild(node, block);   
      }
    });

    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node code = new Node(Token.SCRIPT);
    code.addChildToFront(new Node(Token.VAR, new Node(Token.NAME, IR.string("s")), new Node(Token.STRING, IR.string("this string is "))));

    scriptRoot.addChildToBack(code);
    root.addChildToBack(scriptRoot);

    nodeTraversal.traverse(externs, root);

    Assert.assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProtectSideEffects() {
    // Error scenario
    // Test the protectSideEffects() method when there are problem nodes
    // Expect the problem nodes to be replaced with a function call and the compiler to report a code change
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new AbstractCompiler(), CheckLevel.WARNING, true);
    Node problemNode1 = new Node(Token.VAR, new Node(Token.LP));
    problemNode1.addChildToBack(new Node(Token.NAME, IR.string("x")));
    problemNode1.addChildToBack(new Node(Token.ENUM, IR.string("foo")));
    Node problemNode2 = new Node(Token.VAR, new Node(Token.LP));
    problemNode2.addChildToBack(new Node(Token.NAME, IR.string("y")));
    problemNode2.addChildToBack(new Node(Token.ENUM, IR.string("bar")));
    checkSideEffects.problemNodes.add(problemNode1);
    checkSideEffects.problemNodes.add(problemNode2);

    checkSideEffects.protectSideEffects();

    Assert.assertEquals(2, problemNode1.getParent().getChildCount());
    Assert.assertTrue(problemNode1.getNext().isFunction());
    Assert.assertTrue(problemNode2.getNext().isFunction());
    Assert.assertTrue(checkSideEffects.compiler.hasCodeChanged());
  }

  @Test
  public void testAddExtern() {
    // Edge case
    // Test the addExtern() method when the protector function is not yet in the externs
    // Expect the protector function to be added to the externs with a @noalias JSDoc annotation
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new AbstractCompiler(), CheckLevel.WARNING, true);

    checkSideEffects.addExtern();

    Node externs = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler);
    Node protectorFnName = externs.getFirstChild().getFirstChild().getFirstChild();
    Assert.assertEquals("JSCOMPILER_PRESERVE", protectorFnName.getString());
    Assert.assertTrue(protectorFnName.getBooleanProp(Node.IS_CONSTANT_NAME));
    Assert.assertNotNull(protectorFnName.getJSDocInfo());
    Assert.assertTrue(protectorFnName.getJSDocInfo().isNoAlias());
  }

  @Test
  public void testStripProtection() {
    // Typical use case
    // Test the StripProtection class with a JavaScript code that has a protected side-effect
    // Expect the protected side-effect to be removed

    StripProtection stripProtection = new StripProtection(new AbstractCompiler());
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node code = new Node(Token.SCRIPT);
    Node protectorFnCall = new Node(Token.CALL, new Node(Token.NAME, IR.string("JSCOMPILER_PRESERVE")));
    Node protectedSideEffect = new Node(Token.STRING);
    protectedSideEffect.setString("protected side effect");
    protectorFnCall.addChildToBack(protectedSideEffect);
    code.addChildToFront(protectorFnCall);

    scriptRoot.addChildToBack(code);
    root.addChildToBack(scriptRoot);

    stripProtection.process(externs, root);

    Node newRoot = new Node(Token.ROOT);
    newRoot.addChildToBack(scriptRoot);

    NodeTraversal.traverse(new AbstractCompiler(), newRoot, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        Assert.assertEquals(Token.STRING, node.getType());
        Assert.assertEquals("protected side effect", node.getString());
      }
    });
  }

}