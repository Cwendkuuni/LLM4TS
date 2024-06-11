import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        checkSideEffects = new CheckSideEffects(null, null, false);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        checkSideEffects.visit(t, n, parent);
        // Assert here
    }

    @Test
    public void testProtectSideEffects() {
        checkSideEffects.protectSideEffects();
        // Assert here
    }

    @Test
    public void testAddExtern() {
        checkSideEffects.addExtern();
        // Assert here
    }

    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        stripProtection.visit(t, n, parent);
        // Assert here
    }
}