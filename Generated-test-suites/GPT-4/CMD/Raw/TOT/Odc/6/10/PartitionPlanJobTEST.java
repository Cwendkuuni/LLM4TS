import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    @Test(expected = UnExpectedException.class)
    public void testInterruptThrowsUnExpectedException() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}