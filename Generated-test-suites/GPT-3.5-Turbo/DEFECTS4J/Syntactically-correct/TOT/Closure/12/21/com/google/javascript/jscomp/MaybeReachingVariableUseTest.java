import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

    @Mock
    private ControlFlowGraph<Node> mockCfg;

    @Mock
    private Scope mockJsScope;

    private MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        maybeReachingVariableUse = new MaybeReachingVariableUse(mockCfg, mockJsScope);
    }

    // Test for flowThrough method
    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        n.setType(Token.BLOCK);

        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

        assertNotNull(output);
    }

    // Test for isForward method
    @Test
    public void testIsForward() {
        assertFalse(maybeReachingVariableUse.isForward());
    }

    // Test for createEntryLattice method
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();

        assertNotNull(entryLattice);
    }

    // Test for createInitialEstimateLattice method
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = maybeReachingVariableUse.createInitialEstimateLattice();

        assertNotNull(initialEstimateLattice);
    }

    // Test for hasExceptionHandler method
    @Test
    public void testHasExceptionHandler() {
        Node cfgNode = new Node(Token.BLOCK);

        assertFalse(maybeReachingVariableUse.hasExceptionHandler(cfgNode));
    }

    // Test for computeMayUse method
    @Test
    public void testComputeMayUse() {
        Node n = new Node(Token.BLOCK);
        Node cfgNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();

        maybeReachingVariableUse.computeMayUse(n, cfgNode, output, false);

        assertNotNull(output);
    }

    // Test for addToUseIfLocal method
    @Test
    public void testAddToUseIfLocal() {
        String name = "name1";
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

        maybeReachingVariableUse.addToUseIfLocal(name, node, use);

        // Assert content of use.mayUseMap
        assertEquals(1, use.mayUseMap.size());
        assertEquals(node, use.mayUseMap.get(mockJsScope.getVar(name)).iterator().next());
    }

    // Test for removeFromUseIfLocal method
    @Test
    public void testRemoveFromUseIfLocal() {
        String name = "name1";
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
        use.mayUseMap.put(mockJsScope.getVar(name), node);

        maybeReachingVariableUse.removeFromUseIfLocal(name, use);

        // Assert content of use.mayUseMap
        assertEquals(0, use.mayUseMap.size());
    }

    // Test for getUses method
    @Test
    public void testGetUses() {
        String name = "name1";
        Node defNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses.ReachingUses output = new MaybeReachingVariableUse.ReachingUses.ReachingUses();
        output.mayUseMap.put(mockJsScope.getVar(name), defNode);

        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);

        // Assert content of uses
        assertEquals(1, uses.size());
        assertTrue(uses.contains(defNode));
    }
}