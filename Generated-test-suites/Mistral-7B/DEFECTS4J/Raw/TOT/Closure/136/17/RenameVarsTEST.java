package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @org.junit.Test
  public void testRenameVars() {
    CompilerPass pass = new RenameVars(mockCompiler(), "", false, false, false, null, null, null);

    // Test with empty input
    Node root = NodeFactory.createRoot();
    pass.process(NodeFactory.createList(), root);
    Node traversed = NodeTraversal.deepestChild(root);
    assertThat(traversed).isNotNull();
    assertThat(traversed.getType()).isEqualTo(Token.NULL);

    // Test with simple variable declaration
    Node declarationNode = NodeFactory.createName("a");
    Node statementNode = NodeFactory.createStatement(declarationNode);
    Node listNode = NodeFactory.createList();
    listNode.appendChild(statementNode);
    Node scriptNode = NodeFactory.createScript(listNode);
    pass.process(NodeFactory.createList(), scriptNode);
    Node newDeclarationNode = NodeTraversal.deepestChild(scriptNode);
    String newName = RenameVars.getVariableMap().lookupNewName("a");
    assertThat(newDeclarationNode).isNotNull();
    assertThat(newDeclarationNode.getString()).isEqualTo(newName);

    // Test with multiple variable declarations
    Node multipleDeclarationsNode = NodeFactory.createList();
    multipleDeclarationsNode.appendChild(
        NodeFactory.createStatement(NodeFactory.createName("a"))
    );
    multipleDeclarationsNode.appendChild(
        NodeFactory.createStatement(NodeFactory.createName("b"))
    );
    Node multipleDeclarationsScriptNode = NodeFactory.createScript(multipleDeclarationsNode);
    pass.process(NodeFactory.createList(), multipleDeclarationsScriptNode);
    Node newA = NodeTraversal.deepestChild(multipleDeclarationsScriptNode);
    Node newB = NodeTraversal.getNextSibling(newA);
    String newNameA = RenameVars.getVariableMap().lookupNewName("a");
    String newNameB = RenameVars.getVariableMap().lookupNewName("b");
    assertThat(newA).isNotNull();
    assertThat(newA.getString()).isEqualTo(newNameA);
    assertThat(newB).isNotNull();
    assertThat(newB.getString()).isEqualTo(newNameB);

    // Test with local variables
    Node localVariableDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "c",
            NodeFactory.createName("c"),
            null
        )
    );
    Node localVariableScriptNode = NodeFactory.createScript(
        NodeFactory.createList(localVariableDeclarationNode)
    );
    pass.process(NodeFactory.createList(), localVariableScriptNode);
    Node newLocalVariable = NodeTraversal.getNextSibling(newA);
    String newNameLocalVariable = RenameVars.getVariableMap().lookupNewName("c");
    assertThat(newLocalVariable).isNotNull();
    assertThat(newLocalVariable.getString()).isEqualTo(newNameLocalVariable);

    // Test with function name preservation
    Node preserveFunctionNameNode = NodeFactory.createFunctionDeclaration(
        "foo",
        Arrays.asList(NodeFactory.createList(NodeFactory.createName("a"))),
        NodeFactory.createRoot(),
        null
    );
    Node preserveFunctionNameScriptNode = NodeFactory.createScript(
        NodeFactory.createList(preserveFunctionNameNode)
    );
    pass.process(NodeFactory.createList(), preserveFunctionNameScriptNode);
    Node newFunctionName = NodeTraversal.deepestChild(preserveFunctionNameScriptNode);
    String newNameFunctionName = RenameVars.getVariableMap().lookupNewName("foo");
    assertThat(newFunctionName).isNotNull();
    assertThat(newFunctionName.getString()).isEqualTo(newNameFunctionName);

    // Test with generating pseudo names
    RenameVars generatePseudoNamesPass = new RenameVars(
        mockCompiler(), "", false, false, true, null, null, null
    );
    Node generatePseudoNamesScriptNode = NodeFactory.createScript(
        NodeFactory.createList(
            NodeFactory.createStatement(NodeFactory.createName("a"))
        )
    );
    generatePseudoNamesPass.process(NodeFactory.createList(), generatePseudoNamesScriptNode);
    Node newPseudoName = NodeTraversal.deepestChild(generatePseudoNamesScriptNode);
    String newNamePseudoName = RenameVars.getVariableMap().lookupNewName("a");
    assertThat(newPseudoName).isNotNull();
    assertThat(newPseudoName.getString()).contains("$$");

    // Test with extern variables
    Node externVariableDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "EXPORTED_VAR",
            NodeFactory.createName("EXPORTED_VAR"),
            null
        )
    );
    Node externVariableScriptNode = NodeFactory.createScript(
        NodeFactory.createList(externVariableDeclarationNode)
    );
    pass.process(NodeFactory.createList(), externVariableScriptNode);
    Node newExternVariable = NodeTraversal.getNextSibling(newB);
    String newNameExternVariable = RenameVars.getVariableMap().lookupNewName("EXPORTED_VAR");
    assertThat(newExternVariable).isNull();

    // Test with reserved names
    Node reservedNameDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "reservedName",
            NodeFactory.createName("reservedName"),
            null
        )
    );
    Set<String> reservedNames = new HashSet<String>(Arrays.asList("reservedName"));
    RenameVars reservedNamePass = new RenameVars(
        mockCompiler(), "", false, false, false, null, null, reservedNames
    );
    Node reservedNameScriptNode = NodeFactory.createScript(
        NodeFactory.createList(reservedNameDeclarationNode)
    );
    reservedNamePass.process(NodeFactory.createList(), reservedNameScriptNode);
    Node newReservedName = NodeTraversal.getNextSibling(newLocalVariable);
    String newNameReservedName = RenameVars.getVariableMap().lookupNewName("reservedName");
    assertThat(newReservedName).isNull();

    // Test with variable reuse
    Map<String, String> prevUsedRenameMap = new HashMap<String, String>();
    prevUsedRenameMap.put("prevVar", "prevName");
    RenameVars prevUsedPass = new RenameVars(
        mockCompiler(), "", false, false, false, prevUsedRenameMap, null, null
    );
    Node prevUsedVariableDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "prevVar",
            NodeFactory.createName("prevVar"),
            null
        )
    );
    Node prevUsedVariableScriptNode = NodeFactory.createScript(
        NodeFactory.createList(prevUsedVariableDeclarationNode)
    );
    prevUsedPass.process(NodeFactory.createList(), prevUsedVariableScriptNode);
    Node newPrevUsedVariable = NodeTraversal.getNextSibling(newPseudoName);
    String newNamePrevUsedVariable = RenameVars.getVariableMap().lookupNewName("prevVar");
    String prevName = RenameVars.getVariableMap().lookupOldName("prevName");
    assertThat(newPrevUsedVariable).isNotNull();
    assertThat(newPrevUsedVariable.getString()).isEqualTo(newNamePrevUsedVariable);
    assertThat(RenameVars.getVariableMap().lookupOldName(newNamePrevUsedVariable)).isEqualTo(prevName);

    // Test error scenarios
    Node errorScenarioDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "error",
            NodeFactory.createName("error"),
            NodeFactory.createList(
                NodeFactory.createName("error")
            )
        )
    );
    Node errorScenarioScriptNode = NodeFactory.createScript(
        NodeFactory.createList(errorScenarioDeclarationNode)
    );
    pass.process(NodeFactory.createList(), errorScenarioScriptNode);
    Node newError = NodeTraversal.getNextSibling(newExternVariable);
    String newNameError = RenameVars.getVariableMap().lookupNewName("error");
    assertThat(newError).isNotNull();
    assertThat(newError.getString()).isEqualTo(newNameError);
    String oldNameError = RenameVars.getVariableMap().lookupOldName(newNameError);
    assertThat(oldNameError).isEqualTo("error");

    Node cyclicDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "cyclicVar",
            NodeFactory.createName("cyclicVar"),
            NodeFactory.createList(NodeFactory.createName("cyclicVar"))
        )
    );
    Node cyclicDeclarationScriptNode = NodeFactory.createScript(
        NodeFactory.createList(cyclicDeclarationNode)
    );
    pass.process(NodeFactory.createList(), cyclicDeclarationScriptNode);
    Node newCyclicVariable = NodeTraversal.getNextSibling(newError);
    String newNameCyclicVariable = RenameVars.getVariableMap().lookupNewName("cyclicVar");
    assertThat(newCyclicVariable).isNull();

    Node nonLocalDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "nonLocalVar",
            NodeFactory.createName("nonLocalVar"),
            null
        )
    );
    Node nonLocalDeclarationScriptNode = NodeFactory.createScript(
        NodeFactory.createList(nonLocalDeclarationNode)
    );
    Node nonLocalVariable = NodeTraversal.deepestChild(nonLocalDeclarationScriptNode);
    String oldNameNonLocalVariable = NodeTraversal.getParentNode(nonLocalVariable).getString();
    String newNameNonLocalVariable = RenameVars.getVariableMap().lookupNewName(oldNameNonLocalVariable);
    String parentName = RenameVars.getVariableMap().lookupOldName(newNameNonLocalVariable);
    Node newNonLocalParent = NodeTraversal.getParentNode(NodeTraversal.deepestChild(scriptNode));
    String newParentName = newNonLocalParent.getString();
    assertThat(newNameNonLocalVariable).isNotNull();
    assertThat(newParentName).isEqualTo("nonLocalVar");
    assertThat(parentName).isEqualTo(oldNameNonLocalVariable);

    Node sameNameDeclarationNode = NodeFactory.createStatement(
        NodeFactory.createVariableDeclarationStatement(
            "sameNameVar",
            NodeFactory.createName("sameNameVar"),
            null
        )
    );
    Node sameNameDeclarationScriptNode = NodeFactory.createScript(
        NodeFactory.createList(sameNameDeclarationNode)
    );
    Node sameNameDeclarationVariable = NodeTraversal.deepestChild(sameNameDeclarationScriptNode);
    String oldNameSameNameVariable = NodeTraversal.getParentNode(sameNameDeclarationVariable).getString();
    String newNameSameNameVariable = RenameVars.getVariableMap().lookupNewName(oldNameSameNameVariable);
    Node newSameNameDeclarationVariable = NodeTraversal.getNextSibling(newError);
    String newParentSameNameVariable = NodeTraversal.getParentNode(newSameNameDeclarationVariable).getString();
    String newNameParentSameNameVariable = RenameVars.getVariableMap().lookupNewName(newParentSameNameVariable);
    Node newSameNameDeclarationParent = NodeTraversal.getParentNode(NodeTraversal.deepestChild(scriptNode));
    String newParentNameSameNameVariable = newSameNameDeclarationParent.getString();
    assertThat(newNameSameNameVariable).isNotNull();
    assertThat(newParentNameSameNameVariable).isEqualTo("sameNameVar");
    assertThat(newParentSameNameVariable).isEqualTo("sameNameVar");
    assertThat(newNameParentSameNameVariable).isEqualTo("parentSameNameVar");
  }

  private Compiler compiler() {
    // Mock the compiler to not throw any errors during the test
    Compiler compiler = mock(Compiler.class);
    when(compiler.getCodingConvention()).thenReturn(new EmptyCodingConvention());
    return compiler;
  }
}

// Helper class for a dummy coding convention that doesn't throw any errors
class EmptyCodingConvention implements CodingConvention {
  @Override
  public void checkExported(String name, boolean isLocal) {
  }
}