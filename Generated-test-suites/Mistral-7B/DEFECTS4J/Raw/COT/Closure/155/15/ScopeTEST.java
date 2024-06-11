package com.google.javascript.jscomp; // import statements for test classes and libraries

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.NodeTraversal.forEachChildRecursive;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.CompilerInput;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {
    private static final String TEST_SCOPE_NAME = "testScope";
    private static final String TEST_VAR_NAME = "testVar";
    private static final String TEST_FUNCTION_NAME = "testFunction";
    private static final int TEST_DEPTH = 5;
    private static final int TEST_VAR_INDEX = 0;
    private Scope rootScope;
    private Scope testScope;
    private Node testRootNode;
    private ErrorReporter errorReporter;
    private CompilerInput testInput;

    @Before
    public void setUp() {
        errorReporter = mock(ErrorReporter.class);
        testInput = new CompilerInput(new SourceName("Test File"), null, null, null);
        testRootNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null, null, null, null);
        rootScope = new Scope(null, testRootNode, testInput);
        testScope = new Scope(rootScope, testRootNode);
    }

    @Test
    public void testScopeConstructorWithParentAndRootNode() {
        Scope expectedScope = new Scope(rootScope, testRootNode);
        assertThat(testScope).isEqualTo(expectedScope);
    }

    @Test
    public void testScopeConstructorWithParentNullAndRootNode() {
        Node rootNode = Node.newBlock(Token.BLOCK, null, null);
        ObjectType thisType = mock(ObjectType.class);
        Scope expectedScope = new Scope(rootNode, thisType);
        assertThat(testScope).isEqualTo(expectedScope);
    }

    @Test
    public void testScopeConstructorWithParentNullAndRootNodeWithThisTypeNull() {
        Node rootNode = Node.newBlock(Token.BLOCK, null, null);
        Scope expectedScope = new Scope(rootNode, null);
        assertThat(testScope).isEqualTo(expectedScope);
    }

    @Test
    public void testGetScope() {
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        Scope expectedScope = testVar.getScope();
        assertThat(testScope.getScope()).isEqualTo(expectedScope);
    }

    @Test
    public void testGetParent() {
        Scope parentScope = mock(Scope.class);
        when(parentScope.getVars()).thenReturn(createTestVarsIterator());
        testScope.setParent(parentScope);
        Scope expectedParent = parentScope;
        assertThat(testScope.getParent()).isEqualTo(expectedParent);
    }

    @Test
    public void testGetDepth() {
        assertThat(testScope.getDepth()).isEqualTo(TEST_DEPTH);
    }

    @Test
    public void testIsBottom() {
        assertThat(testScope.isBottom()).isFalse();
    }

    @Test
    public void testGetRootNode() {
        assertThat(testScope.getRootNode()).isSameInstanceAs(testRootNode);
    }

    @Test
    public void testGetParentScope() {
        Scope parentScope = mock(Scope.class);
        testScope.setParent(parentScope);
        StaticScope<JSType> expectedParentScope = parentScope;
        assertThat(testScope.getParentScope()).isEqualTo(expectedParentScope);
    }

    @Test
    public void testGetTypeOfThis() {
        ObjectType thisType = mock(ObjectType.class);
        when(thisType.getName()).thenReturn("TestThisType");
        testScope.setTypeOfThis(thisType);
        ObjectType expectedThisType = thisType;
        assertThat(testScope.getTypeOfThis()).isEqualTo(expectedThisType);
    }

    @Test
    public void testDeclare() {
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        Var expectedVar = testVar;
        assertThat(testScope.getOwnSlot(TEST_VAR_NAME)).isEqualTo(expectedVar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareWithDuplicateName() {
        testScope.declare(TEST_VAR_NAME, null, JSType.UNKNOWN, testInput);
        testScope.declare(TEST_VAR_NAME, null, JSType.UNKNOWN, testInput);
    }

    @Test
    public void testUndeclare() {
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        testScope.undeclare(testVar);
        assertThat(testScope.getOwnSlot(TEST_VAR_NAME)).isNull();
    }

    @Test
    public void testGetVar() {
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        Var expectedVar = testVar;
        assertThat(testScope.getVar(TEST_VAR_NAME)).isEqualTo(expectedVar);
    }

    @Test
    public void testGetSlot() {
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        StaticSlot<JSType> expectedSlot = testVar;
        assertThat(testScope.getSlot(TEST_VAR_NAME)).isEqualTo(expectedSlot);
    }

    @Test
    public void testIsDeclared() {
        testScope.declare(TEST_VAR_NAME, null, JSType.UNKNOWN, testInput);
        boolean expectedResult = true;
        assertThat(testScope.isDeclared(TEST_VAR_NAME, true)).isEqualTo(expectedResult);
    }

    @Test
    public void testGetVars() {
        testScope.declare(TEST_VAR_NAME, null, JSType.UNKNOWN, testInput);
        Iterator<Var> expectedIterator = createTestVarsIterator();
        assertThat(testScope.getVars()).isSameInstanceAs(expectedIterator);
    }

    @Test
    public void testGetVarCount() {
        testScope.declare(TEST_VAR_NAME, null, JSType.UNKNOWN, testInput);
        int expectedCount = 1;
        assertThat(testScope.getVarCount()).isEqualTo(expectedCount);
    }

    @Test
    public void testIsGlobal() {
        boolean expectedResult = false;
        assertThat(testScope.isGlobal()).isEqualTo(expectedResult);
    }

    @Test
    public void testIsLocal() {
        boolean expectedResult = true;
        assertThat(testScope.isLocal()).isEqualTo(expectedResult);
    }

    @Test
    public void testJoin() {
        Scope otherScope = mock(Scope.class);
        when(otherScope.getVars()).thenReturn(createTestVarsIterator());
        Map<String, Var> otherMap = otherScope.getVars();
        Var testVar = createTestVar();
        testScope.declare(TEST_VAR_NAME, testVar.getNameNode(), JSType.UNKNOWN, testInput);
        testScope.declare(TEST_FUNCTION_NAME, Node.newFunction(Token.FUNCTION, null, null, null, null, null, null, null, null, null), JSType.UNKNOWN, testInput);

        testScope = testScope.join(otherScope);

        Iterator<Var> mergedIterator = testScope.getVars();
        Var mergedTestVar = mergedIterator.next();
        Var mergedTestFunction = mergedIterator.next();

        String mergedTestVarName = mergedTestVar.getName();
        String mergedTestFunctionName = mergedTestFunction.getName();

        // Test that mergedTestVar has the same name as the original testVar
        assertThat(mergedTestVarName).isEqualTo(TEST_VAR_NAME);

        // Test that mergedTestFunction has the same name as the original testFunction
        assertThat(mergedTestFunctionName).isEqualTo(TEST_FUNCTION_NAME);

        Map<String, Var> mergedMap = testScope.getVars();
        Var mergedTestVarMerge = mergedMap.get(TEST_VAR_NAME);
        Var mergedTestFunctionMerge = mergedMap.get(TEST_FUNCTION_NAME);

        Var expectedTestVar = testVar;
        Var expectedTestFunction = testScope.getVar(TEST_FUNCTION_NAME);

        // Test that mergedTestVar has the same Var instance as the original testVar
        assertThat(mergedTestVarMerge).isEqualTo(expectedTestVar);

        // Test that mergedTestFunction has the same Var instance as the original testFunction
        assertThat(mergedTestFunctionMerge).isEqualTo(expectedTestFunction);
    }

    private Var createTestVar() {
        Node nameNode = Node.newName(TEST_VAR_NAME, null, null, null);
        return testScope.declare(TEST_VAR_NAME, nameNode, JSType.UNKNOWN, testInput);
    }

    private Iterator<Var> createTestVarsIterator() {
        Var testVar = createTestVar();
        return new Iterator<Var>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Var next() {
                return testVar;
            }
        };
    }
}