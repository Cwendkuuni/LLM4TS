package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_";
        InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        // When
        Node root = Node.newObjectLit(
            Node.newPair("a", Node.newNumber(1)),
            Node.newPair("b", Node.newNumber(2))
        );
        Node externs = Node.newObjectLit();
        pass.process(externs, root);

        // Then
        Scope scope = compiler.getCurrentScope();
        Iterator<Var> it = scope.getVars();

        Var varA = it.next();
        Var varB = it.next();

        assertEquals(Node.newString(Token.NAME, "a"), varA.getNameNode());
        assertEquals(Node.newNumber(1), varA.getValue());

        assertEquals(Node.newString(Token.NAME, "b"), varB.getNameNode());
        assertEquals(Node.newNumber(2), varB.getValue());

        it = root.childrenIterator();
        assertNull(it.hasNext());
    }

    @Test
    public void testInlineObjectLiteralsWithFunctionCall() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_";
        InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        // When
        Node root = Node.newCall(Node.newName("foo"), Lists.newArrayList(
            Node.newObjectLit(
                Node.newPair("a", Node.newNumber(1)),
                Node.newPair("b", Node.newNumber(2))
            )
        ));
        Node externs = Node.newObjectLit();
        pass.process(externs, root);

        // Then
        Scope scope = compiler.getCurrentScope();
        Iterator<Var> it = scope.getVars();

        Var varA = it.next();
        Var varB = it.next();

        assertEquals(Node.newString(Token.NAME, "a"), varA.getNameNode());
        assertEquals(Node.newNumber(1), varA.getValue());

        assertEquals(Node.newString(Token.NAME, "b"), varB.getNameNode());
        assertEquals(Node.newNumber(2), varB.getValue());

        root = compiler.getRootNode();
        assertEquals(Node.newCall(Node.newName("foo"), Lists.newArrayList(
            Node.newVar(Node.newString(Token.NAME, "a")),
            Node.newVar(Node.newString(Token.NAME, "b"))
        )), root);
    }

    @Test
    public void testInlineObjectLiteralsWithComplexKey() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_";
        InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        // When
        Node root = Node.newObjectLit(
            Node.newPair(Node.newString("key1"), Node.newObjectLit(
                Node.newPair("key2", Node.newNumber(1)),
                Node.newPair("key3", Node.newNumber(2))
            )),
            Node.newPair(Node.newString("key4"), Node.newNumber(3))
        );
        Node externs = Node.newObjectLit();
        pass.process(externs, root);

        // Then
        Scope scope = compiler.getCurrentScope();
        Iterator<Var> it = scope.getVars();

        Var varKey1 = it.next();
        Var varKey2 = it.next();
        Var varKey3 = it.next();
        Var varKey4 = it.next();

        assertEquals(Node.newString("key1"), varKey1.getNameNode());
        assertNull(varKey1.getValue());

        assertEquals(Node.newString("key2"), varKey2.getNameNode());
        assertEquals(Node.newObjectLit(
            Node.newPair(Node.newString("key3"), Node.newNumber(1)),
            Node.newPair(Node.newString("key4"), Node.newNumber(3))
        ), varKey2.getValue());

        assertEquals(Node.newString("key4"), varKey4.getNameNode());
        assertEquals(Node.newNumber(3), varKey4.getValue());

        root = compiler.getRootNode();
        assertEquals(Node.newObjectLit(
            Node.newPair(Node.newString("key1"), Node.newObjectLit(
                Node.newPair(Node.newString("key2"), Node.newVar(Node.newString("key3_key2"))),
                Node.newPair(Node.newString("key3"), Node.newVar(Node.newString("key3_key3"))),
                Node.newPair(Node.newString("key4"), Node.newVar(Node.newString("key4_key4")))
            )),
            Node.newPair(Node.newString("key4"), Node.newVar(Node.newString("key4_key4")))
        ), root);
    }
}