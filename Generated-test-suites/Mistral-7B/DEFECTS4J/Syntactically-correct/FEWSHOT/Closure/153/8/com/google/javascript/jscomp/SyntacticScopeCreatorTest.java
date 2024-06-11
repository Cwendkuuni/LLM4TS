package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.JdkStrings;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class SyntacticScopeCreatorTest {

    private static final String SOURCE_NAME = "sourceName";
    private static final String FUNCTION_NAME = "functionName";
    private static final String VAR_NAME_1 = "varName1";
    private static final String VAR_NAME_2 = "varName2";
    private static final String ARGUMENTS_NAME = "arguments";

    private AbstractCompiler compiler;
    private SyntacticScopeCreator scopeCreator;
    private RedeclarationHandler redeclarationHandler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        scopeCreator = new SyntacticScopeCreator(compiler);
        redeclarationHandler = mock(RedeclarationHandler.class);
        when(compiler.getInput(SOURCE_NAME)).thenReturn(createInput(VAR_NAME_1, VAR_NAME_2));
    }

    @Test
    public void testCreateScope() {
        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of());
        Scope scope = scopeCreator.createScope(rootNode, null);
        assertNotNull(scope);
    }

    @Test
    public void testCreateScopeWithParent() {
        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of());
        Node parentNode = createVariableNode(VAR_NAME_1);
        Scope scope = scopeCreator.createScope(rootNode, parentNode);
        assertNotNull(scope);
    }

    @Test
    public void testScanRoot() {
        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createVariableNode(VAR_NAME_1),
                createNameNode(ARGUMENTS_NAME),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, null);

        Scope expectedScope = new Scope(rootNode, compiler);
        expectedScope.declare(VAR_NAME_1, rootNode.getFirstChild(), null, createInput(VAR_NAME_1, VAR_NAME_2));
        expectedScope.declare(ARGUMENTS_NAME, rootNode.getFirstChild().getNext(), null, createInput(VAR_NAME_1, VAR_NAME_2));

        Scope actualScope = scopeCreator.scope;
        checkScopeEquals(expectedScope, actualScope);
    }

    @Test
    public void testScanRootWithFunction() {
        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createVariableNode(VAR_NAME_1),
                createNameNode(FUNCTION_NAME),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, null);

        Scope expectedScope = new Scope(rootNode, compiler);
        expectedScope.declare(VAR_NAME_1, rootNode.getFirstChild(), null, createInput(VAR_NAME_1, VAR_NAME_2));
        expectedScope.declare(FUNCTION_NAME, rootNode.getFirstChild().getNext(), null, createInput(VAR_NAME_1, VAR_NAME_2));

        Scope actualScope = scopeCreator.scope;
        checkScopeEquals(expectedScope, actualScope);
    }

    @Test
    public void testScanRootWithFunctionArgument() {
        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createNameNode(ARGUMENTS_NAME),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, null);

        Scope expectedScope = new Scope(rootNode, compiler);
        expectedScope.declare(ARGUMENTS_NAME, rootNode.getFirstChild(), JSType.ARRAY, createInput(VAR_NAME_1, VAR_NAME_2));

        Scope actualScope = scopeCreator.scope;
        checkScopeEquals(expectedScope, actualScope);
    }

    @Test
    public void testScanVars() {
        Node variableNode1 = createVariableNode(VAR_NAME_1);
        Node variableNode2 = createVariableNode(VAR_NAME_2);

        Node variableNode3 = createVariableNode(VAR_NAME_1);
        Node blockNode = createBlockNode();

        scopeCreator.scanVars(blockNode, null);

        Scope expectedScope = new Scope(blockNode, compiler);
        expectedScope.declare(VAR_NAME_1, variableNode1, null, createInput(VAR_NAME_1, VAR_NAME_2));
        expectedScope.declare(VAR_NAME_2, variableNode2, null, createInput(VAR_NAME_1, VAR_NAME_2));

        Scope actualScope = scopeCreator.scope;
        checkScopeEquals(expectedScope, actualScope);
    }

    @Test
    public void testScanVarsWithFunctionDeclaration() {
        Node functionNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createVariableNode(VAR_NAME_1),
                createNameNode(FUNCTION_NAME),
                createBlockNode()));

        scopeCreator.scanVars(functionNode, null);

        Scope expectedScope = new Scope(functionNode, compiler);
        expectedScope.declare(VAR_NAME_1, functionNode.getFirstChild(), null, createInput(VAR_NAME_1, VAR_NAME_2));
        expectedScope.declare(FUNCTION_NAME, functionNode.getFirstChild().getNext(), null, createInput(VAR_NAME_1, VAR_NAME_2));

        Scope actualScope = scopeCreator.scope;
        checkScopeEquals(expectedScope, actualScope);
    }

    @Test
    public void testOnRedeclarationWithGlobalScope() {
        // Given
        String name = VAR_NAME_1;
        Node n = createVariableNode(name);
        Node parent = createNameNode(ARGUMENTS_NAME);
        Node gramps = createBlockNode();
        Node nodeWithLineNumber = createNumberNode(1);

        when(redeclarationHandler.onRedeclaration(
            eq(scopeCreator.scope), eq(name), eq(n), any(), eq(gramps), eq(nodeWithLineNumber)))
            .thenAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) {
                    compiler.report(JSError.make(SOURCE_NAME, n, VAR_MULTIPLY_DECLARED_ERROR, name, "varName1"));
                    return null;
                }
            });

        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createVariableNode(name),
                createNameNode(name),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, null);

        // Then
        assertNull(scopeCreator.scope.getVar(name));
    }

    @Test
    public void testOnRedeclarationWithLocalScope() {
        // Given
        String name = VAR_NAME_1;
        Node n = createVariableNode(name);
        Node parent = createNameNode(VAR_NAME_1);
        Node gramps = createBlockNode();
        Node nodeWithLineNumber = createNumberNode(1);

        when(redeclarationHandler.onRedeclaration(
            eq(scopeCreator.scope), eq(name), eq(n), any(), eq(gramps), eq(nodeWithLineNumber)))
            .thenAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) {
                    compiler.report(JSError.make(SOURCE_NAME, n, VAR_MULTIPLY_DECLARED_ERROR, name, "varName1"));
                    return null;
                }
            });

        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createVariableNode(VAR_NAME_1),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, parent);

        // Then
        assertNull(scopeCreator.scope.getVar(name));
    }

    @Test
    public void testOnRedeclarationWithArgumentsShadowing() {
        // Given
        Node n = createNameNode(ARGUMENTS_NAME);
        Node parent = createBlockNode();
        Node gramps = createBlockNode();
        Node nodeWithLineNumber = createNumberNode(1);

        when(redeclarationHandler.onRedeclaration(
            eq(scopeCreator.scope), eq(ARGUMENTS_NAME), eq(n), any(), eq(gramps), eq(nodeWithLineNumber)))
            .thenAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) {
                    compiler.report(JSError.make(SOURCE_NAME, n, VAR_ARGUMENTS_SHADOWED_ERROR));
                    return null;
                }
            });

        Node rootNode = createFunctionNode(FUNCTION_NAME, ImmutableList.of(
                createNameNode(ARGUMENTS_NAME),
                createBlockNode()));

        scopeCreator.scanRoot(rootNode, null);

        // Then
        assertNull(scopeCreator.scope.getVar(ARGUMENTS_NAME));
    }

    private Node createFunctionNode(String name, List<Node> arguments) {
        Node functionNode = new Node_Function();
        functionNode.setString(Node.NAME_PROP, name);
        functionNode.setFirstChild(createFunctionNameNode(name));

        if (!arguments.isEmpty()) {
            Node argumentsNode = new Node_List();
            functionNode.setFirstChild(argumentsNode);
            for (Node argument : arguments) {
                argumentsNode.addChild(argument);
            }
        }

        Node bodyNode = createBlockNode();
        functionNode.setNext(bodyNode);
        return functionNode;
    }

    private Node createVariableNode(String name) {
        Node variableNode = new Node_Variable();
        variableNode.setString(Node.NAME_PROP, name);
        return variableNode;
    }

    private Node createNameNode(String name) {
        Node nameNode = new Node_Name();
        nameNode.setString(Node.NAME_PROP, name);
        return nameNode;
    }

    private Node createBlockNode() {
        Node blockNode = new Node_StatementList();
        return blockNode;
    }

    private Node createFunctionNameNode(String name) {
        Node functionNameNode = new Node_Identifier();
        functionNameNode.setString(Node.NAME_PROP, name);
        return functionNameNode;
    }

    private Node createNumberNode(int value) {
        Node numberNode = new Node_Number();
        numberNode.setDouble(Node.NUM_PROP, value);
        return numberNode;
    }

    private Input createInput(String... names) {
        return new Input(JdkStrings.join("", names), null);
    }

    private void checkScopeEquals(Scope expectedScope, Scope actualScope) {
        checkState(expectedScope != null && actualScope != null);
        assertEquals(expectedScope.getVariables().size(), actualScope.getVariables().size());

        for (Scope.Var expectedVar : expectedScope.getVariables()) {
            Scope.Var actualVar = actualScope.getVar(expectedVar.getName());
            assertEquals(expectedVar.getName(), actualVar.getName());
            assertEquals(expectedVar.getNode(), actualVar.getNode());
            assertEquals(expectedVar.getType(), actualVar.getType());
            assertEquals(expectedVar.getInput(), actualVar.getInput());
        }
    }
}

class Input {
    private String name;
    private Object input;

    Input(String name, Object input) {
        this.name = name;
        this.input = input;
    }

    String getName() {
        return name;
    }

    Object getInput() {
        return input;
    }
}