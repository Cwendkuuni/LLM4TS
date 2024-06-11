import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setUp() {
        deadAssignmentsElimination = new DeadAssignmentsElimination(new AbstractCompiler());
    }

    @After
    public void tearDown() {
        deadAssignmentsElimination = null;
    }

    @Test
    public void testProcess() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testEnterScope() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testExitScope() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testVisit() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testTryRemoveDeadAssignments() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testTryRemoveAssignment() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testIsVariableStillLiveWithinExpression() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testIsVariableReadBeforeKill() {
        // TODO: implement test
        Assert.fail("Not yet implemented");
    }

}