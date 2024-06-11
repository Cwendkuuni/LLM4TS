package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    @Test
    public void testCheckSideEffects() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, false);

        // Test empty statement
        Node emptyStatement = Node.newExprResult(Node.newVoid(1));
        NodeTraversal nodeTraversal = new NodeTraversal();
        nodeTraversal.setCompiler(null);
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), emptyStatement.getParent());
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.problemNodes.clear();

        // Test simple statement
        Node simpleStatement = Node.newExprResult(Node.newName("x", 1));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), simpleStatement.getParent());
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.problemNodes.clear();

        // Test statement with side effect
        Node statementWithSideEffect = Node.newExprResult(Node.newCall(Node.newName("foo"), Lists.newArrayList()));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), statementWithSideEffect.getParent());
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with simple operator
        Node statementWithSimpleOperator = Node.newExprResult(Node.newCall(Node.newName("+"),
                Lists.newArrayList(Node.newString("foo"), Node.newString("bar"))));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), statementWithSimpleOperator.getParent());
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.problemNodes.clear();

        // Test statement within for loop
        Node forLoop = Node.newExprResult(Node.newFor(Node.newName("i"), Node.newName("i", 1), Node.newName("i", 1), Node.newBlock(
                Node.newExprStatement(Node.newAssignment(Node.newName("x", 1), Node.newCall(Node.newName("foo"), Lists.newArrayList(Node.newName("i"))))),
                Node.newBlock(Node.newExprStatement(Node.newExpressionStatement(Node.newCall(Node.newName("println"), Lists.newArrayList(Node.newString("i"))))),
                        Node.newEmptyStatement())),
                Node.newEmptyStatement()));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), forLoop.getParent());
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with multi-line string
        Node multiLineString = Node.newExprResult(Node.newCall(Node.newName("+"),
                Lists.newArrayList(Node.newString("foo"),
                        Node.newString(" this string is "),
                        Node.newString("continued on the next line but you forgot the +"))));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), multiLineString.getParent());
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.problemNodes.clear();

        // Test statement with missing '='
        Node missingAssign = Node.newExprResult(Node.newCall(Node.newName("x"), Node.newName("=="), Node.newName("foo")));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), missingAssign.getParent());
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with stray semicolon
        Node straySemicolon = Node.newExprResult(Node.newCall(Node.newName("foo"), Lists.newArrayList()));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), Node.newBlock(Node.newStatement(straySemicolon), Node.newEmptyStatement()));
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.problemNodes.clear();

        // Test statement within a block
        Node block = Node.newBlock(
                Node.newExprStatement(Node.newAssignment(Node.newName("x"), Node.newName("y"))),
                Node.newExprStatement(Node.newCall(Node.newName("foo"), Lists.newArrayList()))
        );
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), block);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with complex expression
        Node complexExpression = Node.newExprResult(Node.newCall(Node.newName("+"),
                Lists.newArrayList(Node.newCall(Node.newName("+"),
                        Lists.newArrayList(Node.newName("x"), Node.newNumber(2))),
                        Node.newCall(Node.newName("*"),
                                Lists.newArrayList(Node.newName("y"), Node.newNumber(3))))));
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), complexExpression.getParent());
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with multiple statements
        Node multipleStatements = Node.newBlock(
                Node.newExprStatement(Node.newAssignment(Node.newName("x"), Node.newName("y"))),
                Node.newExprStatement(Node.newCall(Node.newName("foo"), Lists.newArrayList()))
        );
        nodeTraversal.traverse(checkSideEffects, Node.newRoot(), multipleStatements);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
    }
}