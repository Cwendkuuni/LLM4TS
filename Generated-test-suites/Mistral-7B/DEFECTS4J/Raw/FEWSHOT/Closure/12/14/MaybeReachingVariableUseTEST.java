package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

    private static Scope getScope() {
        // Create a dummy scope for testing purposes
        return new Scope();
    }

    @Test
    public void testEmpty() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, getScope(null), null);
        ReachingUses result = analysis.computeReachingUses(cfg.getEntryNode());
        assertEquals(new ReachingUses(), result);
    }

    @Test
    public void testSimpleBlock() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        Scope jsScope = getScope();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

        Node entry = new Node(Token.BLOCK);
        Node assignment = new Node(Token.ASSIGN);
        Node name = new Node(Token.NAME, "x");
        Node value = new Node(Token.NUMBER, 1);
        Node use = new Node(Token.NAME, "x");

        entry.addChild(assignment);
        assignment.addChild(name);
        assignment.addChild(value);
        entry.addChild(use);

        cfg.addNode(entry);
        cfg.addEdge(entry, entry);

        ReachingUses result = analysis.computeReachingUses(entry);
        Collection<Node> uses = analysis.getUses("x", entry);
        assertEquals(use, uses.iterator().next());
    }

    @Test
    public void testConditionalBlock() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        Scope jsScope = getScope();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

        Node entry = new Node(Token.BLOCK);
        Node condition = new Node(Token.IF);
        Node trueBranch = new Node(Token.BLOCK);
        Node assignment = new Node(Token.ASSIGN);
        Node name = new Node(Token.NAME, "x");
        Node value = new Node(Token.NUMBER, 1);
        Node use = new Node(Token.NAME, "x");

        entry.addChild(condition);
        condition.addChild(new Node(Token.BOOL, true));
        condition.addChild(trueBranch);
        trueBranch.addChild(assignment);
        assignment.addChild(name);
        assignment.addChild(value);
        trueBranch.addChild(use);

        cfg.addNode(entry);
        cfg.addEdge(entry, trueBranch);
        cfg.addEdge(entry, Condition.ELSE);

        ReachingUses resultTrueBranch = new ReachingUses();
        resultTrueBranch.mayUseMap.put(jsScope.getVar("x"), trueBranch);

        ReachingUses resultFalseBranch = new ReachingUses();

        ReachingUses result = new ReachingUses();
        result.mayUseMap.putAll(resultTrueBranch.mayUseMap);
        result.mayUseMap.putAll(resultFalseBranch.mayUseMap);

        ReachingUses expected = new ReachingUses();
        expected.mayUseMap.putAll(resultTrueBranch.mayUseMap);

        analysis.computeReachingUses(entry);
        ReachingUses resultEntry = analysis.getAnalysis(entry);
        assertEquals(expected, resultEntry);
    }

    @Test
    public void testLoop() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        Scope jsScope = getScope();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

        Node entry = new Node(Token.BLOCK);
        Node loop = new Node(Token.FOR);
        Node condition = new Node(Token.NAME, "i");
        Node comparison = new Node(Token.LT);
        Node value = new Node(Token.NUMBER, 0);
        Node increment = new Node(Token.ASSIGN, null, new Node(Token.NAME, "i"), new Node(Token.NUMBER, 1));
        Node body = new Node(Token.BLOCK);
        Node use = new Node(Token.NAME, "i");

        entry.addChild(loop);
        loop.addChild(condition);
        condition.addChild(comparison);
        comparison.addChild(condition.getFirstChild());
        comparison.addChild(value);
        loop.addChild(increment);
        loop.addChild(body);
        body.addChild(use);

        cfg.addNode(entry);
        cfg.addEdge(entry, loop);
        cfg.addEdge(loop, loop); // loop condition
        cfg.addEdge(loop, body);

        ReachingUses result = analysis.computeReachingUses(entry);
        Collection<Node> uses = analysis.getUses("i", entry);
        assertEquals(use, uses.iterator().next());
    }

    @Test
    public void testFunctionCall() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        Scope jsScope = getScope();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

        Node entry = new Node(Token.BLOCK);
        Node functionCall = new Node(Token.CALL, null, new Node(Token.NAME, "someFunction"));

        entry.addChild(functionCall);

        cfg.addNode(entry);
        cfg.addEdge(entry, entry);

        Var variable = jsScope.getVar("x");
        Preconditions.checkState(variable != null, "Variable x should exist");

        Node definition = new Node(Token.ASSIGN);
        Node name = new Node(Token.NAME, "x");
        Node value = new Node(Token.NUMBER, 1);

        definition.addChild(name);
        definition.addChild(value);

        ReachingUses resultDefinition = new ReachingUses();
        resultDefinition.mayUseMap.put(variable, definition);

        ReachingUses resultFunctionCall = new ReachingUses();
        resultFunctionCall.mayUseMap.put(variable, functionCall);

        ReachingUses result = new ReachingUses();
        result.mayUseMap.putAll(resultDefinition.mayUseMap);
        result.mayUseMap.putAll(resultFunctionCall.mayUseMap);

        analysis.computeReachingUses(entry);
        ReachingUses resultEntry = analysis.getAnalysis(entry);
        assertEquals(result, resultEntry);
    }

    @Test
    public void testEscapedVariable() {
        ControlFlowGraph cfg = new ControlFlowGraph();
        Scope jsScope = getScope();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

        Node entry = new Node(Token.BLOCK);
        Node declaration = new Node(Token.VAR, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 1));
        Node assignment = new Node(Token.ASSIGN);
        Node name = new Node(Token.NAME, "x");
        Node value = new Node(Token.NUMBER, 1);
        Node use = new Node(Token.NAME, "x");

        entry.addChild(declaration);
        entry.addChild(assignment);
        declaration.addChild(name);
        assignment.addChild(name);
        assignment.addChild(value);
        entry.addChild(use);

        jsScope.addVar(new Var("x", Var.Local, declaration));
        jsScope.addVar(new Var("y", Var.Local, null));

        cfg.addNode(entry);
        cfg.addEdge(entry, assignment);
        cfg.addEdge(assignment, entry);

        analysis.escaped.add(jsScope.getVar("x"));

        ReachingUses result = analysis.computeReachingUses(entry);
        Collection<Node> uses = analysis.getUses("x", entry);
        assertNull(uses);
    }
}