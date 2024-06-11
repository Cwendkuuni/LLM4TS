import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    // Setup
    Compiler compiler = mock(Compiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Test empty case
    Node root = Node.makeBlock("{}");
    renameLabels.process(Node.root(), root);
    assertEquals("", root.toSource());

    // Test single label
    Node labelNode = Node.makeLabel("a");
    Node labelStatement = Node.makeBlock(labelNode);
    Node rootWithLabel = Node.makeBlock(labelStatement);
    renameLabels.process(Node.root(), rootWithLabel);
    assertEquals("a:", rootWithLabel.toSource());

    // Test nested labels
    Node labelNode2 = Node.makeLabel("b");
    Node nestedLabelStatement = Node.makeBlock(labelNode2);
    Node labelStatementWithNestedLabel = Node.makeBlock(labelNode, nestedLabelStatement);
    Node rootWithNestedLabel = Node.makeBlock(labelStatementWithNestedLabel);
    renameLabels.process(Node.root(), rootWithNestedLabel);
    assertEquals("b:", rootWithNestedLabel.toSource());
    assertEquals("a:", labelStatementWithNestedLabel.toSource());

    // Test label with break statement
    Node breakStatement = Node.makeBreak();
    Node labelStatementWithBreak = Node.makeBlock(labelNode, breakStatement);
    Node rootWithLabelAndBreak = Node.makeBlock(labelStatementWithBreak);
    renameLabels.process(Node.root(), rootWithLabelAndBreak);
    assertEquals("b:", rootWithLabelAndBreak.toSource());
    assertEquals("a:", labelStatementWithBreak.toSource());

    // Test label with continue statement
    Node continueStatement = Node.makeContinue();
    Node labelStatementWithContinue = Node.makeBlock(labelNode, continueStatement);
    Node rootWithLabelAndContinue = Node.makeBlock(labelStatementWithContinue);
    renameLabels.process(Node.root(), rootWithLabelAndContinue);
    assertEquals("b:", rootWithLabelAndContinue.toSource());
    assertEquals("a:", labelStatementWithContinue.toSource());

    // Test label with break and continue statements
    Node rootWithLabelBreakAndContinue = Node.makeBlock(
        Node.makeBlock(labelNode, breakStatement, Node.makeLabel("c"), continueStatement)
    );
    renameLabels.process(Node.root(), rootWithLabelBreakAndContinue);
    assertEquals("b:", rootWithLabelBreakAndContinue.toSource());
    assertEquals("a:", Node.makeBlock(labelNode, Node.makeLabel("c"), continueStatement).toSource());

    // Test label with multiple references
    Node rootWithLabelMultipleReferences = Node.makeBlock(
        Node.makeBlock(labelNode, Node.makeLabel("c")),
        Node.makeBlock(labelNode, Node.makeLabel("c"))
    );
    renameLabels.process(Node.root(), rootWithLabelMultipleReferences);
    assertEquals("b:", rootWithLabelMultipleReferences.toSource());
    assertEquals("a:", Node.makeBlock(labelNode, Node.makeLabel("c")).toSource());
    assertEquals("a:", Node.makeBlock(labelNode, Node.makeLabel("c")).toSource());

    // Test label with empty block statement
    Node rootWithLabelEmptyBlock = Node.makeBlock(LabelNode, Node.makeBlock());
    renameLabels.process(Node.root(), rootWithLabelEmptyBlock);
    assertEquals("b:", rootWithLabelEmptyBlock.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with declaration statement
    Node declarationStatement = Node.makeVar("x", Node.makeNull());
    Node labelStatementWithDeclaration = Node.makeBlock(labelNode, declarationStatement);
    Node rootWithLabelAndDeclaration = Node.makeBlock(labelStatementWithDeclaration);
    renameLabels.process(Node.root(), rootWithLabelAndDeclaration);
    assertEquals("b:", rootWithLabelAndDeclaration.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with expression statement
    Node expressionStatement = Node.makeCall(null, "console.log", Node.makeString("hello"));
    Node labelStatementWithExpression = Node.makeBlock(labelNode, expressionStatement);
    Node rootWithLabelAndExpression = Node.makeBlock(labelStatementWithExpression);
    renameLabels.process(Node.root(), rootWithLabelAndExpression);
    assertEquals("b:", rootWithLabelAndExpression.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with loop statement
    Node loopStatement = Node.makeForLoop(
        Node.makeIdentifier("i"),
        Node.makeNumber(0),
        Node.makeNumber(1),
        Node.makeNull()
    );
    Node labelStatementWithLoop = Node.makeBlock(labelNode, loopStatement);
    Node rootWithLabelAndLoop = Node.makeBlock(labelStatementWithLoop);
    renameLabels.process(Node.root(), rootWithLabelAndLoop);
    assertEquals("b:", rootWithLabelAndLoop.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with if statement
    Node labelStatementWithIf = Node.makeBlock(
        labelNode,
        Node.makeIfStatement(Node.makeBooleanLiteral(true), Node.makeString("yes"))
    );
    Node rootWithLabelAndIf = Node.makeBlock(labelStatementWithIf);
    renameLabels.process(Node.root(), rootWithLabelAndIf);
    assertEquals("b:", rootWithLabelAndIf.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with function statement
    Node labelStatementWithFunction = Node.makeBlock(
        labelNode,
        Node.makeFunction("test", null, Node.makeBlock(), Node.makeBlock())
    );
    Node rootWithLabelAndFunction = Node.makeBlock(labelStatementWithFunction);
    renameLabels.process(Node.root(), rootWithLabelAndFunction);
    assertEquals("b:", rootWithLabelAndFunction.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with try statement
    Node tryStatement = Node.makeTryCatchBlock(
        Node.makeBlock(),
        Node.makeBlock(),
        Node.makeBlock()
    );
    Node labelStatementWithTry = Node.makeBlock(labelNode, tryStatement);
    Node rootWithLabelAndTry = Node.makeBlock(labelStatementWithTry);
    renameLabels.process(Node.root(), rootWithLabelAndTry);
    assertEquals("b:", rootWithLabelAndTry.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with while statement
    Node labelStatementWithWhile = Node.makeBlock(labelNode, Node.makeWhileLoop(Node.makeBooleanLiteral(true), Node.makeBlock()));
    Node rootWithLabelAndWhile = Node.makeBlock(labelStatementWithWhile);
    renameLabels.process(Node.root(), rootWithLabelAndWhile);
    assertEquals("b:", rootWithLabelAndWhile.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with switch statement
    Node labelStatementWithSwitch = Node.makeBlock(labelNode, Node.makeSwitchStatement(Node.makeStringLiteral("hello"), Node.makeBlock()));
    Node rootWithLabelAndSwitch = Node.makeBlock(labelStatementWithSwitch);
    renameLabels.process(Node.root(), rootWithLabelAndSwitch);
    assertEquals("b:", rootWithLabelAndSwitch.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with for-in statement
    Node labelStatementWithForIn = Node.makeBlock(labelNode, Node.makeForInStatement(Node.makeIdentifier("i"), Node.makeObjectLiteral(), Node.makeBlock()));
    Node rootWithLabelAndForIn = Node.makeBlock(labelStatementWithForIn);
    renameLabels.process(Node.root(), rootWithLabelAndForIn);
    assertEquals("b:", rootWithLabelAndForIn.toSource());
    assertEquals("a:", LabelNode.toSource());

    // Test label with empty statement
    Node labelStatementWithEmpty = Node.makeBlock(labelNode);
    Node rootWithLabelAndEmpty = Node.makeBlock(labelStatementWithEmpty);
    renameLabels.process(Node.root(), rootWithLabelAndEmpty);
    assertEquals("b:", rootWithLabelAndEmpty.toSource());
    assertEquals("a:", LabelNode.toSource());
  }

  @Test
  public void testRenameLabelsWithRemoveUnused() {
    // Setup
    Compiler compiler = mock(Compiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    // Test empty case
    Node root = Node.makeBlock("{}");
    renameLabels.process(Node.root(), root);
    assertEquals("", root.toSource());

    // Test single label
    Node labelNode = Node.makeLabel("a");
    Node labelStatement = Node.makeBlock(labelNode);
    Node rootWithLabel = Node.makeBlock(labelStatement);
    renameLabels.process(Node.root(), rootWithLabel);
    assertEquals("", rootWithLabel.toSource());

    // Test nested labels
    Node labelNode2 = Node.makeLabel("b");
    Node nestedLabelStatement = Node.makeBlock(labelNode2);
    Node labelStatementWithNestedLabel = Node.makeBlock(labelNode, nestedLabelStatement);
    Node rootWithNestedLabel = Node.makeBlock(labelStatementWithNestedLabel);
    renameLabels.process(Node.root(), rootWithNestedLabel);
    assertEquals("", rootWithNestedLabel.toSource());

    // Test label with break statement
    Node breakStatement = Node.makeBreak();
    Node labelStatementWithBreak = Node.makeBlock(labelNode, breakStatement);
    Node rootWithLabelAndBreak = Node.makeBlock(labelStatementWithBreak);
    renameLabels.process(Node.root(), rootWithLabelAndBreak);
    assertEquals("", rootWithLabelAndBreak.toSource());

    // Test label with continue statement
    Node continueStatement = Node.makeContinue();
    Node labelStatementWithContinue = Node.makeBlock(labelNode, continueStatement);
    Node rootWithLabelAndContinue = Node.makeBlock(labelStatementWithContinue);
    renameLabels.process(Node.root(), rootWithLabelAndContinue);
    assertEquals("", rootWithLabelAndContinue.toSource());

    // Test label with break and continue statements
    Node rootWithLabelBreakAndContinue = Node.makeBlock(
        Node.makeBlock(labelNode, breakStatement, Node.makeLabel("c"), continueStatement)
    );
    renameLabels.process(Node.root(), rootWithLabelBreakAndContinue);
    assertEquals("", rootWithLabelBreakAndContinue.toSource());

    // Test label with multiple references
    Node rootWithLabelMultipleReferences = Node.makeBlock(
        Node.makeBlock(labelNode, Node.makeLabel("c")),
        Node.makeBlock(labelNode, Node.makeLabel("c"))
    );
    renameLabels.process(Node.root(), rootWithLabelMultipleReferences);
    assertEquals("", rootWithLabelMultipleReferences.toSource());

    // Test label with empty block statement
    Node rootWithLabelEmptyBlock = Node.makeBlock(LabelNode, Node.makeBlock());
    renameLabels.process(Node.root(), rootWithLabelEmptyBlock);
    assertEquals("", rootWithLabelEmptyBlock.toSource());

    // Test label with declaration statement
    Node declarationStatement = Node.makeVar("x", Node.makeNull());
    Node labelStatementWithDeclaration = Node.makeBlock(labelNode, declarationStatement);
    Node rootWithLabelAndDeclaration = Node.makeBlock(labelStatementWithDeclaration);
    renameLabels.process(Node.root(), rootWithLabelAndDeclaration);
    assertEquals("", rootWithLabelAndDeclaration.toSource());

    // Test label with expression statement
    Node expressionStatement = Node.makeCall(null, "console.log", Node.makeString("hello"));
    Node labelStatementWithExpression = Node.makeBlock(labelNode, expressionStatement);
    Node rootWithLabelAndExpression = Node.makeBlock(labelStatementWithExpression);
    renameLabels.process(Node.root(), rootWithLabelAndExpression);
    assertEquals("", rootWithLabelAndExpression.toSource());

    // Test label with loop statement
    Node loopStatement = Node.makeForLoop(
        Node.makeIdentifier("i"),
        Node.makeNumber(0),
        Node.makeNumber(1),
        Node.makeNull()
    );
    Node labelStatementWithLoop = Node.makeBlock(labelNode, loopStatement);
    Node rootWithLabelAndLoop = Node.makeBlock(labelStatementWithLoop);
    renameLabels.process(Node.root(), rootWithLabelAndLoop);
    assertEquals("", rootWithLabelAndLoop.toSource());

    // Test label with if statement
    Node labelStatementWithIf = Node.makeBlock(labelNode, Node.makeIfStatement(Node.makeBooleanLiteral(true), Node.makeString("yes")));
    Node rootWithLabelAndIf = Node.makeBlock(labelStatementWithIf);
    renameLabels.process(Node.root(), rootWithLabelAndIf);
    assertEquals("", rootWithLabelAndIf.toSource());

    // Test label with function statement
    Node labelStatementWithFunction = Node.makeBlock(labelNode, Node.makeFunction("test", null, Node.makeBlock(), Node.makeBlock()));
    Node rootWithLabelAndFunction = Node.makeBlock(labelStatementWithFunction);
    renameLabels.process(Node.root(), rootWithLabelAndFunction);
    assertEquals("", rootWithLabelAndFunction.toSource());

    // Test label with try statement
    Node tryStatement = Node.makeTryCatchBlock(
        Node.makeBlock(),
        Node.makeBlock(),
        Node.makeBlock()
    );
    Node labelStatementWithTry = Node.makeBlock(labelNode, tryStatement);
    Node rootWithLabelAndTry = Node.makeBlock(labelStatementWithTry);
    renameLabels.process(Node.root(), rootWithLabelAndTry);
    assertEquals("", rootWithLabelAndTry.toSource());

    // Test label with while statement
    Node labelStatementWithWhile = Node.makeBlock(labelNode, Node.makeWhileLoop(Node.makeBooleanLiteral(true), Node.makeBlock()));
    Node rootWithLabelAndWhile = Node.makeBlock(labelStatementWithWhile);
    renameLabels.process(Node.root(), rootWithLabelAndWhile);
    assertEquals("", rootWithLabelAndWhile.toSource());

    // Test label with switch statement
    Node labelStatementWithSwitch = Node.makeBlock(labelNode, Node.makeSwitchStatement(Node.makeStringLiteral("hello"), Node.makeBlock()));
    Node rootWithLabelAndSwitch = Node.makeBlock(labelStatementWithSwitch);
    renameLabels.process(Node.root(), rootWithLabelAndSwitch);
    assertEquals("", rootWithLabelAndSwitch.toSource());

    // Test label with for-in statement
    Node labelStatementWithForIn = Node.makeBlock(labelNode, Node.makeForInStatement(Node.makeIdentifier("i"), Node.makeObjectLiteral(), Node.makeBlock()));
    Node rootWithLabelAndForIn = Node.makeBlock(labelStatementWithForIn);
    renameLabels.process(Node.root(), rootWithLabelAndForIn);
    assertEquals("", rootWithLabelAndForIn.toSource());

    // Test label with empty statement
    Node labelStatementWithEmpty = Node.makeBlock(labelNode);
    Node rootWithLabelAndEmpty = Node.makeBlock(labelStatementWithEmpty);
    renameLabels.process(Node.root(), rootWithLabelAndEmpty);
    assertEquals("", rootWithLabelAndEmpty.toSource());
  }
}