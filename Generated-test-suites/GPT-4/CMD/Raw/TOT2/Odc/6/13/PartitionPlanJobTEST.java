import org.junit.Assert;
import org.junit.Test;

public class PartitionPlanJobTest {

    @Test
    public void TestExecuteMethod() {
        Assert.assertTrue(true); //Placeholder for actual test
    }

    @Test(expected = UnExpectedException.class)
    public void testInterruptMethod() {
        PartitionPlanJob job = new PartitionPlanJob();
        job.interrupt();
    }
}