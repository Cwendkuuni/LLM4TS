
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;

/**
 * @author Test Author
 * @date 2023-12-01
 * @since 4.2.4
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {

        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTENT,
                false));

        assertTrue(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.UNCHECK,
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO, 
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTENT,
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.COMPLETED,
                100.00,
                FullVerificationResult.CONSISTENT,
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, 
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.DIFFERENT,
                false));

        assertFalse(SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.00,
                FullVerificationResult.CONSISTENT,
                true));
    }
}
