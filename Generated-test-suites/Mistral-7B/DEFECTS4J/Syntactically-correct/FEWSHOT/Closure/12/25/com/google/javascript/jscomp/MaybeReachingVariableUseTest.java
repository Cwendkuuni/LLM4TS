package org.apache.commons.cli; // for consistency with the given example

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {
    @Test
    public void testEmpty() {
        // Test empty constructor
        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(null, null, null);
        ReachingUses initialEstimate = instance.createInitialEstimateLattice();
        assertNull(initialEstimate);

        ReachingUses entryLattice = instance.createEntryLattice();
        assertNull(entryLattice);
    }

    @Test
    public void testBasic() {
        // Test basic usage with a simple statement
        String code = "var x = 1; alert(x);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(1, uses.size());
        assertEquals(cfg.getNode(1), uses.iterator().next());

        assertEquals(initialEstimate, result);
        assertEquals(entryLattice, result);
    }

    @Test
    public void testAssignment() {
        // Test assignment statement
        String code = "var x = 1; x = 2; alert(x);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(2, uses.size());
        List<Node> nodes = Lists.newArrayList(uses.iterator().next(), uses.iterator().next());
        assertFalse(nodes.get(0).equals(nodes.get(1)));
        assertTrue(nodes.get(0).getTokenPosition().getLine() < nodes.get(1).getTokenPosition().getLine());
    }

    @Test
    public void testConditional() {
        // Test conditional statement
        String code = "var x = 1; if (false) { x = 2; } alert(x);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(1, uses.size());
        assertEquals(cfg.getNode(1), uses.iterator().next());
    }

    @Test
    public void testLoop() {
        // Test while loop
        String code = "var x = 1; while (true) { x++; alert(x); }";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(Inf.instance(), uses);
    }

    @Test
    public void testFunction() {
        // Test function call
        String code = "function foo() { var x = 1; return x; } var x = foo(); alert(x);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(2, uses.size());
        List<Node> nodes = Lists.newArrayList(uses.iterator().next(), uses.iterator().next());
        assertFalse(nodes.get(0).equals(nodes.get(1)));
        assertTrue(nodes.get(0).getTokenPosition().getLine() < nodes.get(1).getTokenPosition().getLine());
    }

    @Test
    public void testForIn() {
        // Test for...in loop
        String code = "var array = [1, 2, 3]; for (var x in array) { alert(x); }";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(3, uses.size());
        List<Node> nodes = Lists.newArrayList(uses.iterator().next(), uses.iterator().next(), uses.iterator().next());
        assertFalse(nodes.get(0).equals(nodes.get(1)));
        assertFalse(nodes.get(0).equals(nodes.get(2)));
        assertFalse(nodes.get(1).equals(nodes.get(2)));
        assertTrue(nodes.get(0).getTokenPosition().getLine() < nodes.get(1).getTokenPosition().getLine());
        assertTrue(nodes.get(0).getTokenPosition().getLine() < nodes.get(2).getTokenPosition().getLine());
    }

    @Test
    public void testMultipleAssignments() {
        // Test multiple assignments
        String code = "var x = 1, y = 2; alert(x + y);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> uses = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(1, uses.size());
        assertEquals(cfg.getNode(1), uses.iterator().next());

        uses = analysis.getUses("y", cfg.getEntryNode());
        assertNotNull(uses);
        assertEquals(1, uses.size());
        assertEquals(cfg.getNode(2), uses.iterator().next());
    }

    @Test
    public void testComplexStatement() {
        // Test complex statement
        String code = "var x = 1, y = 2; if (x > 0) { x++; } else { y--; } alert(x + y);";
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createASTFromString(code), null);
        Scope jsScope = new Scope(cfg.getEntryNode());
        AbstractCompiler compiler = new NoOpCompiler();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
        ReachingUses entryLattice = analysis.createEntryLattice();

        ReachingUses result = analysis.analyze(initialEstimate, entryLattice);

        Collection<Node> usesX = analysis.getUses("x", cfg.getEntryNode());
        assertNotNull(usesX);
        assertEquals(2, usesX.size());
        List<Node> nodesX = Lists.newArrayList(usesX.iterator().next(), usesX.iterator().next());
        assertFalse(nodesX.get(0).equals(nodesX.get(1)));
        assertTrue(nodesX.get(0).getTokenPosition().getLine() < nodesX.get(1).getTokenPosition().getLine());

        Collection<Node> usesY = analysis.getUses("y", cfg.getEntryNode());
        assertNotNull(usesY);
        assertEquals(1, usesY.size());
        assertEquals(cfg.getNode(3), usesY.iterator().next());
    }
}

class NoOpCompiler implements AbstractCompiler {
    @Override
    public void process(Node root) {}
}