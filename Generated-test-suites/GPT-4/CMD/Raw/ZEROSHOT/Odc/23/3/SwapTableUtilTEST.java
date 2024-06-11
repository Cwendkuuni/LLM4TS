
/*
 * This code is subject to the test of the SwapTableUtil methods
 * The existing Apache License still applies to the code presented here.
 * @version 1.0
 */

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Assert;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
        Assert.assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.UNCHECK, false));

        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.INIT, 100.00, FullVerificationResult.CONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.00, FullVerificationResult.CONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.INCONSISTENT, false));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, true));
        Assert.assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.00, FullVerificationResult.CONSISTENT, false));
    }
}
