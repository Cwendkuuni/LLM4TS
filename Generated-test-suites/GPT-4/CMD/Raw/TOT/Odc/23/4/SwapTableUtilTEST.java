import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    private SwapTableType swapTableType;
    private TaskStatus taskStatus;
    private Double fullTransferProgressPercentage;
    private FullVerificationResult fullVerificationResult;
    private boolean manualSwapTableStarted;

    @Before
    public void setUp() {
        this.fullTransferProgressPercentage = 100.0;
    }

    @Test
    public void testIsSwapTableEnable_NormalScenario() {
        this.swapTableType = SwapTableType.MANUAL;
        this.taskStatus = TaskStatus.RUNNING;
        this.fullVerificationResult = FullVerificationResult.CONSISTENT;
        this.manualSwapTableStarted = false;

        boolean isSwapTableEnable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertTrue(isSwapTableEnable);
    }

    @Test
    public void testIsSwapTableEnable_EdgeCasePercentageNotFull() {
        this.swapTableType = SwapTableType.MANUAL;
        this.taskStatus = TaskStatus.RUNNING;
        this.fullVerificationResult = FullVerificationResult.CONSISTENT;
        this.manualSwapTableStarted = false;
        this.fullTransferProgressPercentage = 99.0;

        boolean isSwapTableEnable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(isSwapTableEnable);
    }

    @Test
    public void testIsSwapTableEnable_ErrorScenarioManualSwapStarted() {
        this.swapTableType = SwapTableType.MANUAL;
        this.taskStatus = TaskStatus.RUNNING;
        this.fullVerificationResult = FullVerificationResult.CONSISTENT;
        this.manualSwapTableStarted = true;

        boolean isSwapTableEnable = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        assertFalse(isSwapTableEnable);
    }
}