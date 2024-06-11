package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {
  private static final String SCOPING_METHOD_NAME = ScopedAliases.SCOPING_METHOD_NAME;

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new TestCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new TestAliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newProgram();
    Node node = Node.newExpressionStatement(
        Node.newCall(Node.newName(SCOPING_METHOD_NAME), Node.newFunctionExpression(
            Node.newName("anonymousFunction"), Node.newArray(), Node.newBlock(
                Node.newVariable("alias", Node.newName("goog"), Node.newName("dom"),
                    Node.newName("DIV"), Node.newCall(Node.newName("createElement"),
                        Node.newName("goog.dom"), Node.newName("DIV")))))));
    root.setSourceFile("test.js");
    root.setPrintContext(new PrintContext() {
      @Override
      public void print(Node node, PrintFlags flags) {
        // Do nothing.
      }
    });
    scopedAliases.process(null, root);

    // Check if alias definitions were removed and aliases were applied.
    Collection<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertFalse("Unexpected alias definitions.", aliasDefinitionsInOrder.isEmpty());
    assertFalse("Unexpected scope calls.", scopeCalls.isEmpty());
    assertFalse("Unexpected alias usages.", aliasUsages.isEmpty());

    // Check alias definitions.
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Var aliasVar = aliasDefinition.getParent().getFirstChild();
    String aliasName = aliasVar.getName();
    String qualifiedName = aliasDefinition.getLastChild().getQualifiedName();

    AliasTransformation transformation = transformationHandler.getAliasTransformation();
    assertEquals("Unexpected alias name.", aliasName, transformation.getAliasName());
    assertEquals("Unexpected qualified name.", qualifiedName, transformation.getQualifiedName());

    // Check scope calls.
    for (Node scopeCall : scopeCalls) {
      Node scopeCallExpression = scopeCall.getLastChild();
      Node scopeClosureBlock = scopeCallExpression.getLastChild();
      Node scopeClosureBody = scopeClosureBlock.getFirstChild();
      Node aliasDefinitionInScope = null;

      for (Node child = scopeClosureBody.getFirstChild(); child != null;
           child = child.getNext()) {
        if (child.getType() == Token.VAR && child.getFirstChild().isName()) {
          Var childAliasVar = child.getFirstChild();
          String childAliasName = childAliasVar.getName();
          Node childAliasDefinition = aliasDefinitionsInOrder.get(
              aliasDefinitionsInOrder.indexOf(aliasDefinition) + aliasUsages.size() +
                  aliasUsages.indexOf(new AliasedNode(child, aliasDefinition)));

          if (aliasName.equals(childAliasName)) {
            aliasDefinitionInScope = childAliasDefinition;
            break;
          }
        }
      }

      assertNotNull("Unexpected, no alias definition found for alias.", aliasDefinitionInScope);
      assertSame("Unexpected, different alias definitions.", aliasDefinition, aliasDefinitionInScope);
    }

    // Check alias usages.
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        Node aliasReference = aliasedNode.aliasReference;
        Node aliasDefinition = aliasedNode.aliasDefinition;
        assertTrue("Unexpected alias reference.", aliasReference.getParent() instanceof Var);
        Var aliasVar = ((Var) aliasReference.getParent());
        String aliasName = aliasVar.getName();
        assertTrue("Unexpected alias reference.", aliasReference.equals(aliasDefinition));
        assertTrue("Unexpected alias definition.", aliases.containsKey(aliasName));
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        Node aliasReference = aliasedTypeNode.aliasReference;
        String correctedType = aliasedTypeNode.correctedType;
        String aliasName = aliasReference.getString();
        Var aliasVar = aliases.get(aliasName);
        assertNotNull("Unexpected, no alias definition found for alias.", aliasVar);
        Node aliasDefinition = aliasVar.getInitialValue();
        assertTrue("Unexpected alias reference.", aliasReference.getParent() instanceof Var);
        var aliasVar2 = ((Var) aliasReference.getParent());
        assertEquals("Unexpected alias name.", aliasName, aliasVar.getName());
        assertEquals("Unexpected corrected type.", correctedType, aliasDefinition.getQualifiedName());
      }
    }
  }
}

class TestCompiler extends CompilerPassAdapter {
  @Override
  public void process(Node externs, Node root) {
    // Do nothing.
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  private AliasTransformation aliasTransformation = null;

  @Override
  public void logAliasTransformation(String fileName, SourcePosition position) {
    aliasTransformation = new AliasTransformation(fileName, position);
  }

  @Override
  public AliasTransformation getAliasTransformation() {
    return aliasTransformation;
  }
}